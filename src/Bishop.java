import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

public class Bishop extends Piece {

	public Bishop(boolean isWhite, Spot position, Board board) {
		super(isWhite, position, board);

		try {
			img = isWhite ? ImageIO.read(getClass().getResource("./images/wb.png")) : ImageIO.read(getClass().getResource("./images/bb.png"));
			resize();
		}
		catch (Exception e) {

		}
	}
	
	@Override
	public Set<Spot> getCover() {
		return super.getDiagonalCover();
	}

}
