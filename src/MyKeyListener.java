import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//KeyListener class that waits for number and notation number input
public class MyKeyListener implements KeyListener {

    private final PlayingField playingField;

    MyKeyListener(PlayingField playingField) {
        this.playingField = playingField;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    //numbers and notations get placed if buttons are released
    @Override
    public void keyReleased(KeyEvent e) {

        //Keycodes between 49 and 57 is the number row from 1 to , and they are used for placing numbers
        if (e.getKeyCode() >= 49 && e.getKeyCode() <= 57) {
            playingField.setButtonNumber(e.getKeyCode() - 48);
            return;
        }

        //Keycode 127 is from the delete key and is used to remove a number from a field
        if (e.getKeyCode() == 127) {
            playingField.deleteNumberFromSelectedCell();
            return;
        }

        //Keycodes between 97 and 105 is the numpad of they keyboard which is used to place and remove notation buttons
        if (e.getKeyCode() >= 97 && e.getKeyCode() <= 105) {
            playingField.setNotationLayerNumber(e.getKeyCode() - 96);
            return;
        }

        //Keycode 106 is the multiply key of the numpad (looks like an x) and deletes all the notes made previously
        if (e.getKeyCode() == 106) {
            playingField.deleteAllNotesInSelectedCell();
            return;
        }

        switch (e.getKeyCode()) {
            case 37 -> playingField.moveSelectedCell(-1, 0);
            case 38 -> playingField.moveSelectedCell(0, -1);
            case 39 -> playingField.moveSelectedCell(1, 0);
            case 40 -> playingField.moveSelectedCell(0, 1);
            default -> {
            }
        }
    }
}
