package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.MoveCalculatorUtils.iterateDirections;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

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

        iterateDirections(board, myPosition, directions, validMoves);
        return validMoves;
    }
}
