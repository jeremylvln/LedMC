package fr.blueslime.ledmc.peripheral;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 26/08/2017
 */
public interface IKeyboardController
{
    void enable() throws Exception;
    void disable() throws Exception;

    void setLightningForKey(int keyCode, int red, int green, int blue) throws Exception;
    void removeLightningForKey(int keyCode) throws Exception;

    boolean isCompatible();
}
