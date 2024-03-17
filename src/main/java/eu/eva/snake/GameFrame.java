package eu.eva.snake;

import javax.swing.*;

public class GameFrame extends JFrame {

    private final int boardWidth;
    private final int boardHeight;

    public GameFrame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        this.setTitle("Snake");
        this.setVisible(true);
        this.setSize(boardWidth, boardHeight);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel(boardWidth, boardHeight);
        this.add(panel);
        this.pack();
        panel.requestFocus();
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }
}
