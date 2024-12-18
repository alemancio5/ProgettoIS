package network.message.serverSide;

import model.Player;
import network.message.Message;
import network.message.MessageType;

import java.io.Serial;
import java.util.List;

/**
 * message used to send to the client information about the game (players and number)
 */
public class InfoGameMessage extends Message {
    @Serial
    private static final long serialVersionUID = -6954035833019978470L;
    private final List<Player> players;
    private final int num;

    public InfoGameMessage(List<Player> players, int num) {
        super("SERVER", MessageType.INFO_GAME);
        this.players = players;
        this.num = num;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getNum() {
        return num;
    }

    @Override
    public String toString() {
        return "InfoGame{" +
                "players=" + players.toString() +
                ", num=" + num +
                '}';
    }
}
