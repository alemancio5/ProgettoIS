package network.message.serverSide;

import model.CommonGoalCardScore;
import network.message.Message;
import network.message.MessageType;

import java.io.Serial;
import java.util.List;

public class ShowCommonScoresMessage extends Message {
    @Serial
    private final static long serialVersionUID = -4408464676916081390L;
    private final List<CommonGoalCardScore> commonGoalCardScores;

    public ShowCommonScoresMessage(List<CommonGoalCardScore> commonGoalCardScores) {
        super("SERVER", MessageType.COMMON_SCORES);
        this.commonGoalCardScores = commonGoalCardScores;
    }

    public List<CommonGoalCardScore> getCommonGoalCardScores() {
        return commonGoalCardScores;
    }

    @Override
    public String toString() {
        return "ShowCommonScoresMessage{}";
    }
}
