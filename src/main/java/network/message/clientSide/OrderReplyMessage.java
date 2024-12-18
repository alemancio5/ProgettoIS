package network.message.clientSide;

import model.Tile;
import network.message.Message;
import network.message.MessageType;

import java.io.Serial;
import java.util.List;

/**
 * message used to send the selected order for the tiles
 */
public class OrderReplyMessage extends Message {
    @Serial
    private static final long serialVersionUID = 2147176422511553721L;
    private final List<Tile> tiles;

    public OrderReplyMessage(String nickname, List<Tile> tiles) {
        super(nickname, MessageType.ORDER_REPLY);
        this.tiles = tiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        return "OrderReplyMessage{nickname= " + getNickname() +
                ", tiles= " + tiles.toString() +
                '}';
    }
}
