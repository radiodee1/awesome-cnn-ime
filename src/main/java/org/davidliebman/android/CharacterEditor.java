package org.davidliebman.android;

import com.sun.prism.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

    double [][] screen = new double[28][28];
    boolean write = true;

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

    public void prep() {


        inputPanel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int xx = 0, yy = 0;

                int sizex = inputPanel.getWidth();
                int sizey = inputPanel.getHeight();

                xx = e.getX();
                yy = e.getY();

                int posx = (int) (xx / (float)sizex * 28 );
                int posy = (int) (yy / (float)sizey * 28 );

                OUTLabel.setText(posx + "/" + posy);

                if (posx >= 0 && posx < 28 && posy >= 0 && posy < 28) {
                    if (write) {
                        screen[posx][posy] = 1.0d;
                    }
                    else {
                        screen[posx][posy] = 0.0d;
                    }
                }

                ((ScreenPanel)inputPanel).setScreen(screen);
                inputPanel.revalidate();
                inputPanel.repaint();
                //drawInputPanel();
            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here

        EXITButton = new JButton();
        ENTERButton = new JButton();
        TOGGLEButton = new JButton();
        WRITEERASEButton = new JButton();
        inputPanel =  new ScreenPanel( screen);
        OUTLabel = new JLabel();
        outerPanel = new JPanel();
        prep();
    }
}

class ScreenPanel extends JPanel {

    double [][] screen;

    Graphics gg ;

    public ScreenPanel( double [][] in) {
        super();
        screen = in;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gg = g;

        drawInputPanel(g);

    }

    public void setScreen (double in [][]) {
        screen = in;
    }



    public void drawInputPanel( Graphics g) {

        for (int i = 0; i < 28; i ++) {
            for (int j = 0; j < 28; j ++ ) {
                if (screen[i][j] > 0.5d) {

                    g.setColor(Color.BLUE);
                    g.fillRect(i * 28, j* 28, 25,25);
                }
            }
        }
    }
}