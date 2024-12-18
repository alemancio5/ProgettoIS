package network.server;

import network.message.Message;

/**
 * interface used to handle clients: the RMIClientHandler and SocketClientHandler implements it
 */
public interface ClientHandler {

    /**
     * sends a message from the server to the client
     * @param message to be sent
     */
    void sendMessageToClient(Message message);

    /**
     * disconnect the client from the server
     */
    void disconnectClient();
}
