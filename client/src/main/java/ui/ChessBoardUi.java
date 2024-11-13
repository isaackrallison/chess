package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import chess.ChessBoard;
import chess.ChessGame;

import static ui.EscapeSequences.*;

public class ChessBoardUi {
    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int BOARD_BOARDER_SIZE_IN_PADDED_CHARS = 1;

    // Padded characters.
//    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        ChessGame game = new ChessGame();
        drawChessBoard(out, game.getBoard());

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
//        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
//        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        int prefixLength = 1 ;
        int suffixLength = 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }


    private static void drawChessBoard(PrintStream out, chess.ChessBoard chessBoard) {
        for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; --boardRow) {
            drawRowOfSquares(out, chessBoard, boardRow);
        }
    }

    private static void drawRowOfSquares(PrintStream out, chess.ChessBoard chessBoard, int boardRow) {
        String backGroundColor = null;
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                boolean isWhiteSquare = (boardRow + boardCol) % 2 == 0;
                if (isWhiteSquare) {
                    out.print(SET_BG_COLOR_WHITE);
                    backGroundColor = "white";
                } else {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    backGroundColor = "grey";
                }

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));

                    // Get the piece at this position and print it, or print an empty space
                    chess.ChessPiece piece = chessBoard.getPiece(new chess.ChessPosition(boardRow + 1, boardCol + 1));
                    if (piece != null) {
                        printPlayer(out, getPieceSymbol(piece), backGroundColor);
                    } else {
                        out.print(EMPTY);
                    }

                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }
                setBlack(out);
            }
            out.println();
        }
    }

    // Helper method to return the correct symbol for each chess piece
    private static String getPieceSymbol(chess.ChessPiece piece) {
        switch (piece.getTeamColor()) {
            case WHITE:
                switch (piece.getPieceType()) {
                    case KING:
                        return WHITE_KING;
                    case QUEEN:
                        return WHITE_QUEEN;
                    case BISHOP:
                        return WHITE_BISHOP;
                    case KNIGHT:
                        return WHITE_KNIGHT;
                    case ROOK:
                        return WHITE_ROOK;
                    case PAWN:
                        return WHITE_PAWN;
                }
                break;
            case BLACK:
                switch (piece.getPieceType()) {
                    case KING:
                        return BLACK_KING;
                    case QUEEN:
                        return BLACK_QUEEN;
                    case BISHOP:
                        return BLACK_BISHOP;
                    case KNIGHT:
                        return BLACK_KNIGHT;
                    case ROOK:
                        return BLACK_ROOK;
                    case PAWN:
                        return BLACK_PAWN;
                }
                break;
        }
        return EMPTY;
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
//        out.print(SET_TEXT_COLOR_MAGENTA);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String symbol, String backGroundColor) {
        if (backGroundColor == "white") {
            out.print(SET_BG_COLOR_WHITE);
            out.print(symbol);
        } else {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(symbol);
        }
    }
}

