package org.davidliebman.android;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dave on 1/23/16.
 */
public class CharacterEditor {
    private JButton EXITButton;
    private JButton ENTERButton;
    private JButton TOGGLEButton;
    private JButton WRITEERASEButton;
    private JPanel inputPanel;
    private JLabel OUTLabel;
    private JPanel outerPanel;


    public CharacterEditor() {
        /*
        JFrame frame = new JFrame("CharacterEditor");
        frame.setContentPane(new CharacterEditor().outerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        */
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("CharacterEditor");
        frame.setContentPane(new CharacterEditor().outerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        EXITButton = new JButton();
        ENTERButton = new JButton();
        TOGGLEButton = new JButton();
        WRITEERASEButton = new JButton();
        inputPanel = new JPanel( );
        OUTLabel = new JLabel();
        outerPanel = new JPanel();

    }
}
