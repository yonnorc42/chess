package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        int[][] directions = {
                {-1,  1}, // up-right
                {-1, -1}, // up-left
                { 1,  1}, // down-right
                { 1, -1}  // down-left
        };

        for (int[] dir : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            while (true) {
                row += dir[0];
                col += dir[1];

                // Stop if off board
                if (row < 1 || row > 8 || col < 1 || col > 8) break;
                ChessPosition possiblePosition = new ChessPosition(row, col);
                ChessMove possibleMove = new ChessMove(myPosition, possiblePosition, null);
                // Spot is empty
                if (board.getPiece(possiblePosition) == null) {
                    validMoves.add(possibleMove);
                }
                // Spot has teams color, can't move there or past, break loop
                else if (board.getPiece(possiblePosition).getTeamColor() == myColor) {
                    break;
                }
                // Spot has other teams color, can move there but not past, add move and break loop
                else {
                    validMoves.add(possibleMove);
                    break;
                }
            }

        }
        return validMoves;
    }
}
