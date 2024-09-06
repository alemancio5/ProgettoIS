package network.message.serverSide;

import model.CommonGoalCard;
import network.message.Message;
import network.message.MessageType;

import java.io.Serial;

/**
 * message to notify that a player completed the common goal card
 */
public class CommonGoalCompleteMessage extends Message {
    @Serial
    private static final long serialVersionUID = 5506828047860036031L;
    private final CommonGoalCard commonGoal;
    private final int commonGoalScore;

    public CommonGoalCompleteMessage(CommonGoalCard commonGoal, int commonGoalScore) {
        super("SERVER", MessageType.COMMON_GOAL_COMPLETE);
        this.commonGoal = commonGoal;
        this.commonGoalScore = commonGoalScore;
    }

    public CommonGoalCard getCommonGoal() {
        return commonGoal;
    }

    public int getCommonGoalScore() { return commonGoalScore;
    }

    @Override
    public String toString() {
        return "CommonGoalCompleteMessage{" +
                "commonGoal=" + commonGoal.toString() +
                ", commonGoalScore=" + commonGoalScore +
                '}';
    }
}
