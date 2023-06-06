import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameWindow implements Runnable {
    private final JFrame gameWindow;
    private final Board board;

    public GameWindow(){

        gameWindow = new JFrame("Chess");
        board = new Board();


        try {
            gameWindow.setIconImage(ImageIO.read(getClass().getResource("./images/bk.png")));
        } catch (Exception e) {

        }

        gameWindow.add(board, BorderLayout.CENTER);

        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gameWindow.setResizable(false);
        gameWindow.setMinimumSize(gameWindow.getPreferredSize());
        gameWindow.setSize(gameWindow.getPreferredSize());

        gameWindow.pack();
        gameWindow.setVisible(true);

    }

    public void run() {

    }
}
