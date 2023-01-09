import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;


//Panel that represents the playing field with all its cells
public class PlayingField extends JPanel {
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    private final Sudoku s;
    private NumberField numberField;
    private final JPanel[] blocks = new JPanel[9];
    private final JButton[] buttons = new JButton[9 * 9];
    private final JLabel[] notationLayer = new JLabel[81 * 9];
    private int[] gridBlocks;
    private int[] gridRows;
    private int[] gridColumns;
    private final Vector<Integer> counterVec = new Vector<>();
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

    //this method is sets up the playing field with all its buttons and generates a Sudoku based on the chosen difficulty
    public void setup(int width, int height, NumberField numberField, Difficulty difficulty) {
        this.numberField = numberField;

        //generate sudoku grid of desired difficulty
        SudokuGenerator sGen = new SudokuGenerator(difficulty);
        gridBlocks = sGen.getGridBlocks();
        gridRows = sGen.getGridRows();
        gridColumns = sGen.getGridColumns();

        //setup this panel
        setSize((height*75)/100, (height*75)/100);
        setBounds(width/2 - getWidth()/2,30,getWidth(), getHeight());
        setLayout(new GridLayout(3, 3, 4,4));
        setBorder(BorderFactory.createLineBorder(s.getColor(4),4));
        setBackground(s.getColor(4));

        //setup all the buttons
        setBlocks();
        setButtons();
        setNotationLayer();
    }

    //sets up the block panels
    private void setBlocks() {
        for (int i = 0; i < blocks.length; ++i) {
            blocks[i] = new JPanel();
            blocks[i].setLayout(new GridLayout(3, 3,1,1));
            blocks[i].setBackground(s.getColor(4));
            add(blocks[i]);
        }
    }

    //sets up all cell buttons based on the playing fields start configuration
    private void setButtons() {
        for (int block = 0; block < 9; ++block) {
            for (int walker = 0; walker < 9; ++walker) {
                //basic button configuration
                buttons[block * 9 + walker] = new JButton();
                buttons[block * 9 + walker].addActionListener(this::playButtons);
                buttons[block * 9 + walker].setFont(new Font(Sudoku.FONT, Font.PLAIN, 50));
                buttons[block * 9 + walker].setFocusable(false);
                buttons[block * 9 + walker].setBackground(block % 2 == 0 ? s.getColor(0) : s.getColor(2));
                buttons[block * 9 + walker].setBorder(BorderFactory.createLineBorder(s.getColor(4), 1));
                buttons[block * 9 + walker].setLayout(new GridLayout(3, 3));
                blocks[block].add(buttons[block * 9 + walker]);

                //set the buttons that contain a number in the start configuration
                if(gridBlocks[block * 9 + walker] != 0) {
                    buttons[block * 9 + walker].setText("" + gridBlocks[block * 9 + walker]);
                    buttons[block * 9 + walker].setRolloverEnabled(false);
                    buttons[block * 9 + walker].setForeground(s.getColor(4));
                    counterVec.add(block * 9 + walker);
                } else {
                    buttons[block * 9 + walker].setForeground(s.getColor(1));
                }
            }
        }
    }

    //sets up all notationLayer labels
    private void setNotationLayer() {
        for (int cell = 0; cell < 81; cell++) {
            for (int number = 0; number < 9; number++) {
                notationLayer[cell * 9 + number] = new JLabel();
                notationLayer[cell * 9 + number].setFont(new Font(Sudoku.FONT, Font.BOLD, 20));
                notationLayer[cell * 9 + number].setForeground(s.getColor(1));
                notationLayer[cell * 9 + number].setVerticalAlignment(JLabel.CENTER);
                notationLayer[cell * 9 + number].setHorizontalAlignment(JLabel.CENTER);
                buttons[cell].add(notationLayer[cell * 9 + number]);
            }
        }
    }


    //Action Listeners for Buttons
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    //Action listener for the buttons of the cells
    private void playButtons(ActionEvent e) {
        //Searches for index of the button that was pressed
        int currentCell = 0;
        for (int cell = 0; cell < 81; cell++) {
            if (e.getSource() == buttons[cell]) {
                currentCell = cell;
                break;
            }
        }

        //do nothing if cell is already selected
        if(currentCell == selectedCell) {
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
        selectedCell = currentCell;
        buttons[currentCell].setBackground(s.getColor(3));
        for (int number = 0; number < 9; number++) {
            if (!notationLayer[selectedCell * 9 + number].getText().equals("")) {
                numberField.setNotationMarked(number);
            }
        }
        s.repaint();
    }


    //public interface functions
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    //inserts number at selected cell
    public void setButtonNumber(int number) {
        //check if there is a cell currently selected
        if(selectedCell != Sudoku.NOBUTTON && !buttons[selectedCell].getForeground().equals(s.getColor(4))) {
            int row = Sudoku.getRow(selectedCell);
            int column = Sudoku.getColumn(selectedCell);

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
            //currently removed this feature because it made the game a bit to easy
            /*for(int i = 0; i < 9; i++) {
                notationLayer[block * 81 + i * 9 + number - 1].setText("");
                notationLayer[((row / 3) * 27 + (row % 3) * 3 + (i / 3) * 9 + (i % 3)) * 9 + number - 1].setText("");
                notationLayer[((column / 3) * 9 + (column % 3) + (i / 3) * 27 + (i % 3) * 3) * 9 + number - 1].setText("");
            }*/

            s.repaint();

            //if the counterVec size gets to 81 that means the sudoku if filled completely, and we need to check if
            if(counterVec.size() == 81) {
                s.checkIfWon();
            }
        }
    }

    //this method deletes from the selected cell
    public void deleteNumberFromSelectedCell() {
        //check if there is a cell currently selected and if the there is a number inside
        if(selectedCell != Sudoku.NOBUTTON
                && !buttons[selectedCell].getForeground().equals(s.getColor(4))
                && !buttons[selectedCell].getText().equals(""))
        {

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

    //sets the number of the notation layer in the selected cell
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

    //deletes all notations in the selected cell
    public void deleteAllNotesInSelectedCell() {
        //check if there is a cell currently selected
        if (selectedCell != Sudoku.NOBUTTON && buttons[selectedCell].getText().equals("")) {
            for(int i = 0; i < 9; i++) {
                notationLayer[selectedCell * 9 + i].setText("");
                numberField.setNotationUnmarked(i);
            }
        }
    }

    //moves the selected cell by x and y in the grid (used for the key listener to move selected cell with arrow keys)
    public void moveSelectedCell(int x, int y) {
        //check if there is a cell selected
        if(selectedCell != Sudoku.NOBUTTON) {

            //block is the block of the old cell to put the right background color and row and column is the new field
            int block = selectedCell/9;
            int row = Sudoku.getRow(selectedCell) + y;
            int column = Sudoku.getColumn(selectedCell) + x;

            //check if our move is still inside the grid
            if(0 <= row && row <= 8 && 0 <= column && column <= 8) {
                //set the background color of the old selected cell and unmark all the notation buttons
                buttons[selectedCell].setBackground(block%2 == 0 ? s.getColor(0) : s.getColor(2));
                for (int i = 0; i < 9; i++) {
                    numberField.setNotationUnmarked(i);
                }

                //change selected cell to the new cell and mark the notation buttons
                int rowIndex = row * 9 + column;
                selectedCell = Sudoku.getBlockIndexFromRow(rowIndex);
                buttons[selectedCell].setBackground(s.getColor(3));
                for (int number = 0; number < 9; number++) {
                    if (!notationLayer[selectedCell * 9 + number].getText().equals("")) {
                        numberField.setNotationMarked(number);
                    }
                }
                s.repaint();
            }
        }
    }

    //returns the size of the cells
    public int getCellSize() {
        return getWidth() / 9;
    }
}

