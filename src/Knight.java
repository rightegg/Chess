import java.util.Set;
import java.util.HashSet;

public class Knight extends Piece {

    public Knight(boolean isWhite, Spot position, Board b) {
        super(isWhite, position, b);
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
