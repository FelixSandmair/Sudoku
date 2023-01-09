import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


//This class implements the frame of the launcher
public class SudokuLauncher extends JFrame {

    private static final int YOffset = 30;
    private static final int XOffset = 50;
    private static final int NEWGAMEY = 450;
    private static final int NEWGAMEX = 35;
    private static final int width = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
    private static final int height = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();

    //different color themes
    private Color[][] colors = {{new Color(0xffffff), new Color(0xbe1622), new Color(0x999999), new Color(0x575756), new Color(0x1d1d1b)}
            ,{new Color(0xfeeee0), new Color(0xcfb7a3), new Color(0xb39378), new Color(0x9c6e4a), new Color(0x644229)}
            ,{new Color(0xf1e5ed), new Color(0xe2b8d7), new Color(0xc695c3), new Color(0x936aab), new Color(0x4e3c5a)}
            ,{new Color(0xe7d1c4), new Color(0xf0855c), new Color(0xbf938f), new Color(0x655772), new Color(0x322942)}
            ,{new Color(0xe3ecec), new Color(0x9dcef1), new Color(0x94acb7), new Color(0x1e6a6f), new Color(0x1e4a58)}};
    private int theme = 1;
    private final JButton start = new JButton();
    private final JButton newGame = new JButton();
    private final JButton easy = new JButton();
    private final JButton medium = new JButton();
    private final JButton hard = new JButton();
    private final JButton colorTheme = new JButton();
    private final JButton quit = new JButton();
    private final JButton[] colorThemeChooser = new JButton[5];
    private final JLabel sudokuTitleScreen = new JLabel();
    private final JLabel sudokuLogoLauncher = new JLabel();
    private final JLabel backgroundLauncher = new JLabel();
    private final JLabel sudokuIllustration = new JLabel();
    private final JLabel logoLabel = new JLabel();
    private final JLayeredPane background = new JLayeredPane();

    //standard game launcher
    SudokuLauncher() {
        setLocation(width/2 - 750, height/2 - 530);

        setTitleScreen();
    }

    //used for menu buttons to go back to menu
    SudokuLauncher(int prevX, int prevY) {
        setLocation(prevX, prevY);

        setTitleScreen();

        startListener(new ActionEvent(hard, 1, ""));
    }

    //sets up the title screen
    public void setTitleScreen() {
        //setup this frame
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1500,1030);
        setResizable(true);
        setTitle("");
        setIconImage(new ImageIcon("images\\GameIcon.jpeg").getImage());
        add(background);


        //set background settings
        background.setOpaque(true);
        background.setLayout(null);
        background.setBounds(0,0,1500,1060);
        background.setBackground(colors[theme - 1][0]);

        //setup background for title screen
        //setup sudoku logo
        sudokuTitleScreen.setIcon(new ImageIcon("images\\Theme " + theme + "\\Titel Groß.png"));
        sudokuTitleScreen.setBounds(background.getWidth()/2 - sudokuTitleScreen.getIcon().getIconWidth()/2,150,sudokuTitleScreen.getIcon().getIconWidth(),sudokuTitleScreen.getIcon().getIconHeight());
        background.add(sudokuTitleScreen,Integer.valueOf(1));

        //setup start button
        start.setIcon(new ImageIcon("images\\Theme " + theme + "\\Start.png"));
        start.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Start - Active.png"));
        start.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Start - Pressed.png"));
        start.setBorderPainted(false);
        start.setBackground(colors[theme - 1][0]);
        start.setBounds(background.getWidth()/2 - start.getIcon().getIconWidth()/2 - 40,800,start.getIcon().getIconWidth(),start.getIcon().getIconHeight());
        start.addActionListener(this::startListener);
        background.add(start, Integer.valueOf(1));

        //set frame visible
        setVisible(true);
    }


    //sets up all the difficulty buttons
    public void setDifficultyButtons() {

        //easy button settings
        easy.setIcon(new ImageIcon("images\\Theme " + theme + "\\Easy.png"));
        easy.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Easy - Active.png"));
        easy.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Easy - Pressed.png"));
        easy.setBorderPainted(false);
        easy.setBackground(colors[theme - 1][0]);
        easy.setBounds(NEWGAMEX + XOffset, NEWGAMEY + 46 + YOffset, easy.getIcon().getIconWidth(),easy.getIcon().getIconHeight());
        easy.addActionListener(this::difficultyListeners);

        //medium button settings
        medium.setIcon(new ImageIcon("images\\Theme " + theme + "\\Medium.png"));
        medium.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Medium - Active.png"));
        medium.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Medium - Pressed.png"));
        medium.setBorderPainted(false);
        medium.setBackground(colors[theme - 1][0]);
        medium.setBounds(easy.getX() + XOffset,easy.getY() + easy.getHeight() + YOffset - 10,medium.getIcon().getIconWidth(),medium.getIcon().getIconHeight());
        medium.addActionListener(this::difficultyListeners);

        //hard button settings
        hard.setIcon(new ImageIcon("images\\Theme " + theme + "\\Hard.png"));
        hard.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Hard - Active.png"));
        hard.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Hard - Pressed.png"));
        hard.setBorderPainted(false);
        hard.setBackground(colors[theme - 1][0]);
        hard.setBounds(medium.getX() + XOffset,medium.getY() + medium.getHeight() + YOffset,hard.getIcon().getIconWidth(),hard.getIcon().getIconHeight());
        hard.addActionListener(this::difficultyListeners);

        //add to background but invisible
        background.add(easy, Integer.valueOf(1));
        background.add(medium, Integer.valueOf(1));
        background.add(hard, Integer.valueOf(1));
        easy.setVisible(false);
        medium.setVisible(false);
        hard.setVisible(false);
    }

    public void setColorThemeChooser() {
        for(int i = 1; i <= 5; i++) {
            colorThemeChooser[i-1] = new JButton();
            colorThemeChooser[i-1].setIcon(new ImageIcon("images\\Theme " + theme + "\\Theme #" + i + ".png"));
            colorThemeChooser[i-1].setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Theme #" + i + " - Active.png"));
            colorThemeChooser[i-1].setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Theme #" + i + " - Pressed.png"));
            colorThemeChooser[i-1].setBorderPainted(false);
            colorThemeChooser[i-1].setBackground(colors[theme - 1][0]);
            colorThemeChooser[i-1].setBounds(colorTheme.getX() + XOffset + colorThemeChooser[i-1].getIcon().getIconWidth() * (i-1) + 20
                    , colorTheme.getY() + colorTheme.getHeight() + YOffset
                    , colorThemeChooser[i-1].getIcon().getIconWidth()
                    , colorThemeChooser[i-1].getIcon().getIconHeight());
            colorThemeChooser[i-1].setVisible(false);
            colorThemeChooser[i-1].addActionListener(this::colorChooserListener);
            background.add(colorThemeChooser[i-1], Integer.valueOf(1));
        }
    }

    public void startListener(ActionEvent e) {
        //remove old components
        background.remove(sudokuTitleScreen);
        background.remove(start);

        //Set new background
        backgroundLauncher.setIcon(new ImageIcon("images\\Theme " + theme + "\\Hintergrund.png"));
        backgroundLauncher.setBounds(0,0,backgroundLauncher.getIcon().getIconWidth(),backgroundLauncher.getIcon().getIconHeight());
        background.add(backgroundLauncher,Integer.valueOf(0));

        //Sudoku logo on background
        sudokuLogoLauncher.setIcon(new ImageIcon("images\\Theme " + theme + "\\Titel Klein.png"));
        sudokuLogoLauncher.setBounds(background.getWidth()/2 - sudokuLogoLauncher.getIcon().getIconWidth()/2 - 100,60,sudokuLogoLauncher.getIcon().getIconWidth(),sudokuLogoLauncher.getIcon().getIconHeight());
        background.add(sudokuLogoLauncher, Integer.valueOf(1));

        //Add new game button
        newGame.setIcon(new ImageIcon("images\\Theme " + theme + "\\New Game.png"));
        newGame.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\New Game - Active.png"));
        newGame.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\New Game - Pressed.png"));
        newGame.setBorderPainted(false);
        newGame.setBackground(colors[theme - 1][0]);
        newGame.setBounds(NEWGAMEX,NEWGAMEY,newGame.getIcon().getIconWidth(),newGame.getIcon().getIconHeight());
        newGame.addActionListener(this::newGameListener);
        background.add(newGame,Integer.valueOf(1));

        //Add color theme button
        colorTheme.setIcon(new ImageIcon("images\\Theme " + theme + "\\Color Theme.png"));
        colorTheme.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Color Theme - Active.png"));
        colorTheme.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Color Theme - Pressed.png"));
        colorTheme.setBorderPainted(false);
        colorTheme.setBackground(colors[theme - 1][0]);
        colorTheme.addActionListener(this::colorThemeListener);
        colorTheme.setBounds(newGame.getX() + XOffset,newGame.getY() + newGame.getHeight() + YOffset,colorTheme.getIcon().getIconWidth(),colorTheme.getIcon().getIconHeight());
        background.add(colorTheme, Integer.valueOf(1));

        //Add quit button
        quit.setIcon(new ImageIcon("images\\Theme " + theme + "\\Quit.png"));
        quit.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Quit - Active.png"));
        quit.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Quit - Pressed.png"));
        quit.setBorderPainted(false);
        quit.setBackground(colors[theme - 1][0]);
        quit.addActionListener(this::quitListener);
        quit.setBounds(colorTheme.getX() + XOffset,colorTheme.getY() + colorTheme.getHeight() + YOffset,quit.getIcon().getIconWidth(), quit.getIcon().getIconHeight());
        background.add(quit, Integer.valueOf(1));

        //Sudoku illustration
        sudokuIllustration.setIcon(new ImageIcon("images\\Theme " + theme + "\\Sudoku.png"));
        sudokuIllustration.setBounds(900,350,sudokuIllustration.getIcon().getIconWidth(), sudokuIllustration.getIcon().getIconHeight());
        background.add(this.sudokuIllustration, Integer.valueOf(1));

        //set Logo
        logoLabel.setIcon(new ImageIcon("images\\Theme " + theme + "\\Logo.png"));
        logoLabel.setBounds(backgroundLauncher.getIcon().getIconWidth() - logoLabel.getIcon().getIconWidth() - 35, backgroundLauncher.getIcon().getIconHeight() - logoLabel.getIcon().getIconHeight() - 50, logoLabel.getIcon().getIconWidth(), logoLabel.getIcon().getIconHeight());
        background.add(logoLabel, Integer.valueOf(1));

        setDifficultyButtons();

        setColorThemeChooser();

        repaint();
    }

    public void newGameListener(ActionEvent e) {
        if(!easy.isVisible()) {
            easy.setVisible(true);
            medium.setVisible(true);
            hard.setVisible(true);
            colorTheme.setBounds(hard.getX() + XOffset, hard.getY() + hard.getIcon().getIconHeight() + YOffset, colorTheme.getIcon().getIconWidth(), colorTheme.getIcon().getIconHeight());
            if(colorThemeChooser[0].isVisible()) {
                for(int i = 0; i < 5; i++) {
                    colorThemeChooser[i].setBounds(colorTheme.getX() + XOffset + colorThemeChooser[i].getWidth() * i + 20, colorTheme.getY() + colorTheme.getIcon().getIconHeight() + YOffset, colorThemeChooser[i].getWidth(), colorThemeChooser[i].getHeight());
                }
                quit.setBounds(colorThemeChooser[0].getX() + XOffset, colorThemeChooser[0].getY() + colorThemeChooser[0].getHeight() + YOffset, quit.getWidth(), quit.getHeight());
            } else {
                quit.setBounds(colorTheme.getX() + XOffset, colorTheme.getY() + colorTheme.getHeight() + YOffset, quit.getWidth(), quit.getHeight());
            }
        } else {
            easy.setVisible(false);
            medium.setVisible(false);
            hard.setVisible(false);
            colorTheme.setBounds(newGame.getX() + XOffset, newGame.getY() + newGame.getIcon().getIconHeight() + YOffset, colorTheme.getIcon().getIconWidth(), colorTheme.getIcon().getIconHeight());
            if(colorThemeChooser[0].isVisible()) {
                for(int i = 0; i < 5; i++) {
                    colorThemeChooser[i].setBounds(colorTheme.getX() + XOffset + colorThemeChooser[i].getWidth() * i + 20, colorTheme.getY() + colorTheme.getIcon().getIconHeight() + YOffset, colorThemeChooser[i].getWidth(), colorThemeChooser[i].getHeight());
                }
                quit.setBounds(colorThemeChooser[0].getX() + XOffset, colorThemeChooser[0].getY() + colorThemeChooser[0].getHeight() + YOffset, quit.getWidth(), quit.getHeight());
            } else {
                quit.setBounds(colorTheme.getX() + XOffset, colorTheme.getY() + colorTheme.getHeight() + YOffset, quit.getWidth(), quit.getHeight());
            }
        }
        repaint();
    }

    public void colorThemeListener(ActionEvent e) {
        if(!colorThemeChooser[0].isVisible()) {
            for(int i = 0; i < 5; i++) {
                colorThemeChooser[i].setBounds(colorTheme.getX() + XOffset + colorThemeChooser[i].getWidth() * i + 20, colorTheme.getY() + colorTheme.getIcon().getIconHeight() + YOffset, colorThemeChooser[i].getWidth(), colorThemeChooser[i].getHeight());
                colorThemeChooser[i].setVisible(true);
            }
            colorThemeChooser[theme-1].setIcon(new ImageIcon("images\\Theme " + theme + "\\Theme #" + theme + " - Active.png"));
            quit.setBounds(colorThemeChooser[0].getX() + XOffset, colorThemeChooser[0].getY() + colorThemeChooser[0].getHeight() + YOffset, quit.getWidth(), quit.getHeight());
        } else {
            for(int i = 0; i < 5; i++) {
                colorThemeChooser[i].setVisible(false);
            }
            quit.setBounds(colorTheme.getX() + XOffset, colorTheme.getY() + colorTheme.getHeight() + YOffset, quit.getWidth(), quit.getHeight());
        }
    }

    public void updateTheme() {

        easy.setIcon(new ImageIcon("images\\Theme " + theme + "\\Easy.png"));
        easy.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Easy - Active.png"));
        easy.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Easy - Pressed.png"));
        medium.setIcon(new ImageIcon("images\\Theme " + theme + "\\Medium.png"));
        medium.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Medium - Active.png"));
        medium.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Medium - Pressed.png"));
        hard.setIcon(new ImageIcon("images\\Theme " + theme + "\\Hard.png"));
        hard.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Hard - Active.png"));
        hard.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Hard - Pressed.png"));

        for(int i = 0; i < 5; i++) {
            colorThemeChooser[i].setIcon(new ImageIcon("images\\Theme " + theme + "\\Theme #" + (i + 1) + ".png"));
            colorThemeChooser[i].setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Theme #" + (i + 1) + " - Active.png"));
            colorThemeChooser[i].setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Theme #" + (i + 1) + " - Pressed.png"));
            colorThemeChooser[i].setBackground(colors[theme - 1][0]);
            if(theme == (i + 1)) {
                colorThemeChooser[i].setIcon(new ImageIcon("images\\Theme " + theme + "\\Theme #" + (i + 1) + " - Active.png"));
            }
        }

        backgroundLauncher.setIcon(new ImageIcon("images\\Theme " + theme + "\\Hintergrund.png"));

        sudokuLogoLauncher.setIcon(new ImageIcon("images\\Theme " + theme + "\\Titel Klein.png"));

        newGame.setIcon(new ImageIcon("images\\Theme " + theme + "\\New Game.png"));
        newGame.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\New Game - Active.png"));
        newGame.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\New Game - Pressed.png"));

        colorTheme.setIcon(new ImageIcon("images\\Theme " + theme + "\\Color Theme.png"));
        colorTheme.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Color Theme - Active.png"));
        colorTheme.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Color Theme - Pressed.png"));

        quit.setIcon(new ImageIcon("images\\Theme " + theme + "\\Quit.png"));
        quit.setRolloverIcon(new ImageIcon("images\\Theme " + theme + "\\Quit - Active.png"));
        quit.setPressedIcon(new ImageIcon("images\\Theme " + theme + "\\Quit - Pressed.png"));

        sudokuIllustration.setIcon(new ImageIcon("images\\Theme " + theme + "\\Sudoku.png"));

        logoLabel.setIcon(new ImageIcon("images\\Theme " + theme + "\\Logo.png"));

        background.setBackground(colors[theme - 1][0]);
        start.setBackground(colors[theme - 1][0]);
        easy.setBackground(colors[theme - 1][0]);
        medium.setBackground(colors[theme - 1][0]);
        hard.setBackground(colors[theme - 1][0]);
        newGame.setBackground(colors[theme - 1][0]);
        colorTheme.setBackground(colors[theme - 1][0]);
        quit.setBackground(colors[theme - 1][0]);
    }

    public void difficultyListeners(ActionEvent e) {
        dispose();
        if(e.getSource() == easy) {
            Sudoku s = new Sudoku(Difficulty.EASY, colors[theme-1], getX(), getY(), getWidth(), getHeight(), theme);
        }
        if(e.getSource() == medium) {
            Sudoku s = new Sudoku(Difficulty.MEDIUM, colors[theme-1], getX(), getY(), getWidth(), getHeight(), theme);
        }
        if(e.getSource() == hard) {
            Sudoku s = new Sudoku(Difficulty.HARD, colors[theme-1], getX(), getY(), getWidth(), getHeight(), theme);
        }
    }

    public void colorChooserListener(ActionEvent e) {
        for(int i = 0; i < 5; i++) {
            if(e.getSource() == colorThemeChooser[i]) {
                theme = i+1;
            }
        }
        updateTheme();
        repaint();
    }

    public void quitListener(ActionEvent e) {
        dispose();
    }
}
