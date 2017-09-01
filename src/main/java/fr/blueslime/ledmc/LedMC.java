package fr.blueslime.ledmc;

import fr.blueslime.ledmc.exception.KeyboardSetupException;
import fr.blueslime.ledmc.exception.ModuleSetupException;
import fr.blueslime.ledmc.module.AbstractModule;
import fr.blueslime.ledmc.module.EnumModules;
import fr.blueslime.ledmc.peripheral.EnumKeyboardLayout;
import fr.blueslime.ledmc.peripheral.IKeyboardController;
import fr.blueslime.ledmc.peripheral.EnumKeyboardProvider;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 26/08/2017
 */
@Mod(modid = LedMC.MODID, name = LedMC.NAME, version = LedMC.VERSION, dependencies = "required-after:forge", acceptedMinecraftVersions = "[1.12,)", clientSideOnly = true)
public class LedMC
{
    public static final String MODID = "ledmc";
    public static final String NAME = "LedMC";
    public static final String VERSION = "${version}";

    public static Logger LOGGER;

    public static IKeyboardController KEYBOARD_CONTROLLER;
    public static EnumKeyboardLayout KEYBOARD_LAYOUT;
    public static List<AbstractModule> ENABLED_MODULES = new ArrayList<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception
    {
        LOGGER = event.getModLog();
        LOGGER.info("Hi console reader! Now initializing...");

        Configuration configuration = new Configuration(event.getSuggestedConfigurationFile());

        if (!configuration.hasKey("general", "keyboard_provider"))
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            GuiConfiguration dialog = new GuiConfiguration(configuration);
            dialog.pack();
            dialog.setLocation(screenSize.width / 2 - dialog.getSize().width / 2, screenSize.height / 2 - dialog.getSize().height / 2);
            dialog.setVisible(true);

            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            configuration.save();
        }

        if (!configuration.hasKey("general", "keyboard_provider"))
        {
            LOGGER.info("No keyboard provider selected in the configuration GUI. This mod will be inactive.");
            return;
        }

        EnumKeyboardProvider keyboardProvider = EnumKeyboardProvider.byName(configuration.getCategory("general").get("keyboard_provider").getString());

        if (keyboardProvider == null || keyboardProvider == EnumKeyboardProvider.NONE)
        {
            LOGGER.info("The user don't have a compatible keyboard. This mod will be inactive.");
            return;
        }

        try
        {
            KEYBOARD_CONTROLLER = keyboardProvider.getPeripheralController().newInstance();
            KEYBOARD_LAYOUT = EnumKeyboardLayout.byName(configuration.getCategory("general").get("keyboard_layout").getString());

            if (KEYBOARD_LAYOUT == null)
                throw new KeyboardSetupException("Failed to find the configured keyboard layout. Maybe you need to reconfigure the mod?");

            if (KEYBOARD_CONTROLLER.isCompatible())
            {
                KEYBOARD_CONTROLLER.enable();

                this.reloadModules(configuration);

                Runtime.getRuntime().addShutdownHook(new Thread(() ->
                {
                    try
                    {
                        KEYBOARD_CONTROLLER.disable();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }));
            }
            else
            {
                throw new KeyboardSetupException("The selected keyboard provider is not compatible with this system!");
            }
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new KeyboardSetupException("Failed to create an instance of the peripheral controller! (" + e.getMessage() + ")");
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        boolean betaModules = ENABLED_MODULES.stream().anyMatch(moduleInstance -> EnumModules.byInstance(moduleInstance).isBetaModule());

        if (betaModules)
            event.player.sendMessage(new TextComponentString("§c[LedMC] §lWarning! §r§cYou are using BETA modules. Weird things can happen to your keyboard!"));
    }

    private void reloadModules(Configuration configuration) throws ModuleSetupException
    {
        for (AbstractModule module : ENABLED_MODULES)
            module.disable();

        ENABLED_MODULES.clear();

        for (EnumModules module : EnumModules.values())
        {
            boolean hasToBeActivated = configuration.get("modules", module.getConfigurationKey(), !module.isBetaModule(), module.getConfigurationDescription()).getBoolean();

            if (hasToBeActivated)
            {
                try
                {
                    AbstractModule moduleInstance = module.getModuleClass().newInstance();
                    moduleInstance.enable();

                    ENABLED_MODULES.add(moduleInstance);

                    LOGGER.info("Enabled module " + module.name() + "!");
                }
                catch (IllegalAccessException | InstantiationException e)
                {
                    throw new ModuleSetupException("Failed to create an instance of the module! (" + e.getMessage() + ")");
                }
            }
        }

        configuration.save();
    }
}
