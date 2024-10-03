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
            return bishop_moves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            return rook_moves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            return queen_moves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            return king_moves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            return knight_moves(board, myPosition);
        }else if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            return pawn_moves(board, myPosition);
        }
        return new ArrayList<>();
    }

    private boolean within_board(int x, int y){
        return x > 0 && x <= 8 && y > 0 && y <= 8;
    }

    private Collection<ChessMove> inf_moves( int[][] directions, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> poss_moves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        for (int[] direction: directions) {
            int dx = direction[0];
            int dy = direction[1];
            int new_x = x + dx;
            int new_y = y + dy;

            while (within_board(new_x,new_y)) {
                ChessPosition new_position = new ChessPosition(new_x,new_y);
                ChessMove new_move = new ChessMove(myPosition, new_position, null);

                if (board.getPiece(new_position) == null) {
                    poss_moves.add(new_move);
                } else if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    poss_moves.add(new_move);
                    break;
                } else {
                    break;
                }

                new_x = new_x + dx;
                new_y = new_y + dy;
            }

        }


        return poss_moves;
    }

    private Collection<ChessMove> non_inf_moves( int[][] directions, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> poss_moves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        for (int[] direction: directions) {
            int dx = direction[0];
            int dy = direction[1];
            int new_x = x + dx;
            int new_y = y + dy;

            if (within_board(new_x,new_y)) {
                ChessPosition new_position = new ChessPosition(new_x,new_y);
                ChessMove new_move = new ChessMove(myPosition, new_position, null);

                if (board.getPiece(new_position) == null) {
                    poss_moves.add(new_move);
                } else if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    poss_moves.add(new_move);
                }
            }
        }

        return poss_moves;
    }

    private Collection<ChessMove> bishop_moves(ChessBoard board, ChessPosition myPosition) {

        int[][] directions = {
                {1, 1},
                {1, -1},
                {-1, 1},
                {-1, -1}
        };

        return inf_moves(directions, board, myPosition);
    }


    private Collection<ChessMove> rook_moves(ChessBoard board, ChessPosition myPosition) {

        int[][] directions = {
                {1, 0},
                {0, -1},
                {-1, 0},
                {0, 1}
        };

        return inf_moves(directions,board, myPosition);
    }


    private Collection<ChessMove> queen_moves(ChessBoard board, ChessPosition myPosition) {

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

        return inf_moves(directions, board, myPosition);
    }

    private Collection<ChessMove> king_moves(ChessBoard board, ChessPosition myPosition) {

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

        return non_inf_moves(directions, board, myPosition);
    }


    private Collection<ChessMove> knight_moves(ChessBoard board, ChessPosition myPosition) {

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

        return non_inf_moves(directions, board, myPosition);
    }


    private void promotion(ChessPosition myPosition, ChessPosition new_position, ArrayList<ChessMove> poss_moves) {
        poss_moves.add(new ChessMove(myPosition, new_position, PieceType.QUEEN));
        poss_moves.add(new ChessMove(myPosition, new_position, PieceType.ROOK));
        poss_moves.add(new ChessMove(myPosition, new_position, PieceType.BISHOP));
        poss_moves.add(new ChessMove(myPosition, new_position, PieceType.KNIGHT));
    }


    private Collection<ChessMove> pawn_moves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> poss_moves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        int[][] directions = {};
        int[][] white_pawn_directions = { {1,0} };
        int[][] black_pawn_directions = { {-1,0} };

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            directions = white_pawn_directions;
        } else {
            directions = black_pawn_directions;
        }

        for (int[] direction: directions) {
            int dx = direction[0];
            int dy = direction[1];
            int new_x = x + dx;
            int new_y = y + dy;

            if (within_board(new_x,new_y)) {
                ChessPosition new_position = new ChessPosition(new_x,new_y);
                ChessMove new_move = new ChessMove(myPosition, new_position, null);

                if (new_x == 8 || new_x == 1){
                    promotion(myPosition, new_position, poss_moves);
                    break;
                }

                if (board.getPiece(new_position) == null) {
                    poss_moves.add(new_move);
                    if (x == 2 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE &&
                            board.getPiece(new ChessPosition(4, y)) == null) {
                        poss_moves.add(new ChessMove(myPosition, new ChessPosition(4, y), null));
                        break;
                    } if (x == 7 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK &&
                            board.getPiece(new ChessPosition(5, y)) == null) {
                        poss_moves.add(new ChessMove(myPosition, new ChessPosition(5, y), null));
                        break;
                    }
                } else {
                    break;
                }
            }

        }

        int[][] take_directions = {};
        int[][] white_pawn_take_directions = { {1,1}, {1,-1} };
        int[][] black_pawn_take_directions = { {-1,1}, {-1,-1} };

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            take_directions = white_pawn_take_directions;
        } else {
            take_directions = black_pawn_take_directions;
        }

        for (int[] direction: take_directions) {
            int dx = direction[0];
            int dy = direction[1];
            int new_x = x + dx;
            int new_y = y + dy;

            if (within_board(new_x, new_y)) {
                ChessPosition new_position = new ChessPosition(new_x, new_y);
                ChessMove new_move = new ChessMove(myPosition, new_position, null);

                if (board.getPiece(new_position) == null) {
                    continue;
                }

                if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    if (new_x == 8 || new_x == 1){
                        promotion(myPosition, new_position, poss_moves);
                        break;
                    }
                    poss_moves.add(new_move);
                }
            }
        }
        return poss_moves;
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
