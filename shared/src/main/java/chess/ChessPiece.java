package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP){
            return BishopMoves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            return RookMoves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            return queenMoves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            return kingMoves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        }
        return new ArrayList<>();
    }

    private boolean withinBoard(int x, int y){
        return x > 0 && x <= 8 && y > 0 && y <= 8;
    }

    private Collection<ChessMove> infMoves( int[][] directions, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possMoves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        for (int[] direction: directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

            while (withinBoard(newX,newY)) {
                ChessPosition newPosition = new ChessPosition(newX,newY);
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);

                if (board.getPiece(newPosition) == null) {
                    possMoves.add(newMove);
                } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    possMoves.add(newMove);
                    break;
                } else {
                    break;
                }

                newX = newX + dx;
                newY = newY + dy;
            }

        }


        return possMoves;
    }

    private Collection<ChessMove> nonInfMoves( int[][] directions, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possMoves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        for (int[] direction: directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

            if (withinBoard(newX,newY)) {
                ChessPosition newPosition = new ChessPosition(newX,newY);
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);

                if (board.getPiece(newPosition) == null) {
                    possMoves.add(newMove);
                } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    possMoves.add(newMove);
                }
            }
        }

        return possMoves;
    }

    private Collection<ChessMove> BishopMoves(ChessBoard board, ChessPosition myPosition) {

        int[][] directions = {
                {1, 1},
                {1, -1},
                {-1, 1},
                {-1, -1}
        };

        return infMoves(directions, board, myPosition);
    }


    private Collection<ChessMove> RookMoves(ChessBoard board, ChessPosition myPosition) {

        int[][] directions = {
                {1, 0},
                {0, -1},
                {-1, 0},
                {0, 1}
        };

        return infMoves(directions,board, myPosition);
    }


    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {

        int[][] directions = {
                {1, 0},
                {0, -1},
                {-1, 0},
                {0, 1},
                {1, 1},
                {1, -1},
                {-1, 1},
                {-1, -1}
        };

        return infMoves(directions, board, myPosition);
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {

        int[][] directions = {
                {1, 0},
                {0, -1},
                {-1, 0},
                {0, 1},
                {1, 1},
                {1, -1},
                {-1, 1},
                {-1, -1}
        };

        return nonInfMoves(directions, board, myPosition);
    }


    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {

        int[][] directions = {
                {2, 1},
                {2, -1},
                {-2, 1},
                {-2, -1},
                {1, 2},
                {1, -2},
                {-1, 2},
                {-1, -2}
        };

        return nonInfMoves(directions, board, myPosition);
    }


    private void promotion(ChessPosition myPosition, ChessPosition newPosition, ArrayList<ChessMove> possMoves) {
        possMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
        possMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
        possMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
        possMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
    }


    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possMoves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        int[][] directions = {};
        int[][] whitePawnDirections = { {1,0} };
        int[][] blackPawnDirections = { {-1,0} };

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            directions = whitePawnDirections;
        } else {
            directions = blackPawnDirections;
        }

        for (int[] direction: directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

            if (withinBoard(newX,newY)) {
                ChessPosition newPosition = new ChessPosition(newX,newY);
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);

                if (newX == 8 || newX == 1){
                    promotion(myPosition, newPosition, possMoves);
                    break;
                }

                if (board.getPiece(newPosition) == null) {
                    possMoves.add(newMove);
                    if (x == 2 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE &&
                            board.getPiece(new ChessPosition(4, y)) == null) {
                        possMoves.add(new ChessMove(myPosition, new ChessPosition(4, y), null));
                        break;
                    } if (x == 7 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK &&
                            board.getPiece(new ChessPosition(5, y)) == null) {
                        possMoves.add(new ChessMove(myPosition, new ChessPosition(5, y), null));
                        break;
                    }
                } else {
                    break;
                }
            }

        }

        int[][] take_directions = {};
        int[][] whitePawnTakeDirections = { {1,1}, {1,-1} };
        int[][] blackPawnTakeDirections = { {-1,1}, {-1,-1} };

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            take_directions = whitePawnTakeDirections;
        } else {
            take_directions = blackPawnTakeDirections;
        }

        for (int[] direction: take_directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

            if (withinBoard(newX, newY)) {
                ChessPosition newPosition = new ChessPosition(newX, newY);
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);

                if (board.getPiece(newPosition) == null) {
                    continue;
                }

                if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    if (newX == 8 || newX == 1){
                        promotion(myPosition, newPosition, possMoves);
                        break;
                    }
                    possMoves.add(newMove);
                }
            }
        }
        return possMoves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
