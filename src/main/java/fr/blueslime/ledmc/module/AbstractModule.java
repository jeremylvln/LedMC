package fr.blueslime.ledmc.module;

import fr.blueslime.ledmc.LedMC;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 26/08/2017
 */
public abstract class AbstractModule
{
    protected static int[] KEYBOARD_KEYS;

    public abstract void enable();
    public abstract void disable();

    protected static void flashKeyboard(int red, int green, int blue, long milliseconds)
    {
        try
        {
            lightKeyboard(red, green, blue);

            new Thread(() ->
            {
                try
                {
                    Thread.sleep(milliseconds);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                clearKeyboard();
            }).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected static void flashKeyboardAndFade(int red, int green, int blue, long stayMilliseconds, long sleepFadeOutMilliseconds)
    {
        flashKeyboardKeysAndFade(null, red, green, blue, stayMilliseconds, sleepFadeOutMilliseconds);
    }

    protected static void flashKeyboardKeysAndFade(int[] keyCodes, int red, int green, int blue, long stayMilliseconds, long sleepFadeOutMilliseconds)
    {
        flashKeyboardKeysAndFade(keyCodes, red, green, blue, stayMilliseconds, sleepFadeOutMilliseconds, true);
    }

    protected static void flashKeyboardKeysAndFade(int[] keyCodes, int red, int green, int blue, long stayMilliseconds, long sleepFadeOutMilliseconds, boolean transform)
    {
        try
        {
            lightKeyboardKeys(keyCodes, red, green, blue, transform);
            new Thread(new FadeStayFade(keyCodes, red, green, blue, 0, stayMilliseconds, sleepFadeOutMilliseconds, transform)).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected static void fadeKeyboardAndFade(int red, int green, int blue, long sleepFadeInMilliseconds, long stayMilliseconds, long sleepFadeOutMilliseconds)
    {
        fadeKeyboardAndFade(null, red, green, blue, sleepFadeInMilliseconds, stayMilliseconds, sleepFadeOutMilliseconds);
    }

    protected static void fadeKeyboardAndFade(int[] keyCodes, int red, int green, int blue, long sleepFadeInMilliseconds, long stayMilliseconds, long sleepFadeOutMilliseconds)
    {
        fadeKeyboardAndFade(keyCodes, red, green, blue, sleepFadeInMilliseconds, stayMilliseconds, sleepFadeOutMilliseconds, true);
    }

    protected static void fadeKeyboardAndFade(int[] keyCodes, int red, int green, int blue, long sleepFadeInMilliseconds, long stayMilliseconds, long sleepFadeOutMilliseconds, boolean transform)
    {
        try
        {
            lightKeyboardKeys(keyCodes, red, green, blue, transform);
            new Thread(new FadeStayFade(keyCodes, red, green, blue, sleepFadeInMilliseconds, stayMilliseconds, sleepFadeOutMilliseconds, transform)).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected static void clearKeyboard()
    {
        try
        {
            for (int keyboardKey : KEYBOARD_KEYS)
                removeKeyboardKeyLight(keyboardKey);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected static void lightKeyboard(int red, int green, int blue)
    {
        try
        {
            for (int keyboardKey : KEYBOARD_KEYS)
                lightKeyboardKey(keyboardKey, red, green, blue);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected static void lightKeyboardKey(int keyCode, int red, int green, int blue)
    {
        lightKeyboardKey(keyCode, red, green, blue, true);
    }

    protected static void lightKeyboardKey(int keyCode, int red, int green, int blue, boolean transform)
    {
        lightKeyboardKeys(new int[] { keyCode }, red, green, blue, transform);
    }

    protected static void lightKeyboardKeys(int[] keyCodes, int red, int green, int blue, boolean transform)
    {
        try
        {
            for (int keyCode : keyCodes)
                LedMC.KEYBOARD_CONTROLLER.setLightningForKey(transform ? LedMC.KEYBOARD_LAYOUT.transformKeyCode(keyCode) : keyCode, red, green, blue);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected static void removeKeyboardKeyLight(int keyCode)
    {
        removeKeyboardKeyLight(keyCode, true);
    }

    protected static void removeKeyboardKeyLight(int keyCode, boolean transform)
    {
        removeKeyboardKeysLight(new int[] { keyCode }, transform);
    }

    protected static void removeKeyboardKeysLight(int[] keyCodes, boolean transform)
    {
        try
        {
            for (int keyCode : keyCodes)
                LedMC.KEYBOARD_CONTROLLER.removeLightningForKey(transform ? LedMC.KEYBOARD_LAYOUT.transformKeyCode(keyCode) : keyCode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static
    {
        int i = 0;

        try
        {
            Field[] fields = Keyboard.class.getDeclaredFields();

            for (Field field : fields)
                if (Modifier.isStatic(field.getModifiers()) && field.getName().startsWith("KEY_"))
                    i++;

            KEYBOARD_KEYS = new int[i];
            i = 0;

            for (Field field : fields)
            {
                if (Modifier.isStatic(field.getModifiers()) && field.getName().startsWith("KEY_"))
                {
                    KEYBOARD_KEYS[i] = field.getInt(null);
                    i++;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static class FadeStayFade implements Runnable
    {
        private final int[] keyCodes;
        private final long stayMilliseconds;
        private final long sleepFadeInMilliseconds;
        private final long sleepFadeOutMilliseconds;
        private final boolean transform;

        private final int initialRed;
        private final int initialGreen;
        private final int initialBlue;

        private int red;
        private int green;
        private int blue;

        private boolean hasToContinue;

        FadeStayFade(int[] keyCodes, int initialRed, int initialGreen, int initialBlue, long sleepFadeInMilliseconds, long stayMilliseconds, long sleepFadeOutMilliseconds, boolean transform)
        {
            this.keyCodes = keyCodes;

            this.initialRed = initialRed;
            this.initialGreen = initialGreen;
            this.initialBlue = initialBlue;

            this.red = 0;
            this.green = 0;
            this.blue = 0;

            this.stayMilliseconds = stayMilliseconds;
            this.sleepFadeInMilliseconds = sleepFadeInMilliseconds;
            this.sleepFadeOutMilliseconds = sleepFadeOutMilliseconds;
            this.transform = transform;

            this.hasToContinue = true;
        }

        @Override
        public void run()
        {
            if (this.sleepFadeInMilliseconds != 0)
            {
                while (this.hasToContinue)
                {
                    this.red++;
                    this.green++;
                    this.blue++;

                    if (this.red > this.initialRed)
                        this.red = this.initialRed;

                    if (this.green > this.initialGreen)
                        this.green = this.initialGreen;

                    if (this.blue > this.initialBlue)
                        this.blue = this.initialBlue;

                    if (this.red == this.initialRed && this.green == this.initialGreen && this.blue == this.initialBlue)
                    {
                        this.hasToContinue = false;
                    }
                    else
                    {
                        try
                        {
                            if (this.keyCodes == null)
                            {
                                lightKeyboard(this.red, this.green, this.blue);
                            }
                            else
                            {
                                for (int keyCode : this.keyCodes)
                                    lightKeyboardKey(keyCode, this.red, this.green, this.blue, this.transform);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        try
                        {
                            Thread.sleep(this.sleepFadeInMilliseconds);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }

            this.hasToContinue = true;

            try
            {
                Thread.sleep(this.stayMilliseconds);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            if (this.sleepFadeOutMilliseconds != 0)
            {
                while (this.hasToContinue)
                {
                    this.red--;
                    this.green--;
                    this.blue--;

                    if (this.red < 0)
                        this.red = 0;

                    if (this.green < 0)
                        this.green = 0;

                    if (this.blue < 0)
                        this.blue = 0;

                    if (this.red == 0 && this.green == 0 && this.blue == 0)
                    {
                        this.hasToContinue = false;
                    }
                    else
                    {
                        try
                        {
                            if (this.keyCodes == null)
                            {
                                lightKeyboard(this.red, this.green, this.blue);
                            }
                            else
                            {
                                for (int keyCode : this.keyCodes)
                                    lightKeyboardKey(keyCode, this.red, this.green, this.blue, this.transform);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        try
                        {
                            Thread.sleep(this.sleepFadeOutMilliseconds);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
