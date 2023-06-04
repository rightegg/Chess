import java.util.Set;

public class Bishop extends Piece {

	public Bishop(boolean isWhite, Spot position, Board board) {
		super(isWhite, position, board);
	}
	
	@Override
	public Set<Spot> getPossibleMoves() {
		return super.getDiagonalCover();
	}

}
