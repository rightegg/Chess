
public class Spot {
	private Board board;
	private Piece piece;
	private boolean isOccupied;
	private int x;
	private int y;
	
	public Spot(Board b, int x, int y) {
		board = b;
		this.x = x;
		this.y = y;
		isOccupied = false;
	}
	
	public boolean capture(Piece p) {
		if (!isOccupied) {
			return false;
		}
		
		if (p.isWhite() != piece.isWhite()) {
			board.removePiece(piece, this);
			removePiece();
			setPiece(p);
			
			return true;
		}
		
		return false;
	}
	
	public boolean isOccupied() {
		return isOccupied;
	}
	
	public void setPiece(Piece p) {
		if (isOccupied) {
			throw new IllegalArgumentException("Tried setting occupied spot");
		}
		
		piece = p;
		isOccupied = true;
	}
	
	public void removePiece() {
		if (!isOccupied) {
			throw new IllegalArgumentException("Tried removing empty spot");
		}

		piece = null;
		isOccupied = false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Piece getPiece() {
		return piece;
	}

}
