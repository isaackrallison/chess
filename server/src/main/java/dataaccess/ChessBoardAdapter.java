package dataaccess;

import chess.ChessBoard;
import chess.ChessPiece;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardAdapter implements JsonSerializer<ChessBoard>, JsonDeserializer<ChessBoard> {

    @Override
    public JsonElement serialize(ChessBoard board, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonBoard = new JsonArray();

        // Iterate through the 2D array and serialize each ChessPiece
        for (int row = 0; row < board.squares.length; row++) {
            JsonArray jsonRow = new JsonArray();
            for (int col = 0; col < board.squares[row].length; col++) {
                ChessPiece piece = board.squares[row][col];
                if (piece != null) {
                    jsonRow.add(context.serialize(piece)); // Serialize the piece
                } else {
                    jsonRow.add(JsonNull.INSTANCE); // Represent empty squares as null
                }
            }
            jsonBoard.add(jsonRow);
        }

        return jsonBoard; // Return the entire board as a JSON array
    }

    @Override
    public ChessBoard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonBoard = json.getAsJsonArray();
        ChessPiece[][] squares = new ChessPiece[8][8];

        // Iterate through the JSON array and reconstruct the ChessBoard
        for (int row = 0; row < jsonBoard.size(); row++) {
            JsonArray jsonRow = jsonBoard.get(row).getAsJsonArray();
            for (int col = 0; col < jsonRow.size(); col++) {
                JsonElement pieceElement = jsonRow.get(col);
                if (!pieceElement.isJsonNull()) {
                    ChessPiece piece = context.deserialize(pieceElement, ChessPiece.class); // Deserialize the piece
                    squares[row][col] = piece;
                } else {
                    squares[row][col] = null; // Handle empty squares
                }
            }
        }

        ChessBoard chessBoard = new ChessBoard();
        chessBoard.squares = squares; // Set the deserialized squares
        return chessBoard; // Return the constructed ChessBoard
    }
}


