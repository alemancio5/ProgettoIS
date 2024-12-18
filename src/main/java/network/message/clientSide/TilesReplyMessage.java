package network.message.clientSide;

import model.Tile;
import network.message.Message;
import network.message.MessageType;

import java.io.Serial;
import java.util.List;

/**
 * message used to notify the selected tiles from the board
 */
public class TilesReplyMessage extends Message {
    @Serial
    private static final long serialVersionUID = 5909872357046367409L;
    private final List<Tile> tiles;

    public TilesReplyMessage(String nickname, List<Tile> tiles) {
        super(nickname, MessageType.TILES_REPLY);
        this.tiles = tiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        return "TilesReplyMessage{nickname= " + getNickname() +
                ", tiles= " + tiles.toString() +
                '}';
    }
}
