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
        if (board.getPiece(myPosition) != null) {
            return valid_move_calculator(board, myPosition);
        }
        return new ArrayList<>();
    }

    private Collection<ChessMove> valid_move_calculator(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).type == PieceType.BISHOP) {
            return bishop_move_calculator(board, myPosition);
        } else if (board.getPiece(myPosition).type == PieceType.ROOK) {
            return rook_move_calculator(board, myPosition);
        } else if (board.getPiece(myPosition).type == PieceType.QUEEN) {
            return queen_move_calculator(board, myPosition);
        }
        return new ArrayList<ChessMove>();
    }

    private Collection<ChessMove> bishop_move_calculator(ChessBoard board, ChessPosition myPosition) {
//        create valid_move list
        ArrayList<ChessMove> valid_moves = new ArrayList<ChessMove>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

//      Build int array for possible direction piece can move
        int[][] poss_directions = {
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1}
        };

//      Initialize variable
        int newX = 0;
        int newY = 0;
        int dx = 0;
        int dy = 0;

//      Loop through direction array, adding (or subtracting) x and y each time
        for (int[] direction : poss_directions) {
            dx = direction[0];
            dy = direction[1];
            newX = x + dx;
            newY = y + dy;

//          while the coordinates are in the board area, initialize varibles and check for other pieces/ color
            while (new ChessPosition(newX,newY).WithinBoard()) {
                ChessPosition newPosition = new ChessPosition(newX, newY);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);

                if (pieceAtPosition == null) {
                    valid_moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    if (pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        valid_moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                newX = newX + dx;
                newY = newY + dy;
            }
        }

        return valid_moves;
    }

    private Collection<ChessMove> rook_move_calculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> valid_moves = new ArrayList<ChessMove>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        int[][] poss_directions = {
                {1,0},
                {0,1},
                {-1,0},
                {0,-1}
        };

        int newX = 0;
        int newY = 0;
        int dx = 0;
        int dy = 0;

        for (int[] direction : poss_directions) {
            dx = direction[0];
            dy = direction[1];
            newX = x + dx;
            newY = y + dy;

//          while the coordinates are in the board area, initialize varibles and check for other pieces/ color
            while (new ChessPosition(newX,newY).WithinBoard()) {
                ChessPosition newPosition = new ChessPosition(newX, newY);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);

                if (pieceAtPosition == null) {
                    valid_moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    if (pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        valid_moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                newX = newX + dx;
                newY = newY + dy;
            }
        }

        return valid_moves;

    }

    private Collection<ChessMove> queen_move_calculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> valid_moves = new ArrayList<ChessMove>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        int[][] poss_directions = {
                {1,0},
                {0,1},
                {-1,0},
                {0,-1},
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1}
        };

        int newX = 0;
        int newY = 0;
        int dx = 0;
        int dy = 0;

        for (int[] direction : poss_directions) {
            dx = direction[0];
            dy = direction[1];
            newX = x + dx;
            newY = y + dy;

//          while the coordinates are in the board area, initialize varibles and check for other pieces/ color
            while (new ChessPosition(newX,newY).WithinBoard()) {
                ChessPosition newPosition = new ChessPosition(newX, newY);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);

                if (pieceAtPosition == null) {
                    valid_moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    if (pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        valid_moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                newX = newX + dx;
                newY = newY + dy;
            }
        }

        return valid_moves;

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
