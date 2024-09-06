package network.message.serverSide;

import network.message.Message;
import network.message.MessageType;

import java.io.Serial;

/**
 * message used to ask the client to choose the number of players in the game
 */
public class NumPlayersRequestMessage extends Message {

    @Serial
    private static final long serialVersionUID = -6991222229656932962L;

    public NumPlayersRequestMessage() {
        super("SERVER", MessageType.NUM_PLAYERS_REQ);
    }

    @Override
    public String toString() {
        return "SetNumPlayers{" +
                "nickname=" + getNickname() + "}";
    }
}
