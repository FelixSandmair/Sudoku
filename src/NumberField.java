import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NumberField extends JPanel {
    private Sudoku s;
    private PlayingField playingField;
    private JButton[] notationButtons = new JButton[10];
    private JButton[] numberButtons = new JButton[10];
    NumberField(Sudoku s) {
        this.s = s;
    }

    public void setup(PlayingField playingField) {
        this.playingField = playingField;
        setNumberButtons();
        setNotationButtons();
    }

    public void setNumberButtons() {
        setLayout(new GridLayout(1,10,15,0));
        setSize(playingField.getButtonSize() * 10 + 15 * 9, playingField.getButtonSize());
        setBackground(s.getColor(2));
        setBounds(750-getWidth()/2, playingField.getY() + playingField.getHeight() + 50, getWidth(), getHeight());
        for(int i = 0; i < 10; ++i) {
            numberButtons[i] = new JButton();
            numberButtons[i].setFont(new Font(Sudoku.FONT, Font.PLAIN, 50));
            if(i != 9) {
                numberButtons[i].setText("" + (i + 1));
                numberButtons[i].addActionListener(this::numberButtons);
            } else {
                numberButtons[i].setText("X");
                numberButtons[i].addActionListener(this::deleteButton);
            }
            numberButtons[i].setBackground(s.getColor(0));
            numberButtons[i].setForeground(s.getColor(4));
            numberButtons[i].setBorder(BorderFactory.createLineBorder(s.getColor(4),3));
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
        playingField.setButtonNumber(currentIndex + 1);
    }

    public void deleteButton(ActionEvent e) {
        playingField.deleteNumberFromSelectedCell();
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
            notationButtons[i].setFont(new Font(Sudoku.FONT, Font.BOLD, 20));
            notationButtons[i].setBackground(s.getColor(0));
            notationButtons[i].setBorder(BorderFactory.createLineBorder(s.getColor(4),2));
            notationButtons[i].setVerticalAlignment(JButton.CENTER);
            notationButtons[i].setHorizontalAlignment(JButton.CENTER);
            notationButtons[i].setFocusable(false);
            notationButtons[i].setOpaque(true);
            notationButtons[i].setBounds(0,0,30,30);
            notationButtons[i].setForeground(s.getColor(4));
            numberButtons[i].add(notationButtons[i]);
        }
    }

    public void notationButtonListener(ActionEvent e) {
        int currentIndex = 0;
        for (int i = 0; i < 9; i++) {
            if (e.getSource() == notationButtons[i]) {
                currentIndex = i;
                break;
            }
        }
        playingField.setNotationLayerNumber(currentIndex + 1);
    }

    public void notationDeleteButtonListener(ActionEvent e) {
        playingField.deletAllNotesInSelectedCell();
    }

    public void setNotationMarked(int number) {
        notationButtons[number].setBackground(s.getColor(3));
    }

    public void setNotationUnmarked(int number) {
        notationButtons[number].setBackground(s.getColor(0));
    }
}
