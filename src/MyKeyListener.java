import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {

    Sudoku mySudoku;

    MyKeyListener(Sudoku s) {
        mySudoku = s;
    }

    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println(e.getKeyCode());
        if(mySudoku.playingField.selectedField != mySudoku.NOBUTTON) {
            if(e.getKeyCode() >= 49 && e.getKeyCode() <= 57) {
                int row = mySudoku.playingField.getRowIndex(mySudoku.playingField.selectedField + 1);
                int collumn = mySudoku.playingField.getCollumnIndex(mySudoku.playingField.selectedField + 1);
                if(mySudoku.playingField.buttons[mySudoku.playingField.selectedField].getText().equals("")) {
                    mySudoku.playingField.counterVec.add(mySudoku.playingField.selectedField);
                }
                mySudoku.playingField.fieldRows[row * 9 + collumn] = e.getKeyCode() - 48;
                mySudoku.playingField.fieldCollumns[collumn * 9 + row] = e.getKeyCode() - 48;
                mySudoku.playingField.fieldBlocks[mySudoku.playingField.selectedField] = e.getKeyCode() - 48;
                mySudoku.playingField.buttons[mySudoku.playingField.selectedField].setText("" + (e.getKeyCode() - 48));
                for(int i = 0; i < 9; i++) {
                    mySudoku.playingField.buttonLayer[mySudoku.playingField.selectedField * 9 + i].setVisible(false);
                    mySudoku.numberField.notationButtons[i].setBackground(mySudoku.colors[0]);
                }
                mySudoku.repaint();
                if(mySudoku.playingField.counterVec.size() == 81) {
                    mySudoku.checkIfWon();
                }
            }
            if(e.getKeyCode() == 127) {
                if (!mySudoku.playingField.buttons[mySudoku.playingField.selectedField].getText().equals("")) {
                    mySudoku.playingField.counterVec.remove(mySudoku.playingField.selectedField);
                    int row = mySudoku.playingField.getRowIndex(mySudoku.playingField.selectedField + 1);
                    int collumn = mySudoku.playingField.getCollumnIndex(mySudoku.playingField.selectedField + 1);
                    mySudoku.playingField.fieldRows[row * 9 + collumn] = 0;
                    mySudoku.playingField.fieldCollumns[collumn * 9 + row] = 0;
                    mySudoku.playingField.fieldBlocks[mySudoku.playingField.selectedField] = 0;
                    mySudoku.playingField.buttons[mySudoku.playingField.selectedField].setText("");
                    for(int i = 0; i < 9; i++) {
                        mySudoku.playingField.buttonLayer[mySudoku.playingField.selectedField * 9 + i].setVisible(true);
                        if(!mySudoku.playingField.buttonLayer[mySudoku.playingField.selectedField * 9 + i].getText().equals("")) {
                            mySudoku.numberField.notationButtons[i].setBackground(mySudoku.colors[3]);
                        }
                    }
                    mySudoku.repaint();
                }
            }
            if(e.getKeyCode() >= 97 && e.getKeyCode() <= 105 && mySudoku.playingField.buttons[mySudoku.playingField.selectedField].getText().equals("")) {
                if(mySudoku.playingField.buttonLayer[mySudoku.playingField.selectedField * 9 + e.getKeyCode() - 97].getText().equals("")) {
                    mySudoku.playingField.buttonLayer[mySudoku.playingField.selectedField * 9 + e.getKeyCode() - 97].setText("" + (e.getKeyCode() - 96));
                    mySudoku.numberField.notationButtons[e.getKeyCode() - 97].setBackground(mySudoku.colors[3]);
                } else {
                    mySudoku.playingField.buttonLayer[mySudoku.playingField.selectedField * 9 + e.getKeyCode() - 97].setText("");
                    mySudoku.numberField.notationButtons[e.getKeyCode() - 97].setBackground(mySudoku.colors[0]);
                }
            }
            if(e.getKeyCode() == 106 && mySudoku.playingField.buttons[mySudoku.playingField.selectedField].getText().equals("")) {
                for(int i = 0; i < 9; i++) {
                    mySudoku.playingField.buttonLayer[mySudoku.playingField.selectedField * 9 + i].setText("");
                    mySudoku.numberField.notationButtons[i].setBackground(mySudoku.colors[0]);
                }
            }
        }

    }
}
