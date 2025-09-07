package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        // Moves above rook
        for (int col= myPosition.getColumn() + 1; col<=8; col++) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow(), col);
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

        // Moves below rook
        for (int col= myPosition.getColumn() - 1; col>=1; col--) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow(), col);
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

        // Moves to right of rook
        for (int row= myPosition.getRow() + 1; row<=8; row++) {
            ChessPosition possiblePosition = new ChessPosition(row, myPosition.getColumn());
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
        // Moves to left of rook
        for (int row= myPosition.getRow() - 1; row>=1; row--) {
            ChessPosition possiblePosition = new ChessPosition(row, myPosition.getColumn());
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
        return validMoves;
    }
}
