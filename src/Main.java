import java.awt.*;


public class Main {

    static Color[][] colors = {{new Color(0xffffff), new Color(0xbe1622), new Color(0x999999), new Color(0x575756), new Color(0x1d1d1b)}
            ,{new Color(0xfeeee0), new Color(0xcfb7a3), new Color(0xb39378), new Color(0x9c6e4a), new Color(0x644229)}
            ,{new Color(0xe8e7e7), new Color(0x9aca8a), new Color(0xcc8481), new Color(0x789b7b), new Color(0x8c7763)}
            ,{new Color(0xfffcf7), new Color(0xdfccc3), new Color(0xbbcac0), new Color(0x87886c), new Color(0xcc9868)}
            ,{new Color(0xe3ecec), new Color(0x9dcef1), new Color(0x94acb7), new Color(0x1e6a6f), new Color(0x1e4a58)}};
    public static void main(String[] args) {
        //Sudoku s = new Sudoku(0, colors[0]);
        SudokuLauncher sudokuLauncher = new SudokuLauncher();
    }
}