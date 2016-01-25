package org.davidliebman.android;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    double[][] screen = new double[28][28];
    int type = Operation.EVAL_SINGLE_ALPHA_LOWER;
    boolean write = true;
    public boolean hasInput = false;
    String output = "";

    Operation [] operations;


    int marginTop = 5, marginBottom = 5, marginLeft = 5, marginRight = 5;

    public CharacterEditor() {

        JFrame frame = new JFrame("CharacterEditor");
        frame.setContentPane(outerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public void addOperations ( Operation op1, Operation op2, Operation op3) {
        operations = new Operation[] {op1,op2,op3};
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CharacterEditor");
        frame.setContentPane(new CharacterEditor().outerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public void prep() {

        ENTERButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //
                hasInput = true;
                System.out.println("hasInput " + operations.length);

                if (operations.length == 3) {
                    try {
                        switch (type) {
                            case Operation.EVAL_SINGLE_ALPHA_LOWER:
                                operations[0].startOperation(getScreen());
                                setOutput(operations[0].getOutput());
                                break;
                            case Operation.EVAL_SINGLE_ALPHA_UPPER:
                                operations[1].startOperation(getScreen());
                                setOutput(operations[1].getOutput());
                                break;
                            case Operation.EVAL_SINGLE_NUMERIC:
                                operations[2].startOperation(getScreen());
                                setOutput(operations[2].getOutput());
                                break;
                        }
                    } catch (Exception p) {
                        p.printStackTrace();
                    }
                }
            }
        });

        //CLEAR
        EXITButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 28; i ++ ) {
                    for (int j = 0; j < 28; j ++ ) {
                        screen[i][j] = 0.0d;
                    }
                }
                hasInput = false;
                inputPanel.revalidate();
                inputPanel.repaint();
            }

        });

        TOGGLEButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (type) {
                    case Operation.EVAL_SINGLE_ALPHA_LOWER:
                        TOGGLEButton.setText("UPPER");
                        type = Operation.EVAL_SINGLE_ALPHA_UPPER;
                        break;
                    case Operation.EVAL_SINGLE_ALPHA_UPPER:
                        TOGGLEButton.setText("#NUM#");
                        type = Operation.EVAL_SINGLE_NUMERIC;
                        break;
                    case Operation.EVAL_SINGLE_NUMERIC:
                        TOGGLEButton.setText("lower");
                        type = Operation.EVAL_SINGLE_ALPHA_LOWER;
                        break;
                    default:
                        TOGGLEButton.setText("lower");
                        type = Operation.EVAL_SINGLE_ALPHA_LOWER;
                        break;
                }
            }
        });

        WRITEERASEButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(WRITEERASEButton.getModel().isPressed() || true) {
                    if (write) {
                        write = false;
                        WRITEERASEButton.setText("ERASE");
                        //System.out.println("erase");
                    }
                    else {
                        write = true;
                        WRITEERASEButton.setText("WRITE");
                        //System.out.println("write");
                    }
                }
                //OUTLabel.setText("data " + e.getSource().toString());
            }
        });

        inputPanel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int xx = 0, yy = 0;

                int sizex = inputPanel.getWidth() - (marginRight + marginLeft);
                int sizey = inputPanel.getHeight() - (marginTop + marginBottom);

                xx = e.getX() - marginLeft;
                yy = e.getY() - marginTop;

                int posx = (int) (xx / (float) sizex * 28);
                int posy = (int) (yy / (float) sizey * 28);

                //OUTLabel.setText(posx + "/" + posy);

                if (posx >= 0 && posx < 28 && posy >= 0 && posy < 28) {
                    if (write) {
                        screen[posx][posy] = 1.0d;
                    } else {
                        screen[posx][posy] = 0.0d;
                    }
                }

                //((ScreenPanel)inputPanel).setScreen(screen);
                //hasInput = false;
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

    public int getType() {
        int out = type;
        if (!hasInput) out = -1;
        return out;
    }

    public double [][] getScreen () {
        return screen;
    }

    public void setOutput(String in) {
        hasInput = false;

        output = in;
        OUTLabel.setText(output);

    }

    public boolean getHasInput() {
        return hasInput;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        EXITButton = new JButton();
        ENTERButton = new JButton();
        TOGGLEButton = new JButton();
        WRITEERASEButton = new JButton();
        inputPanel = new ScreenPanel();
        OUTLabel = new JLabel();
        outerPanel = new JPanel();

        //WRITEERASEButton.setText("WRITE");

        prep();
    }


    class ScreenPanel extends JPanel {


        Graphics gg;

        public ScreenPanel() {
            super();
            //screen = in;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            gg = g;

            drawInputPanel(g);

        }



        public void drawInputPanel(Graphics g) {
            float xx = (this.getWidth() - (marginLeft+marginRight)) / 28.0f;
            float yy = (this.getHeight() - (marginTop+marginBottom)) / 28.0f;

            g.setColor(Color.BLACK);
            g.fillRect(0,this.getHeight() - 2, this.getWidth(), 2);

            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++) {
                    if (screen[i][j] > 0.5d) {
                        int xpos = (int) (i * xx) + marginLeft;
                        int ypos = (int) (j * yy) + marginTop;
                        g.setColor(Color.BLUE);
                        g.fillRect(xpos, ypos, (int) (xx - 2), (int) (yy - 2));
                    }
                }
            }
        }
    }

}