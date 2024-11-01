    package dataaccess;
    import com.google.gson.*;

    import chess.ChessPiece;
    import chess.ChessGame;


    import java.lang.reflect.Type;

    public class ChessPieceAdapter implements JsonSerializer<ChessPiece>, JsonDeserializer<ChessPiece> {

        @Override
        public JsonElement serialize(ChessPiece piece, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", piece.getPieceType().toString());
            jsonObject.addProperty("color", piece.getTeamColor().toString());
            return jsonObject;
        }

        @Override
        public ChessPiece deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String typeString = jsonObject.get("type").getAsString();
            String colorString = jsonObject.get("color").getAsString();

            ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(colorString.toUpperCase());
            ChessPiece.PieceType type = ChessPiece.PieceType.valueOf(typeString.toUpperCase());

            return new ChessPiece(color, type);
        }
    }

