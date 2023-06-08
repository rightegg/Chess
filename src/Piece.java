import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public abstract class Piece {
	private final boolean isWhite;
	private Set<Spot> legalMoves;
	private Spot position;
	protected Board board;
	private final CheckHandler chd;
	protected Image img;

	public Piece(boolean isWhite, Spot position, Board board) {
		img = null;
		this.isWhite = isWhite;
		this.position = position;
		this.board = board;
		legalMoves = new HashSet<Spot>();
		this.chd = board.getChd();
	}

	public void resize() {
		img = img.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
	}

	public void draw(Graphics g) {
		int x = this.getPos().getX();
		int y = this.getPos().getY();

		g.drawImage(img, x, y, null);
	}

	public void initialize() {
		updateLegalMoves();
	}
	
	public boolean move(Spot dest) {
		if (!legalMoves.contains(dest)) {
			System.out.println("awman");
			return false;
		}
		
		if (dest.isOccupied()) {
			return dest.capture(this);
		}

		position.removePiece();
		position = dest;
		dest.setPiece(this);
		
		return true;		
	}

	public boolean moveNoCheck(Spot dest) {
		if (this instanceof Pawn) {
			if (!((Pawn)this).getMoves().contains(dest)) {
				return false;
			}
		}
		else if (this instanceof King) {
			if (!((King)this).getMoves().contains(dest)) {
				return false;
			}
		}
		else if (!this.getCover().contains(dest)) {
			return false;
		}

		if (dest.isOccupied()) {
			return dest.capture(this);
		}

		position.removePiece();
		position = dest;
		dest.setPiece(this);

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
		return chd.getLegalMoves(this);
	}
	
	public abstract Set<Spot> getCover();
	
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
