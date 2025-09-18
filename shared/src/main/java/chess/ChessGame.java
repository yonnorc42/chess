package chess;

import java.util.ArrayList;
import java.util.Collection;
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
        // turn flag to true so we know piece has moved, and can't castle
        board.getPiece(move.getEndPosition()).hasMadeMoveFlagTrue();

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

    /**
     *
     * @param teamColor team of king we're looking for
     * @return position of king, null if we can't find him, that shouldn't happen
     */
    private ChessPosition findKingPosition(TeamColor teamColor) {
        Collection<ChessPosition> positions = getAllBoardPositions();
        for (ChessPosition pos : positions) {
            ChessPiece piece = board.getPiece(pos);
            // check if there's a piece in each spot. If there is, check if it's a king.
            // If it is, check if it's teamColor. If all are true, that's our king
            if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                return pos;
            }
        }
        // THIS SHOULD NEVER HAPPEN
        return null;
    }


    /**
     *
     * @param attackingColor the enemy team
     * @param attackedPosition the position of the friendly king
     * @return true if friendly king is under attack, otherwise false
     */
    private boolean isPositionAttacked(TeamColor attackingColor, ChessPosition attackedPosition) {
        Collection<ChessPosition> positions = getAllBoardPositions();
        for (ChessPosition pos : positions) {
            if (isEnemyAttacking(attackingColor, pos, attackedPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param attackingColor the enemy team
     * @param pos the pos of the piece potentially attacking king
     * @param target the position of the friendly king
     * @return true if friendly king is under attack by piece in pos, otherwise false
     */
    private boolean isEnemyAttacking(TeamColor attackingColor, ChessPosition pos, ChessPosition target) {
        ChessPiece piece = board.getPiece(pos);
        if (piece == null || piece.getTeamColor() != attackingColor) {
            return false;
        }
        for (ChessMove move : piece.pieceMoves(board, pos)) {
            if (move.getEndPosition().equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return a list of all the positions on the board
     */
    private Collection<ChessPosition> getAllBoardPositions() {
        Collection<ChessPosition> positions = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                positions.add(new ChessPosition(row, col));
            }
        }
        return positions;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (ChessPosition pos : getAllBoardPositions()) {
            ChessPiece piece = board.getPiece(pos);
            if (piece != null && piece.getTeamColor() == teamColor) {
                if (hasEscapingMove(pos, piece, teamColor)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param startPos the starting position of the piece attempting to move
     * @param piece the piece attempting to move
     * @param teamColor which team is under threat
     * @return true if the king can escape, false otherwise
     */
    private boolean hasEscapingMove(ChessPosition startPos, ChessPiece piece, TeamColor teamColor) {
        Collection<ChessMove> moves = validMoves(startPos);
        if (moves == null) {
            return false;
        }

        for (ChessMove move : moves) {
            if (simulateMoveDoesEscapeCheck(startPos, piece, move, teamColor)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param startPos the start position of piece attempting to move
     * @param piece the piece moving
     * @param move the move it makes
     * @param teamColor which team to check for check
     * @return True if the king escapes check with move, otherwise false
     */
    private boolean simulateMoveDoesEscapeCheck(ChessPosition startPos, ChessPiece piece, ChessMove move, TeamColor teamColor) {
        ChessPiece captured = board.getPiece(move.getEndPosition());

        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(startPos, null);

        boolean escapesCheck = !isInCheck(teamColor);

        board.addPiece(startPos, piece);
        board.addPiece(move.getEndPosition(), captured);

        return escapesCheck;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        for (ChessPosition pos : getAllBoardPositions()) {
            ChessPiece piece = board.getPiece(pos);
            if (piece != null && piece.getTeamColor() == teamColor) {
                if (hasEscapingMove(pos, piece, teamColor)) {
                    return false;
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