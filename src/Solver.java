import java.util.Arrays;
import java.util.Vector;


//this is a class used to solve sudokus
//different difficulties can be set to tell the solver which strategies are used to try to solve the sudoku (not yet finished)
public class Solver {
    private Difficulty difficulty;
    private int emptyCells = 0;
    private int[] gridRows;
    private int[] gridColumns = new int[81];
    private int[] gridBlocks = new int[81];
    private final int[][] gridRowsNotes = new int[81][9];
    private final int[] gridRowsNotesCounter = new int[81];
    private final int[][] gridColumnsNotes = new int[81][9];
    private final int[] gridColumnsNotesCounter = new int[81];
    private final int[][] gridBlocksNotes = new int[81][9];
    private final int[] gridBlocksNotesCounter = new int[81];
    Solver(int[] gridRows, Difficulty difficulty) {
        this.gridRows = gridRows;
        this.difficulty = difficulty;
        setGridColumns();
        setGridBlocks();
        setNotes();
    }

    //Sets column indexed grid representation
    private void setGridColumns() {
        //loop over columns
        for(int column = 0; column < 9; column++) {
            //loop through columns
            for(int offset = 0; offset < 9; offset++) {
                gridColumns[column * 9 + offset] = gridRows[column + offset * 9];
            }
        }
    }

    //Sets column indexed grid representation
    private void setGridBlocks() {
        for(int rowIndex = 0; rowIndex < 81; rowIndex++) {
            int perBox = Sudoku.getBlockIndexFromRow(rowIndex);
            gridBlocks[rowIndex] = gridRows[perBox];
        }
    }

    //Sets Notes and the Notes counter for each cell and representation
    private void setNotes() {
        for(int cell = 0; cell < 81; cell++) {
            gridRowsNotesCounter[cell] = 0;
            gridColumnsNotesCounter[cell] = 0;
            gridBlocksNotesCounter[cell] = 0;
            for(int number = 0; number < 9; number++) {
                gridRowsNotes[cell][number] = 0;
                gridColumnsNotes[cell][number] = 0;
                gridBlocksNotes[cell][number] = 0;
            }
        }
    }

    public int[] getGridBlocks() {
        return gridBlocks;
    }

    public int[] getGridRows() {
        return gridRows;
    }

    public int[] getGridColumns() {
        return gridColumns;
    }

    //deletes cell at rowIndex (updates all representations) and returns deleted number
    public int deleteCell(int rowIndex) {
        int removed = gridRows[rowIndex];
        int blockIndex = Sudoku.getBlockIndexFromRow(rowIndex);
        int columnIndex = (rowIndex % 9) * 9 + rowIndex / 9;

        gridRows[rowIndex] = 0;
        gridColumns[columnIndex] = 0;
        gridBlocks[blockIndex] = 0;

        emptyCells++;

        return removed;
    }

    //fills cell at rowIndex (updates all representations) with the value of number
    public void fillCell(int rowIndex, int number) {
        int blockIndex = Sudoku.getBlockIndexFromRow(rowIndex);
        int columnIndex = (rowIndex % 9) * 9 + rowIndex / 9;

        gridRows[rowIndex] = number;
        gridColumns[columnIndex] = number;
        gridBlocks[blockIndex] = number;

        emptyCells--;
    }

    //Setups all the notes for all cells
    private void initiateNotes() {
        //deletes all notes and notes counters from previous solve attempts
        setNotes();

        //helping variables
        int row = 0;
        int column = 0;
        int block = 0;

        //variable to store index for block representation
        int blockIndex = 0;

        //Vector that is keeps all possible numbers that can be put in a cell
        Vector<Integer> notes = new Vector<Integer>(9);

        //iterates over all 81 cells
        for(int cell = 0; cell < 81; cell++) {
            //setup notes vector
            notes.removeAllElements();
            for(int number = 1; number < 10; number++) {
                notes.add(number);
            }
            //if cell is empty we set the notes
            if(gridRows[cell] == 0) {
                row = cell/9;
                column = cell%9;
                blockIndex = Sudoku.getBlockIndexFromRow(cell);
                block = blockIndex/9;
                //removes the numbers from the notes vector that appear in the cells row, column or block
                for(int walker = 0; walker < 9; walker++) {
                    notes.remove(Integer.valueOf(gridRows[row*9+walker]));
                    notes.remove(Integer.valueOf(gridColumns[column*9+walker]));
                    notes.remove(Integer.valueOf(gridBlocks[block*9+walker]));
                }

                //sets the notes of the current cell based on the notes vector
                for(int walker = 0; walker < 9; walker++) {
                    if(notes.contains(walker+1)) {
                        gridRowsNotes[cell][walker] = 1;
                        gridColumnsNotes[column*9+row][walker] = 1;
                        gridBlocksNotes[blockIndex][walker] = 1;
                    }
                }

                //sets the notes counter for the cell (0 if there is no possible number to be placed in the cell and 9 if you can place every number)
                gridRowsNotesCounter[cell] = notes.size();
                gridColumnsNotesCounter[column*9+row] = notes.size();
                gridBlocksNotesCounter[blockIndex] = notes.size();
            }
        }
    }

    //delete notes based on new number inserted at rowIndex
    private void deleteNotes(int rowIndex, int number) {
        int row = rowIndex/9;
        int column = rowIndex%9;
        int blockIndex = Sudoku.getBlockIndexFromRow(rowIndex);
        int block = blockIndex/9;

        //deletes the notes of the number that was set in the cells block, column and row
        for(int walker = 0; walker < 9; walker++) {
            removeNotesRow(row * 9 + walker, number-1);
            removeNotesColumn(column * 9 + walker, number-1);
            removeNotesBlock(block * 9 + walker, number-1);
        }

        //deletes all the notes from the cell that was just placed
        for(int num = 0; num < 9; num++) {
            removeNotesRow(rowIndex, num);
        }
    }

    //Function fills in one cell if there is only one possible number to be placed
    //Return value: true if a cell was filled and false if not
    private boolean checkForCellWithOnePossible() {
        int columnIndex;
        int blockIndex;

        //loop through every cell
        for(int cell = 0; cell < 81; cell++) {

            //if the cells notes counter is one that we can insert the number for the counter
            if(gridRowsNotesCounter[cell] == 1) {
                columnIndex = (cell % 9) * 9 + cell / 9;
                blockIndex = Sudoku.getBlockIndexFromRow(cell);

                //searches for single number that is noted in the current cell and then exits function by returning true
                for(int numberToInsert = 1; numberToInsert < 10; numberToInsert++) {
                    if (gridRowsNotes[cell][numberToInsert - 1] == 1) {

                        //print statements for debugging
                        //System.out.println();
                        //SudokuGenerator.printGrid(gridRows);
                        //System.out.println("Insert " + numberToInsert + " in row " + (cell/9) + " column " + (cell%9) + " because only possible");

                        gridRows[cell] = numberToInsert;
                        gridColumns[columnIndex] = numberToInsert;
                        gridBlocks[blockIndex] = numberToInsert;

                        deleteNotes(cell, numberToInsert);
                        emptyCells--;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //checks if there is a number that can only be placed in one cell of a block
    private boolean checkOnePossibleBlock() {
        int cellToFill = 0;     //variable to mark first cell you encounter a note with the numberToInsert you are checking
        int row = 0;
        int column = 0;
        boolean onlyOnce = false;
        for(int numberToInsert = 1; numberToInsert < 10; numberToInsert++) {    //check numbers 1 through 9 if there is only one possible place to be in block 0 to 8
            for(int block = 0; block < 9; block++) {    //checking block 'block' to place numberToInsert
                for(int walker = 0; walker < 9; walker++) {    //walker goes through block 'block'
                    if(gridBlocksNotes[block*9 + walker][numberToInsert-1] == 1) {
                        if(!onlyOnce) {     //encountered numberToInsert for the first time
                            onlyOnce = true;
                            cellToFill = block * 9 + walker;
                        } else { //encountered numberToInsert for the second time, and we don't need to continue searching
                            onlyOnce = false;
                            break;
                        }
                    }
                }
                if(onlyOnce) {  //found numberToInsert once
                    row = Sudoku.getRow(cellToFill);
                    column = Sudoku.getColumn(cellToFill);

                    //print statements for debugging
                    //System.out.println();
                    //SudokuGenerator.printGrid(gridRows);
                    //System.out.println("Insert " + numberToInsert + " in row " + row + " column " + column + " because block");

                    gridBlocks[cellToFill] = numberToInsert;
                    gridRows[row * 9 + column] = numberToInsert;
                    gridColumns[column * 9 + row] = numberToInsert;

                    deleteNotes(row * 9 + column, numberToInsert);
                    emptyCells--;
                    return true;
                }
            }
        }
        return false;
    }

    //checks if there is a number that can only be placed in one cell of a row
    private boolean checkOnePossibleRow() {
        int cellToFill = 0;     //variable to mark first cell you encounter a note with the numberToInsert you are checking
        int blockIndex = 0;     //Index for block representation
        int columnIndex = 0;    //Index for column representation
        boolean onlyOnce = false;
        for(int numberToInsert = 1; numberToInsert < 10; numberToInsert++) {    //check numbers 1 through 9 if there is only one possible place to be in row 0 to 8
            for(int row = 0; row < 9; row++) {    //checking row to place numberToInsert
                for(int walker = 0; walker < 9; walker++) {    //walker walks through row
                    if(gridRowsNotes[row*9 + walker][numberToInsert-1] == 1) {
                        if(!onlyOnce) {     //encountered numberToInsert for the first time
                            onlyOnce = true;
                            cellToFill = row * 9 + walker;
                        } else {    //encountered numberToInsert for the second time, we don't need to continue searching
                            onlyOnce = false;
                            break;
                        }
                    }
                }
                if(onlyOnce) {  //found numberToInsert once
                    columnIndex = (cellToFill % 9) * 9 + cellToFill / 9;
                    blockIndex = Sudoku.getBlockIndexFromRow(cellToFill);

                    //print statements for debugging
                    //System.out.println();
                    //SudokuGenerator.printGrid(gridRows);
                    //System.out.println("Insert " + numberToInsert + " in row " + (cellToFill/9) + " column " + (columnIndex/9) + " because row");

                    gridRows[cellToFill] = numberToInsert;
                    gridColumns[columnIndex] = numberToInsert;
                    gridBlocks[blockIndex] = numberToInsert;

                    deleteNotes(cellToFill, numberToInsert);
                    emptyCells--;
                    return true;
                }
            }
        }
        return false;
    }

    //checks if there is a number that can only be placed in one cell of a column
    private boolean checkOnePossibleColumn() {
        int cellToFill = 0;     //variable to mark first cell you encounter a note with the numberToInsert you are checking
        int rowIndex = 0;       //Index for row representation
        int blockIndex = 0;     //Index for block representation
        boolean onlyOnce = false;
        for(int numberToInsert = 1; numberToInsert < 10; numberToInsert++) {    //check numbers 1 through 9 if there is only one possible place to be in block 0 to 8
            for(int column = 0; column < 9; column++) {    //checking block 'block' to place numberToInsert
                for(int walker = 0; walker < 9; walker++) {    //walker walks through block 'block'
                    if(gridColumnsNotes[column*9 + walker][numberToInsert-1] == 1) {
                        if(!onlyOnce) {     //encountered numberToInsert for the first time
                            onlyOnce = true;
                            cellToFill = column * 9 + walker;
                        } else {    //encountered numberToInsert for the second time, and we don't need to continue searching
                            onlyOnce = false;
                            break;
                        }
                    }
                }
                if(onlyOnce) {  //found numberToInsert once
                    rowIndex = (cellToFill % 9) * 9 + cellToFill / 9;
                    blockIndex = Sudoku.getBlockIndexFromRow(rowIndex);

                    //print statements for debugging
                    //System.out.println();
                    //SudokuGenerator.printGrid(gridRows);
                    //System.out.println("Insert " + numberToInsert + " in row " + (rowIndex/9) + " column " + (cellToFill/9) + " because column");

                    gridColumns[cellToFill] = numberToInsert;
                    gridRows[rowIndex] = numberToInsert;
                    gridBlocks[blockIndex] = numberToInsert;

                    deleteNotes(rowIndex, numberToInsert);
                    emptyCells--;
                    return true;
                }
            }
        }
        return false;
    }

    //searches for naked pair in the sudoku row notes and deletes notes based on that naked pair
    private boolean searchNakedPairRow() {
        boolean foundNaked = false;
        boolean notesChanged = false;
        int firstIndex = 0;
        int secondIndex = 0;
        int num1 = 10;
        int num2 = 10;

        //search in each row
        for(int row = 0; row < 9; row++) {
            //walks through row
            for (int walker = 0; walker < 9; walker++) {
                //when the counter of the current cell is equal to 2 we search for a second cell with the same numbers noted
                if (gridRowsNotesCounter[row * 9 + walker] == 2) {
                    for (int i = 0; i < 9; i++) {
                        if (gridRowsNotesCounter[row * 9 + i] == 2
                                && Arrays.equals(gridRowsNotes[row * 9 + walker], gridRowsNotes[row * 9 + i])
                                && i != walker) {
                            //if i is smaller than walker then we already checked this naked pair
                            if(i < walker) {
                                break;
                            }
                            if (!foundNaked) {
                                //first time we encountered a cell with the same pair
                                foundNaked = true;
                                firstIndex = walker;
                                secondIndex = i;
                            } else {
                                //second time we encountered a cell with the same pair (no naked pair anymore)
                                foundNaked = false;
                                break;
                            }
                        }
                    }

                    if (foundNaked) {
                        //check which are the two numbers of the naked pair
                        for(int number = 0; number < 9; number++) {
                            if(gridRowsNotes[row * 9 + firstIndex][number] == 1) {
                                if(num1 == 10) {
                                    num1 = number;
                                } else {
                                    num2 = number;
                                    break;
                                }
                            }
                        }

                        //delete the notes of num1 and num2 in the cells of the row
                        for(int i = 0; i < 9; i++) {
                            if(i != firstIndex && i != secondIndex) {
                                if(gridRowsNotes[row * 9 + i][num1] == 1) {
                                    removeNotesRow(row * 9 + i, num1);
                                    notesChanged = true;
                                }
                                if(gridRowsNotes[row * 9 + i][num2] == 1) {
                                    removeNotesRow(row * 9 + i, num2);
                                    notesChanged = true;
                                }
                            }
                        }

                        //reset variables for next loop iteration
                        foundNaked = false;
                        num1 = 10;
                        num2 = 10;
                    }
                }
            }
        }
        //if there was a change done to the notes it is notesChange is true
        return notesChanged;
    }

    //searches for naked pair in the sudoku column notes and deletes notes based on that naked pair
    private boolean searchNakedPairColumn() {
        boolean foundNaked = false;
        boolean notesChanged = false;
        int firstIndex = 0;
        int secondIndex = 0;
        int num1 = 10;
        int num2 = 10;

        //search in each column
        for(int column = 0; column < 9; column++) {
            //walks through column
            for (int walker = 0; walker < 9; walker++) {
                //when the counter of the current cell is equal to 2 we search for a second cell with the same numbers noted
                if (gridColumnsNotesCounter[column * 9 + walker] == 2) {
                    for (int i = 0; i < 9; i++) {
                        if (gridColumnsNotesCounter[column * 9 + i] == 2
                                && Arrays.equals(gridColumnsNotes[column * 9 + walker], gridColumnsNotes[column * 9 + i])
                                && i != walker) {
                            //if i is smaller than walker then we already checked this naked pair
                            if(i < walker) {
                                break;
                            }
                            if (!foundNaked) {
                                //first time we encountered a cell with the same pair
                                foundNaked = true;
                                firstIndex = walker;
                                secondIndex = i;
                            } else {
                                //second time we encountered a cell with the same pair (no naked pair anymore)
                                foundNaked = false;
                                break;
                            }
                        }
                    }
                    if (foundNaked) {
                        //check which are the two numbers of the naked pair
                        for(int number = 0; number < 9; number++) {
                            if(gridColumnsNotes[column * 9 + firstIndex][number] == 1) {
                                if(num1 == 10) {
                                    num1 = number;
                                } else {
                                    num2 = number;
                                    break;
                                }
                            }
                        }

                        //delete the notes of num1 and num2 in the cells of the row
                        for(int i = 0; i < 9; i++) {
                            if(i != firstIndex && i != secondIndex) {
                                if(gridColumnsNotes[column * 9 + i][num1] == 1) {
                                    removeNotesColumn(column * 9 + i, num1);
                                    notesChanged = true;
                                }
                                if(gridColumnsNotes[column * 9 + i][num2] == 1) {
                                    removeNotesColumn(column * 9 + i, num2);
                                    notesChanged = true;
                                }
                            }
                        }

                        //reset variables for next loop iteration
                        foundNaked = false;
                        num1 = 10;
                        num2 = 10;
                    }
                }
            }
        }
        //if there was a change done to the notes it is notesChange is true
        return notesChanged;
    }

    //searches for naked pair in the sudoku block notes and deletes notes based on that naked pair
    private boolean searchNakedPairBlock() {
        boolean foundNaked = false;
        boolean notesChanged = false;
        int firstIndex = 0;
        int secondIndex = 0;
        int num1 = 10;
        int num2 = 10;

        //search in each block
        for(int block = 0; block < 9; block++) {
            //walks through column
            for (int walker = 0; walker < 9; walker++) {
                //when the counter of the current cell is equal to 2 we search for a second cell with the same numbers noted
                if (gridBlocksNotesCounter[block * 9 + walker] == 2) {
                    for (int i = 0; i < 9; i++) {
                        if (gridBlocksNotesCounter[block * 9 + i] == 2
                                && Arrays.equals(gridBlocksNotes[block * 9 + walker], gridBlocksNotes[block * 9 + i])
                                && i != walker) {
                            //if i is smaller than walker then we already checked this naked pair
                            if(i < walker) {
                                break;
                            }
                            if (!foundNaked) {
                                //first time we encountered a cell with the same pair
                                foundNaked = true;
                                firstIndex = walker;
                                secondIndex = i;
                            } else {
                                //second time we encountered a cell with the same pair (no naked pair anymore)
                                foundNaked = false;
                                break;
                            }
                        }
                    }
                    if (foundNaked) {
                        //check which are the two numbers of the naked pair
                        for(int number = 0; number < 9; number++) {
                            if(gridBlocksNotes[block * 9 + firstIndex][number] == 1) {
                                if(num1 == 10) {
                                    num1 = number;
                                } else {
                                    num2 = number;
                                    break;
                                }
                            }
                        }

                        //delete the notes of num1 and num2 in the cells of the row
                        for(int i = 0; i < 9; i++) {
                            if(i != firstIndex && i != secondIndex) {
                                if(gridBlocksNotes[block * 9 + i][num1] == 1) {
                                    removeNotesBlock(block * 9 + i, num1);
                                    notesChanged = true;
                                }
                                if(gridBlocksNotes[block * 9 + i][num2] == 1) {
                                    removeNotesBlock(block * 9 + i, num2);
                                    notesChanged = true;
                                }
                            }
                        }

                        //reset variables for next loop iteration
                        foundNaked = false;
                        num1 = 10;
                        num2 = 10;
                    }
                }
            }
        }
        //if there was a change done to the notes it is notesChange is true
        return notesChanged;
    }

    //removes notes based on rowIndex
    private void removeNotesRow(int rowIndex, int number) {
        if(gridRowsNotes[rowIndex][number] == 1) {
            int column = rowIndex % 9;
            int row = rowIndex / 9;
            int blockIndex = Sudoku.getBlockIndexFromRow(rowIndex);
            removeNotes(blockIndex, rowIndex, column * 9 + row, number);
        }
    }

    //removes notes based on columnIndex
    private void removeNotesColumn(int columnIndex, int number) {
        if(gridColumnsNotes[columnIndex][number] == 1) {
            int row = columnIndex % 9;
            int column = columnIndex / 9;
            int blockIndex = Sudoku.getBlockIndexFromRow(row * 9 + column);
            removeNotes(blockIndex, row * 9 + column, columnIndex, number);
        }
    }

    //removes notes based on blockIndex
    private void removeNotesBlock(int blockIndex, int number) {
        if(gridBlocksNotes[blockIndex][number] == 1) {
            int row = Sudoku.getRow(blockIndex);
            int column = Sudoku.getColumn(blockIndex);
            removeNotes(blockIndex, row * 9 + column, column * 9 + row, number);
        }
    }

    //removes the note of number in the cells given by the 3 different indecies
    private void removeNotes(int blockIndex, int rowIndex, int columnIndex, int number) {
        gridColumnsNotes[columnIndex][number] = 0;
        gridColumnsNotesCounter[columnIndex]--;
        gridRowsNotes[rowIndex][number] = 0;
        gridRowsNotesCounter[rowIndex]--;
        gridBlocksNotes[blockIndex][number] = 0;
        gridBlocksNotesCounter[blockIndex]--;
    }

    //function solves sGen configuration based on difficulty: EASY, MEDIUM, HARD (difficulty is not yet used it uses fixed strategy)
    public boolean solve() {
        int numberOfCellsToFill = emptyCells;
        initiateNotes();  //insert starting notes (Numbers that are possible in each cell)
        for(int iteration = 0; iteration < numberOfCellsToFill; iteration++) {

            if(checkForCellWithOnePossible()) {
                continue;
            }

            if(checkOnePossibleBlock()) {
                continue;
            }

            if(checkOnePossibleRow()) {
                continue;
            }

            if(checkOnePossibleColumn()) {
                continue;
            }

            //found = searchNakedPairBlock() || found;
            //found = searchNakedPairRow() || found;
            //found = searchNakedPairColumn() || found;

            /*if(found) {
                nakedUsed = true;
                iteration--;
                found = false;
                continue;
            }*/
            break;
        }
        return emptyCells == 0;
    }

    //function tries to solve Sudoku with strategies based on difficulty and resets sGen configuration after the attempt.
    public boolean solveable() {
        int[] copyRows = gridRows.clone();
        int[] copyColumns = gridColumns.clone();
        int[] copyBlocks = gridBlocks.clone();
        int emtpyCellsOnStart = emptyCells;

        boolean solveable = solve();

        gridRows = copyRows;
        gridColumns = copyColumns;
        gridBlocks = copyBlocks;
        emptyCells = emtpyCellsOnStart;
        return solveable;
    }
}
