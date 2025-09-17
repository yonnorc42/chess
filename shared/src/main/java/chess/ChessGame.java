package chess;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null || piece.getTeamColor() != teamTurn) {
            Collections.emptyList();
        }

        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new java.util.ArrayList<>();

        for (ChessMove move : potentialMoves) {
            // save current board state
            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(startPosition, null);

            // check if king is safe
            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }

            // undo the move
            board.addPiece(startPosition, piece);
            board.addPiece(move.getEndPosition(), capturedPiece);
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());
        if (movingPiece == null || movingPiece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("No piece of your team at start position");
        }

        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if (moves == null || !moves.contains(move)) {
            throw new InvalidMoveException("Move not valid");
        }

        // Save captured piece for undo
        ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
        ChessPiece.PieceType promotionType = move.getPromotionPiece();

        // Execute move (check if it's a promoting pawn)
        if (promotionType == null) {
            board.addPiece(move.getEndPosition(), movingPiece);

        }
        else {
            board.addPiece(move.getEndPosition(), new ChessPiece(teamTurn, promotionType));
        }
        board.addPiece(move.getStartPosition(), null);

        // Check if move leaves own king in check
        if (isInCheck(teamTurn)) {
            // Undo move
            board.addPiece(move.getStartPosition(), movingPiece);
            board.addPiece(move.getEndPosition(), capturedPiece);
            throw new InvalidMoveException("Move leaves king in check");
        }

        // Switch turn
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // get opposite of teamColor
        TeamColor enemyColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        // find teamColor king
        ChessPosition kingPos = findKingPosition(teamColor);

        // check if king is attacked
        return isPositionAttacked(enemyColor, kingPos);
    }

    private ChessPosition findKingPosition(TeamColor teamColor) {
        for (int row=1; row<=8; row++) {
            for (int col=1; col<=8; col++) {
                ChessPosition potentialKingPos = new ChessPosition(row, col);
                ChessPiece potentialKing = board.getPiece(potentialKingPos);
                // check if there's a piece in each spot. If there is, check if it's a king. If it is, check if it's teamColor. If all are true, that's our king
                if (potentialKing != null && potentialKing.getPieceType() == ChessPiece.PieceType.KING && potentialKing.getTeamColor() == teamColor) {
                    return potentialKingPos;
                }
            }
        }
        // THIS SHOULD NEVER HAPPEN
        return null;
    }

    private boolean isPositionAttacked(TeamColor attackingColor, ChessPosition attackedPosition) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                // check if there's an enemy piece in each position
                if (piece != null && piece.getTeamColor() == attackingColor) {
                    Collection<ChessMove> attackingMoves = piece.pieceMoves(board, pos);
                    for (ChessMove move : attackingMoves) {
                        if (move.getEndPosition().equals(attackedPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) return false;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(pos);
                    if (moves != null) {
                        for (ChessMove move : moves) {
                            // simulate move
                            ChessPiece captured = board.getPiece(move.getEndPosition());
                            board.addPiece(move.getEndPosition(), piece);
                            board.addPiece(pos, null);
                            boolean stillInCheck = isInCheck(teamColor);
                            // undo
                            board.addPiece(pos, piece);
                            board.addPiece(move.getEndPosition(), captured);
                            if (!stillInCheck) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(pos);
                    if (moves != null) {
                        for (ChessMove move : moves) {
                            ChessPiece captured = board.getPiece(move.getEndPosition());
                            board.addPiece(move.getEndPosition(), piece);
                            board.addPiece(pos, null);
                            boolean leavesKingSafe = !isInCheck(teamColor);
                            board.addPiece(pos, piece);
                            board.addPiece(move.getEndPosition(), captured);
                            if (leavesKingSafe) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}