import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;
import java.util.HashSet;

public class King extends Piece {
	
	private boolean hasMoved;

	public King(boolean isWhite, Spot position, Board board) {
		super(isWhite, position, board);
		hasMoved = false;

		try {
			img = isWhite ? ImageIO.read(getClass().getResource("./images/wk.png")) : ImageIO.read(getClass().getResource("./images/bk.png"));
			resize();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public boolean move(Spot dest) {
		//normal
		if (Math.abs(dest.getX() - this.getPos().getX()) <= 1 && Math.abs(dest.getY() - this.getPos().getY()) <= 1) {
			if (super.move(dest)) {
				hasMoved = true;
				return true;
			}
			else {
				return false;
			}
		}
		//castling
		else if (!hasMoved && this.getCover().contains(dest) && Math.abs(dest.getX() - this.getPos().getX()) == 2) {
			if (dest.getX() > this.getPos().getX()) {
				dest.setPiece(this);
				this.getPos().removePiece();
				hasMoved = true;
				((Rook) board.getBoard()[7][this.getPos().getY()].getPiece()).castle();
			}
			else {
				dest.setPiece(this);
				this.getPos().removePiece();
				hasMoved = true;
				((Rook) board.getBoard()[0][this.getPos().getY()].getPiece()).castle();
			}
			
			this.getPos().removePiece();
			this.setPos(dest);
		}
		
		
		return false;
	}

	public Set<Spot> getMoves() {
		Set<Spot> returned = new HashSet<Spot>();
		
		Board b = board;
		
		//castling
		if (!hasMoved) {
			if (this.isWhite()) {
				if (b.getBoard()[7][7].getPiece() instanceof Rook && !((Rook) b.getBoard()[7][7].getPiece()).getHasMoved()) {
					if (!b.getBoard()[6][7].isOccupied() && 
						b.getBCover().get(b.getBoard()[6][7]).isEmpty() &&
						!b.getBoard()[5][7].isOccupied() &&
						b.getBCover().get(b.getBoard()[5][7]).isEmpty()
							) {
						returned.add(b.getBoard()[6][7]);
					}
				}
				if (b.getBoard()[0][7].getPiece() instanceof Rook && !((Rook) b.getBoard()[0][7].getPiece()).getHasMoved()) {
					if (!b.getBoard()[1][7].isOccupied() && 
						b.getBCover().get(b.getBoard()[1][7]).isEmpty() &&
						!b.getBoard()[2][7].isOccupied() &&
						b.getBCover().get(b.getBoard()[2][7]).isEmpty() &&
						!b.getBoard()[3][7].isOccupied() &&
						b.getBCover().get(b.getBoard()[3][7]).isEmpty()
							) {
						returned.add(b.getBoard()[2][7]);
					}
				}
			}
			
			else {
				if (b.getBoard()[7][0].getPiece() instanceof Rook && !((Rook) b.getBoard()[7][0].getPiece()).getHasMoved()) {
					if (!b.getBoard()[5][0].isOccupied() && 
						b.getWCover().get(b.getBoard()[5][0]).isEmpty() &&
						!b.getBoard()[6][0].isOccupied() &&
						b.getWCover().get(b.getBoard()[6][0]).isEmpty()
							) {
						returned.add(b.getBoard()[6][0]);
					}
				}
				if (b.getBoard()[0][0].getPiece() instanceof Rook && !((Rook) b.getBoard()[0][0].getPiece()).getHasMoved()) {
					if (!b.getBoard()[1][0].isOccupied() && 
						b.getWCover().get(b.getBoard()[1][0]).isEmpty() &&
						!b.getBoard()[2][0].isOccupied() &&
						b.getWCover().get(b.getBoard()[2][0]).isEmpty() &&
						!b.getBoard()[3][0].isOccupied() &&
						b.getWCover().get(b.getBoard()[3][0]).isEmpty()
							) {
						returned.add(b.getBoard()[2][0]);
					}
				}
			}
			
		}
		
		
		//normal
		Spot s = this.getPos();
		int x = s.getX();
		int y = s.getY();
		
		for (double i = 0; i < 2 * Math.PI; i += Math.PI/4) {
			int checkedX = (int) (x + Math.cos(i) * 1.75);
			int checkedY = (int) (y + Math.sin(i) * 1.75);
			try {
				if (this.isWhite()) {
					if (b.getBCover().get(b.getBoard()[checkedX][checkedY]).isEmpty()) {
						returned.add(b.getBoard()[checkedX][checkedY]);
					}
				}
				else {
					if (b.getWCover().get(b.getBoard()[checkedX][checkedY]).isEmpty()) {
						returned.add(b.getBoard()[checkedX][checkedY]);
					}
				}
			}
			catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
		
		return returned;
	}

	@Override
	public Set<Spot> getCover() {
		Set<Spot> returned = new HashSet<Spot>();

		Spot s = this.getPos();
		int x = s.getX();
		int y = s.getY();

		for (double i = 0; i < 2 * Math.PI; i += Math.PI/4) {
			int checkedX = (int) (x + Math.cos(i) * 1.75);
			int checkedY = (int) (y + Math.sin(i) * 1.75);
			try {
				returned.add(board.getBoard()[checkedX][checkedY]);
			}
			catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}

		return returned;
	}

}
