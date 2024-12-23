package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor color;

    public ChessGame() {

        this.color = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.color;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.color = team;
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
//     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    private ChessBoard cloneBoard(ChessBoard original) {
        ChessBoard clone = new ChessBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = original.getPiece(position);
                if (piece != null) {
                    clone.addPiece(position, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                }
            }
        }
        return clone;
    }


    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        setTeamTurn(board.getPiece(startPosition).getTeamColor());
        ChessPiece piece = getBoard().getPiece(startPosition);
        if (piece == null) {
            return null;
        }

        Collection<ChessMove> moves = piece.pieceMoves(getBoard(), startPosition);
        Iterator<ChessMove> iterator = moves.iterator();
        Collection<ChessMove> validMoves = new ArrayList<>();
        TeamColor myColor = piece.getTeamColor();

        while (iterator.hasNext()) {
            ChessMove move = iterator.next();
            // Save the current state of the board
            ChessBoard originalBoard = cloneBoard(getBoard());


            try {
                makeMove(move);
                if (!isInCheck(myColor)) {
                    validMoves.add(move);
                }
            } catch (InvalidMoveException e) {
                continue;
            } finally {
                // Restore the original board state
                setBoard(originalBoard);



            }

            if (getTeamTurn() == TeamColor.BLACK) {
                setTeamTurn(TeamColor.WHITE);
            } else {
                setTeamTurn(TeamColor.BLACK);
            }
        }
        return validMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece at start position");
        }

        if (!board.getPiece(move.getStartPosition()).pieceMoves(board, move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Not contained in valid moves");
        }

        if (board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn())
            {throw new InvalidMoveException("Out of Turn");}

        ChessPiece promo = board.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() != null) {
            ChessPiece.PieceType thing = move.getPromotionPiece();
            promo = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), thing);
        }

        board.addPiece(move.getEndPosition(), promo);
        board.addPiece(move.getStartPosition(), null);

        if (isInCheck(getTeamTurn())) {
            throw new InvalidMoveException("Can't move King into check");
        }

        if (getTeamTurn() == TeamColor.BLACK) {
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }
    }

    private ChessPosition findKing(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = getBoard().getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(i, j);
                }
            }
        }
        return null;
    }

    private Set<ChessMove> enemyMoves(TeamColor teamColor) {
        Set<ChessMove> allMoves = new HashSet<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (getBoard().getPiece(position) != null) {
                    if (getBoard().getPiece(position).getTeamColor() == teamColor) {
                        Collection<ChessMove> moves = getBoard().getPiece(position).pieceMoves(getBoard(), position);
                        allMoves.addAll(moves);
                    }
                }
            }
        }
        return allMoves;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = findKing(teamColor);
        TeamColor opponentColor = TeamColor.WHITE;
        if (teamColor == TeamColor.WHITE) {
            opponentColor = TeamColor.BLACK;
        }
        Set<ChessMove> enemyMoves = enemyMoves(opponentColor);
        for (ChessMove move : enemyMoves) {

            if (move.getEndPosition().equals(kingPos)) {
                return true;
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
        if (!isInCheck(teamColor)) {
            return false;
        }

        ChessPosition kingPos = findKing(teamColor);
        if (hasValidKingMoves(kingPos)) {
            return false;
        }

        return !hasValidMovesForAnyPiece(teamColor);
    }

    private boolean hasValidKingMoves(ChessPosition kingPos) {
        Collection<ChessMove> kingMoves = validMoves(kingPos);
        return !kingMoves.isEmpty();
    }

    private boolean hasValidMovesForAnyPiece(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = getBoard().getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (hasValidMoveForPiece(position, teamColor)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasValidMoveForPiece(ChessPosition position, TeamColor teamColor) {
        Collection<ChessMove> moves = validMoves(position);
        ChessBoard originalBoard = cloneBoard(getBoard());

        for (ChessMove move : moves) {
            try {
                makeMove(move);
                if (!isInCheck(teamColor)) {
                    setBoard(originalBoard);
                    return true;
                }
            } catch (InvalidMoveException ignored) {
            } finally {
                setBoard(originalBoard);
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */

    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = getBoard().getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(position);
                    if (!moves.isEmpty()) {
                        return false;
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
        public ChessBoard getBoard () {
            return this.board;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(board);
    }
}
