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

        try {
            returned.add(b[thisX+2][thisY+1]);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {

        }
        try {
            returned.add(b[thisX+2][thisY-1]);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {

        }
        try {
            returned.add(b[thisX-2][thisY+1]);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {

        }
        try {
            returned.add(b[thisX-2][thisY-1]);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {

        }
        try {
            returned.add(b[thisX+1][thisY+2]);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {

        }
        try {
            returned.add(b[thisX-1][thisY+2]);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {

        }
        try {
            returned.add(b[thisX+1][thisY-2]);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {

        }
        try {
            returned.add(b[thisX-1][thisY-2]);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {

        }

        return returned;
    }
}
