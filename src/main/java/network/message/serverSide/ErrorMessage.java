package network.message.serverSide;

import network.message.Message;
import network.message.MessageType;

import java.io.Serial;

/**
 * message used to notify an error to the client
 */
public class ErrorMessage extends Message {
    @Serial
    private static final long serialVersionUID = 5355978810741721251L;
    private final String messageError;

    public ErrorMessage(String messageError) {
        super("SERVER", MessageType.ERROR);
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "messageError='" + messageError + '\'' +
                '}';
    }
}
