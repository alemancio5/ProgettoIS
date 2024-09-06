package network.message.serverSide;

import network.message.Message;
import network.message.MessageType;

import java.io.Serial;

/**
 * message to notify the winner player of the game
 */
public class WinnerPlayerMessage extends Message {
    @Serial
    private static final long serialVersionUID = 7453168361368250724L;
    private final String winner;

    public WinnerPlayerMessage(String winner) {
        super("SERVER", MessageType.WINNER);
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        return "WinnerPlayerMessage{" +
                "winner='" + winner + '\'' +
                '}';
    }
}
