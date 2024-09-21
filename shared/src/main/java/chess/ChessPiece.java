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
        return null;
    }

    private Collection<ChessMove> valid_move_calculator(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).type == PieceType.BISHOP) {
            return bishop_move_calculator(board, myPosition);
        } else if (board.getPiece(myPosition).type == PieceType.ROOK) {
            return rook_move_calculator(board, myPosition);
        } else if (board.getPiece(myPosition).type == PieceType.QUEEN) {
            return queen_move_calculator(board, myPosition);
        } else if (board.getPiece(myPosition).type == PieceType.KING) {
            return king_move_calculator(board, myPosition);
        } else if (board.getPiece(myPosition).type == PieceType.KNIGHT) {
            return knight_move_calculator(board, myPosition);
        } else if (board.getPiece(myPosition).type == PieceType.PAWN) {
            return pawn_move_calculator(board, myPosition);
        }
        return new ArrayList<>();
    }

    private Collection<ChessMove> bishop_move_calculator(ChessBoard board, ChessPosition myPosition) {
//        create valid_move list
        ArrayList<ChessMove> valid_moves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

//      Build int array for possible direction piece can move
        int[][] poss_directions = {
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1}
        };

//      Loop through direction array, adding (or subtracting) x and y each time
        for (int[] direction : poss_directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

//          while the coordinates are in the board area, initialize variables and check for other pieces/ color
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
        ArrayList<ChessMove> valid_moves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        int[][] poss_directions = {
                {1,0},
                {0,1},
                {-1,0},
                {0,-1}
        };

        for (int[] direction : poss_directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

//          while the coordinates are in the board area, initialize variables and check for other pieces/ color
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
        ArrayList<ChessMove> valid_moves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

//      queen has a combination of rook and bishop movement
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

        for (int[] direction : poss_directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

//          while the coordinates are in the board area, initialize variables and check for other pieces/ color
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

    private Collection<ChessMove> king_move_calculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> valid_moves = new ArrayList<>();

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

        for (int[] direction : poss_directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

//          while the coordinates are in the board area, initialize variables and check for other pieces/ color
//          King has an if statement instead of a while
            if (new ChessPosition(newX,newY).WithinBoard()) {
                ChessPosition newPosition = new ChessPosition(newX, newY);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);

                if (pieceAtPosition == null) {
                    valid_moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    if (pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        valid_moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }

        return valid_moves;

    }

    private Collection<ChessMove> knight_move_calculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> valid_moves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        int[][] poss_directions = {
                {1,2},
                {2,1},
                {-1,2},
                {2,-1},
                {1,-2},
                {-2,1},
                {-2,-1},
                {-1,-2},
        };

        for (int[] direction : poss_directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

//          while the coordinates are in the board area, initialize variables and check for other pieces/ color
//          If like the king because it has a saved set of spaces it can move
            if (new ChessPosition(newX,newY).WithinBoard()) {
                ChessPosition newPosition = new ChessPosition(newX, newY);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);

                if (pieceAtPosition == null) {
                    valid_moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    if (pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        valid_moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }

        return valid_moves;

    }

    private int promotion_options(Collection<ChessMove> valid_moves, ChessPosition myPosition, ChessPosition newPosition) {
        valid_moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
        valid_moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
        valid_moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
        valid_moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
        return 0;
    }


    private Collection<ChessMove> pawn_move_calculator(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> valid_moves = new ArrayList<>();

        int x = myPosition.getRow();
        int y = myPosition.getColumn();

//      The pawn is different from all other pieces, it can move up or down based on its color and
//      sideways based upon there being a piece diagonal to it in the same direction it moves

//      for moving forward as the pawn
        int[][] poss_directions = {};
        int[][] black_pawn_poss = {
                {-1,0}
        };

        int[][] white_pawn_poss = {
                {1,0}
        };

        if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE){
            poss_directions = white_pawn_poss.clone();
        } else {poss_directions = black_pawn_poss.clone();
        }


        for (int[] direction : poss_directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = x + dx;
            int newY = y + dy;

//          while the coordinates are in the board area, initialize variables and check for other pieces/ color
            if (new ChessPosition(newX, newY).WithinBoard()) {
                ChessPosition newPosition = new ChessPosition(newX, newY);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);

                if (pieceAtPosition == null) {
                    if (newPosition.getRow() == 8 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) {
                        promotion_options(valid_moves, myPosition, newPosition);
                    } else if (newPosition.getRow() == 1 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK) {
                        promotion_options(valid_moves, myPosition, newPosition);
                    } else {
                        valid_moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }


//      Adding taking mechanics
        int[][] take_directions = {};
        int[][] black_pawn_take = {
                {-1,1},
                {-1,-1}
        };

        int[][] white_pawn_take = {
                {1,1},
                {1,-1}
        };

        if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE){
            take_directions = white_pawn_take.clone();
        } else {take_directions = black_pawn_take.clone();
        }

        int newX = 0;
        int newY = 0;

        for (int[] direction : take_directions) {
            int dx = direction[0];
            int dy = direction[1];
            newX = x + dx;
            newY = y + dy;


            if (new ChessPosition(newX, newY).WithinBoard()) {
                ChessPosition newPosition = new ChessPosition(newX, newY);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);

                if (pieceAtPosition != null && pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    if (newPosition.getRow() == 8 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) {
                        promotion_options(valid_moves, myPosition, newPosition);
                    } else if (newPosition.getRow() == 1 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK) {
                        promotion_options(valid_moves, myPosition, newPosition);
                    } else {
                        valid_moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }

//      checks to see if the pawn can move two spaces
        if(myPosition.getRow()== 2){
            ChessPosition in_front_1_white = new ChessPosition(3, y);
            ChessPiece Piece_in_front_white = board.getPiece(in_front_1_white);
                if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) {
                    if (Piece_in_front_white == null) {
                        if (board.getPiece(new ChessPosition(4, y)) == null) {
                            valid_moves.add(new ChessMove(myPosition, new ChessPosition((4), y), null));
                        }
                    }
                }
        }



        if(myPosition.getRow()== 7){
            ChessPosition in_front_1_black = new ChessPosition(6, y);
            ChessPiece Piece_in_front_black = board.getPiece(in_front_1_black);
            if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK) {
                if (Piece_in_front_black == null) {
                    if (board.getPiece(new ChessPosition(5, y)) == null) {
                        valid_moves.add(new ChessMove(myPosition, new ChessPosition((5), y), null));
                    }
                }
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
