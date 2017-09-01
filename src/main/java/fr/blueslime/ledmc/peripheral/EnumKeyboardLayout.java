package fr.blueslime.ledmc.peripheral;

import org.lwjgl.input.Keyboard;

import java.util.function.Function;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 26/08/2017
 */
public enum EnumKeyboardLayout
{
    QWERTY("QWERTY"),
    AZERTY("AZERTY", (keyCode) ->
    {
        if (keyCode == Keyboard.KEY_Q) return Keyboard.KEY_A;
        else if (keyCode == Keyboard.KEY_W) return Keyboard.KEY_Z;
        else if (keyCode == Keyboard.KEY_A) return Keyboard.KEY_Q;
        else if (keyCode == Keyboard.KEY_SEMICOLON) return Keyboard.KEY_M;
        else if (keyCode == Keyboard.KEY_Z) return Keyboard.KEY_W;
        else if (keyCode == Keyboard.KEY_M) return Keyboard.KEY_SEMICOLON;
        else return keyCode;
    });

    private final String name;
    private final Function<Integer, Integer> layoutTransformationFunction;

    EnumKeyboardLayout(String name)
    {
        this(name, null);
    }

    EnumKeyboardLayout(String name, Function<Integer, Integer> layoutTransformationFunction)
    {
        this.name = name;
        this.layoutTransformationFunction = layoutTransformationFunction;
    }

    public int transformKeyCode(int keyCode)
    {
        if (this.layoutTransformationFunction != null)
            return this.layoutTransformationFunction.apply(keyCode);

        return keyCode;
    }

    public String getName()
    {
        return this.name;
    }

    public static EnumKeyboardLayout byName(String name)
    {
        for (EnumKeyboardLayout keyboardLayout : values())
            if (keyboardLayout.getName().equals(name))
                return keyboardLayout;

        return null;
    }
}
