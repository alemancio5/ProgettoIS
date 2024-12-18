package network.message.serverSide;

import model.Bookshelf;
import model.Tile;
import network.message.Message;
import network.message.MessageType;

import java.io.Serial;
import java.util.List;

/**
 * message sent to the client to insert the selected tiles into the bookshelf
 */
public class InsertTilesRequestMessage extends Message {
    @Serial
    private static final long serialVersionUID = 6352397701669107548L;
    private final List <Tile> tiles;
    private final Bookshelf bookshelf;

    public InsertTilesRequestMessage(List <Tile> tiles, Bookshelf bookshelf ) {
        super("SERVER", MessageType.INSERT_TILE_REQ);
        this.bookshelf = bookshelf;
        this.tiles = tiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    @Override
    public String toString() {
        return "InsertTilesRequestMessage";
    }
}
