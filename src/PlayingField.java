import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class PlayingField extends JPanel {
    public static int NOBUTTON = 100;
    public static String schriftart = "MV Boli";
    Sudoku s;
    NumberField numberField;
    JPanel[] squares = new JPanel[9];
    JButton[] buttons = new JButton[9 * 9];
    JLabel[] buttonLayer = new JLabel[81 * 9];
    int[] fieldBlocks = new int[81];
    int[] fieldRows = new int[81];
    int[] fieldCollumns = new int[81];
    Vector<Integer> counterVec = new Vector<>();
    int selectedField = NOBUTTON;

    PlayingField(Sudoku s) {
        this.s = s;
    }

    public void setup() {
        setSquares();
        setButtons();
        setButtonLayer();
        setPlayingField(new int[]{0,1,3,7,5,4,9,8,0,7,0,4,3,9,8,1,5,6,5,8,9,6,2,1,4,7,3,2,7,5,1,9,6,3,4,8,4,6,3,5,8,2,9,7,1,9,1,8,3,4,7,2,5,6,8,2,1,5,6,7,4,3,9,6,3,5,8,4,9,2,1,7,7,9,4,1,3,2,8,6,0});
    }

    public void setNumberField(NumberField numberField) {
        this.numberField = numberField;
    }

    public void setSquares() {
        this.setLayout(new GridLayout(3, 3, 5,5));
        for (int i = 0; i < squares.length; ++i) {
            squares[i] = new JPanel();
            squares[i].setLayout(new GridLayout(3, 3,1,1));
            squares[i].setBackground(s.colors[4]);
            //squares[i].setBorder(BorderFactory.createLineBorder(s.colors[4], 4));
            this.setBackground(s.colors[4]);
            this.add(squares[i]);
        }
    }

    public void setButtons() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                buttons[i * 9 + j] = new JButton();
                buttons[i * 9 + j].addActionListener(this::playButtons);
                buttons[i * 9 + j].setFont(new Font(schriftart, Font.PLAIN, 50));
                buttons[i * 9 + j].setFocusable(false);
                buttons[i * 9 + j].setBackground(i % 2 == 0 ? s.colors[0] : s.colors[2]);
                buttons[i * 9 + j].setForeground(s.colors[1]);
                buttons[i * 9 + j].setBorder(BorderFactory.createLineBorder(s.colors[4], 1));
                buttons[i * 9 + j].setLayout(new GridLayout(3, 3));
                squares[i].add(buttons[i * 9 + j]);
            }
        }
    }

    public void playButtons(ActionEvent e) {
        int currentIndex = 0;
        int currentBlock = 0;
        for (int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if (e.getSource() == buttons[i*9+j]) {
                    currentIndex = i*9+j;
                    currentBlock = i;
                    break;
                }
            }
        }
        if (selectedField == currentIndex) {  //Alle button inaktiv setzen
            buttons[currentIndex].setBackground(currentBlock%2 == 0 ? s.colors[0] : s.colors[2]);
            for (int i = 0; i < 9; i++) {
                numberField.notationButtons[i].setBackground(s.colors[0]);
            }
            selectedField = NOBUTTON;
            return;
        }
        if (selectedField != NOBUTTON) {
            buttons[selectedField].setBackground((selectedField/9)%2 == 0 ? s.colors[0] : s.colors[2]);
            for (int i = 0; i < 9; i++) {
                numberField.notationButtons[i].setBackground(s.colors[0]);
            }
        }
        selectedField = currentIndex;
        buttons[currentIndex].setBackground(s.colors[3]);
        for (int i = 0; i < 9; i++) {
            if (!buttonLayer[selectedField * 9 + i].getText().equals("")) {
                numberField.notationButtons[i].setBackground(s.colors[3]);
            }
        }
        this.repaint();
    }

    public void setButtonLayer() {
        for (int i = 0; i < 81; i++) {
            for (int j = 0; j < 9; j++) {
                buttonLayer[i * 9 + j] = new JLabel();
                buttonLayer[i * 9 + j].setFont(new Font(schriftart, Font.BOLD, 20));
                buttonLayer[i * 9 + j].setForeground(s.colors[1]);
                buttonLayer[i * 9 + j].setVerticalAlignment(JLabel.CENTER);
                buttonLayer[i * 9 + j].setHorizontalAlignment(JLabel.CENTER);
                buttons[i].add(buttonLayer[i * 9 + j]);
            }
        }
    }

    public void setPlayingField(int[] configuration) {
        fieldBlocks = configuration;
        int row;
        int collumn;
        for (int i = 0; i < configuration.length; ++i) {
            if (configuration[i] != 0) {
                row = getRowIndex(i + 1);
                collumn = getCollumnIndex(i + 1);
                fieldRows[row * 9 + collumn] = configuration[i];
                fieldCollumns[collumn * 9 + row] = configuration[i];
                buttons[i].setText("" + configuration[i]);
                buttons[i].setRolloverEnabled(false);
                MouseListener[] listeners = buttons[i].getMouseListeners();
                buttons[i].setForeground(s.colors[4]);
                for (MouseListener ml : listeners) {
                    buttons[i].removeMouseListener(ml);
                }
                counterVec.add(i);
            }
        }
    }

    public static int getRowIndex(int blockIndex) {
        switch (blockIndex % 9) {
            case 1,2,3: //row 1, 4 or 7
                if(blockIndex <= 27) {
                    return 0;
                }
                if(blockIndex <= 54) {
                    return 3;
                }
                return 6;
            case 4,5,6: //row 2, 5 or 8
                if(blockIndex <= 27) {
                    return 1;
                }
                if(blockIndex <= 54) {
                    return 4;
                }
                return 7;
            case 7,8,0: //row 3, 6 or 9
                if(blockIndex <= 27) {
                    return 2;
                }
                if(blockIndex <= 54) {
                    return 5;
                }
                return 8;
            default:
                return 0;
        }
    }

    public static int getCollumnIndex(int blockIndex) {
        switch (blockIndex % 3) {
            case 1: //collumn 1, 4 or 7
                for (int i = 1; i <= 9; ++i) {
                    if (blockIndex <= i * 9) {
                        if (i % 3 == 1) {
                            return 0;
                        }
                        if (i % 3 == 2) {
                            return 3;
                        }
                        return 6;
                    }
                }
            case 2://collumn 2, 5 or 8
                for (int i = 1; i <= 9; ++i) {
                    if (blockIndex <= i * 9) {
                        if (i % 3 == 1) {
                            return 1;
                        }
                        if (i % 3 == 2) {
                            return 4;
                        }
                        return 7;
                    }
                }
            case 0://collumn 3, 6 or 9
                for (int i = 1; i <= 9; ++i) {
                    if (blockIndex <= i * 9) {
                        if (i % 3 == 1) {
                            return 2;
                        }
                        if (i % 3 == 2) {
                            return 5;
                        }
                        return 8;
                    }
                }
        }
        return 0;
    }
}

