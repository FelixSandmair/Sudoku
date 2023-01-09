import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


//Panel that hold the buttons used to insert notations and numbers in the sudoku grid
public class NumberField extends JPanel {
    private final Sudoku s;
    private PlayingField playingField;
    private final JButton[] notationButtons = new JButton[10];
    private final JButton[] numberButtons = new JButton[10];
    NumberField(Sudoku s) {
        this.s = s;
    }

    //this method is used to set up the number field with the corresponding playing field
    public void setup(PlayingField playingField) {
        this.playingField = playingField;

        //set up the panel
        setLayout(new GridLayout(1,10,15,0));
        setSize(playingField.getCellSize() * 10 + 15 * 9, playingField.getCellSize());
        setBackground(s.getColor(2));
        setBounds(750-getWidth()/2, playingField.getY() + playingField.getHeight() + 50, getWidth(), getHeight());

        //setup all the buttons
        setNumberButtons();
        setNotationButtons();
    }

    //This method sets up the buttons for the Big Numbers to put into the sudoku cell
    private void setNumberButtons() {
        for(int button = 0; button < 10; ++button) {
            numberButtons[button] = new JButton();

            //if button index is not 9 then it's a number button else it's the delete button
            if(button != 9) {
                numberButtons[button].setText("" + (button + 1));
                numberButtons[button].addActionListener(this::numberButtons);
            } else {
                numberButtons[button].setText("X");
                numberButtons[button].addActionListener(this::deleteButton);
            }

            //general button settings
            numberButtons[button].setFont(new Font(Sudoku.FONT, Font.PLAIN, 50));
            numberButtons[button].setBackground(s.getColor(0));
            numberButtons[button].setForeground(s.getColor(4));
            numberButtons[button].setBorder(BorderFactory.createLineBorder(s.getColor(4),3));
            numberButtons[button].setFocusable(false);
            numberButtons[button].setLayout(null);
            add(numberButtons[button]);
        }
    }

    //this method is the action listener for the number buttons
    //Once a number button is pressed we insert the number that was pressed into the selected cell of the playing field
    private void numberButtons(ActionEvent e) {
        //check which of the numbers was pressed
        int numberPressed = 0;
        for(int number = 0; number < 9; ++number) {
            if (e.getSource() == numberButtons[number]) {
                numberPressed = number + 1;
                break;
            }
        }

        playingField.setButtonNumber(numberPressed);
    }

    //this method is the listener for the delete button
    //it deletes the number from the selected field in the playing field and restores the old notes
    private void deleteButton(ActionEvent e) {
        playingField.deleteNumberFromSelectedCell();
    }

    //set up the buttons for inserting the notes on the sudoku grid
    private void setNotationButtons() {
        //loop for all the buttons
        for(int button = 0; button < 10; button++) {
            notationButtons[button] = new JButton();

            //if button index is 9 it's one of the number buttons else it's the delete button
            if(button != 9) {
                notationButtons[button].setText("" + (button + 1));
                notationButtons[button].addActionListener(this::notationButtonListener);
            } else {
                notationButtons[9].setText("X");
                notationButtons[9].addActionListener(this::notationDeleteButtonListener);
            }

            //general settings for the buttons
            notationButtons[button].setFont(new Font(Sudoku.FONT, Font.BOLD, 20));
            notationButtons[button].setBackground(s.getColor(0));
            notationButtons[button].setBorder(BorderFactory.createLineBorder(s.getColor(4),2));
            notationButtons[button].setVerticalAlignment(JButton.CENTER);
            notationButtons[button].setHorizontalAlignment(JButton.CENTER);
            notationButtons[button].setFocusable(false);
            notationButtons[button].setOpaque(true);
            notationButtons[button].setBounds(0,0,30,30);
            notationButtons[button].setForeground(s.getColor(4));
            numberButtons[button].add(notationButtons[button]);
        }
    }

    //this method is the listener for the number notation buttons
    private void notationButtonListener(ActionEvent e) {
        int numberPressed = 0;
        for (int number = 0; number < 9; number++) {
            if (e.getSource() == notationButtons[number]) {
                numberPressed = number + 1;
                break;
            }
        }
        playingField.setNotationLayerNumber(numberPressed);
    }

    //this method is the listener for the button that deletes all notes
    private void notationDeleteButtonListener(ActionEvent e) {
        playingField.deleteAllNotesInSelectedCell();
    }

    //public method to mark a notation button as active
    public void setNotationMarked(int number) {
        notationButtons[number].setBackground(s.getColor(3));
    }

    //public method to mark a notation button as inactive
    public void setNotationUnmarked(int number) {
        notationButtons[number].setBackground(s.getColor(0));
    }
}
