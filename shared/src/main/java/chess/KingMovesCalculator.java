package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        int[][] directions = {
                {-1,  1},
                {-1, -1},
                { 1,  1},
                { 1, -1},
                { 1, 0 },
                {-1, 0 },
                { 0, 1 },
                { 0,-1 }
        };

        for (int[] dir : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            row += dir[0];
            col += dir[1];

            // Stop if off board
            if (row < 1 || row > 8 || col < 1 || col > 8) continue;
            ChessPosition possiblePosition = new ChessPosition(row, col);
            ChessMove possibleMove = new ChessMove(myPosition, possiblePosition, null);
            // Spot is empty or the other teams color
            if (board.getPiece(possiblePosition) == null || board.getPiece(possiblePosition).getTeamColor() != myColor) {
                validMoves.add(possibleMove);
            }
        }
        return validMoves;
    }
}
