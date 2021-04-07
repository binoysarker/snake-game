import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 110;
    static int[] x = new int[GAME_UNITS];
    static int[] y = new int[GAME_UNITS];
    public int bodyParts = 6;
    public int appleEaten = 0;
    public int appleX;
    public int appleY;
    public static final char[] defaultKeyStrokes = {'L', 'R', 'U', 'D'};
    public char direction = defaultKeyStrokes[3];
    public boolean running = false;
    public Timer timer = new Timer(DELAY, this);
    public Random random = new Random();
    JButton jButton = new JButton("restart");
    MyKeyAdapter myKeyAdapter = new MyKeyAdapter();


    GamePanel() {


        startGame();
    }

    public void startGame() {
        x = new int[GAME_UNITS];
        y = new int[GAME_UNITS];
        appleEaten = 0;
        bodyParts = 6;

        direction = defaultKeyStrokes[1];
        jButton.setVisible(false);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.add(jButton);
        this.setFocusable(true);
        this.addKeyListener(myKeyAdapter);
        newApple();
        running = true;

        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(g);
    }

    public void draw(Graphics g) {
        if (running) {

            // draw the grid
            for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE); i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            // draw the apple
            g.setColor(Color.RED);
            g.fillOval(this.appleX, this.appleY, UNIT_SIZE, UNIT_SIZE);
            // draw the head and body of the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(random.nextInt(225), random.nextInt(225), 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            // to show the record
            String text = "Score: " + appleEaten;
            FontMetrics fontMetrics = getFontMetrics(g.getFont());
            showText(g, text, 30, SCREEN_WIDTH / 2 - 100, g.getFont().getSize() + 50);
        } else {
            gameOver(g);
        }
    }

    public void showText(Graphics g, String text, int fontSize, int width, int height) {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, fontSize));

        g.drawString(text, width, height);
    }

    public void newApple() {
        this.appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        this.appleX = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = this.bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (direction == defaultKeyStrokes[2]) {
            y[0] = y[0] - UNIT_SIZE;
        } else if (direction == defaultKeyStrokes[3]) {
            y[0] = y[0] + UNIT_SIZE;
        } else if (direction == defaultKeyStrokes[0]) {
            x[0] = x[0] - UNIT_SIZE;
        } else if (direction == defaultKeyStrokes[1]) {
            x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        // if head touch border
        if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        // stop timer
        if (!running) {
            timer.stop();
        }
    }
    public void resetGame(Graphics g){
        this.invalidate();
        this.validate();
        this.repaint();
        this.update(g);

        startGame();


    }

    public void gameOver(Graphics g) {
        String text = "Game Over";
        FontMetrics fontMetrics = getFontMetrics(g.getFont());
        showText(g, text, 75, SCREEN_WIDTH / 2 - 200, SCREEN_HEIGHT / 2);
        jButton.setVisible(true);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("game restarts");

                resetGame(g);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != defaultKeyStrokes[1]) {
                        direction = defaultKeyStrokes[0];
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != defaultKeyStrokes[0]) {
                        direction = defaultKeyStrokes[1];
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != defaultKeyStrokes[3]) {
                        direction = defaultKeyStrokes[2];
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != defaultKeyStrokes[2]) {
                        direction = defaultKeyStrokes[3];
                    }
                    break;
            }
        }
    }
}
