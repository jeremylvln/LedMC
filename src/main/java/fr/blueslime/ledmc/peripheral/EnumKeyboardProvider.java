package fr.blueslime.ledmc.peripheral;

import fr.blueslime.ledmc.peripheral.logitech.LogitechController;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 26/08/2017
 */
public enum EnumKeyboardProvider
{
    NONE("None (Disable mod)", null),
    LOGITECH("Logitech", LogitechController.class);

    private final String name;
    private final Class<? extends IKeyboardController> peripheralController;

    EnumKeyboardProvider(String name, Class<? extends IKeyboardController> peripheralController)
    {
        this.name = name;
        this.peripheralController = peripheralController;
    }

    public String getName()
    {
        return this.name;
    }

    public Class<? extends IKeyboardController> getPeripheralController()
    {
        return this.peripheralController;
    }

    public static EnumKeyboardProvider byName(String name)
    {
        for (EnumKeyboardProvider keyboardProviders : values())
            if (keyboardProviders.getName().equals(name))
                return keyboardProviders;

        return null;
    }
}
