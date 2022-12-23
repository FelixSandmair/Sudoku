import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NumberField extends JPanel {
    public static int NOBUTTON = 100;
    public static String schriftart = "MV Boli";
    Sudoku s;
    PlayingField playingField;
    JButton[] notationButtons = new JButton[10];
    JButton[] numberButtons = new JButton[10];
    NumberField(Sudoku s) {
        this.s = s;
    }

    public void setPlayingField(PlayingField playingField) {
        this.playingField = playingField;
    }

    public void setup() {
        setNumberButtons();
        setNotationButtons();
    }

    public void setNumberButtons() {
        setLayout(new GridLayout());
        setPreferredSize(new Dimension(1000, 100));
        for(int i = 0; i < 10; ++i) {
            numberButtons[i] = new JButton();
            numberButtons[i].setFont(new Font(schriftart, Font.PLAIN, 50));
            if(i != 9) {
                numberButtons[i].setText("" + (i + 1));
                numberButtons[i].addActionListener(this::numberButtons);
            } else {
                numberButtons[i].setText("X");
                numberButtons[i].addActionListener(this::deleteButton);
            }
            numberButtons[i].setBackground(s.colors[0]);
            numberButtons[i].setForeground(s.colors[4]);
            numberButtons[i].setBorder(BorderFactory.createLineBorder(s.colors[4],2));
            numberButtons[i].setFocusable(false);
            numberButtons[i].setLayout(null);
            add(numberButtons[i]);
        }
    }

    public void numberButtons(ActionEvent e) {
        int currentIndex = 0;
        for(int i = 0; i < 9; ++i) {
            if(e.getSource() == numberButtons[i]) {
                currentIndex = i;
                break;
            }
        }
        if(playingField.selectedField != NOBUTTON) {
            int row = playingField.getRowIndex(playingField.selectedField + 1);
            int collumn = playingField.getCollumnIndex(playingField.selectedField + 1);
            if(playingField.buttons[playingField.selectedField].getText().equals("")) {
                playingField.counterVec.add(playingField.selectedField);
            }
            playingField.fieldRows[row * 9 + collumn] = currentIndex + 1;
            playingField.fieldCollumns[collumn * 9 + row] = currentIndex + 1;
            playingField.fieldBlocks[playingField.selectedField] = currentIndex + 1;
            playingField.buttons[playingField.selectedField].setText("" + (currentIndex + 1));
            for(int i = 0; i < 9; i++) {
                playingField.buttonLayer[playingField.selectedField * 9 + i].setVisible(false);
                notationButtons[i].setBackground(s.colors[0]);
            }
            this.repaint();
        }
        if(playingField.counterVec.size() == 81) {
            s.checkIfWon();
        }
    }

    public void deleteButton(ActionEvent e) {
        if(playingField.selectedField != NOBUTTON) {
            if (!playingField.buttons[playingField.selectedField].getText().equals("")) {
                playingField.counterVec.remove(playingField.selectedField);
                int row = playingField.getRowIndex(playingField.selectedField + 1);
                int collumn = playingField.getCollumnIndex(playingField.selectedField + 1);
                playingField.fieldRows[row * 9 + collumn] = 0;
                playingField.fieldCollumns[collumn * 9 + row] = 0;
                playingField.fieldBlocks[playingField.selectedField] = 0;
                playingField.buttons[playingField.selectedField].setText("");
                for(int i = 0; i < 9; i++) {
                    playingField.buttonLayer[playingField.selectedField * 9 + i].setVisible(true);
                    if(!playingField.buttonLayer[playingField.selectedField * 9 + i].getText().equals("")) {
                        notationButtons[i].setBackground(s.colors[3]);
                    }
                }
                this.repaint();
            }
        }
    }

    public void setNotationButtons() {
        for(int i = 0; i < 10; i++) {
            notationButtons[i] = new JButton();
            if(i != 9) {
                notationButtons[i].setText("" + (i + 1));
                notationButtons[i].addActionListener(this::notationButtonListener);
            } else {
                notationButtons[9].setText("X");
                notationButtons[9].addActionListener(this::notationDeleteButtonListener);
            }
            notationButtons[i].setFont(new Font(schriftart, Font.BOLD, 20));
            notationButtons[i].setBackground(s.colors[0]);
            notationButtons[i].setBorder(BorderFactory.createLineBorder(s.colors[4],2));
            notationButtons[i].setVerticalAlignment(JButton.CENTER);
            notationButtons[i].setHorizontalAlignment(JButton.CENTER);
            notationButtons[i].setFocusable(false);
            notationButtons[i].setOpaque(true);
            notationButtons[i].setBounds(0,0,30,30);
            notationButtons[i].setForeground(s.colors[4]);
            numberButtons[i].add(notationButtons[i]);
        }
    }

    public void notationButtonListener(ActionEvent e) {
        if(playingField.selectedField != NOBUTTON && playingField.buttons[playingField.selectedField].getText().equals("")) {
            int currentIndex = 0;
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == notationButtons[i]) {
                    currentIndex = i;
                    break;
                }
            }
            if (playingField.buttonLayer[playingField.selectedField * 9 + currentIndex].getText().equals("")) {
                playingField.buttonLayer[playingField.selectedField * 9 + currentIndex].setText("" + (currentIndex + 1));
                notationButtons[currentIndex].setBackground(s.colors[3]);
            } else {
                playingField.buttonLayer[playingField.selectedField * 9 + currentIndex].setText("");
                notationButtons[currentIndex].setBackground(s.colors[0]);
            }
        }
    }

    public void notationDeleteButtonListener(ActionEvent e) {
        if(playingField.selectedField != NOBUTTON) {
            for(int i = 0; i < 9; i++) {
                playingField.buttonLayer[playingField.selectedField * 9 + i].setText("");
                notationButtons[i].setBackground(s.colors[0]);
            }
        }
    }
}
