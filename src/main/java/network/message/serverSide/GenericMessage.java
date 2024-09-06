package network.message.serverSide;

import network.message.Message;
import network.message.MessageType;

import java.io.Serial;

/**
 * message used to show a generic text during the game
 */
public class GenericMessage extends Message {
    @Serial
    private static final long serialVersionUID = -1459285387719574659L;
    private final String message;

    public GenericMessage(String nickname, String message) {
        super(nickname, MessageType.GENERIC);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GenericMessage{" +
                "message : '" + message + '\'' +
                '}';
    }
}
