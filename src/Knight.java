import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;
import java.util.HashSet;

public class Knight extends Piece {

    public Knight(boolean isWhite, Spot position, Board board) {
        super(isWhite, position, board);

        try {
            img = isWhite ? ImageIO.read(getClass().getResource("./images/wn.png")) : ImageIO.read(getClass().getResource("./images/bn.png"));
            resize();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public Set<Spot> getCover() {
        Set<Spot> returned = new HashSet<Spot>();
        Spot[][] b = board.getBoard();
        int thisX = this.getPos().getX();
        int thisY = this.getPos().getY();

        for (double i = 0; i < 2 * Math.PI; i+= Math.PI / 2) {
            try {
                returned.add(b[(int) (thisX + 2* Math.cos(i) + Math.sin(i))][(int)(thisY + 2*Math.sin(i) + Math.cos(i))]);
            }
            catch (ArrayIndexOutOfBoundsException ignored) {

            }
            try {
                returned.add(b[(int) (thisX + 2* Math.cos(i) - Math.sin(i))][(int)(thisY + 2*Math.sin(i) - Math.cos(i))]);
            }
            catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }

        return returned;
    }
}
