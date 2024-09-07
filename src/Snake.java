import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

public class Snake extends JPanel {
    private int rows = 10;
    private int colums = 10;
    private Tile[][] grid;
    private ArrayList<Tile> snakebody = new ArrayList<>();
    private static int currentDirection;
    private static boolean allowed = true;
    private boolean appled = false;
    private int score;
    
    public Snake() {
        setPreferredSize(new Dimension(700, 700));
        setBackground(Color.BLACK);
        initialize();
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileSize = getWidth() / colums;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                if (grid[i][j].object == Tile.snake) {
                    g.setColor(Color.GREEN);
                } else if (grid[i][j].object == Tile.apple) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                g.setColor(Color.GRAY);
                g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
            }
        }
    }

    public void start() {
        boolean running = true;
        double fps = 1000 / 2;
        score = 0;
        currentDirection = 1;
        spawnApple();
        while(running) {
            snakebody.get(0);
            int[] previous = moveHead();
            for(Tile tile : snakebody) {
                if(snakebody.indexOf(tile) != 0) {
                    tile.object = Tile.nothing;
                    grid[previous[0]][previous[1]].object = Tile.snake;
                    previous = getTileCoordinates(tile);
                }
            }
            if(!appled) {
                snakebody.remove(snakebody.size()-1);
            }
            appled = false;
            allowed = true;
            repaint();
            try {
                Thread.sleep((int) fps);
            } catch (Exception e1) {}
        }
    }

    private int[] moveHead() {
        Tile tile = snakebody.get(0);
        int[] coords = getTileCoordinates(tile);
        int newRow = coords[0];
        int newCol = coords[1];
        
        switch(currentDirection) {
            case 1: // Up
                newRow--;
                break;
            case 2: // Left
                newCol--;
                break;
            case 3: // Right
                newCol++;
                break;
            case 4: // Down
                newRow++;
                break;
        }

        if(newCol >= colums || newRow >= rows || newRow < 0 || newCol < 0 || grid[newRow][newCol].object == Tile.snake) {
            System.out.println("you lost bruv");
            JOptionPane.showMessageDialog(null, "You bumped into something, score: "+score, "Game Over", JOptionPane.OK_OPTION);
            System.exit(0);
        }

        if(grid[newRow][newCol].object == Tile.apple) {
            appled = true;
            score++;
            spawnApple();
        }

        grid[newRow][newCol].object = Tile.snake;
        snakebody.add(0, grid[newRow][newCol]);
        tile.object = Tile.nothing;
        return coords;
    }

    private void initialize() {
        grid = new Tile[rows][colums];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                grid[i][j] = new Tile(Tile.nothing);
            }
        }
        grid[4][5].object = Tile.snake;
        grid[5][5].object = Tile.snake;
        grid[6][5].object = Tile.snake;
        snakebody.add(grid[4][5]);
        snakebody.add(grid[5][5]);
        snakebody.add(grid[6][5]);
    }

    private void spawnApple() {
        Tile appleTile;
        do {
            appleTile = randomTile();
        } while(appleTile.object == Tile.snake);
        appleTile.object = Tile.apple;
    }

    private Tile randomTile() {
        double randomx = Math.random() * rows;
        double randomy = Math.random() * colums;
        return grid[(int) randomx][(int) randomy];
    }

    private int[] getTileCoordinates(Tile tile) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                if (grid[i][j] == tile) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    class Tile {
        public static final int nothing = 0;
        public static final int apple = 1;
        public static final int snake = 2;
        public int object;

        public Tile(int obj) {
            this.object = obj;
        }
    }

    public static void main(String[] args) {
        Snake snake = new Snake();
        JFrame frame = new JFrame("Snake Game");
        frame.setSize(700, 700);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setAlwaysOnTop(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setVisible(true);
        frame.add(snake);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Pressed");
                if(allowed) {
                allowed = false;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        if(currentDirection != 4) {
                            currentDirection = 1;
                        }
                        break;
                    case KeyEvent.VK_A:
                        if(currentDirection != 3) {
                            currentDirection = 2;
                        }
                        break;
                    case KeyEvent.VK_D:
                        if(currentDirection != 2) {
                            currentDirection = 3;
                        }
                        break;
                    case KeyEvent.VK_S:
                        if(currentDirection != 1) {
                            currentDirection = 4;
                        }
                        break;
                    default:
                        allowed = true;
                }}
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        while(allowed) {
            try {
                Thread.sleep(100);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        snake.start();
    }
}