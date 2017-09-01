package fr.blueslime.ledmc.peripheral.logitech;

import com.logitech.gaming.LogiLED;
import fr.blueslime.ledmc.exception.KeyboardColorException;
import fr.blueslime.ledmc.exception.KeyboardSetupException;
import fr.blueslime.ledmc.peripheral.IKeyboardController;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 26/08/2017
 */
public class LogitechController implements IKeyboardController
{
    private static final boolean[] COMPATIBLE_OS = {
            SystemUtils.IS_OS_WINDOWS_VISTA, SystemUtils.IS_OS_WINDOWS_7,
            SystemUtils.IS_OS_WINDOWS_8, SystemUtils.IS_OS_WINDOWS_10
    };

    @Override
    public void enable() throws Exception
    {
        try
        {
            String arch = System.getProperty("sun.arch.data.model");
            byte[] buffer = new byte[1024];
            int read;

            InputStream isJarDll = LogitechController.class.getResourceAsStream("/LogitechLedJNI_x" + arch + ".dll");
            File tempDll = File.createTempFile("LogitechLedJNI_x" + arch + "_.dll", "");
            FileOutputStream osJarDll = new FileOutputStream(tempDll);

            while ((read = isJarDll.read(buffer)) != -1)
                osJarDll.write(buffer, 0, read);

            osJarDll.close();
            isJarDll.close();

            System.load(tempDll.getAbsolutePath());
        }
        catch (IOException e)
        {
            throw new KeyboardSetupException("Failed to extract and load the native library from the jar.");
        }

        if (!LogiLED.LogiLedInit())
            throw new KeyboardSetupException("Failed to connect to Logitech Gaming Software!");

        if (!LogiLED.LogiLedSaveCurrentLighting())
            throw new KeyboardSetupException("Failed to save current keyboard lightning, maybe the connection with Logitech Gaming Software was lost?");

        if (!LogiLED.LogiLedSetTargetDevice(LogiLED.LOGI_DEVICETYPE_PERKEY_RGB))
            throw new KeyboardSetupException("Failed to set the target device type, maybe the connection with Logitech Gaming Software was lost?");
    }

    @Override
    public void disable() throws Exception
    {
        LogiLED.LogiLedRestoreLighting();
        LogiLED.LogiLedShutdown();
    }

    @Override
    public void setLightningForKey(int keyCode, int red, int green, int blue) throws Exception
    {
        if (!LogiLED.LogiLedSetLightingForKeyWithKeyName(keyCode, red * 100 / 255, green * 100 / 255, blue * 100 / 255))
            throw new KeyboardColorException("Failed to color the key (" + keyCode + ") with RGB (" + red + ", " + green + ", " + blue + "). Maybe the connection with Logitech Gaming Software was lost?");
    }

    @Override
    public void removeLightningForKey(int keyCode) throws Exception
    {
        this.setLightningForKey(keyCode, 0, 0, 0);
    }

    @Override
    public boolean isCompatible()
    {
        boolean osCompatible = false;

        for (boolean os : COMPATIBLE_OS)
        {
            if (os)
            {
                osCompatible = true;
                break;
            }
        }

        return osCompatible;
    }
}
