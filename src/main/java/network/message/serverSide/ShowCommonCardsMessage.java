package network.message.serverSide;

import model.CommonGoalCard;
import network.message.Message;
import network.message.MessageType;

import java.io.Serial;
import java.util.List;

/**
 * message used to show the common goal cards of the game
 */
public class ShowCommonCardsMessage extends Message {
    @Serial
    private static final long serialVersionUID = 4574676858326174828L;
    private final List<CommonGoalCard> commonGoalCards;

    public ShowCommonCardsMessage(List<CommonGoalCard> commonGoalCards) {
        super("SERVER", MessageType.SHOW_COMMON);
        this.commonGoalCards = commonGoalCards;
    }

    public List<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    @Override
    public String toString() {
        return "ShowCommonCards{" +
                "commonGoalCards=" + commonGoalCards.toString() +
                '}';
    }
}
