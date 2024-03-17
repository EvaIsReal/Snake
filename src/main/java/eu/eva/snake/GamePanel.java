package eu.eva.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {

    private final int boardWidth;
    private final int boardHeight;
    private final int tileSize;
    private int velocityX, velocityY;

    private GameTile snakeHead;
    private GameTile food;
    private ArrayList<GameTile> snakeBody;

    private Timer gameLoop;
    private boolean gameOver;
    private int score;

    public GamePanel(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = 25;

        this.velocityX = 0;
        this.velocityY = 0;

        this.addKeyListener(this);
        this.setFocusable(true);

        this.snakeHead = new GameTile(5, 5);
        this.food = new GameTile(10, 10);
        this.snakeBody = new ArrayList<>();

        this.gameLoop = new Timer(100, this::onTick);

        gameLoop.start();
        this.setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        this.setBackground(Color.black);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid
        /*for (int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }*/

        // Food
        g.setColor(Color.RED);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        // Snake Head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        // Snake Body
        snakeBody.forEach(bodyTile -> {
            g.fillRect(bodyTile.x * tileSize, bodyTile.y * tileSize, tileSize, tileSize);
        });

        // Score
        g.setFont(new Font("Arial", Font.BOLD, 16));
        if(gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(this.score), tileSize - 16, tileSize);
        } else {
            g.setColor(Color.white);
            g.drawString("Score: " + String.valueOf(this.score), tileSize - 16, tileSize);
        }
    }

    private void move() {
        // Eating food
        if(isColliding(snakeHead, food)) {
            snakeBody.add(new GameTile(food.x, food.y));
            score++;
            placeFoodRandomly();
        }

        // Snake Body
        if(!snakeBody.isEmpty()) {
            for (int i = snakeBody.size() - 1; i >= 0; i--) {
                GameTile part = snakeBody.get(i);
                if(i == 0) {
                    part.x = snakeHead.x;
                    part.y = snakeHead.y;
                } else {
                    GameTile prevPart = snakeBody.get(i - 1);
                    part.x = prevPart.x;
                    part.y = prevPart.y;
                }
            }
        }

        // SnakeHead
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // GameOver
        for (GameTile part : snakeBody) {
            if(isColliding(snakeHead, part)) this.gameOver = true;
        }

        if(snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            this.gameOver = true;
        }
    }

    private boolean isColliding(GameTile a, GameTile b) {
        return a.x == b.x && a.y == b.y;
    }
    private void onTick(ActionEvent e) {
        repaint();
        move();
        if(gameOver) {
            gameLoop.stop();
        }
    }

    public void placeFoodRandomly() {
        Random r = new Random();
        food.x = r.nextInt(boardWidth/tileSize);
        food.y = r.nextInt(boardHeight/tileSize);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            this.velocityX = 0;
            this.velocityY = -1;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            this.velocityX = 0;
            this.velocityY = 1;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            this.velocityX = -1;
            this.velocityY = 0;
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            this.velocityX = 1;
            this.velocityY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    private static class GameTile {
        private int x;
        private int y;

        public GameTile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
