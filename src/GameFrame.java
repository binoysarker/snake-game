import javax.swing.*;

public class GameFrame extends JFrame {
    GamePanel gamePanel = new GamePanel();
    GameFrame(){
        this.add(gamePanel);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}
