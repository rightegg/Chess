import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {
	
	private boolean hasMoved;

	public Pawn(boolean isWhite, Spot position, Board board) {
		super(isWhite, position, board);
		hasMoved = false;

		try {
			img = isWhite ? ImageIO.read(getClass().getResource("./images/wp.png")) : ImageIO.read(getClass().getResource("./images/bp.png"));
			resize();
		}
		catch (Exception e) {

		}
	}

	public Set<Spot> getMoves() {
		Set<Spot> returned = new HashSet<Spot>();
		
		Spot myPos = this.getPos();
		
		if (!board.getBoard()[myPos.getX()][myPos.getY()+1].isOccupied()) {
			returned.add(board.getBoard()[myPos.getX()][myPos.getY()+1]);
			
			if (!hasMoved && !board.getBoard()[myPos.getX()][myPos.getY()+2].isOccupied()) {
				returned.add(board.getBoard()[myPos.getX()][myPos.getY()+2]);
			}
		}
			
		return returned;
	}
	
	public Set<Spot> getCover() {
		Set<Spot> returned = new HashSet<Spot>();
		if (this.isWhite()) {
			try {
				returned.add(board.getBoard()[this.getPos().getX() + 1][this.getPos().getY() - 1]);
			} catch (ArrayIndexOutOfBoundsException e) {

			}
			try {
				returned.add(board.getBoard()[this.getPos().getX() - 1][this.getPos().getY() - 1]);
			} catch (ArrayIndexOutOfBoundsException e) {

			}
		} else {
			try {
				returned.add(board.getBoard()[this.getPos().getX() + 1][this.getPos().getY() + 1]);
			} catch (ArrayIndexOutOfBoundsException e) {

			}
			try {
				returned.add(board.getBoard()[this.getPos().getX() - 1][this.getPos().getY() + 1]);
			} catch (ArrayIndexOutOfBoundsException e) {

			}
		}

		return returned;
	}
}
