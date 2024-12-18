package view.GUI.Scene;

import model.Player;
import observer.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.List;


/**
 * controller of the lobby panel, where the players wait for the game to start
 * @author Stefano Morano
 */
public class LobbyController extends ViewObservable implements Controller {
    @FXML
    ImageView pl1, pl2, pl3, pl4, white1, white2, white3, white4;
    @FXML
    Text pl1Text, pl2Text, pl3Text, pl4Text, number;

    public void init(List<Player> players, int num_players){
        switch (num_players){
            case 2 -> {
                pl3.setOpacity(0);
                pl4.setOpacity(0);
                white3.setOpacity(0);
                white4.setOpacity(0);
            }
            case 3 -> {
                pl4.setOpacity(0);
                white4.setOpacity(0);
            }
            case 4 -> {}
        }
        pl1Text.setText("");
        pl2Text.setText("");
        pl3Text.setText("");
        pl4Text.setText("");
        update(players, num_players);
    }

    /**
     * this method is called by the Gui to update the lobby panel
     * @param players the list of the players in the lobby
     * @param num_players the number of players in the game
     */
    public void update(List<Player> players, int num_players) {
        for (int x=1; x<=players.size(); x++)
            switch (x) {
                case 1 -> pl1Text.setText(players.get(0).getNickname());
                case 2 -> pl2Text.setText(players.get(1).getNickname());
                case 3 -> pl3Text.setText(players.get(2).getNickname());
                case 4 -> pl4Text.setText(players.get(3).getNickname());
            }
        number.setText(players.size() + "/" + num_players);
    }
}
