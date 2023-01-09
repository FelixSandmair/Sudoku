import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


//This clas implements the frame used for the sudoku game
public class Sudoku extends JFrame {

    public static int NOBUTTON = 100;
    public static String FONT = "MV Boli";
    private boolean muted = false;
    private final int displayWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
    private final int displayHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
    private final PlayingField playingField;
    private final NumberField numberField;
    private final MyKeyListener mkl;
    private final JButton muteButton = new JButton();
    private final JButton menuWin = new JButton();
    private final JButton loosingScreen = new JButton();
    private final JLabel winningScreen = new JLabel();
    private final JLayeredPane background = new JLayeredPane();
    private final Color[] colors; //0:background1, 1:own numbers, 2:background2, 3:selectedField, 4:grid and fixed numbers
    private final int theme;
    private final Difficulty difficulty; //0 = easy, 1 = medium, 2 = hard
    Sudoku(Difficulty difficulty, Color[] colors, int prevX, int prevY, int prevWidth, int prevHeight, int theme) {
        this.colors = colors;
        this.difficulty = difficulty;
        this.theme = theme;
        this.playingField = new PlayingField(this);
        this.numberField = new NumberField(this);
        this.mkl = new MyKeyListener(playingField);


        setFrame(prevWidth, prevHeight, prevX, prevY);

        repaint();
    }

    public Color getColor(int index) {
        return colors[index];
    }

    //GUI setup functions
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    //sets up the Sudoku frame
    private void setFrame(int prevWidth, int prevHeight, int prevX, int prevY) {
        //Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku");
        setLayout(null);
        setSize(prevWidth, prevHeight);
        setResizable(false);
        setLocation(prevX + prevWidth/2 - getWidth()/2, prevY + prevHeight/2 - getHeight()/2);
        setFocusable(true);
        addKeyListener(mkl);
        setVisible(true);
        add(background);

        //Background Layered Pane setup
        background.setBackground(colors[2]);
        background.setOpaque(true);
        background.setBounds(0,0,displayWidth, displayHeight);
        background.add(playingField, Integer.valueOf(1));
        background.add(numberField, Integer.valueOf(1));
        background.add(winningScreen,Integer.valueOf(1));
        background.add(loosingScreen,Integer.valueOf(1));
        background.add(muteButton, Integer.valueOf(1));

        //setup playing field panel and number field panel and adding it to the background
        playingField.setup(prevWidth, prevHeight, numberField, difficulty);
        numberField.setup(playingField);

        //setup winning screen and make it invisible
        winningScreen.setIcon(new ImageIcon("images\\Theme " + theme + "\\Win.png"));
        winningScreen.setBounds(0,0, winningScreen.getIcon().getIconWidth(), winningScreen.getIcon().getIconHeight());
        winningScreen.setVisible(false);
        winningScreen.add(menuWin);

        //Menu button for winning screen
        menuWin.setIcon(new ImageIcon("images\\Theme " + theme + "\\Menu.png"));
        menuWin.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Menu - Active.png"));
        menuWin.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Menu - Pressed.png"));
        menuWin.setBounds(winningScreen.getWidth()/2 - menuWin.getIcon().getIconWidth()/2, winningScreen.getHeight()/2 - menuWin.getIcon().getIconHeight()/2 - 50,
                menuWin.getIcon().getIconWidth(), menuWin.getIcon().getIconHeight());
        menuWin.setBorderPainted(false);
        menuWin.addActionListener(this::menuWinListener);

        //setting up loosing screen and make it invisible
        loosingScreen.setIcon(new ImageIcon("images\\Theme " + theme + "\\Mistake.png"));
        loosingScreen.setBounds(0,0, loosingScreen.getIcon().getIconWidth(), loosingScreen.getIcon().getIconHeight());
        loosingScreen.setBorderPainted(false);
        loosingScreen.addActionListener(this::loosingListener);
        loosingScreen.setVisible(false);

        //setting mute button
        muteButton.setIcon(new ImageIcon("images\\Theme " + theme + "\\Sound.png"));
        muteButton.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Sound.png"));
        muteButton.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Mute.png"));
        muteButton.setBorderPainted(false);
        muteButton.setBounds(playingField.getX() + playingField.getWidth() + 30, playingField.getY() + playingField.getCellSize()/2 - muteButton.getIcon().getIconHeight()/2, muteButton.getIcon().getIconWidth(), muteButton.getIcon().getIconHeight());
        muteButton.addActionListener(this::muteButtonListener);
    }


    //mute button listener
    private void muteButtonListener(ActionEvent e) {
        if(!muted) {
            muteButton.setIcon(new ImageIcon("images\\Theme " + theme + "\\Mute.png"));
            muteButton.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Mute.png"));
            muteButton.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Sound.png"));
            muted = true;
        } else {
            muteButton.setIcon(new ImageIcon("images\\Theme " + theme + "\\Sound.png"));
            muteButton.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Sound.png"));
            muteButton.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Mute.png"));
            muted = false;
        }
        repaint();
    }

    //menu button listener
    private void menuWinListener(ActionEvent e) {
        dispose();
        SudokuLauncher sL = new SudokuLauncher(getX(), getY());
    }

    //ActionListener for the loosing screen button
    private void loosingListener(ActionEvent e) {
        playingField.deleteNumberFromSelectedCell();
        background.setOpaque(true);
        playingField.setVisible(true);
        numberField.setVisible(true);
        loosingScreen.setVisible(false);
        addKeyListener(mkl);
        repaint();
    }

    //checks if the Sudoku is solved correctly and
    public void checkIfWon() {
        playingField.setVisible(false);
        numberField.setVisible(false);
        background.setOpaque(false);
        removeKeyListener(mkl);
        if (SudokuGenerator.isPerfect(playingField.getGridRows())) {
            winningScreen.setVisible(true);
        } else {
            loosingScreen.setVisible(true);
        }
        repaint();
    }

    //returns column from blockIndex
    public static int getColumn(int blockIndex) {
        int block = blockIndex / 9;
        switch (blockIndex % 3) {
            //column 0, 3 or 6
            case 0 -> {
                return switch (block % 3) {
                    //block 0, 3, 6 => column 0
                    case 0 -> 0;
                    //block 0, 3, 6 => column 0
                    case 1 -> 3;
                    //column 6 last option
                    default -> 6;
                };
            }
            //column 1, 4 or 7
            case 1 -> {
                return switch (block % 3) {
                    //block 0, 3, 6 => column 1
                    case 0 -> 1;
                    //block 0, 3, 6 => column 4
                    case 1 -> 4;
                    //column 7 last option
                    default -> 7;
                };
            }
            //column 2, 5 or 8
            case 2 -> {
                return switch (block % 3) {
                    //block 0, 3, 6 => column 2
                    case 0 -> 2;
                    //block 0, 3, 6 => column 5
                    case 1 -> 5;
                    //column 8 last option
                    default -> 8;
                };
            }
        }
        return 0;
    }

    //returns row from blockIndex
    public static int getRow(int blockIndex) {
        switch (blockIndex % 9) {
            //row 0, 3 or 6
            case 0, 1, 2 -> {
                //block 0, 1 or 2 => row 0
                if (blockIndex < 27) {
                    return 0;
                }
                //block 3, 4 or 5 => row 3
                if (blockIndex < 54) {
                    return 3;
                }
                //column 6 last option
                return 6;
            }
            //row 1, 4 or 7
            case 3, 4, 5 -> {
                //block 0, 1 or 2 => row 1
                if (blockIndex < 27) {
                    return 1;
                }
                //block 3, 4 or 5 => row 4
                if (blockIndex < 54) {
                    return 4;
                }
                //column 7 last option
                return 7;
            }
            //row 2, 5 or 8
            case 6, 7, 8 -> {
                //block 0, 1 or 2 => row 2
                if (blockIndex < 27) {
                    return 2;
                }
                //block 3, 4 or 5 => row 5
                if (blockIndex < 54) {
                    return 5;
                }
                //column 8 last option
                return 8;
            }
            default -> {
                return 0;
            }
        }
    }

    public static int getBlockIndexFromRow(int rowIndex) {
        return ((rowIndex / 3) % 3) * 9 + ((rowIndex % 27) / 9) * 3 + (rowIndex / 27) * 27 + (rowIndex % 3);
    }
}
