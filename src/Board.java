import java.util.*;

public class Board {
	private final Spot[][] board;
	private final Set<Piece> BPieces;
	private final Set<Piece> WPieces;
	private final Map<Spot, List<Piece>> BCover;
	private final Map<Spot, List<Piece>> WCover;
	
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
		board = new Spot[8][8];
		BPieces = new HashSet<Piece>();
		WPieces = new HashSet<Piece>();
		BCover = new HashMap<Spot, List<Piece>>();
		WCover = new HashMap<Spot, List<Piece>>();
		
		//black pieces
		board[0][0] = new Spot(this,0,0);
		board[0][0].setPiece(new Rook(false, board[0][0], this));
		board[7][0] = new Spot(this,7,0);
		board[7][0].setPiece(new Rook(false, board[7][0], this));

		board[1][0] = new Spot(this,1,0);
		board[1][0].setPiece(new Knight(false, board[1][0], this));
		board[6][0] = new Spot(this,6,0);
		board[6][0].setPiece(new Knight(false, board[6][0], this));

		board[2][0] = new Spot(this,2,0);
		board[2][0].setPiece(new Bishop(false, board[2][0], this));
		board[5][0] = new Spot(this,5,0);
		board[5][0].setPiece(new Bishop(false, board[5][0], this));

		board[3][0] = new Spot(this,3,0);
		board[3][0].setPiece(new Queen(false, board[3][0], this));

		board[4][0] = new Spot(this,4,0);
		board[4][0].setPiece(new King(false, board[4][0], this));

		//black pawns
		for (int i = 0; i < 8; i++) {
			board[i][1] = new Spot(this,i,1);
			board[i][1].setPiece(new Pawn(false, board[i][1], this));
		}

		//white pieces
		board[0][7] = new Spot(this, 0,7);
		board[0][7].setPiece(new Rook(true, board[0][7], this));
		board[7][7] = new Spot(this,7,7);
		board[7][7].setPiece(new Rook(true, board[7][7], this));

		board[1][7] = new Spot(this,1,7);
		board[1][7].setPiece(new Knight(true, board[1][7], this));
		board[6][7] = new Spot(this,6,7);
		board[6][7].setPiece(new Knight(true, board[6][7], this));

		board[2][7] = new Spot(this,2,7);
		board[2][7].setPiece(new Bishop(true, board[2][7], this));
		board[5][7] = new Spot(this,5,7);
		board[5][7].setPiece(new Bishop(true, board[5][7], this));

		board[3][7] = new Spot(this,3,7);
		board[3][7].setPiece(new Queen(true, board[3][7], this));

		board[4][7] = new Spot(this,4,7);
		board[4][7].setPiece(new King(true, board[4][7], this));

		//white pawns
		for (int i = 0; i < 8; i++) {
			board[i][6] = new Spot(this,i,6);
			board[i][6].setPiece(new Pawn(true, board[i][6], this));
		}

		//rest of empty squares
		for (int i = 0; i < 8; i++) {
			for (int j = 2; j <= 5; j++) {
				board[i][j] = new Spot(this,i,j);
			}
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
	}
	
	public boolean removePiece(Piece p, Spot s) {
		if (s.isOccupied() && p == s.getPiece()) {
			if (p.isWhite()) {
				WPieces.remove(p);
			}
			else {
				BPieces.remove(p);
			}
			
			return true;
		}
		
		return false;
	}
	
	public Spot[][] getBoard() {
		return board;
	}
	
	private void updateCover() {
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				BCover.get(board[i][j]).clear();
				WCover.get(board[i][j]).clear();
			}
		}
		
		for (Piece p : BPieces) {
			if (!(p instanceof Pawn)) {
				for (Spot s : p.getCover()) {
					BCover.get(s).add(p);
				}
			}
			else {
				for (Spot s : ((Pawn) p).getCover()) {
					BCover.get(s).add(p);
				}
			}
		}
		
		for (Piece p : WPieces) {
			if (!(p instanceof Pawn)) {
				for (Spot s : p.getLegalMoves()) {
					WCover.get(s).add(p);
				}
			}
			else {
				for (Spot s : ((Pawn) p).getCover()) {
					WCover.get(s).add(p);
				}
			}
		}
	}
	
	public Map<Spot, List<Piece>> getWCover() {
		return WCover;
	}
	
	public Map<Spot, List<Piece>> getBCover() {
		return BCover;
	}
}
