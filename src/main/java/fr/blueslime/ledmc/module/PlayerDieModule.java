package fr.blueslime.ledmc.module;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 26/08/2017
 */
public class PlayerDieModule extends AbstractModule
{
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
    public void onPlayerDie(LivingDeathEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer)
            flashKeyboardAndFade(255, 0, 0, 800, 4);
    }
}
