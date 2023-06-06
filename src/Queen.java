import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Queen extends Piece {

	public Queen(boolean isWhite, Spot position, Board board) {
		super(isWhite, position, board);

		try {
			img = isWhite ? ImageIO.read(getClass().getResource("./images/wq.png")) : ImageIO.read(getClass().getResource("./images/bq.png"));
			resize();
		}
		catch (Exception e) {

		}
	}

	@Override
	public Set<Spot> getCover() {
		Set<Spot> returned = new HashSet<Spot>();
		for (Spot s : super.getDiagonalCover()) {
			returned.add(s);
		}
		for (Spot s : super.getLineCover()) {
			returned.add(s);
		}
		
		return returned;
	}

}
