package network.message.serverSide;

import model.Player;
import network.message.Message;
import network.message.MessageType;

import java.io.Serial;

/**
 * message used to show the personal goal card
 */
public class ShowPersonalCardMessage extends Message {
    @Serial
    private static final long serialVersionUID = 4878920160881688169L;
    private final Player player;

    public ShowPersonalCardMessage(Player player) {
        super("SERVER", MessageType.SHOW_PERSONAL);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "ShowPersonalCardMessage{" +
                "player=" + player + " ,personal=" + player.getPersonalGoalCard().getType() +
                '}';
    }
}
