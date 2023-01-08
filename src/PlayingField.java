import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class PlayingField extends JPanel {
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    private Sudoku s;
    private NumberField numberField;
    private JPanel[] blocks = new JPanel[9];
    private JButton[] buttons = new JButton[9 * 9];
    private JLabel[] notationLayer = new JLabel[81 * 9];
    private int[] gridBlocks;
    private int[] gridRows;
    private int[] gridColumns;
    private Vector<Integer> counterVec = new Vector<>();
    private int selectedCell = Sudoku.NOBUTTON;

    PlayingField(Sudoku s) {
        this.s = s;
    }

    //getter functions
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public int[] getGridRows() {
        return gridRows;
    }


    //Functions used for initial setup
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    public void setup(int width, int height, NumberField numberField, Difficulty difficulty) {
        this.numberField = numberField;

        //generate sudoku grid of desired difficulty
        SudokuGenerator sGen = new SudokuGenerator(difficulty);
        gridBlocks = sGen.getGridBlocks();
        gridRows = sGen.getGridRows();
        gridColumns = sGen.getGridColumns();

        gridBlocks[0] = 0;
        gridRows[0] = 0;
        gridColumns[0] = 0;

        //setup this panel
        setSize((height*75)/100, (height*75)/100);
        setBounds(width/2 - getWidth()/2,30,getWidth(), getHeight());
        setLayout(new GridLayout(3, 3, 4,4));
        setBorder(BorderFactory.createLineBorder(s.getColor(4),4));
        setBackground(s.getColor(4));

        setBlocks();
        setButtons();
        setNotationLayer();
    }

    //setups the block panels
    private void setBlocks() {
        for (int i = 0; i < blocks.length; ++i) {
            blocks[i] = new JPanel();
            blocks[i].setLayout(new GridLayout(3, 3,1,1));
            blocks[i].setBackground(s.getColor(4));
            add(blocks[i]);
        }
    }

    //setups all cell buttons based on the playing fields start configuration
    private void setButtons() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                //basic button configuration
                buttons[i * 9 + j] = new JButton();
                buttons[i * 9 + j].addActionListener(this::playButtons);
                buttons[i * 9 + j].setFont(new Font(Sudoku.FONT, Font.PLAIN, 50));
                buttons[i * 9 + j].setFocusable(false);
                buttons[i * 9 + j].setBackground(i % 2 == 0 ? s.getColor(0) : s.getColor(2));
                buttons[i * 9 + j].setForeground(s.getColor(1));
                buttons[i * 9 + j].setBorder(BorderFactory.createLineBorder(s.getColor(4), 1));
                buttons[i * 9 + j].setLayout(new GridLayout(3, 3));
                blocks[i].add(buttons[i * 9 + j]);

                //set the buttons that contain a number in the start configuration
                if(gridBlocks[i * 9 + j] != 0) {
                    buttons[i * 9 + j].setText("" + gridBlocks[i * 9 + j]);
                    buttons[i * 9 + j].setRolloverEnabled(false);
                    buttons[i * 9 + j].setForeground(s.getColor(4));
                    counterVec.add(i * 9 + j);
                }
            }
        }
    }

    //setups all notationLayer labels
    private void setNotationLayer() {
        for (int i = 0; i < 81; i++) {
            for (int j = 0; j < 9; j++) {
                notationLayer[i * 9 + j] = new JLabel();
                notationLayer[i * 9 + j].setFont(new Font(Sudoku.FONT, Font.BOLD, 20));
                notationLayer[i * 9 + j].setForeground(s.getColor(1));
                notationLayer[i * 9 + j].setVerticalAlignment(JLabel.CENTER);
                notationLayer[i * 9 + j].setHorizontalAlignment(JLabel.CENTER);
                buttons[i].add(notationLayer[i * 9 + j]);
            }
        }
    }


    //Action Listeners for Buttons
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    private void playButtons(ActionEvent e) {
        //Searches for index of the button that was pressed
        int currentIndex = 0;
        for (int i = 0; i < 81; i++) {
            if (e.getSource() == buttons[i]) {
                currentIndex = i;
                break;
            }
        }

        //do nothing if cell is already selected
        if(currentIndex == selectedCell) {
            return;
        }

        //Block of previously selected cell
        int block = selectedCell/9;

        //If there was a cell selected then we need to remove the selection color from this button
        if (selectedCell != Sudoku.NOBUTTON) {
            buttons[selectedCell].setBackground(block%2 == 0 ? s.getColor(0) : s.getColor(2));
            for (int i = 0; i < 9; i++) {
                numberField.setNotationUnmarked(i);
            }
        }

        //We set the color of the newly selected cell and update selectedCell variable
        selectedCell = currentIndex;
        buttons[currentIndex].setBackground(s.getColor(3));
        for (int i = 0; i < 9; i++) {
            if (!notationLayer[selectedCell * 9 + i].getText().equals("")) {
                numberField.setNotationMarked(i);
            }
        }
        s.repaint();
    }


    //public Interface functions
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    //inserts Number at selected Field
    public void setButtonNumber(int number) {
        //check if there is a cell currently selected
        if(selectedCell != Sudoku.NOBUTTON && !buttons[selectedCell].getForeground().equals(s.getColor(4))) {
            int row = Sudoku.getRow(selectedCell);
            int column = Sudoku.getColumn(selectedCell);
            int block = selectedCell/9;

            //if the button was empty previously we need to add the cell to the counterVec
            if(buttons[selectedCell].getText().equals("")) {
                counterVec.add(selectedCell);
            }

            //change grid representations and button text
            gridRows[row * 9 + column] = number;
            gridColumns[column * 9 + row] = number;
            gridBlocks[selectedCell] = number;
            buttons[selectedCell].setText("" + number);

            //set all notes of the cell to not be visible
            for(int i = 0; i < 9; i++) {
                notationLayer[selectedCell * 9 + i].setVisible(false);
                numberField.setNotationUnmarked(i);
            }

            //delete all notes base on the number inserted
            /*for(int i = 0; i < 9; i++) {
                notationLayer[block * 81 + i * 9 + number - 1].setText("");
                notationLayer[((row / 3) * 27 + (row % 3) * 3 + (i / 3) * 9 + (i % 3)) * 9 + number - 1].setText("");
                notationLayer[((column / 3) * 9 + (column % 3) + (i / 3) * 27 + (i % 3) * 3) * 9 + number - 1].setText("");
            }*/

            s.repaint();

            //if the counterVec size gets to 81 that means the sudoku if filled completely and we need to check if
            if(counterVec.size() == 81) {
                s.checkIfWon();
            }
        }
    }

    public void deleteNumberFromSelectedCell() {
        //check if there is a cell currently selected and if the there is a number inside
        if(selectedCell != Sudoku.NOBUTTON && !buttons[selectedCell].getForeground().equals(s.getColor(4))) {
            if (!buttons[selectedCell].getText().equals("")) {

                //remove this cell from the counterVec and update all grids and the button
                counterVec.remove(Integer.valueOf(selectedCell));
                int row = Sudoku.getRow(selectedCell);
                int column = Sudoku.getColumn(selectedCell);
                gridRows[row * 9 + column] = 0;
                gridColumns[column * 9 + row] = 0;
                gridBlocks[selectedCell] = 0;
                buttons[selectedCell].setText("");

                //set old notation visible and change color
                for (int i = 0; i < 9; i++) {
                    notationLayer[selectedCell * 9 + i].setVisible(true);
                    if (!notationLayer[selectedCell * 9 + i].getText().equals("")) {
                        numberField.setNotationMarked(i);
                    }
                }
                s.repaint();
            }
        }
    }

    public void setNotationLayerNumber(int number) {
        //check if there is a cell currently selected
        if(selectedCell != Sudoku.NOBUTTON && buttons[selectedCell].getText().equals("")) {
            //if notation of number was not set, set the text to "number"
            if(notationLayer[selectedCell * 9 + number - 1].getText().equals("")) {
                notationLayer[selectedCell * 9 + number - 1].setText("" + number);
                numberField.setNotationMarked(number-1);
            } else {
                //else remove the number from the notations
                notationLayer[selectedCell * 9 + number - 1].setText("");
                numberField.setNotationUnmarked(number-1);
            }
        }
    }

    public void deletAllNotesInSelectedCell() {
        //check if there is a cell currently selected
        if (selectedCell != Sudoku.NOBUTTON && buttons[selectedCell].getText().equals("")) {
            for(int i = 0; i < 9; i++) {
                notationLayer[selectedCell * 9 + i].setText("");
                numberField.setNotationUnmarked(i);
            }
        }
    }

    public void moveSelectedCell(int x, int y) {
        if(selectedCell != Sudoku.NOBUTTON) {
            int block = selectedCell/9;
            int row = Sudoku.getRow(selectedCell);
            int column = Sudoku.getColumn(selectedCell);
            row = row + y;
            column = column + x;
            if(0 <= row && row <= 8 && 0 <= column && column <= 8) {
                buttons[selectedCell].setBackground(block%2 == 0 ? s.getColor(0) : s.getColor(2));
                for (int i = 0; i < 9; i++) {
                    numberField.setNotationUnmarked(i);
                }
                int rowIndex = row * 9 + column;
                selectedCell = ((rowIndex / 3) % 3) * 9 + ((rowIndex % 27) / 9) * 3 + (rowIndex / 27) * 27 + (rowIndex %3);
                buttons[selectedCell].setBackground(s.getColor(3));
                for (int i = 0; i < 9; i++) {
                    if (!notationLayer[selectedCell * 9 + i].getText().equals("")) {
                        numberField.setNotationMarked(i);
                    }
                }
                s.repaint();
            }
        }
    }

    public void moveLeft() {

    }

    public void moveUp() {
        if(selectedCell != Sudoku.NOBUTTON) {
            int block = selectedCell/9;
            int row = Sudoku.getRow(selectedCell);
            int column = Sudoku.getColumn(selectedCell);
            if(column > 0) {
                buttons[selectedCell].setBackground(block%2 == 0 ? s.getColor(0) : s.getColor(2));
                for (int i = 0; i < 9; i++) {
                    numberField.setNotationUnmarked(i);
                }
                int rowIndex = row * 9 + column - 1;
                selectedCell = ((rowIndex / 3) % 3) * 9 + ((rowIndex % 27) / 9) * 3 + (rowIndex / 27) * 27 + (rowIndex %3);
                buttons[selectedCell].setBackground(s.getColor(3));
                for (int i = 0; i < 9; i++) {
                    if (!notationLayer[selectedCell * 9 + i].getText().equals("")) {
                        numberField.setNotationMarked(i);
                    }
                }
                s.repaint();
            }
        }
    }

    public void moveRight() {
        if(selectedCell != Sudoku.NOBUTTON) {
            int block = selectedCell/9;
            int row = Sudoku.getRow(selectedCell);
            int column = Sudoku.getColumn(selectedCell);
            if(column > 0) {
                buttons[selectedCell].setBackground(block%2 == 0 ? s.getColor(0) : s.getColor(2));
                for (int i = 0; i < 9; i++) {
                    numberField.setNotationUnmarked(i);
                }
                int rowIndex = row * 9 + column - 1;
                selectedCell = ((rowIndex / 3) % 3) * 9 + ((rowIndex % 27) / 9) * 3 + (rowIndex / 27) * 27 + (rowIndex %3);
                buttons[selectedCell].setBackground(s.getColor(3));
                for (int i = 0; i < 9; i++) {
                    if (!notationLayer[selectedCell * 9 + i].getText().equals("")) {
                        numberField.setNotationMarked(i);
                    }
                }
                s.repaint();
            }
        }
    }

    public void moveDown() {
        if(selectedCell != Sudoku.NOBUTTON) {
            int block = selectedCell/9;
            int row = Sudoku.getRow(selectedCell);
            int column = Sudoku.getColumn(selectedCell);
            if(column > 0) {
                buttons[selectedCell].setBackground(block%2 == 0 ? s.getColor(0) : s.getColor(2));
                for (int i = 0; i < 9; i++) {
                    numberField.setNotationUnmarked(i);
                }
                int rowIndex = row * 9 + column - 1;
                selectedCell = ((rowIndex / 3) % 3) * 9 + ((rowIndex % 27) / 9) * 3 + (rowIndex / 27) * 27 + (rowIndex %3);
                buttons[selectedCell].setBackground(s.getColor(3));
                for (int i = 0; i < 9; i++) {
                    if (!notationLayer[selectedCell * 9 + i].getText().equals("")) {
                        numberField.setNotationMarked(i);
                    }
                }
                s.repaint();
            }
        }
    }

    public int getButtonSize() {
        return getWidth() / 9;
    }
}

