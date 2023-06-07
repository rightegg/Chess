import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class Board extends JPanel implements MouseListener {
	private final Spot[][] board;
	private final Set<Piece> BPieces;
	private final Set<Piece> WPieces;
	private final Map<Spot, List<Piece>> BCover;
	private final Map<Spot, List<Piece>> WCover;
	private final CheckHandler chd;
	private boolean whiteTurn;
	private Spot curSpot;
	private Spot lastMove1;
	private Spot lastMove2;
	private int curX;
	private int curY;
	public final int SPOT_SIZE = 50;
	
	/* R N B Q K B N R <-- black (7, 0)
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * R N B Q K B N R <-- white (7, 7)
	 */
	
	public Board() {
		curSpot = null;
		curX = 0;
		curY = 0;
		lastMove1 = null;
		lastMove2 = null;
		board = new Spot[8][8];
		BPieces = new HashSet<Piece>();
		WPieces = new HashSet<Piece>();
		BCover = new HashMap<Spot, List<Piece>>();
		WCover = new HashMap<Spot, List<Piece>>();
		whiteTurn = true;
		chd = new CheckHandler(null, null, this);

		setLayout(new GridLayout(8,8,0,0));


		//initialize pieces
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = (i + j) % 2 == 0 ? new Spot(this, i, j, true) : new Spot(this, i, j, false);

				this.add(board[i][j]);
			}
		}

		this.setPreferredSize(new Dimension(800, 800));
		this.setMinimumSize(this.getPreferredSize());

		this.addMouseListener(this);
		//black pieces
		board[0][0].setPiece(new Rook(false, board[0][0], this));
		board[7][0].setPiece(new Rook(false, board[7][0], this));

		board[1][0].setPiece(new Knight(false, board[1][0], this));
		board[6][0].setPiece(new Knight(false, board[6][0], this));

		board[2][0].setPiece(new Bishop(false, board[2][0], this));
		board[5][0].setPiece(new Bishop(false, board[5][0], this));

		board[3][0].setPiece(new Queen(false, board[3][0], this));

		King BKing = new King(false, board[4][0], this);
		board[4][0].setPiece(BKing);

		//black pawns
		for (int i = 0; i < 8; i++) {
			board[i][1].setPiece(new Pawn(false, board[i][1], this));
		}

		//white pieces
		board[0][7].setPiece(new Rook(true, board[0][7], this));
		board[7][7].setPiece(new Rook(true, board[7][7], this));

		board[1][7].setPiece(new Knight(true, board[1][7], this));
		board[6][7].setPiece(new Knight(true, board[6][7], this));

		board[2][7].setPiece(new Bishop(true, board[2][7], this));
		board[5][7].setPiece(new Bishop(true, board[5][7], this));

		board[3][7].setPiece(new Queen(true, board[3][7], this));

		King WKing = new King(true, board[4][7], this);
		board[4][7].setPiece(WKing);

		//white pawns
		for (int i = 0; i < 8; i++) {
			board[i][6].setPiece(new Pawn(true, board[i][6], this));
		}

		//add pieces to piece list
		for (int i = 0; i < 8; i++) {
			BPieces.add(board[i][0].getPiece());
			BPieces.add(board[i][1].getPiece());
			WPieces.add(board[i][6].getPiece());
			WPieces.add(board[i][7].getPiece());
		}

		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				BCover.put(board[i][j], new LinkedList<Piece>());
				WCover.put(board[i][j], new LinkedList<Piece>());
			}
		}

		chd.setKings(BKing, WKing);
		updateCover();
		initializePieces();
	}

	private void initializePieces() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 2; j++) {
				board[i][j].getPiece().initialize();
			}
		}
		for (int i = 0; i < 7; i++) {
			for (int j = 6; j < 8; j++) {
				board[i][j].getPiece().initialize();
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		// super.paintComponent(g);

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Spot sq = board[y][x];
				sq.paintComponent(g);
			}
		}
	}

	public CheckHandler getChd() {
		return this.chd;
	}
	
	public void removePiece(Piece p, Spot s) {
		if (s.isOccupied() && p == s.getPiece()) {
			if (p.isWhite()) {
				WPieces.remove(p);
			}
			else {
				BPieces.remove(p);
			}
		}
	}
	
	public Spot[][] getBoard() {
		return board;
	}
	
	public void updateCover() {
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				BCover.get(board[i][j]).clear();
				WCover.get(board[i][j]).clear();
			}
		}
		
		for (Piece p : BPieces) {
			for (Spot s : p.getCover()) {
				BCover.get(s).add(p);
			}
		}
		
		for (Piece p : WPieces) {
			for (Spot s : p.getCover()) {
				WCover.get(s).add(p);
			}
		}
	}
	
	public Map<Spot, List<Piece>> getWCover() {
		return WCover;
	}
	
	public Map<Spot, List<Piece>> getBCover() {
		return BCover;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		curX = e.getY();
		curY = e.getX();
		Spot clickedSpot = (Spot) this.getComponentAt(curX, curY);

		if (clickedSpot.isOccupied()) {
			System.out.println(clickedSpot.getPiece().getClass().getSimpleName());
			for (Spot s : clickedSpot.getPiece().getCover()) {
				System.out.println(" ahhh " + s.getX() + ", " + s.getY());
			}
		}

		if (clickedSpot == null) {
			curSpot.setCur(false);
		}
		else if (curSpot == null) {
			curSpot = clickedSpot;
			curSpot.setCur(true);
		}
		else if (clickedSpot.isOccupied() && clickedSpot.getPiece().isWhite() == curSpot.getPiece().isWhite()) {
			curSpot.setCur(false);
			curSpot = clickedSpot;
			curSpot.setCur(true);
			curSpot.getPiece().updateLegalMoves();
			for (Spot s : curSpot.getPiece().getLegalMoves()) {
				s.setPossibleMove(true);
			}
		}
		else if (curSpot != clickedSpot) {
			if (curSpot.getPiece().isWhite() && whiteTurn || !curSpot.getPiece().isWhite() && !whiteTurn) {
				if (curSpot.getPiece().getLegalMoves().contains(clickedSpot)) {
					curSpot.getPiece().move(clickedSpot);
					lastMove1 = curSpot;
					lastMove2 = clickedSpot;
					curSpot = null;
				}
			}
		}

		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		curX = e.getY();
		curY = e.getX();
		Spot releasedSpot = (Spot) this.getComponentAt(curX, curY);

		if (releasedSpot == curSpot) {
			return;
		}

		if (curSpot.isOccupied()) {
			if (curSpot.getPiece().isWhite() == whiteTurn) {
				if (curSpot.getPiece().getLegalMoves().contains(releasedSpot)); {
					curSpot.getPiece().move(releasedSpot);
				}
			}
		}

		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
