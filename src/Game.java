import javax.swing.*;

public class Game implements Runnable {
    public void run() {
        SwingUtilities.invokeLater(new GameWindow());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
