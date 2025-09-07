package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        // I DECIDED TO USE LEFT AND RIGHT FOR BLACK FROM BLACKS PERSPECTIVE, NOT WHITES

        //-------------------------------WHITE MOVES-------------------------------
        if (myColor == ChessGame.TeamColor.WHITE) {
            ChessPosition forwardPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            ChessPosition doubleForwardPosition = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
            ChessPosition leftPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
            ChessPosition rightPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);

            ChessMove forwardMove = new ChessMove(myPosition, forwardPosition, null);
            ChessMove doubleForwardMove = new ChessMove(myPosition, doubleForwardPosition, null);
            ChessMove leftMove = new ChessMove(myPosition, leftPosition, null);
            ChessMove rightMove = new ChessMove(myPosition, rightPosition, null);


            // spot in front is empty
            if (board.getPiece(forwardPosition) == null) {
                // check if in starting position
                if (myPosition.getRow() == 2) {
                    validMoves.add(forwardMove);
                    if (board.getPiece(doubleForwardPosition) == null) {
                        validMoves.add(doubleForwardMove);
                    }
                }
                // check if moving to promotion
                else if (myPosition.getRow() == 7) {
                    validMoves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.BISHOP));
                }
                // if neither, just add forward move
                else {
                    validMoves.add(forwardMove);
                }
            }

            // check if spot in front and to left is an enemy piece
            if (myPosition.getColumn() != 1 && board.getPiece(leftPosition) != null && board.getPiece(leftPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (myPosition.getRow() == 7) {
                    validMoves.add(new ChessMove(myPosition, leftPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, leftPosition, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, leftPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, leftPosition, ChessPiece.PieceType.BISHOP));
                }
                else {
                    validMoves.add(leftMove);
                }
            }
            // check if spot in front and to right is an enemy piece
            if (myPosition.getColumn() != 8 && board.getPiece(rightPosition) != null && board.getPiece(rightPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (myPosition.getRow() == 7) {
                    validMoves.add(new ChessMove(myPosition, rightPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, rightPosition, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, rightPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, rightPosition, ChessPiece.PieceType.BISHOP));
                }
                else {
                    validMoves.add(rightMove);
                }
            }
        }

        //-------------------------------BLACK MOVES-------------------------------
        if (myColor == ChessGame.TeamColor.BLACK) {
            ChessPosition forwardPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            ChessPosition doubleForwardPosition = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
            ChessPosition leftPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            ChessPosition rightPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);

            ChessMove forwardMove = new ChessMove(myPosition, forwardPosition, null);
            ChessMove doubleForwardMove = new ChessMove(myPosition, doubleForwardPosition, null);
            ChessMove leftMove = new ChessMove(myPosition, leftPosition, null);
            ChessMove rightMove = new ChessMove(myPosition, rightPosition, null);


            // spot in front is empty
            if (board.getPiece(forwardPosition) == null) {
                // check if in starting position
                if (myPosition.getRow() == 7) {
                    validMoves.add(forwardMove);
                    if (board.getPiece(doubleForwardPosition) == null) {
                        validMoves.add(doubleForwardMove);
                    }
                }
                // check if moving to promotion
                else if (myPosition.getRow() == 2) {
                    validMoves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.BISHOP));
                }
                // if neither, just add forward move
                else {
                    validMoves.add(forwardMove);
                }
            }

            // check if spot in front and to left is an enemy piece
            if (myPosition.getColumn() != 8 && board.getPiece(leftPosition) != null && board.getPiece(leftPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (myPosition.getRow() == 2) {
                    validMoves.add(new ChessMove(myPosition, leftPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, leftPosition, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, leftPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, leftPosition, ChessPiece.PieceType.BISHOP));
                }
                else {
                    validMoves.add(leftMove);
                }
            }
            // check if spot in front and to right is an enemy piece
            if (myPosition.getColumn() != 1 && board.getPiece(rightPosition) != null && board.getPiece(rightPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (myPosition.getRow() == 2) {
                    validMoves.add(new ChessMove(myPosition, rightPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, rightPosition, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, rightPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, rightPosition, ChessPiece.PieceType.BISHOP));
                }
                else {
                    validMoves.add(rightMove);
                }
            }
        }
        return validMoves;
    }
}
