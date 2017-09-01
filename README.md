# :bulb: LedMC [![Build Status](https://img.shields.io/travis/IamBlueSlime/LedMC/master.svg?style=flat-square)](https://travis-ci.org/IamBlueSlime/LedMC) [![Minecraft Version](https://img.shields.io/badge/minecraft%20version-1.12.1-red.svg?style=flat-square)](https://github.com/IamBlueSlime/ForgottenTemple) [![License](https://img.shields.io/badge/license-Ce--CILL--B-blue.svg?style=flat-square)](LICENSE)

Let Minecraft control your RGB keyboard! Today, a lot of gamers have a RGB keyboard to pimp their setup. They can control them with the provided software. Some have preconfigured integrations with games whereas others not. The idea of LedMC is to provide a low-level integration with the game. It will permit to improve your survival life with modules you enabled.

## Installation

This mod works with Minecraft Forge. Place the jar archive of LedMC in the `mods` folder. When your game will start for the first time, you will be asked to select your keyboard provider and layout. If you cancel the configuration window or if you select `I don't have a compatible RGB keyboard`, LedMC will simply be inactive. If your keyboard provider and/or layout is not supported yet, be patient! Maybe awesome contributors will come to rescue you!

TODO: Module explanations.

## Compatibility

| Keyboard Provider      | Model                  | Tested                 |
| :--------------------: | :--------------------: | :--------------------: |
| Logitech               | G910 Orion Spark       | No (Should work)       |
|                        | G810 Orion Spectrum    | Yes                    |

| Keyboard Layout        | Tested                 |
| :--------------------: | :--------------------: |
| QWERTY                 | No (Should work)       |
| AZERTY                 | Yes                    |   

## Contributing

### Adding a keyboard provider

If you are a modder and you want to contribute by adding your keyboard provider, please create a Keyboard Controller class like a did [here](https://github.com/IamBlueSlime/LedMC/blob/master/src/main/java/fr/blueslime/ledmc/peripheral/logitech/LogitechController.java). You just have to implement `IKeyboardController` who contains methods used to do the magic!

```java
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
```

It's important to implement correctly `isCompatible()` because some SDK are compatible with Windows only (like Logitech).

By contributing, you will probably need to support your code during the updates I made to the core of the mod.


### Adding a keyboard layout

TODO.

## License

See [LICENSE](LICENSE).


## Credits

* Logitech for their amazing SDK.


## Author

Jérémy L. ("BlueSlime") - [Website](https://blueslime.fr) - [Twitter](https://twitter.com/iamblueslime)