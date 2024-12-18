package view.GUI;

import model.*;
import observer.ViewObservable;
import view.CLI.ColorCli;
import view.GUI.Scene.EndController;
import view.GUI.Scene.GameControllerScene;
import view.GUI.Scene.LobbyController;
import view.View;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static view.GUI.ErrorType.*;

/**
 * Override of the View class for the GUI
 * Every method of the View are implemented in this class for the GUI
 * @author Stefano Morano
 */

public class Gui extends ViewObservable implements View {
    private int players_number;
    private List<Player> players_in_game;
    private Player owner;
    private boolean isFirst = false;
    private Map<String, Integer> score;
    private static final String INPUT_ERR = ColorCli.RED + "Invalid Input! Please retry." + ColorCli.RESET;

    /**
     * ask the nickname to the client
     */
    @Override
    public void askNickname(){
        Platform.runLater(() -> SceneController.changeRootPane(observers, "namePanel.fxml"));
    }

    /**
     * ask the number of players to the first player
     */
    @Override
    public void askPlayersNumber(){
        this.isFirst = true;
        Platform.runLater(() -> SceneController.changeRootPane(observers, "SelectPlayersPanel.fxml"));
    }

    /**
     * it controls if every parameter is compatible with the game and if the connection works
     * @param isNicknameAccepted if the nickname selected is accepted or not
     * @param isConnectionSuccessful if the connection is successful or not
     * @param nickname of player
     */
    @Override
    public void showLoginResult(boolean isNicknameAccepted, boolean isConnectionSuccessful, String nickname){
        if (!isConnectionSuccessful){
            Platform.runLater(() -> SceneController.popUp(WRONG_SERVER));
            return;
        }
        if(!isNicknameAccepted) {
            Platform.runLater(() -> SceneController.popUp(WRONG_NICKNAME));
            askNickname();
        }
    }

    /**
     * shows to the client the information about the game
     * @param players all the players joined in the game
     * @param numberPlayers number of players in the game
     */
    @Override
    public void showGameInfo(List<Player> players, int numberPlayers){
        LobbyController lobby_ctrl;
        LobbyController new_ctrl;
        final LobbyController new_ctrl2;
        try {
            this.players_in_game=players;
            lobby_ctrl = (LobbyController) SceneController.getActiveController();
            new_ctrl2 = lobby_ctrl;
            Platform.runLater(() -> new_ctrl2.update(players, numberPlayers));
        } catch (ClassCastException e) {
            this.players_in_game=players;
            this.players_number=numberPlayers;
            this.owner=players.get(players.size()-1);
            SceneController.changeRootPane(observers,"lobbyPanel.fxml");
            Stage stage = (Stage) SceneController.getActiveScene().getWindow();
            stage.setWidth(814d);
            stage.setHeight(637d);
            //stage.centerOnScreen();
            lobby_ctrl = (LobbyController) SceneController.getActiveController();
            new_ctrl = lobby_ctrl;
            Platform.runLater(() -> new_ctrl.init(players, numberPlayers));
        }
    }

    /**
     * shows to the client an error
     * @param errorMessage the text message error
     */
    @Override
    public void showError(String errorMessage){
        Platform.runLater(() -> SceneController.popUp(ERROR_OCCURRED));
    }

    /**
     * shows to the client a generic message
     * @param genericMessage the text of generic message
     */
    @Override
    public void showGenericMessage(String genericMessage){
        if (genericMessage.equals(INPUT_ERR + ColorCli.RED + "Tiles selected are NOT removable from the board!" + ColorCli.RESET))
            Platform.runLater(() -> SceneController.popUp(NOT_REMOVABLE_TILES));
        if (genericMessage.equals("\nYou're the only player connected... If no one try to reconnect, game will finish. " +
                "\nTimer of 50 seconds starts! If the timer expires you're are the winner of the game!"))
            Platform.runLater(() -> SceneController.popUp(ONLY_ONE));
    }

    /**
     * shows to the client who won the game
     * @param winner the winning player of the game
     */
    @Override
    public void showWinner(String winner){
        boolean win = false;
        final boolean win2;
        if (winner.equals(owner.getNickname())){
            win=true;
        }
        win2=win;
        EndController end_ctrl;
        Stage stage = (Stage) SceneController.getActiveScene().getWindow();
        stage.setWidth(800d);
        stage.setHeight(637d);
        SceneController.changeRootPane(observers,"endPanel.fxml");
        //stage.centerOnScreen();
        end_ctrl = (EndController) SceneController.getActiveController();
        Platform.runLater(() -> end_ctrl.init(win2, score, players_number));
    }

    /**
     * shows to the client the two common goal card of the match
     * @param commonGoalCards the common goal card of the game
     */
    @Override
    public void showCommonCards(List<CommonGoalCard> commonGoalCards){
        GameControllerScene game_ctrl = getGameControllerScene();
        game_ctrl.setCommonGoalCards(commonGoalCards);
        Platform.runLater(game_ctrl::updateCommonGoalCards);
    }

    /**
     * shows to the client his personal goal card
     * @param player of the game
     */
    @Override
    public void showPersonalCard(Player player){
        GameControllerScene game_ctrl = getGameControllerScene();
        Platform.runLater(() -> game_ctrl.updatePersonalCard(player.getPersonalGoalCard()));
    }

    /**
     * shows to the client the board of the game
     * @param board the board of the game
     */
    @Override
    public void showBoard(Board board){
        GameControllerScene game_ctrl = getGameControllerScene();
        game_ctrl.setBoard(board);
        Platform.runLater(game_ctrl::updateBoard);
    }

    /**
     * asks the client to select the tiles from the board
     * @param board the board of the game
     */
    @Override
    public void askSelectTiles(Board board, Bookshelf bookshelf) {
        GameControllerScene game_ctrl = getGameControllerScene();
        game_ctrl.setBoard(board);
        game_ctrl.setShelf(bookshelf);
        Platform.runLater(() -> game_ctrl.activeSelection(true));
    }

    /**
     * asks the client to insert the tiles in the bookshelf
     * @param bookshelf of the player
     * @param tiles to insert in the bookshelf
     */
    @Override
    public void askInsertTiles(Bookshelf bookshelf, List<Tile> tiles){
        GameControllerScene game_ctrl = getGameControllerScene();
        game_ctrl.setShelf(bookshelf);
        game_ctrl.setFinalTiles(tiles);
        Platform.runLater(() -> game_ctrl.activeShelf(true));
    }

    /**
     * shows to the client the bookshelf
     * @param player to show his bookshelf
     */
    @Override
    public void showBookshelf(Player player){
        GameControllerScene game_ctrl = getGameControllerScene();
        game_ctrl.setShelf(player.getBookshelf());
        game_ctrl.setCurrentPlayer(player);
        Platform.runLater(() -> game_ctrl.updateBookShelf(player));
    }

    /**
     * asks the client to select the order of the insertion of the tiles
     * @param tiles to order
     */
    @Override
    public void askOrderTiles(List<Tile> tiles){
        GameControllerScene game_ctrl = getGameControllerScene();
        Platform.runLater(game_ctrl::sendFinalTiles);
    }

    /**
     * shows the stack of the selected common goal card
     * @param commonGoalCardScores is the stack of the available scores
     */
    @Override
    public void showCommonScores(List<CommonGoalCardScore> commonGoalCardScores){
        GameControllerScene game_ctrl = getGameControllerScene();
        game_ctrl.setCommonGoalCardScores(commonGoalCardScores);
        Platform.runLater(game_ctrl::updateScores);
    }

    /**
     * checks if the player has completed a common Goal Card
     * @param commonGoalCard is the common Goal to check
     * @param score is the score relative at the completed common goal
     */
    @Override
    public void showCommonGoalComplete(CommonGoalCard commonGoalCard, int score){
        GameControllerScene game_ctrl = getGameControllerScene();
        Platform.runLater(() -> game_ctrl.winCard(commonGoalCard, score));
    }

    /**
     * shows the final scores of the players
     * @param playerScore map contains players and their scores
     */
    @Override
    public void showScores(Map<String, Integer> playerScore){
        this.score = playerScore;
    }

    /**
     * notify to all the client a player disconnection
     */
    @Override
    public void showDisconnection(String nickname, boolean isStarted){
       Platform.runLater(() -> SceneController.popUpString(nickname + " disconnected from the game!"));
    }

    /**
     * methods that returns the game controller scene
     * @return the controller of the GameControllerScene
     */

    private GameControllerScene getGameControllerScene(){
        GameControllerScene game_ctrl;
        try {
            game_ctrl = (GameControllerScene) SceneController.getActiveController();
        } catch (ClassCastException e) {
            Stage stage = (Stage) SceneController.getActiveScene().getWindow();
            stage.setWidth(1500d);
            stage.setHeight(807d);
            //stage.centerOnScreen();
            SceneController.changeRootPane(observers,"gamePanel.fxml");
            game_ctrl = (GameControllerScene) SceneController.getActiveController();
            game_ctrl.setNumberPlayers(this.players_number);
            game_ctrl.setPlayersList(this.players_in_game);
            game_ctrl.setOwner(this.owner);
            if (this.isFirst)
                game_ctrl.setFirst(true);
            Platform.runLater(game_ctrl::initGame);
        }
        return game_ctrl;
    }

    /**
     * add the message in the buffer
     * @param sender sender of the message
     * @param destination destination of the message
     * @param message message to add
     */
    @Override
    public void addChatMessage(String sender, String destination, String message) {
        final String new_message;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedTime;
        if (destination.equals("all") || destination.equals(owner.getNickname()) || sender.equals(owner.getNickname())){    //this "if" controls if the owner of the gui can visualize the message
            Date now = new Date();
            formattedTime = sdf.format(now);
            if (sender.equals(owner.getNickname()))
                message = " - [" + formattedTime + "] " + sender + ": " + message + " [to " + destination + "]";
            else if (destination.equals(owner.getNickname()))
                message = " - [" + formattedTime + "] " + sender + ": " + message + " [to you]";
            else if (destination.equals("all"))
                message = " - [" + formattedTime + "] " + sender + ": " + message + " [all]";
            new_message=message;
            GameControllerScene game_ctrl = getGameControllerScene();
            Platform.runLater(() -> game_ctrl.addBuffer(new_message));
        }
    }


}
