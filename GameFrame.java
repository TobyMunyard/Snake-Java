import javax.swing.JFrame;

/**
* Class for displaying the GamePanel wihtin a JFrame. Is to be called by the SnakeGame 
* class's main method.
*/
public class GameFrame extends JFrame {

    /**
     * Constructor for creating the game frame which will contain the GamePanel which
     * contains the snake game content.
     */
    GameFrame(){
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
}