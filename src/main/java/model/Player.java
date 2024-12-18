package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * class that defines a player
 * @author Alessandro Mancini
 */
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = -7872881646495796556L;
    private Bookshelf bookshelf;
    private int score;
    private final String nickname;
    private PersonalGoalCard personalGoalCard;
    private boolean isDoneFirstCommon;
    private boolean isDoneSecondCommon;
    private boolean isDisconnected;
    private boolean isBlockedFirstCommon;
    private boolean isBlockedSecondCommon;


    public Player(String nickname) {
        this.bookshelf = new Bookshelf();
        this.nickname = nickname;
        this.score = 0;
        this.isDoneFirstCommon = false;
        this.isDoneSecondCommon = false;
        this.isDisconnected = false;
        this.isBlockedFirstCommon=false;
        this.isBlockedSecondCommon=false;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public int getScore() {
        return score;
    }

    public void setBookshelf(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setBlockedFirstCommon(boolean blockedFirstCommon) {
        isBlockedFirstCommon = blockedFirstCommon;
    }

    public void setBlockedSecondCommon(boolean blockedSecondCommon) {
        isBlockedSecondCommon = blockedSecondCommon;
    }

    public boolean isBlockedFirstCommon() {
        return isBlockedFirstCommon;
    }

    public boolean isBlockedSecondCommon() {
        return isBlockedSecondCommon;
    }

    public String getNickname() {
        return nickname;
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }

    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    public boolean isDoneFirstCommon() {
        return isDoneFirstCommon;
    }

    public void setDoneFirstCommon(boolean doneFirstCommon) {
        isDoneFirstCommon = doneFirstCommon;
    }

    public boolean isDoneSecondCommon() {
        return isDoneSecondCommon;
    }

    public void setDoneSecondCommon(boolean doneSecondCommon) {
        isDoneSecondCommon = doneSecondCommon;
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    public void setDisconnected(boolean disconnected) {
        isDisconnected = disconnected;
    }

    @Override
    public String toString() {
        return "Player{" +
                "score=" + score +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
