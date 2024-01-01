import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Objects;

public class TicTacToe extends JPanel implements ActionListener {

    // core logic variables
    boolean playerX; // true if player X's turn, false if player O's turn
    boolean gameDone = true; // true if game is over
    boolean scoreUpdated = false;
    int player1wins = 0, player2wins = 0; // number of wins for each player
    String[][] board = {
        {" "," "," "},
        {" "," "," "},
        {" "," "," "}
    };

    static HashMap<String, Integer> scores = new HashMap<>();
    // paint variables
    int lineWidth = 5; // width of the lines
    int lineLength = 270; // length of the lines
    int x = 15, y = 100; // location of first line
    int offset = 95; // square width
    int a = 0; // used for drawing the X's and O's
    int b = 5; // used for drawing the X's and O's
    int selX = 0; // selected square x
    int selY = 0; // selected square y
    int winner = -2;
    // COLORS
    Color turtle = new Color(152, 109, 142);
    Color orange = new Color(255, 165, 0);
    Color offwhite = new Color(0xf7f7f7);
    Color darkgray = new Color(239, 227, 208);
    Color pink = new Color(130, 92, 121);

    // COMPONENTS
    JButton jButton;

    // CONSTRUCTOR
    public TicTacToe() {
        scores.put("tie",0);
        scores.put("X",1);
        scores.put("O",-1);

        Dimension size = new Dimension(440, 300); // size of the panel
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        addMouseListener(new XOListener()); // add mouse listener
        jButton = new JButton("New Game");
        jButton.addActionListener(this); // add action listener
        jButton.setBounds(315, 210, 100, 30); // set button location
        add(jButton); // add button to panel
        //resetGame();
    }

    public void resetGame() {
        playerX = false;
        gameDone = false;
        scoreUpdated = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = " "; // all spots are empty
            }
        }
        getJButton().setVisible(false); // hide the button
    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        drawBoard(page);
        drawUI(page);
        drawGame(page);
    }

    public void drawBoard(Graphics page) {
        setBackground(turtle);
        page.setColor(darkgray);
        page.fillRoundRect(x, y, lineLength, lineWidth, 5, 30);
        page.fillRoundRect(x, y + offset, lineLength, lineWidth, 5, 30);
        page.fillRoundRect(y, x, lineWidth, lineLength, 30, 5);
        page.fillRoundRect(y + offset, x, lineWidth, lineLength, 30, 5);
    }

    public void drawUI(Graphics page) {
        // SET COLOR AND FONT
        page.setColor(pink);
        page.fillRect(300, 0, 140, 300);
        Font font = new Font("Helvetica", Font.PLAIN, 20);
        page.setFont(font);

        // SET WIN COUNTER
        page.setColor(offwhite);
        page.drawString("Win Count", 310, 30);
        page.drawString(": " + player1wins, 362, 70);
        page.drawString(": " + player2wins, 362, 105);

        // DRAW score X
        ImageIcon xIcon = new ImageIcon("orangex.png");
        Image xImg = xIcon.getImage();
        Image newXImg = xImg.getScaledInstance(27, 27, java.awt.Image.SCALE_SMOOTH);
        ImageIcon newXIcon = new ImageIcon(newXImg);
        page.drawImage(newXIcon.getImage(), 44 + offset * 1 + 190, 47 + offset * 0, null);

        // DRAW score O
        page.setColor(offwhite);
        page.fillOval(43 + 190 + offset, 80, 30, 30);
        page.setColor(darkgray);
        page.fillOval(49 + 190 + offset, 85, 19, 19);

        // DRAW WHOS TURN or WINNER
        page.setColor(offwhite);
        Font font1 = new Font("Serif", Font.ITALIC, 18);
        page.setFont(font1);
        String result = MiniMax.checkWinner(board);
        if (gameDone) {
            if (winner == 1) { // x
                page.drawString("The PC Win", 310, 150);
                page.drawImage(xImg, 335, 160, null);
            } else if (winner == -1) { // o
                page.drawString("The winner is", 310, 150);
                page.setColor(offwhite);
                page.fillOval(332, 160, 50, 50);
                page.setColor(darkgray);
                page.fillOval(342, 170, 30, 30);
            } else if (winner == 0) { // tie
                page.drawString("It's a tie", 330, 178);
            }
        } else{
            Font font2 = new Font("Serif", Font.ITALIC, 20);
            page.setFont(font2);
            page.drawString("", 350, 160);
            if (!playerX) {
                page.drawString("PC 's Turn", 325, 180);
            } else {
                page.drawString("Your Turn", 325, 180);
            }
        }
    }

    public void drawGame(Graphics page) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Objects.equals(board[i][j], " ")) {

                } else if (Objects.equals(board[i][j], "X")) {
                    ImageIcon xIcon = new ImageIcon("orangex.png");
                    Image xImg = xIcon.getImage();
                    page.drawImage(xImg, 30 + offset * i, 30 + offset * j, null);
                } else if (Objects.equals(board[i][j], "O")) {
                    page.setColor(offwhite);
                    page.fillOval(30 + offset * i, 30 + offset * j, 50, 50);
                    page.setColor(turtle);
                    page.fillOval(40 + offset * i, 40 + offset * j, 30, 30);
                }
            }
        }
        // AI move start
        if(!gameDone && !playerX){
            MiniMax.playAI(board);
            playerX = true;
        }
        String result = MiniMax.checkWinner(board);
        if(result != null && !scoreUpdated){
            winner = scores.get(result);
            gameDone = true;
            scoreUpdated = true;
            if(winner == 1)
                player1wins++;
            else if(winner == -1)
                player2wins++;
            getJButton().setVisible(true);
        }
        repaint();
    }

    public JButton getJButton() {
        return jButton;
    }

    private class XOListener implements MouseListener {

        public void mouseClicked(MouseEvent event) {
            selX = -1;
            selY = -1;
            if (!gameDone) {
                a = event.getX();
                b = event.getY();
                int selX = 0, selY = 0;
                if (a > 12 && a < 99) {
                    selX = 0;
                } else if (a > 103 && a < 195) {
                    selX = 1;
                } else if (a > 200 && a < 287) {
                    selX = 2;
                } else {
                    selX = -1;
                }

                if (b > 12 && b < 99) {
                    selY = 0;
                } else if (b > 103 && b < 195) {
                    selY = 1;
                } else if (b > 200 && b < 287) {
                    selY = 2;
                } else {
                    selY = -1;
                }
                if (selX != -1 && selY != -1) {

                    if (Objects.equals(board[selX][selY], " ")) {
                        if (playerX) {
                            board[selX][selY] = "O";
                            playerX = false;
                        }
                        System.out.println(" CLICK= x:" + a + ",y: " + b + "; selX,selY: " + selX + "," + selY);
                    }
                } else {
                    System.out.println("invalid click");
                }
            }
        }

        public void mouseReleased(MouseEvent event) {
        }

        public void mouseEntered(MouseEvent event) {
        }

        public void mouseExited(MouseEvent event) {
        }

        public void mousePressed(MouseEvent event) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resetGame();
    }

}