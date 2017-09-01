package fr.blueslime.ledmc.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 26/08/2017
 */
public class KeyBindingsModule extends AbstractModule
{
    private boolean wasGuiControls;

    public KeyBindingsModule()
    {
        this.wasGuiControls = false;
    }

    @Override
    public void enable()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void disable()
    {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiControls)
        {
            try
            {
                lightKeyboardByKeyBindings();
                this.wasGuiControls = true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (this.wasGuiControls)
        {
            clearKeyboard();
            this.wasGuiControls = false;
        }
    }

    @SubscribeEvent
    public void onGuiScreenKeyboardInput(GuiScreenEvent.KeyboardInputEvent event)
    {
        if (event.getGui() instanceof GuiControls)
        {
            try
            {
                lightKeyboardByKeyBindings();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onGuiScreenMouseInput(GuiScreenEvent.MouseInputEvent event)
    {
        if (event.getGui() instanceof GuiControls && Mouse.isButtonDown(0))
        {
            try
            {
                lightKeyboardByKeyBindings();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void lightKeyboardByKeyBindings() throws Exception
    {
        clearKeyboard();

        for (KeyBinding keyBinding : Minecraft.getMinecraft().gameSettings.keyBindings)
        {
            if (isKeyAlreadyBind(keyBinding))
                lightKeyboardKey(keyBinding.getKeyCode(), 255, 0, 0);
            else
                lightKeyboardKey(keyBinding.getKeyCode(), 0, 255, 0);
        }
    }

    private static boolean isKeyAlreadyBind(KeyBinding keyBinding)
    {
        for (KeyBinding keyBinding1 : Minecraft.getMinecraft().gameSettings.keyBindings)
            if (keyBinding != keyBinding1 && keyBinding.conflicts(keyBinding1))
                return true;

        return false;
    }
}
