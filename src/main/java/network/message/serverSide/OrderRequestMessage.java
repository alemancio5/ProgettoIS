package network.message.serverSide;

import model.Tile;
import network.message.Message;
import network.message.MessageType;

import java.io.Serial;
import java.util.List;

/**
 * message used to ask the order of the tiles inserting in the bookshelf
 */
public class OrderRequestMessage extends Message {
    @Serial
    private static final long serialVersionUID = -1757584888349247727L;
    private final List<Tile> tiles;

    public OrderRequestMessage(List<Tile> tiles) {
        super("SERVER", MessageType.ORDER_REQ);
        this.tiles = tiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        return "OrderRequestMessage{" +
                "tiles=" + tiles.toString() +
                '}';
    }
}
