package fr.blueslime.ledmc.util;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 29/08/2017
 */
public class RGB
{
    private final int red;
    private final int green;
    private final int blue;

    public RGB(int red, int green, int blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed()
    {
        return this.red;
    }

    public int getGreen()
    {
        return this.green;
    }

    public int getBlue()
    {
        return this.blue;
    }
}
