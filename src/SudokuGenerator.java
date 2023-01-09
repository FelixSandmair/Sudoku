import java.util.ArrayList;
import java.util.Collections;


//This class generates a finished sudoku grid and then removes cells to get a solvable Sudoku
//This class is not implemented completely by Felix Sandmair it is from the public repository
//of mfgravesjr (https://github.com/mfgravesjr/finished-projects/tree/master/SudokuGridGenerator)
public class SudokuGenerator {
    private int[] grid;
    private Solver s;

    //The Constructor generates a sudoku of the desired difficulty
    SudokuGenerator(Difficulty difficulty) {
        int filledCells = 0;
        int counter = 0;
        switch (difficulty) {
            case EASY -> filledCells = 32;
            case MEDIUM -> filledCells = 22;
            case HARD -> filledCells = 21;
        }

        while(true) {
            //generate solver with the generated grid and the difficulty setting you want to generate
            generateGrid();
            s = new Solver(grid, difficulty);

            //indices that later get shuffled in order to remove cells in random order
            ArrayList<Integer> indices = new ArrayList<>(81);
            int removedNumber = 0;

            for (int i = 0; i < 81; i++)
                indices.add(i);

            Collections.shuffle(indices);

            //removes cells in random order and after removing lets the solver try to solve the puzzle with the missing cells
            for (int removeIndex : indices) {
                removedNumber = s.deleteCell(removeIndex);
                counter++;
                //if the sudoku is not solvable anymore at the difficulty then you fill the cell again and try to remove the next cell
                if (!s.solveable()) {
                    counter--;
                    s.fillCell(removeIndex, removedNumber);
                }
                if(81 - counter == filledCells) {
                    break;
                }
            }

            if(81 - counter == filledCells) {
                break;
            } else {
                counter = 0;
            }
        }
    }

    public int[] getGridBlocks() {
        return s.getGridBlocks();
    }

    public int[] getGridRows() {
        return s.getGridRows();
    }

    public int[] getGridColumns() {
        return s.getGridColumns();
    }


    //generates a solved sudoku grid stores it in object variable and returns it (in row representation)
    private void generateGrid()
    {
        ArrayList<Integer> arr = new ArrayList<Integer>(9);
        grid = new int[81];
        for(int i = 1; i <= 9; i++) arr.add(i);

        //loads all boxes with numbers 1 through 9
        for(int i = 0; i < 81; i++)
        {
            if(i%9 == 0) Collections.shuffle(arr);
            int perBox = ((i / 3) % 3) * 9 + ((i % 27) / 9) * 3 + (i / 27) * 27 + (i %3);
            grid[perBox] = arr.get(i%9);
        }

        //tracks rows and columns that have been sorted
        boolean[] sorted = new boolean[81];

        for(int i = 0; i < 9; i++)
        {
            boolean backtrack = false;
            //0 is row, 1 is column
            for(int a = 0; a<2; a++)
            {
                //every number 1-9 that is encountered is registered
                boolean[] registered = new boolean[10]; //index 0 will intentionally be left empty since there are only number 1-9.
                int rowOrigin = i * 9;
                int colOrigin = i;

                ROW_COL: for(int j = 0; j < 9; j++)
                {
                    //row/column stepping - making sure numbers are only registered once and marking which cells have been sorted
                    int step = (a%2==0? rowOrigin + j: colOrigin + j*9);
                    int num = grid[step];

                    if(!registered[num]) registered[num] = true;
                    else //if duplicate in row/column
                    {
                        //box and adjacent-cell swap (BAS method)
                        //checks for either unregistered and unsorted candidates in same box,
                        //or unregistered and sorted candidates in the adjacent cells
                        for(int y = j; y >= 0; y--)
                        {
                            int scan = (a%2==0? i * 9 + y: i + 9 * y);
                            if(grid[scan] == num)
                            {
                                //box stepping
                                for(int z = (a%2==0? (i%3 + 1) * 3: 0); z < 9; z++)
                                {
                                    if(a%2 == 1 && z%3 <= i%3)
                                        continue;
                                    int boxOrigin = ((scan % 9) / 3) * 3 + (scan / 27) * 27;
                                    int boxStep = boxOrigin + (z / 3) * 9 + (z % 3);
                                    int boxNum = grid[boxStep];
                                    if((!sorted[scan] && !sorted[boxStep] && !registered[boxNum])
                                            || (sorted[scan] && !registered[boxNum] && (a%2==0? boxStep%9==scan%9: boxStep/9==scan/9)))
                                    {
                                        grid[scan] = boxNum;
                                        grid[boxStep] = num;
                                        registered[boxNum] = true;
                                        continue ROW_COL;
                                    }
                                    else if(z == 8) //if z == 8, then break statement not reached: no candidates available
                                    {
                                        //Preferred adjacent swap (PAS)
                                        //Swaps x for y (preference on unregistered numbers), finds occurence of y
                                        //and swaps with z, etc. until an unregistered number has been found
                                        int searchingNo = num;

                                        //noting the location for the blindSwaps to prevent infinite loops.
                                        boolean[] blindSwapIndex = new boolean[81];

                                        //loop of size 18 to prevent infinite loops as well. Max of 18 swaps are possible.
                                        //at the end of this loop, if continue or break statements are not reached, then
                                        //fail-safe is executed called Advance and Backtrack Sort (ABS) which allows the
                                        //algorithm to continue sorting the next row and column before coming back.
                                        //Somehow, this fail-safe ensures success.
                                        for(int q = 0; q < 18; q++)
                                        {
                                            SWAP: for(int b = 0; b <= j; b++)
                                            {
                                                int pacing = (a%2==0? rowOrigin+b: colOrigin+b*9);
                                                if(grid[pacing] == searchingNo)
                                                {
                                                    int adjacentCell = -1;
                                                    int adjacentNo = -1;
                                                    int decrement = (a%2==0? 9: 1);

                                                    for(int c = 1; c < 3 - (i % 3); c++)
                                                    {
                                                        adjacentCell = pacing + (a%2==0? (c + 1)*9: c + 1);

                                                        //this creates the preference for swapping with unregistered numbers
                                                        if(   (a%2==0 && adjacentCell >= 81)
                                                                || (a%2==1 && adjacentCell % 9 == 0)) adjacentCell -= decrement;
                                                        else
                                                        {
                                                            adjacentNo = grid[adjacentCell];
                                                            if(i%3!=0
                                                                    || c!=1
                                                                    || blindSwapIndex[adjacentCell]
                                                                    || registered[adjacentNo])
                                                                adjacentCell -= decrement;
                                                        }
                                                        adjacentNo = grid[adjacentCell];

                                                        //as long as it hasn't been swapped before, swap it
                                                        if(!blindSwapIndex[adjacentCell])
                                                        {
                                                            blindSwapIndex[adjacentCell] = true;
                                                            grid[pacing] = adjacentNo;
                                                            grid[adjacentCell] = searchingNo;
                                                            searchingNo = adjacentNo;

                                                            if(!registered[adjacentNo])
                                                            {
                                                                registered[adjacentNo] = true;
                                                                continue ROW_COL;
                                                            }
                                                            break SWAP;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //begin Advance and Backtrack Sort (ABS)
                                        backtrack = true;
                                        break ROW_COL;
                                    }
                                }
                            }
                        }
                    }
                }

                if(a%2==0)
                    for(int j = 0; j < 9; j++) sorted[i*9+j] = true; //setting row as sorted
                else if(!backtrack)
                    for(int j = 0; j < 9; j++) sorted[i+j*9] = true; //setting column as sorted
                else //reseting sorted cells through to the last iteration
                {
                    backtrack = false;
                    for(int j = 0; j < 9; j++) sorted[i*9+j] = false;
                    for(int j = 0; j < 9; j++) sorted[(i-1)*9+j] = false;
                    for(int j = 0; j < 9; j++) sorted[i-1+j*9] = false;
                    i-=2;
                }
            }
        }

        if(!isPerfect(grid)) throw new RuntimeException("ERROR: Imperfect grid generated.");
    }

    //prints grid in the console (grid needs to be in row representation)
    public static void printGrid(int[] grid)
    {
        if(grid.length != 81) throw new IllegalArgumentException("The grid must be a single-dimension grid of length 81");
        for(int i = 0; i < 81; i++)
        {
            System.out.print("["+(grid[i] != 0 ? grid[i] : " ")+"] "+(i%9 == 8?"\n":""));
            if(i % 3 == 2 && i % 9 != 8) {
                System.out.print(" ");
            }
            if(i % 27 == 26) {
                System.out.println();
            }
        }
    }

    //test if a grid is solved correctly (grid needs to be in row representation)
    public static boolean isPerfect(int[] grid)
    {
        if(grid.length != 81) throw new IllegalArgumentException("The grid must be a single-dimension grid of length 81");

        //tests to see if the grid is perfect

        //for every box
        for(int i = 0; i < 9; i++)
        {
            boolean[] registered = new boolean[10];
            registered[0] = true;
            int boxOrigin = (i * 3) % 9 + ((i * 3) / 9) * 27;
            for(int j = 0; j < 9; j++)
            {
                int boxStep = boxOrigin + (j / 3) * 9 + (j % 3);
                int boxNum = grid[boxStep];
                registered[boxNum] = true;
            }
            int counter = 0;
            for(boolean b: registered) {
                if (!b) {
                    System.out.println("no number " + counter + " in box with origin " + boxOrigin);
                    return false;
                }
                counter++;
            }
        }

        //for every row
        for(int i = 0; i < 9; i++)
        {
            boolean[] registered = new boolean[10];
            registered[0] = true;
            int rowOrigin = i * 9;
            for(int j = 0; j < 9; j++)
            {
                int rowStep = rowOrigin + j;
                int rowNum = grid[rowStep];
                registered[rowNum] = true;
            }
            int counter = 0;
            for(boolean b: registered) {
                if (!b) {
                    System.out.println("no number " + counter + " in row with origin " + rowOrigin);
                    return false;
                }
                counter++;
            }
        }

        //for every column
        for(int i = 0; i < 9; i++)
        {
            boolean[] registered = new boolean[10];
            registered[0] = true;
            int colOrigin = i;
            for(int j = 0; j < 9; j++)
            {
                int colStep = colOrigin + j*9;
                int colNum = grid[colStep];
                registered[colNum] = true;
            }
            int counter = 0;
            for(boolean b: registered) {
                if (!b) {
                    System.out.println("no number " + counter + " in column with origin " + colOrigin);
                    return false;
                }
                counter++;
            }
        }

        return true;
    }
}
