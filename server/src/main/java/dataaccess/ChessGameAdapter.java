package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChessGameAdapter {

        private static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter())
                .registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter())
                .create();

        public static String serialize(ChessGame game) {
            return GSON.toJson(game);
        }

        public static ChessGame deserialize(String json) {
            return GSON.fromJson(json, ChessGame.class);
        }
    }
