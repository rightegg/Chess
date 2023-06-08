import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class CheckHandler {
    King BKing;
    King WKing;
    Board board;

    public CheckHandler(King BKing, King WKing, Board board) {
        this.BKing = BKing;
        this.WKing = WKing;
        this.board = board;
    }

    public void setKings(King BKing, King WKing) {
        this.BKing = BKing;
        this.WKing = WKing;
    }

    public Set<Spot> getLegalMoves(Piece p) {
        Set<Spot> returned;
        Set<Spot> possibleMoves;

        if (p instanceof Pawn) {
            possibleMoves = ((Pawn) p).getMoves();
        }
        else if (p instanceof King) {
            possibleMoves = ((King) p).getMoves();
        }
        else {
            possibleMoves = p.getCover();
        }

        returned = new HashSet<Spot>(possibleMoves);

        for (Spot s : possibleMoves) {

            if ((s.isOccupied() && s.getPiece().isWhite() == p.isWhite()) || !testMoveLegal(p, s)) {
                returned.remove(s);
            }
        }

        return returned;
    }

    private boolean testMoveLegal(Piece p, Spot dest) {
        Spot oldpos = p.getPos();

        boolean isWhite = p.isWhite();
        boolean legal = false;

        if (dest.isOccupied() && dest.getPiece().isWhite() == p.isWhite()) {
            return false;
        }

        if (dest.isOccupied()) {
            Piece oldp = dest.getPiece();
            if (!p.moveNoCheck(dest)) {
                throw new IllegalArgumentException();
            }

            System.out.println("oldpos occ: " + oldpos.isOccupied());
            System.out.println("oldpos pos: " + oldpos.getX() + ", " + oldpos.getY());
            board.updateCover();

            legal = isWhite ? !isWhiteChecked() : !isBlackChecked();

            oldpos.setPiece(p);
            dest.removePiece();
            dest.setPiece(oldp);
            if (oldp.isWhite()) {
                board.getWPieces().add(oldp);
            }
            else {
                board.getBPieces().add(oldp);
            }
        }
        else {
            if (p.moveNoCheck(dest)) {
                board.updateCover();

                legal = isWhite ? !isWhiteChecked() : !isBlackChecked();

                dest.removePiece();
                oldpos.setPiece(p);
                p.setPos(oldpos);
            }

            else {
                return false;
            }

        }

        return legal;
    }

    private boolean canEscape(King k, Map<Spot, List<Piece>> cover) {
        for (Spot s : k.getCover()) {
            if (cover.get(s).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private boolean canCapture(King k, Map<Spot, List<Piece>> OpCover, Map<Spot, List<Piece>> SameCover) {
        if (OpCover.get(k.getPos()).size() > 1) {
            return false;
        }

        Piece attackingPiece = OpCover.get(k.getPos()).get(0);
        List<Piece> possibleCapturingPieces = SameCover.get(attackingPiece.getPos());

        if (possibleCapturingPieces.isEmpty()) {
            return false;
        }

        for (Piece p : possibleCapturingPieces) {
            if (p.getLegalMoves().contains(attackingPiece.getPos())) {
                return true;
            }
        }

        return false;
    }

    private boolean canBlock(King k, Map<Spot, List<Piece>> OpCover, Map<Spot, List<Piece>> SameCover) {
        if (OpCover.get(k.getPos()).size() > 1) {
            return false;
        }

        if (OpCover.get(k.getPos()).get(0) instanceof Knight) {
            return false;
        }

        Spot attackingPos = OpCover.get(k.getPos()).get(0).getPos();
        Spot[][] spots = board.getBoard();
        int KX = k.getPos().getX();
        int KY = k.getPos().getY();

        if (attackingPos.getX() == KX) {
            for (int i = Math.min(attackingPos.getY(), KY) + 1; i < Math.max(attackingPos.getY(), KY); i++) {
                List<Piece> possibleBlockers = SameCover.get(spots[KX][i]);
                if (possibleBlockers.isEmpty()) {
                    continue;
                }

                for (Piece p : possibleBlockers) {
                    if (p.getLegalMoves().contains(spots[KX][i])) {
                        return true;
                    }
                }
            }
        }

        if (attackingPos.getY() == KY) {
            for (int i = Math.min(attackingPos.getX(), KX) + 1; i < Math.max(attackingPos.getX(), KX); i++) {
                List<Piece> possibleBlockers = SameCover.get(spots[i][KY]);
                if (possibleBlockers.isEmpty()) {
                    continue;
                }

                for (Piece p : possibleBlockers) {
                    if (p.getLegalMoves().contains(spots[i][KY])) {
                        return true;
                    }
                }
            }
        }

        if (Math.abs(attackingPos.getY() - KY / attackingPos.getX() - KX) == 1) {
            int diff = (attackingPos.getY() - KY) * (attackingPos.getX() - KX) > 0 ? 1 : -1;
            int checkedY = KY + diff;

            for (int i = Math.min(KX, attackingPos.getX())+1; i < Math.max(KX, attackingPos.getX()); i++, checkedY += diff) {
                List<Piece> possibleBlockers = SameCover.get(spots[i][checkedY]);
                if (possibleBlockers.isEmpty()) {
                    continue;
                }

                for (Piece p : possibleBlockers) {
                    if (p.getLegalMoves().contains(spots[i][checkedY])) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isWhiteCheckmated() {
        if (!isWhiteChecked()) {
            return false;
        }

        if (canEscape(WKing, board.getBCover())) {
            return false;
        }

        if (canBlock(WKing, board.getBCover(), board.getWCover())) {
            return false;
        }

        if (canCapture(WKing, board.getBCover(), board.getWCover())) {
            return false;
        }

        return true;
    }

    public boolean isBlackCheckmated() {
        if (!isBlackChecked()) {
            return false;
        }

        if (canEscape(WKing, board.getWCover())) {
            return false;
        }

        if (canBlock(WKing, board.getWCover(), board.getBCover())) {
            return false;
        }

        if (canCapture(WKing, board.getWCover(), board.getBCover())) {
            return false;
        }

        return true;
    }

    public boolean isWhiteChecked() {
        return board.getBCover().get(WKing.getPos()).size() > 0;
    }

    public boolean isBlackChecked() {
        return board.getWCover().get(BKing.getPos()).size() > 0;
    }
}
