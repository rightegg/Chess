import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Spot extends JComponent {
	private final Board board;
	private Piece piece;
	private boolean isOccupied;
	private final int x;
	private final int y;
	private final Color color;
	private boolean possibleMove;
	private boolean isCur;
	private boolean passantSpot;

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

	public void setCur(boolean isCur) {
		this.isCur = isCur;
	}

	public void paintComponent(Graphics g) {
		if (isCur) {
			g.setColor(new Color(152,180,172));
		}
		else {
			g.setColor(color);
		}

		g.fillRect(this.getX()*this.getWidth(), this.getY()*this.getHeight(), this.getWidth(), this.getHeight());

		if (possibleMove) {
			if (isOccupied() || passantSpot) {
				g.setColor(new Color(169, 169, 169, 175));
				Graphics2D g2d = (Graphics2D)g;
				Shape ring = createRingShape(this.getX()*this.getWidth()+50, this.getY()*this.getHeight()+50, 50, 10);
				g2d.fill(ring);
				g2d.draw(ring);
			}
			else {
				g.setColor(new Color(169, 169, 169, 255));
				g.fillOval(this.getX()*this.getWidth()+this.getWidth()/2-12, this.getY()*this.getHeight()+this.getHeight()/2- 12, 25,25);
			}
		}


		if (isOccupied) {
			//piece.img = piece.img.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
			g.drawImage(piece.img, this.getX()*this.getWidth(), this.getY()*this.getHeight(), null);
		}
	}

	private static Shape createRingShape(
			double x, double y, double r, double thickness)
	{
		Ellipse2D outer = new Ellipse2D.Double(x - r,y - r,2*r,2*r);
		Ellipse2D inner = new Ellipse2D.Double(x - r + thickness,y - r + thickness,2*r - 2*thickness,2*r - 2*thickness);
		Area area = new Area(outer);
		area.subtract(new Area(inner));
		return area;
	}
	
	public boolean capture(Piece p) {
		if (!isOccupied) {
			return false;
		}
		
		if (p.isWhite() != piece.isWhite()) {
			board.removePiece(piece, this);
			removePiece();
			p.getPos().removePiece();
			setPiece(p);

			if (piece.isWhite()) {
				board.getWPieces().remove(piece);
			}
			else {
				board.getBPieces().remove(piece);
			}
			
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
		p.setPos(this);
		isOccupied = true;
	}

	public void setPassantSpot(boolean v) {
		passantSpot = v;
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
