package fr.blueslime.ledmc.module;

import fr.blueslime.ledmc.util.RGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.input.Keyboard.*;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 27/08/2017
 */

/*
 * TODO:
 * Working some times, some times not. To debug.
 * A lot of debug needed.
 */
public class SoundLocatorModule extends AbstractModule
{
    private static final Map<SoundEvent[], RGB> SOUND_COLORS;

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
    public void onPlaySound(PlaySoundEvent event)
    {
        if (event.getSound() instanceof PositionedSound && Minecraft.getMinecraft().getRenderViewEntity() != null)
        {
            if (!event.getSound().getSoundLocation().toString().startsWith("entity") && !event.getSound().getSoundLocation().toString().startsWith("block"))
                return;

            Vec3d playerPosition = Minecraft.getMinecraft().getRenderViewEntity().getPositionVector();
            playerPosition = new Vec3d(playerPosition.x, 0, playerPosition.z);

            Vec3d soundVector = new Vec3d(event.getSound().getXPosF(), 0, event.getSound().getXPosF());

            if (playerPosition.x == soundVector.x && playerPosition.z == soundVector.z)
                return;

            Vec3d playerLook = Minecraft.getMinecraft().getRenderViewEntity().getLookVec();
            Vec3d subVector = soundVector.subtract(playerPosition).normalize();

            double angle = Math.toDegrees(Math.atan2(playerLook.x, playerLook.z));
            angle -= Math.toDegrees(Math.atan2(subVector.x, subVector.z));
            angle = (int) (angle + 22.5) % 360;

            int i = ((int) angle / 45);
            int[] keysToLight;

            if (i == 0) keysToLight = new int[] { KEY_5, KEY_6, KEY_7, KEY_8 };
            else if (i == 1) keysToLight = new int[] { KEY_9, KEY_0, KEY_P };
            else if (i == 2) keysToLight = new int[] { KEY_P, KEY_SEMICOLON, KEY_PERIOD };
            else if (i == 3) keysToLight = new int[] { KEY_SEMICOLON, KEY_PERIOD, KEY_COMMA };
            else if (i == 4) keysToLight = new int[] { KEY_V, KEY_B, KEY_N, KEY_M };
            else if (i == 5) keysToLight = new int[] { KEY_S, KEY_C, KEY_X };
            else if (i == 6) keysToLight = new int[] { KEY_W, KEY_S, KEY_X };
            else keysToLight = new int[] { KEY_W, KEY_3, KEY_4 };

            try
            {
                RGB rgb = getColorIfRegisteredOrDefault((PositionedSound) event.getSound(), new RGB(0, 0, 255));

                clearKeyboard();
                flashKeyboardKeysAndFade(keysToLight, rgb.getRed(), rgb.getGreen(), rgb.getBlue(), 500, 2, false);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static RGB getColorIfRegisteredOrDefault(PositionedSound sound, RGB defaultValue)
    {
        for (Map.Entry<SoundEvent[], RGB> soundEvents : SOUND_COLORS.entrySet())
            for (SoundEvent soundEvent : soundEvents.getKey())
                if (soundEvent.getSoundName() == sound.getSoundLocation())
                    return soundEvents.getValue();

        if (sound.getSoundLocation().toString().startsWith("entity"))
        {

        }

        return defaultValue;
    }

    static
    {
        SOUND_COLORS = new HashMap<>();

        SOUND_COLORS.put(new SoundEvent[] {
                SoundEvents.BLOCK_WOOD_BREAK, SoundEvents.BLOCK_WOOD_STEP, SoundEvents.BLOCK_WOOD_PLACE, SoundEvents.BLOCK_WOOD_HIT, SoundEvents.BLOCK_WOOD_FALL,
                SoundEvents.BLOCK_LADDER_BREAK, SoundEvents.BLOCK_LADDER_STEP, SoundEvents.BLOCK_LADDER_PLACE, SoundEvents.BLOCK_LADDER_HIT, SoundEvents.BLOCK_LADDER_FALL
        }, new RGB(89, 75, 61)); // Brown

        SOUND_COLORS.put(new SoundEvent[] {
                SoundEvents.BLOCK_GRAVEL_BREAK, SoundEvents.BLOCK_GRAVEL_STEP, SoundEvents.BLOCK_GRAVEL_PLACE, SoundEvents.BLOCK_GRAVEL_HIT, SoundEvents.BLOCK_GRAVEL_FALL,
                SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL
        }, new RGB(74, 74, 74)); // Gray

        SOUND_COLORS.put(new SoundEvent[] {
                SoundEvents.BLOCK_GRASS_BREAK, SoundEvents.BLOCK_GRASS_STEP, SoundEvents.BLOCK_GRASS_PLACE, SoundEvents.BLOCK_GRASS_HIT, SoundEvents.BLOCK_GRASS_FALL,
                SoundEvents.BLOCK_SLIME_BREAK, SoundEvents.BLOCK_SLIME_STEP, SoundEvents.BLOCK_SLIME_PLACE, SoundEvents.BLOCK_SLIME_HIT, SoundEvents.BLOCK_SLIME_FALL
        }, new RGB(0, 172, 82)); // Green

        SOUND_COLORS.put(new SoundEvent[] {
                SoundEvents.BLOCK_METAL_BREAK, SoundEvents.BLOCK_METAL_STEP, SoundEvents.BLOCK_METAL_PLACE, SoundEvents.BLOCK_METAL_HIT, SoundEvents.BLOCK_METAL_FALL,
                SoundEvents.BLOCK_ANVIL_BREAK, SoundEvents.BLOCK_ANVIL_STEP, SoundEvents.BLOCK_ANVIL_PLACE, SoundEvents.BLOCK_ANVIL_HIT, SoundEvents.BLOCK_ANVIL_FALL
        }, new RGB(190, 190, 190)); // Gray with more White

        SOUND_COLORS.put(new SoundEvent[] {
                SoundEvents.BLOCK_GLASS_BREAK, SoundEvents.BLOCK_GLASS_STEP, SoundEvents.BLOCK_GLASS_PLACE, SoundEvents.BLOCK_GLASS_HIT, SoundEvents.BLOCK_GLASS_FALL,
                SoundEvents.BLOCK_CLOTH_BREAK, SoundEvents.BLOCK_CLOTH_STEP, SoundEvents.BLOCK_CLOTH_PLACE, SoundEvents.BLOCK_CLOTH_HIT, SoundEvents.BLOCK_CLOTH_FALL,
                SoundEvents.BLOCK_SNOW_BREAK, SoundEvents.BLOCK_SNOW_STEP, SoundEvents.BLOCK_SNOW_PLACE, SoundEvents.BLOCK_SNOW_HIT, SoundEvents.BLOCK_SNOW_FALL
        }, new RGB(255, 255, 255)); // White

        SOUND_COLORS.put(new SoundEvent[] {
                SoundEvents.BLOCK_SAND_BREAK, SoundEvents.BLOCK_SAND_STEP, SoundEvents.BLOCK_SAND_PLACE, SoundEvents.BLOCK_SAND_HIT, SoundEvents.BLOCK_SAND_FALL
        }, new RGB(243, 222, 148)); // Yellow
    }
}
