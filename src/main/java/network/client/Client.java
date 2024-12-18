package network.client;

import network.message.Message;
import observer.Observable;

import java.util.logging.Logger;

/**
 * abstract class represents the generic client used to communicate with the server, every type of connection implements this class
 */
public abstract class Client extends Observable {
    public static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    /**
     * sends a message to the server
     * @param message to be sent
     */
    public abstract void sendMessage(Message message);

    /**
     * reads a message from the server and notifies the ClientController
     */
    public abstract void readMessage();

    /*+
     * disconnect from the server
     */
    public abstract void disconnect();

    /**
     * sends a ping message in order to keep alive the connection between server and client
     * @param isActive is true to enable the ping, false otherwise
     */
    public abstract void sendPingMessage(boolean isActive);
}
