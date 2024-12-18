package controller;

import model.Tile;
import network.client.Client;
import network.client.rmi.RMIClient;
import network.client.socket.SocketClient;
import network.message.Message;
import network.message.clientSide.*;
import network.message.serverSide.*;
import observer.Observer;
import observer.ViewObserver;
import view.View;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * class used from client side, it is the mediator between the network and a generic view (cli or gui)
 */
public class ClientController implements Observer, ViewObserver {
    private final View view;
    private Client client;
    private String nickname;
    private final ExecutorService executorService;

    public ClientController(View view) {
        this.view = view;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * checks if the given ip address is correct (ip address is X.X.X.X, where every X is a number between 0 and 255)
     * @param ip address inserted by the client
     * @return true if the ip address is valid, false otherwise
     */
    public static boolean isValidAddress(String ip) {
        String[] subStrings=ip.split("\\.");

        if(subStrings.length==4){
            for(int i=0; i<4; i++){
                if(Integer.parseInt(subStrings[i])<0 || Integer.parseInt(subStrings[i])>255){
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * checks if the given port is correct (port is a number between 1 and 65535)
     * @param port the port number inserted by the client
     * @return true if the port is valid, false otherwise
     */
    public static boolean isValidPort(String port) {
        try {
            int p = Integer.parseInt(port);
            return p >= 1 && p <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * it updates the client with a specific message based on the message type received from the server
     * @param message the received message from the server
     */
    @Override
    public void update(Message message) {
        if (message != null) {
            switch (message.getMessageType()) {
                case LOGIN_REPLY -> {
                    LoginReplyMessage loginReplyMessage = (LoginReplyMessage) message;
                    executorService.execute(() -> view.showLoginResult(loginReplyMessage.isNicknameAccepted(), loginReplyMessage.isConnectionSuccessful(), nickname));
                }
                case NUM_PLAYERS_REQ -> {
                    executorService.execute(view::askPlayersNumber);
                }
                case CHAT_MESSAGE_REPLY ->{
                    executorService.execute(() -> view.addChatMessage(((ChatReplyMessage) message).getSender(), ((ChatReplyMessage) message).getDestination(), ((ChatReplyMessage) message).getMessage()));
                }
                case ERROR -> {
                    ErrorMessage errorMessage = (ErrorMessage) message;
                    executorService.execute(() -> view.showError(errorMessage.getMessageError()));
                }
                case GENERIC -> {
                    GenericMessage genericMessage = (GenericMessage) message;
                    executorService.execute(() -> view.showGenericMessage(genericMessage.getMessage()));
                }
                case INFO_GAME -> {
                    InfoGameMessage infoGameMessage = (InfoGameMessage) message;
                    executorService.execute(() -> view.showGameInfo(infoGameMessage.getPlayers(), infoGameMessage.getNum()));
                }
                case SHOW_COMMON -> {
                    ShowCommonCardsMessage showCommonCardsMessage = (ShowCommonCardsMessage) message;
                    executorService.execute(() -> view.showCommonCards(showCommonCardsMessage.getCommonGoalCards()));
                }
                case SHOW_PERSONAL -> {
                    ShowPersonalCardMessage showPersonalCardMessage = (ShowPersonalCardMessage) message;
                    executorService.execute(() -> view.showPersonalCard(showPersonalCardMessage.getPlayer()));
                }
                case SELECT_TILE_REQ -> {
                    SelectTileRequestMessage selectTileRequestMessage = (SelectTileRequestMessage) message;
                    executorService.execute(() -> view.askSelectTiles(selectTileRequestMessage.getBoard(), selectTileRequestMessage.getBookshelf()));
                }
                case SHOW_BOARD -> {
                    ShowBoardMessage showBoardMessage = (ShowBoardMessage) message;
                    executorService.execute(() -> view.showBoard(showBoardMessage.getBoard()));
                }
                case INSERT_TILE_REQ -> {
                    InsertTilesRequestMessage insertTilesRequestMessage = (InsertTilesRequestMessage) message;
                    executorService.execute(() -> view.askInsertTiles(insertTilesRequestMessage.getBookshelf(), insertTilesRequestMessage.getTiles()));
                }
                case SHOW_BOOKSHELF -> {
                    ShowBookshelfMessage showBookshelfMessage = (ShowBookshelfMessage) message;
                    executorService.execute(() -> view.showBookshelf(showBookshelfMessage.getPlayer()));
                }
                case ORDER_REQ -> {
                    OrderRequestMessage orderRequestMessage = (OrderRequestMessage) message;
                    executorService.execute(() -> view.askOrderTiles(orderRequestMessage.getTiles()));
                }
                case COMMON_GOAL_COMPLETE -> {
                    CommonGoalCompleteMessage commonGoalComplete1Message = (CommonGoalCompleteMessage) message;
                    executorService.execute(() -> view.showCommonGoalComplete(commonGoalComplete1Message.getCommonGoal(), commonGoalComplete1Message.getCommonGoalScore()));
                }
                case DISCONNECTION -> {
                    DisconnectionMessage disconnectionMessage = (DisconnectionMessage) message;
                    executorService.execute(() -> view.showDisconnection(disconnectionMessage.getNickname(), disconnectionMessage.isStarted()));
                }
                case SCORES -> {
                    ScoresMessage scoresMessage = (ScoresMessage) message;
                    executorService.execute(() -> view.showScores(scoresMessage.getPlayerScore()));
                }
                case COMMON_SCORES -> {
                    ShowCommonScoresMessage showCommonScoresMessage = (ShowCommonScoresMessage) message;
                    executorService.execute(() -> view.showCommonScores(showCommonScoresMessage.getCommonGoalCardScores()));
                }
                case WINNER -> {
                    WinnerPlayerMessage winnerPlayerMessage = (WinnerPlayerMessage) message;
                    executorService.execute(() -> view.showWinner(winnerPlayerMessage.getWinner()));
                }
            }
        }
    }

    /**
     * create a new connection between server and client
     * @param ip the ip address
     * @param port the port number
     * @param type the type of connection (1 for socket, 2 for rmi)
     */
    @Override
    public void createConnection(String ip, String port, int type) {
        try {
            if (type == 1) this.client = new SocketClient(ip, Integer.parseInt(port));
            if (type == 2) this.client = new RMIClient(ip, Integer.parseInt(port));
            this.client.addObserver(this);
            this.client.readMessage();
            this.client.sendPingMessage(true);
            this.executorService.execute(this.view::askNickname);
        } catch (IOException e) {
            this.executorService.execute(() -> this.view.showLoginResult(false, false,null));
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendNickname(String nickname) {
        this.nickname = nickname;
        this.client.sendMessage(new LoginRequestMessage(this.nickname));
    }

    @Override
    public void sendNumPlayers(int numPlayers) {
        client.sendMessage(new NumPlayersReplyMessage(this.nickname, numPlayers));
    }

    @Override
    public void sendSelectTiles(List<Tile> tiles) {
        client.sendMessage(new TilesReplyMessage(this.nickname, tiles));
    }

    @Override
    public void sendInsertTiles(int column, List<Tile> tiles) {
        client.sendMessage(new PositionReplyMessage(this.nickname, column, tiles));
    }

    @Override
    public void sendOrderTiles(List<Tile> tiles) {
        client.sendMessage(new OrderReplyMessage(this.nickname, tiles));
    }

    @Override
    public void sendChatMessage(String destination, String message) {
        client.sendMessage(new ChatRequestMessage(this.nickname, destination, message));
    }
}
