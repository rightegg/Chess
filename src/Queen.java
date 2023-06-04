import java.util.HashSet;
import java.util.Set;

public class Queen extends Piece {

	public Queen(boolean isWhite, Spot position, Board board) {
		super(isWhite, position, board);
	}

	@Override
	public Set<Spot> getLegalMoves() {
		Set<Spot> returned = new HashSet<Spot>();
		for (Spot s : super.getDiagonalCover()) {
			returned.add(s);
		}
		for (Spot s : super.getLineCover()) {
			returned.add(s);
		}
		
		return returned;
	}

}
