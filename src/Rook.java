import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

public class Rook extends Piece {
	private boolean hasMoved;

	public Rook(boolean isWhite, Spot position, Board board) {
		super(isWhite, position, board);
		hasMoved = false;

		try {
			img = isWhite ? ImageIO.read(getClass().getResource("./images/wr.png")) : ImageIO.read(getClass().getResource("./images/br.png"));
			resize();
		}
		catch (Exception e) {

		}
	}
	
	@Override
	public Set<Spot> getCover() {
		return super.getLineCover();
	}

	public boolean move(Spot dest) {
		boolean success = super.move(dest);
		if (success) {
			hasMoved = true;
		}
		
		return success;
	}
	
	public boolean getHasMoved() {
		return hasMoved;
	}
	
	public void castle() {
		if (hasMoved) {
			throw new IllegalArgumentException("Rook has moved");
		}
		
		else {
			if (this.getPos().getX() == 7) {
				board.getBoard()[this.getPos().getX() -2][this.getPos().getY()].setPiece(this);
				this.getPos().removePiece();
				this.setPos(board.getBoard()[this.getPos().getX() -2][this.getPos().getY()]);
			}
			else if (this.getPos().getX() == 0) {
				board.getBoard()[this.getPos().getX() +3][this.getPos().getY()].setPiece(this);
				this.getPos().removePiece();
				this.setPos(board.getBoard()[this.getPos().getX() +3][this.getPos().getY()]);
			}
		}
	}
}
