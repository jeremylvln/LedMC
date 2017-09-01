package fr.blueslime.ledmc;

import fr.blueslime.ledmc.peripheral.EnumKeyboardLayout;
import fr.blueslime.ledmc.peripheral.EnumKeyboardProvider;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import javax.swing.*;
import java.awt.event.*;

public class GuiConfiguration extends JDialog
{
    private static final String TITLE = "LedMC - Configuration";

    private final Configuration configuration;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> keyboardProvider;
    private JComboBox<String> keyboardLayout;
    private JRadioButton noRGBKeyboard;
    private JRadioButton haveRGBKeyboard;

    GuiConfiguration(Configuration configuration)
    {
        this.configuration = configuration;

        this.setContentPane(this.contentPane);
        this.setModal(true);
        this.setAlwaysOnTop(true);
        this.setTitle(TITLE);
        this.getRootPane().setDefaultButton(this.buttonOK);

        this.buttonOK.addActionListener(event -> this.onOK());
        this.buttonCancel.addActionListener(event -> this.onCancel());

        // call onCancel() when cross is clicked
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent event)
            {
                GuiConfiguration.this.onCancel();
            }
        });

        ButtonGroup rgbGroup = new ButtonGroup();
        rgbGroup.add(this.noRGBKeyboard);
        rgbGroup.add(this.haveRGBKeyboard);

        this.noRGBKeyboard.addActionListener(event ->
        {
            this.keyboardProvider.setEnabled(false);
            this.keyboardLayout.setEnabled(false);
        });

        this.haveRGBKeyboard.addActionListener(event ->
        {
            this.keyboardProvider.setEnabled(true);
            this.keyboardLayout.setEnabled(true);
        });

        this.keyboardProvider.setEnabled(false);
        this.keyboardLayout.setEnabled(false);

        // call onCancel() on ESCAPE
        this.contentPane.registerKeyboardAction(event -> this.onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        for (EnumKeyboardProvider keyboardProviderInEnum : EnumKeyboardProvider.values())
            if (keyboardProviderInEnum != EnumKeyboardProvider.NONE)
                this.keyboardProvider.addItem(keyboardProviderInEnum.getName());

        for (EnumKeyboardLayout keyboardLayoutInEnum : EnumKeyboardLayout.values())
            this.keyboardLayout.addItem(keyboardLayoutInEnum.getName());
    }

    private void onOK()
    {
        if (this.noRGBKeyboard.isSelected())
        {
            this.configuration.get("general", "keyboard_provider", EnumKeyboardProvider.NONE.getName());
        }
        else
        {
            this.configuration.get("general", "keyboard_provider", (String) this.keyboardProvider.getSelectedItem());
            this.configuration.get("general", "keyboard_layout", (String) this.keyboardLayout.getSelectedItem());
        }

        this.dispose();
    }

    private void onCancel()
    {
        int selectedOption = JOptionPane.showConfirmDialog(this, "If you cancel this configuration, the mod will be disabled. Are you sure?", TITLE, JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (selectedOption == JOptionPane.OK_OPTION)
            this.dispose();
    }
}
