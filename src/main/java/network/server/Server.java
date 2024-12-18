package network.server;

import controller.GameController;
import network.message.Message;
import view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *  class represents the server of the game and handles the connection with the clients
 */
public class Server {
    public static final Logger LOGGER =  Logger.getLogger(Server.class.getName());
    private final Map<String, ClientHandler> clientHandlerMap;
    private final GameController gameController;

    public Server(GameController gameController) {
        this.gameController = gameController;
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * handles the addition of a new client
     * @param nickname of the new client
     * @param clientHandler of the new client
     */
    public void addClient(String nickname, ClientHandler clientHandler) {
        VirtualView virtualView = new VirtualView(clientHandler);
        //case of first client connection
        if(!gameController.isGameStarted() && clientHandlerMap.isEmpty()) {
            if(gameController.checkLoginNickname(nickname, virtualView)) {
                clientHandlerMap.put(nickname, clientHandler);
                gameController.loginHandler(nickname, virtualView);
                LOGGER.info(() -> nickname + " is the first connected to the game");
            }
        }
        //case of new client connection after the number of players is set
        else if (!gameController.isGameStarted() && gameController.isNumPlayersSet()) {
            if(gameController.checkLoginNickname(nickname, virtualView)) {
                clientHandlerMap.put(nickname, clientHandler);
                gameController.loginHandler(nickname, virtualView);
                LOGGER.info(() -> nickname + " is connected to the game");
            }
        }
        //case of new client connection before the number of players is set
        else if (!gameController.isGameStarted() && !gameController.isNumPlayersSet()) {
            virtualView.showLoginResult(false, false, null);
            LOGGER.info(() -> "Game is not started yet. Player " + nickname + " cannot join the game");
            clientHandler.disconnectClient();
        }
        //case of reconnected client
        else if (gameController.isGameStarted() && gameController.getTurnController().getNicknames().size() < gameController.getNicknames().size()) {
            if(gameController.checkLoginNicknameReconnect(nickname, virtualView)) {
                clientHandlerMap.put(nickname, clientHandler);
                gameController.loginHandler(nickname, virtualView);
                LOGGER.info(() -> nickname + " is reconnected to the game");
            }
        }
        //case of already started game
        else {
            virtualView.showLoginResult(true, false,null);
            LOGGER.info(() -> "Game is already started. Player " + nickname + " cannot join the game");
            clientHandler.disconnectClient();
        }
    }

    /**
     * removes a client given his nickname
     * @param nickname of the client to remove
     * @param isStarted true if the game is already started, false otherwise
     */
    public void removeClient(String nickname, boolean isStarted) {
        if(isStarted) {
            clientHandlerMap.remove(nickname);
            gameController.removeVirtualView(nickname);
            LOGGER.info(() -> "Removed " + nickname + " from the list of connected players");
        }
        else {
            clientHandlerMap.clear();
        }
    }

    /**
     * forwards a message received from the client to the game controller
     * @param message to be forwarded
     */
    public void forwardsMessage(Message message) {
        gameController.onMessageReceived(message);
    }

    /**
     * disconnects a client from the server
     * @param clientHandler the client handler to be disconnected
     */
    public void onDisconnect(ClientHandler clientHandler) {
        String nickname = getNicknameFromClientHandler(clientHandler);
        if(nickname != null) {
            //if is login phase ends the game
            if(!gameController.isGameStarted()) {
                removeClient(nickname, false);
                gameController.broadcastingDisconnection(nickname, false);
                gameController.endGame();
                LOGGER.warning(() -> "Game finishes in Login phase");
            }
            //if is in game phase continue for the disconnection resilience
            else {
                removeClient(nickname, true);
                clientHandlerMap.remove(nickname);
                LOGGER.info(() -> "Game continue for resilience");
            }
        }
    }

    /**
     * returns the corresponding nickname of a ClientHandler
     * @param clientHandler the client handler
     * @return the corresponding nickname of a ClientHandler
     */
    private String getNicknameFromClientHandler(ClientHandler clientHandler) {
        return clientHandlerMap.entrySet()
                .stream()
                .filter(entry -> clientHandler.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
