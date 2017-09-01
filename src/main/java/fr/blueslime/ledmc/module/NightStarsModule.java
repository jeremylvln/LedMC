package fr.blueslime.ledmc.module;

import com.google.common.primitives.Ints;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 29/08/2017
 */
public class NightStarsModule extends AbstractModule
{
    private List<Integer> keysUsed;
    private long ticks;

    public NightStarsModule()
    {
        this.keysUsed = new ArrayList<>();
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
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if (!event.world.isDaytime())
            {
                if (this.ticks == 100)
                {
                    int[] randomKeys = randomKeys(event.world.rand.nextInt(4) + 1);
                    fadeKeyboardAndFade(randomKeys, 243, 222, 148, 5, 800, 5);

                    this.keysUsed.clear();

                    for (int randomKey : randomKeys)
                        this.keysUsed.add(randomKey);

                    this.ticks = 0;
                }
                else
                {
                    this.ticks++;
                }
            }
        }
    }

    private int[] randomKeys(int count)
    {
        int[] finalKeys = new int[count];

        List<Integer> keys = new ArrayList<>(Ints.asList(KEYBOARD_KEYS));
        Collections.shuffle(keys);

        for (int keyUsed : this.keysUsed)
            keys.remove(new Integer(keyUsed));

        for (int i = 0; i < count; i++)
            finalKeys[i] = keys.get(i);

        return finalKeys;
    }
}
