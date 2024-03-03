import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;

/**
* Class for playing the classic game snake. Creates a JPanel that is passed to the
* GameFrame class to be called in SnakeGame's main method to be displayed to the user.
*/
public class GamePanel extends JPanel implements ActionListener {

    //data fields
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 2;
    int applesEaten;
    int appleX, appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    /**
     * Constructor for creating the game panel within GameFrame
     */
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    /**
     * Starts the game by creating the first apple, setting running to true and
     * starting the timer.
     */
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    /**
     * Calls the draw method that draws the content of the screen.
     * @param Graphics object to draw.
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    /**
     * If the game is running will draw the game on screen. If game is not running it
     * will instead call the gameOver method.
     * @param g Graphics object to draw.
     */
    public void draw(Graphics g){

        if(running){
            for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, 0, i*UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }else{
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Comic Sans", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());    
        }else{
            gameOver(g);
        }
    }

    /**
     * Creates a new apple at a random x and y location within the screen size.
     */
    public void newApple(){
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
    }

    /**
     * Moves the mody of the snake depending on the input from the player. Uses
     * the direction char to determine the direction.
     */
    public void move() {
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    /**
     * Checks if the snake has eaten the apple and if so it increments the body size 
     * and applesEaten count as well as creates a new apple.
     */
    public void checkApple() {
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    /**
     * Checks repeatedly if head has touched the body or any of the borders of the screen.
     * Also stops the timer if the game stops running.
     */
    public void checkCollisions() {
        //checks if head collides with body
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i] && y[0] == y[i])){
                running = false;
            }
        }
        //checks if head tocuhes left border
        if(x[0] < 0){
            running = false;
        }
        //checks if head tocuhes right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0){
            running = false;
        }
        //check if head touches top border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }

    /**
     * Draws a game over screen displaying score and game over text. Font is comic sans.
     * Only called if game stops running.
     * @param g Graphics object to draw.
     */
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Comic Sans", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());  
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Comic Sans", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    /**
     * Runs methods needed to make game function at all times, repaints the screen
     * to reflect what is happening.
     * @param e An ActionEvent to indicate something is happening.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    /**
     * Inner class that extends KeyAdapter that is used to check inputs from user.
     */
    public class MyKeyAdapter extends KeyAdapter {

        /**
        * Method for getting the key pressed by the user. Changes direction char and
        * as such direction of snake.
        * @param event An event triggered by user input.
        */
        @Override
        public void keyPressed(KeyEvent event) {
            switch(event.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}

