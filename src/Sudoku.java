import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class Sudoku extends JFrame{

    public static int NOBUTTON = 100;
    public static Color myGreen = new Color(0x0da336);

    public static String schriftart = "MV Boli";

    int displayWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
    int displayHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();

    MyKeyListener mkl = new MyKeyListener(this);
    PlayingField playingField = new PlayingField(this);

    NumberField numberField = new NumberField(this);
    JButton wrong = new JButton();
    Color[] colors; //0:background1, 1:own numbers, 2:background2, 3:selectedField, 4:grid and fixed numbers
    int difficulty; //0 = easy, 1 = medium, 2 = hard
    Sudoku(int difficulty, Color[] colors, int prevX, int prevY) {
        this.colors = colors;
        this.difficulty = difficulty;
        setFrame();
        playingField.setNumberField(numberField);
        numberField.setPlayingField(playingField);
        playingField.setup();
        numberField.setup();
        /*setPlayingField(new int[]{  0,1,3,                      //1 2 3
                                    0,5,4, //1                  //4 5 6
                                    0,8,2,                      //7 8 9

                7,0,4,
                0,9,0, //2
                1,5,6,

                5,8,9,
                0,0,0, //3
                0,7,3,

                0,7,0,
                1,9,0, //4
                3,0,0,

                0,6,0,
                0,0,0, //5
                0,0,1,

                0,0,0,
                0,4,7,  //6
                2,5,0,

                0,0,0,
                5,0,0,  //7
                0,3,0,

                6,3,0,
                0,0,0,  //8
                2,0,7,

                7,9,0,
                0,0,2,  //9
                0,0,0,});*/
        this.setLocation(prevX + 750 - getWidth()/2, prevY + 530 - getHeight()/2);
        this.repaint();
    }

    public void setFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sudoku");
        this.setLayout(new BorderLayout());
        this.setSize((displayHeight / 100) * 85, (displayHeight / 100) * 85);
        this.setResizable(false);
        this.setVisible(true);
        this.addKeyListener(mkl);
        this.add(playingField, BorderLayout.CENTER);
        this.add(numberField, BorderLayout.SOUTH);
    }

    public void wrong(ActionEvent e) {
        int row = playingField.getRowIndex(playingField.selectedField + 1);
        int collumn = playingField.getCollumnIndex(playingField.selectedField + 1);
        playingField.buttons[playingField.selectedField].setText("");
        playingField.fieldBlocks[playingField.selectedField] = 0;
        playingField.fieldRows[row * 9 + collumn] = 0;
        playingField.fieldCollumns[collumn * 9 + row] = 0;
        if(playingField.counterVec.contains(playingField.selectedField)) {
            playingField.counterVec.remove(playingField.selectedField);
        }
        this.remove(wrong);
        this.add(playingField, BorderLayout.CENTER);
        this.add(numberField, BorderLayout.SOUTH);
        this.repaint();
    }

    public void checkIfWon() {
        Vector<Integer> help = new Vector<>();
        boolean won = true;
        for(int i = 0; i < 9 && won; i++) {
            for(int j = 0; j < 9 && won; j++) {
                if(help.contains(playingField.fieldRows[i*9 + j])) {
                    won = false;
                } else {
                    help.add(playingField.fieldRows[i*9 + j]);
                }
            }
            System.out.println("row " + i + " is " + won);
            help = new Vector<>();
        }
        for(int i = 0; i < 9 && won; i++) {
            for(int j = 0; j < 9 && won; j++) {
                if(help.contains(playingField.fieldCollumns[i*9 + j])) {
                    won = false;
                } else {
                    help.add(playingField.fieldCollumns[i*9 + j]);
                }
            }
            System.out.println("Collumn " + i + " is " + won);
            help = new Vector<>();
        }
        for(int i = 0; i < 9 && won; i++) {
            for(int j = 0; j < 9 && won; j++) {
                if(help.contains(playingField.fieldBlocks[i*9 + j])) {
                    won = false;
                } else {
                    help.add(playingField.fieldBlocks[i*9 + j]);
                }
            }
            System.out.println("Block " + i + " is " + won);
            help = new Vector<>();
        }
        if(won) {
            this.remove(playingField);
            this.remove(numberField);
            JLabel finishScreen = new JLabel();
            finishScreen.setText("You have won!");
            finishScreen.setFont(new Font(schriftart, Font.BOLD, 100));
            finishScreen.setVerticalTextPosition(JLabel.CENTER);
            finishScreen.setHorizontalAlignment(JLabel.CENTER);
            finishScreen.setBackground(myGreen);
            finishScreen.setOpaque(true);
            this.add(finishScreen);
            this.repaint();
        } else {
            wrong.setText("Your Sudoku is not solved correctly");
            wrong.setBackground(Color.red);
            wrong.setFont(new Font(schriftart, Font.BOLD, 100));
            wrong.setFocusable(false);
            wrong.addActionListener(this::wrong);
            this.remove(playingField);
            this.remove(numberField);
            this.add(wrong);
            this.repaint();
        }
    }
}
