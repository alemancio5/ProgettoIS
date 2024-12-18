package network.server.socket;

import network.message.Message;
import network.message.MessageType;
import network.server.ClientHandler;
import network.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * socket implementation of the ClientHandler interface
 */
public class SocketClientHandler implements ClientHandler, Runnable {
    private final Socket clientSocket;
    private final SocketServer socketServer;
    private boolean isConnected;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final Object inputLock;
    private final Object outputLock;

    public SocketClientHandler(Socket clientSocket, SocketServer socketServer) {
        this.clientSocket = clientSocket;
        this.socketServer = socketServer;
        this.inputLock = new Object();
        this.outputLock = new Object();
        this.isConnected = true;

        try {
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            this.handleClientConnection();
        } catch (IOException e) {
            Server.LOGGER.severe("SOCKET Client " + clientSocket.getInetAddress() + " connection dropped");
            disconnectClient();
        }
    }

    /**
     * handles the connection with the new client and keep listening to the socket for new messages
     *
     * @throws IOException for Input/Output exceptions
     */
    private void handleClientConnection() throws IOException {
        Server.LOGGER.info("SOCKET Client connected from address " + clientSocket.getInetAddress());
        Server.LOGGER.info("SOCKET Client connected from port " + clientSocket.getLocalPort());

        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (inputLock) {
                    Message message = (Message) inputStream.readObject();
                    if (message != null && message.getMessageType() != MessageType.PING) {
                        //if is a LOGIN message
                        if (message.getMessageType() == MessageType.LOGIN_REQ) {
                            Server.LOGGER.info(() -> "SOCKET Message LoginRequest received from " + message.getNickname() + ": " + message);
                            socketServer.addClient(message.getNickname(), this);
                        } else {
                            Server.LOGGER.info(() -> "SOCKET Message received from: " + message.getNickname() + ": " + message);
                            //forwards the message to the server that sends it to the main controller
                            socketServer.forwardsMessage(message);
                        }
                    }
                }
            }
        } catch (ClassCastException | ClassNotFoundException e) {
            Server.LOGGER.severe("SOCKET Invalid stream from client");
        }
        clientSocket.close();
    }

    /**
     * disconnects the socket client
     */
    @Override
    public void disconnectClient() {
        if (isConnected) {
            try {
                if (!clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                Server.LOGGER.severe(e.getMessage());
            }
            isConnected = false;
            Thread.currentThread().interrupt();
            socketServer.onDisconnect(this);
        }
    }

    /**
     * sends a message to the client
     *
     * @param message to be sent
     */
    @Override
    public void sendMessageToClient(Message message) {
        try {
            synchronized (outputLock) {
                outputStream.writeObject(message);
                outputStream.reset();
                Server.LOGGER.info(() -> "SOCKET Message sent : " + message);
            }
        } catch (IOException e) {
            Server.LOGGER.severe("SOCKET Client " + clientSocket.getInetAddress() + " connection dropped");
            disconnectClient();
        }
    }
}
