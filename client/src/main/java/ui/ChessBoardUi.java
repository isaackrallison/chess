package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import chess.ChessGame;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class ChessBoardUi {
    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        String[] headers = {"h", "g", "f", "e", "d", "c", "b", "a"};

        drawHeaders(out, headers);

        ChessGame game = new ChessGame();
        drawChessBoard(out, game.getBoard(),true);

        drawHeaders(out, headers);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        out.println();
        out.println();

        String[] newHeaders= {"a", "b" ,"c", "d", "e", "f", "g", "h"};

        drawHeaders(out, newHeaders);

        drawChessBoard(out, game.getBoard(),false);

        drawHeaders(out, newHeaders);
    }

    private static void drawHeaders(PrintStream out,String[] headers) {

        setBlack(out);

        out.print(" ");

        out.print("    ");
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
//            drawHeader(out, headers[boardCol]);
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_WHITE);
            out.printf("%-11s", headers[boardCol]);
        }

        out.println();
    }


    private static void drawChessBoard(PrintStream out, chess.ChessBoard chessBoard, boolean foreward) {
        if (foreward) {
            int rowNum = 1;
            for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; --boardRow) {
                drawRowOfSquares(out, chessBoard, boardRow, rowNum, foreward);
                rowNum++;
            }
        } else {
            int rowNum = 8;
            for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; --boardRow) {
                drawRowOfSquares(out, chessBoard, boardRow, rowNum, foreward);
                rowNum--;
            }
        }
    }

    // New helper method to print a piece with color based on team
    private static void printPieceWithColor(PrintStream out, chess.ChessPiece piece, String backGroundColor) {
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                out.print(SET_TEXT_COLOR_RED);
            } else {
                out.print(SET_TEXT_COLOR_GREEN);
            }
            printPiece(out, getPieceSymbol(piece), backGroundColor);
        } else {
            out.print(EMPTY);
        }
    }

    private static void drawRowOfSquares(PrintStream out, chess.ChessBoard chessBoard, int boardRow, int rowNum, boolean forward) {
        String backGroundColor = null;

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            if (squareRow == 1) {
                setBlackWrite(out);
                out.print(rowNum);
                setBlack(out);
            } else {
                out.print(" ");
            }
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                boolean isWhiteSquare = (boardRow + boardCol) % 2 != 0;
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

                    // Print the piece depending on the board orientation
                    if (forward) {
                        chess.ChessPiece piece = chessBoard.getPiece(new chess.ChessPosition(boardRow + 1, boardCol + 1));
                        printPieceWithColor(out, piece, backGroundColor);
                    } else {
                        ChessPosition pos = new chess.ChessPosition(BOARD_SIZE_IN_SQUARES - boardRow, BOARD_SIZE_IN_SQUARES - boardCol);
                        chess.ChessPiece piece = chessBoard.getPiece(pos);
                        printPieceWithColor(out, piece, backGroundColor);
                    }

                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }
            }
            if (squareRow == 1) {
                setBlackWrite(out);
                out.print(rowNum);
            }
            setBlack(out);
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

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlackWrite(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printPiece(PrintStream out, String symbol, String backGroundColor) {
        if (Objects.equals(backGroundColor, "white")) {
            out.print(SET_BG_COLOR_WHITE);
            out.print(symbol);
        } else {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(symbol);
        }
    }
}

