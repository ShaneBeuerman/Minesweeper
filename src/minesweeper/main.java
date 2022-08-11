package minesweeper;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class main {

    static int rows = 0;
    static int cols = 0;
    static int clickedSpaces = 0;
    static int totalSpaces = 0;
    static int mineSpaces = 0;
    static boolean firstPress = true;

    /*
     main method
     */
    public static void main(String[] args) {
        selectModeGUI();
    }

    /*
     Allows the user to create the board.
     */
    public static void selectModeGUI() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        GridLayout gridLayout = new GridLayout(4, 2);
        panel.setLayout(gridLayout);
        JLabel mines = new JLabel("Number of mines?");
        JLabel y = new JLabel("Number of rows");
        JLabel x = new JLabel("Number of columns");
        JTextField mineNumber = new JTextField();
        JTextField xSize = new JTextField();
        JTextField ySize = new JTextField();
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    rows = Integer.parseInt(ySize.getText());
                    cols = Integer.parseInt(xSize.getText());
                    mineSpaces = Integer.parseInt(mineNumber.getText());
                    frame.dispose();
                    initializeBoard();
                } catch (NumberFormatException ex) {
                    errorMessage();
                }
            }

        });
        panel.add(mines);
        panel.add(mineNumber);
        panel.add(y);
        panel.add(ySize);
        panel.add(x);
        panel.add(xSize);
        panel.add(confirm);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /*
     Initializes the board
     */
    public static void initializeBoard() {
        int[][] grid = new int[rows][cols];
        int mines = mineSpaces;
        totalSpaces = rows * cols;
        int curR = 0;
        int curC = 0;
        while (mines > 0) {
            if (curR >= grid.length) {
                curR = 0;
                curC++;
            }
            grid[curR][curC] = 9;
            curR++;
            mines--;
        }
        printGrid(grid);
        randomSwap(grid);
        determineDistance(grid);
        printGrid(grid);
        minesweeperGUI(grid);
    }

    /*
     Determines the distance away from a mine in a 2d array
     */
    public static void determineDistance(int[][] arr) {
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[0].length; c++) {
                int distance = 0;
                if (arr[r][c] == 9) {
                    continue;
                }
                if (r - 1 >= 0) {
                    if (arr[r - 1][c] == 9) {
                        distance++;
                    }
                }
                if (r - 1 >= 0 && c - 1 >= 0) {
                    if (arr[r - 1][c - 1] == 9) {
                        distance++;
                    }
                }
                if (r - 1 >= 0 && c + 1 < arr[0].length) {
                    if (arr[r - 1][c + 1] == 9) {
                        distance++;
                    }
                }
                if (c - 1 >= 0) {
                    if (arr[r][c - 1] == 9) {
                        distance++;
                    }
                }
                if (c + 1 < arr[0].length) {
                    if (arr[r][c + 1] == 9) {
                        distance++;
                    }
                }
                if (r + 1 < arr.length) {
                    if (arr[r + 1][c] == 9) {
                        distance++;
                    }
                }
                if (r + 1 < arr.length && c - 1 >= 0) {
                    if (arr[r + 1][c - 1] == 9) {
                        distance++;
                    }
                }

                if (r + 1 < arr.length && c + 1 < arr[0].length) {
                    if (arr[r + 1][c + 1] == 9) {
                        distance++;
                    }
                }
                arr[r][c] = distance;
            }
        }
    }

    /*
     Randomly swaps a 2d arrays values
     */
    public static void randomSwap(int[][] arr) {
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                int randR = rand.nextInt(arr.length);
                int randC = rand.nextInt(arr[0].length);
                int temp = arr[i][j];
                arr[i][j] = arr[randR][randC];
                arr[randR][randC] = temp;
            }
        }
    }

    /*
     prints the grid for easy testing and reading
     */
    public static void printGrid(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }

    /*
     GUI that makes the minesweeper game
     */
    public static void minesweeperGUI(int[][] grid) {
        int r = grid.length;
        int c = grid[0].length;
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JButton[][] buttons = new JButton[r][c];
        GridLayout layout = new GridLayout(r, c);
        panel.setLayout(layout);
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                buttons[i][j] = new JButton(" ? ");
                buttons[i][j].addActionListener(new leftClick((char) (grid[i][j] + '0')));
                buttons[i][j].addMouseListener(new rightClick());
                panel.add(buttons[i][j]);
            }
        }
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /*
     used to mark a space as a mine or ?
     */
    private static class rightClick extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                Object source = e.getSource();
                JButton button = (JButton) source;
                if (button.getText().equals(" ? ")) {
                    button.setText("M");
                } else if (button.getText().equals("M")) {
                    button.setText(" ? ");
                }
            }
        }

    }

    /*
     used to click on a button
     */
    private static class leftClick implements ActionListener {

        private char val;

        public leftClick(char val) {
            this.val = val;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            JButton button = (JButton) source;
            String value = val + "";
            if (!value.equals(button.getText())) {
                clickedSpaces++;
            }
            button.setText(value);

            if (val == '9') {
                losePopUp();
            }
            if (clickedSpaces == totalSpaces - mineSpaces) {
                winPopUp();
            }

        }
    }

    /*
     Win Message
     */
    public static void winPopUp() {
        JOptionPane.showMessageDialog(null, "Congrats", "You Win", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    /*
     Error message
     */
    public static void errorMessage() {
        JOptionPane.showMessageDialog(null, "Sorry that won't work", "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     Lose Message
     */
    public static void losePopUp() {
        JOptionPane.showMessageDialog(null, "Boom", "You lose", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

}
