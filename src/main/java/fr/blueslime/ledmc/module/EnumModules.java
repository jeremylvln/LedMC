package fr.blueslime.ledmc.module;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 29/08/2017
 */
public enum EnumModules
{
    KEY_BINDINGS(KeyBindingsModule.class, "key_bindings", "Permits to show what keys are bind and show conflicts in the Controls GUI.", false),
    PLAYER_DIE(PlayerDieModule.class, "player_die", "Flash your keyboard when you die.", false),
    //SOUND_LOCATOR(SoundLocatorModule.class, "sound_locator", "Permits to locate the direction of nearby sounds.", true),
    NIGHT_STARS(NightStarsModule.class, "night_stars", "When the night arrives, your keyboard will reflect the stars in the sky.", false);

    private final Class<? extends AbstractModule> moduleClass;
    private final String configurationKey;
    private final String configurationDescription;
    private final boolean betaModule;

    EnumModules(Class<? extends AbstractModule> moduleClass, String configurationKey, String configurationDescription, boolean betaModule)
    {
        this.moduleClass = moduleClass;
        this.configurationKey = configurationKey;
        this.configurationDescription = configurationDescription;
        this.betaModule = betaModule;
    }

    public Class<? extends AbstractModule> getModuleClass()
    {
        return this.moduleClass;
    }

    public String getConfigurationKey()
    {
        return this.configurationKey;
    }

    public String getConfigurationDescription()
    {
        return this.configurationDescription;
    }

    public boolean isBetaModule()
    {
        return this.betaModule;
    }

    public static EnumModules byInstance(AbstractModule moduleInstance)
    {
        for (EnumModules module : values())
            if (module.getModuleClass().equals(moduleInstance.getClass()))
                return module;

        return null;
    }
}
