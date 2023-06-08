import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {
	
	private boolean hasMoved;
	private boolean canPassantLeft;
	private boolean canPassantRight;

	public Pawn(boolean isWhite, Spot position, Board board) {
		super(isWhite, position, board);
		hasMoved = false;
		canPassantLeft = false;
		canPassantRight = false;

		try {
			img = isWhite ? ImageIO.read(getClass().getResource("./images/wp.png")) : ImageIO.read(getClass().getResource("./images/bp.png"));
			resize();
		}
		catch (Exception e) {

		}
	}

	@Override
	public boolean move(Spot dest) {
		boolean returned = true;

		if (dest.isOccupied()) {
			returned = dest.capture(this);
			if (returned) {
				hasMoved = true;
			}
		}

		else {
			if (Math.abs(dest.getX() - this.getPos().getX()) == 1 && Math.abs(dest.getY() - this.getPos().getY()) == 1) {
				if (this.isWhite()) {
					Spot killedSpot = board.getBoard()[dest.getX()][dest.getY()+1];
					killedSpot.removePiece();
					board.removePiece(killedSpot.getPiece(), killedSpot);
				}
				else {
					Spot killedSpot = board.getBoard()[dest.getX()][dest.getY() - 1];
					killedSpot.removePiece();
					board.removePiece(killedSpot.getPiece(), killedSpot);
				}
			}

			this.getPos().removePiece();
			dest.setPiece(this);
			hasMoved = true;
		}

		if ((this.isWhite() && dest.getY() == 0) || (!this.isWhite() && dest.getY() == 7)) {
			this.promote();
		}

		return returned;
	}

	private void promote() {
		this.getPos().removePiece();
		Queen newp;

		if (this.isWhite()) {
			board.getWPieces().remove(this);
			newp = new Queen(true, this.getPos(), board);
			board.getWPieces().add(newp);
		}
		else {
			board.getBPieces().remove(this);
			newp = new Queen(false, this.getPos(), board);
			board.getBPieces().add(newp);
		}

		this.getPos().setPiece(newp);
	}

	public void setCanPassantLeft(boolean can) {
		canPassantLeft = can;
	}

	public void setCanPassantRight(boolean can) {
		canPassantRight = can;
	}

	public Set<Spot> getMoves() {
		Set<Spot> returned = new HashSet<Spot>();
		
		Spot myPos = this.getPos();

		if (!isWhite()) {
			if (!board.getBoard()[myPos.getX()][myPos.getY()+1].isOccupied()) {
				returned.add(board.getBoard()[myPos.getX()][myPos.getY()+1]);

				if (!hasMoved && !board.getBoard()[myPos.getX()][myPos.getY()+2].isOccupied()) {
					returned.add(board.getBoard()[myPos.getX()][myPos.getY()+2]);
				}
			}
		}
		else {
			if (!board.getBoard()[myPos.getX()][myPos.getY()-1].isOccupied()) {
				returned.add(board.getBoard()[myPos.getX()][myPos.getY()-1]);

				if (!hasMoved && !board.getBoard()[myPos.getX()][myPos.getY()-2].isOccupied()) {
					returned.add(board.getBoard()[myPos.getX()][myPos.getY()-2]);
				}
			}
		}

		for (Spot s : getCover()) {
			if (s.isOccupied() && s.getPiece().isWhite() != isWhite()) {
				returned.add(s);
			}
		}

		if (canPassantLeft) {
			if (isWhite()) {
				returned.add(board.getBoard()[myPos.getX()-1][myPos.getY()-1]);
			}
			else {
				returned.add(board.getBoard()[myPos.getX()-1][myPos.getY()+1]);
			}
		}

		if (canPassantRight) {
			if (isWhite()) {
				returned.add(board.getBoard()[myPos.getX()+1][myPos.getY()-1]);
			}
			else {
				returned.add(board.getBoard()[myPos.getX()+1][myPos.getY()+1]);
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
