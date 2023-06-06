import javax.swing.*;
import java.awt.*;

public class Spot extends JComponent {
	private final Board board;
	private Piece piece;
	private boolean isOccupied;
	private final int x;
	private final int y;
	private final Color color;
	private boolean possibleMove;

	public Spot(Board b, int x, int y, boolean isWhite) {
		possibleMove = false;
		board = b;
		this.x = x;
		this.y = y;
		isOccupied = false;
		if (isWhite) {
			color = new Color(255,255,255);
		}
		else {
			color = new Color(118,150,86);
		}
	}

	public void setPossibleMove(boolean canMove) {
		possibleMove = canMove;
	}

	public void paintComponent(Graphics g) {
		g.setColor(color);

		g.fillRect(this.getX()*this.getWidth(), this.getY()*this.getHeight(), this.getWidth(), this.getHeight());

		if (isOccupied) {
			//piece.img = piece.img.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
			g.drawImage(piece.img, this.getX()*this.getWidth(), this.getY()*this.getHeight(), null);
		}
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
