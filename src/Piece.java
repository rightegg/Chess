import java.util.*;

public abstract class Piece {
	private final boolean isWhite;
	private Set<Spot> legalMoves;
	private Spot position;
	protected Board board;
	
	public Piece(boolean isWhite, Spot position, Board board) {
		this.isWhite = isWhite;
		this.position = position;
		this.board = board;
		legalMoves = new HashSet<Spot>();
	}
	
	public boolean move(Spot dest) {
		if (!legalMoves.contains(dest)) {
			return false;
		}
		
		if (dest.isOccupied()) {
			return dest.capture(this);
		}
		
		position = dest;
		dest.setPiece(this);
		updateLegalMoves();
		
		return true;		
	}
	
	public Spot getPos() {
		return position;
	}
	
	public boolean isWhite() {
		return isWhite;
	}
	
	public void updateLegalMoves() {
		legalMoves = getLegalMoves();
	}

	public Set<Spot> getLegalMoves() {

	}
	
	protected abstract Set<Spot> getPossibleMoves();
	
	public Set<Spot> getDiagonalCover() {
		Set<Spot> returned = new HashSet<Spot>();
		
		int x = position.getX();
		int y = position.getY();
		
		for (double i = Math.PI/4; i < 2*Math.PI; i+=Math.PI/2) {
			
			
			int checkedX = x + (int) (Math.cos(i)*2);
			int checkedY = y + (int) (Math.sin(i)*2);
			while (checkedX >= 0 && checkedX < 8 && checkedY >= 0 && checkedY < 8) {			
				
				returned.add(board.getBoard()[checkedX][checkedY]);
				
				if (board.getBoard()[checkedX][checkedY].isOccupied()) {
					break;
				}
				
				checkedX += (int) (Math.cos(i)*2);
				checkedY += (int) (Math.sin(i)*2);
			}
		}
		
		return returned;
	}
	
	protected void setPos(Spot s) {
		position = s;
	}
	
	public Set<Spot> getLineCover() {
		Set<Spot> returned = new HashSet<Spot>();
		
		int x = position.getX();
		int y = position.getY();
		
		for (double i = 0; i < 2*Math.PI; i+=Math.PI/2) {
			int checkedX = x + (int) Math.cos(i);
			int checkedY = y + (int) Math.sin(i);
			
			while (checkedX >= 0 && checkedX < 8 && checkedY >= 0 && checkedY < 8) {
				returned.add(board.getBoard()[checkedX][checkedY]);
				
				if (board.getBoard()[checkedX][checkedY].isOccupied()) {
					break;
				}
				
				checkedX += (int) Math.cos(i);
				checkedY += (int) Math.sin(i);
			}
		}
		
		return returned;
	}
}
