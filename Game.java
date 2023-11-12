import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Game extends Frame implements ActionListener, KeyListener {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 800;
    private static final int TUBE_GAP = WINDOW_HEIGHT/5;
    private static final int TUBE_WIDTH = WINDOW_WIDTH/10;
    private static final int TUBE_HEIGHT = WINDOW_HEIGHT;
    private static final int BOOT_WIDTH = 50;
    private static final int BOOT_HEIGHT = 50;
    private static final int BOOT_X = WINDOW_WIDTH/6;
    private int BOOT_Y = WINDOW_HEIGHT/2;
    private static final int BOOT_JUMP_DELTA = 10;
    private static final int BOOT_FALL_DELTA = 7;
    private static final int BOOT_JUMP_HEIGHT = BOOT_HEIGHT + BOOT_JUMP_DELTA*2;
    private static final int UPDATE_DELTA = 30;
    private static final int TUBE_SPEED = 5;
    private static final int DELAY = 400;
    private boolean doLoop = true;
    private boolean key_released = true;
    private boolean playGame = false;
    private boolean bootJump = false;
    private boolean bootJumping = false;
    private boolean fast_falling = false;
    private JFrame frame = new JFrame("Jetty Boot");
    private JPanel topPanel;
    private JButton startButton;
    private static Game g = new Game();
    private static GameScreen gs;

    public Game() {
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.buildFrame();
                // create a new thread to keep the GUI responsive while the game runs
                Thread t = new Thread() {
                    public void run() {
                        g.gameScreen();
                    }
                };
                t.start();
            }
        });
    }

    private void buildFrame() {
        Image icon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("res/boot.png"));
        frame.setContentPane(createContentPane());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setIconImage(icon);
        frame.addKeyListener(this);
    }

    private JPanel createContentPane() {
        topPanel = new JPanel();
        topPanel.setBackground(Color.BLACK);
        LayoutManager overlay = new OverlayLayout(topPanel);
        topPanel.setLayout(overlay);

        startButton = new JButton("START");
        startButton.setBackground(Color.GREEN);
        startButton.setForeground(Color.BLACK);
        startButton.setFocusable(false);
        startButton.setAlignmentX(0.5f);
        startButton.setAlignmentY(0.5f);
        startButton.setFont(new Font("Arial", Font.BOLD, 48));
        startButton.addActionListener(this);
        topPanel.add(startButton);
        gs = new GameScreen(WINDOW_WIDTH, WINDOW_HEIGHT);
        topPanel.add(gs);
        return topPanel;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton) {
            topPanel.remove(startButton);
            playGame = true;
            topPanel.revalidate();
            topPanel.repaint();
        }
    }

    private void gameScreen() {
        Tube bottomTube1 = new Tube(TUBE_WIDTH, TUBE_HEIGHT);
        Tube topTube1 = new Tube(TUBE_WIDTH, TUBE_HEIGHT);
        Tube bottomTube2 = new Tube(TUBE_WIDTH, TUBE_HEIGHT);
        Tube topTube2 = new Tube(TUBE_WIDTH, TUBE_HEIGHT);

        Boot boot = new Boot(BOOT_WIDTH, BOOT_HEIGHT);
        int boot_X = BOOT_X;
        int boot_Y = BOOT_Y;

        int x1 = WINDOW_WIDTH + DELAY;
        int x2 = (int) (1.5 * (double) WINDOW_WIDTH) + DELAY;
        int y1 = randomTubeY();
        int y2 = randomTubeY();

        long timestamp = System.currentTimeMillis();

        while(doLoop) {
            if((System.currentTimeMillis() - timestamp) > UPDATE_DELTA) {
                if(x1 < (-TUBE_WIDTH)) {
                    x1 = WINDOW_WIDTH;
                    y1 = randomTubeY();
                }
                if(x2 < (-TUBE_WIDTH)) {
                    x2 = WINDOW_WIDTH;
                    y2 = randomTubeY();
                }

                if(playGame) {
                    x1 -= TUBE_SPEED;
                    x2 -= TUBE_SPEED;
                }

                if(bootJumping && playGame) {
                    BOOT_Y = boot_Y;
                    bootJumping = false;
                }

                if(bootJump && playGame) {
                    if(BOOT_Y - boot_Y - BOOT_JUMP_DELTA < BOOT_JUMP_HEIGHT) {
                        if (boot_Y - BOOT_JUMP_DELTA > 0) {
                            boot_Y -= BOOT_JUMP_DELTA;
                        } else {
                            boot_Y = 0;
                            BOOT_Y = boot_Y;
                            bootJump = false;
                        }
                    } else if(playGame) {
                        BOOT_Y = boot_Y;
                        bootJump = false;
                    }
                } else if(playGame) {
                    if(fast_falling) {
                        boot_Y += BOOT_FALL_DELTA*2;
                    } else {
                        boot_Y += BOOT_FALL_DELTA;
                    }
                    BOOT_Y = boot_Y;
                }


                bottomTube1.setX(x1);
                bottomTube1.setY(y1);
                bottomTube2.setX(x2);
                bottomTube2.setY(y2);
                topTube1.setX(x1);
                topTube1.setY(y1 - TUBE_GAP - TUBE_HEIGHT);
                topTube2.setX(x2);
                topTube2.setY(y2 - TUBE_GAP - TUBE_HEIGHT);

                if(playGame) {
                    boot.setX(boot_X);
                    boot.setY(boot_Y);
                    gs.setBoot(boot);
                }

                gs.setBottomTube(bottomTube1, bottomTube2);
                gs.setTopTube(topTube1, topTube2);

                if(playGame && boot.getWidth() != -1) {
                    detectCollisions(bottomTube1, bottomTube2, topTube1, topTube2, boot);
                    updateScore(bottomTube1, bottomTube2, boot);
                }

                topPanel.revalidate();
                topPanel.repaint();

                timestamp = System.currentTimeMillis();
            }
        }
    }

    private int randomTubeY() {
        int tmp = 0;
        while(tmp < TUBE_GAP + 50 || tmp > WINDOW_HEIGHT - TUBE_GAP) {
            tmp = (int) (Math.random() * (double) WINDOW_HEIGHT);
        }
        return tmp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!playGame) return;
        if(e.getKeyCode() == KeyEvent.VK_SPACE && key_released) {
            if(bootJump) bootJumping = true;
            bootJump = true;
            key_released = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            fast_falling = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!playGame) return;
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            key_released = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            fast_falling = false;
        }
    }

    private void updateScore(Tube bt1, Tube bt2, Boot boot) {
        if(bt1.getX() + TUBE_WIDTH == boot.getX()) {
            gs.incrementScore();
        } else if(bt2.getX() + TUBE_WIDTH == boot.getX()) {
            gs.incrementScore();
        }
    }

    private void detectCollisions(Tube bt1, Tube bt2, Tube tt1, Tube tt2, Boot boot) {
        checkCollision(boot.getRectangle(), bt1.getTube());
        checkCollision(boot.getRectangle(), bt2.getTube());
        checkCollision(boot.getRectangle(), tt1.getTube());
        checkCollision(boot.getRectangle(), tt2.getTube());
        if(boot.getY() + BOOT_HEIGHT > WINDOW_HEIGHT*7/8) {
            gameOver();
        }
    }

    private void checkCollision(Rectangle r1, Rectangle r2) {
        if(r1.intersects(r2)) {
            gameOver();
        }
    }

    private void gameOver() {
        gs.sendText("Game Over");
        doLoop = false;
        playGame = false;
    }
}