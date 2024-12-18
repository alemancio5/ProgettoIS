package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * abstract class that defines a common goal card
 * @author Alessandro Mancini
 */

public abstract class CommonGoalCard implements Serializable {
    private int number;
    @Serial
    private static final long serialVersionUID = 8745934719317835737L;

    public abstract boolean check(Bookshelf bookshelf);

    public int getNumber(){
        return number;
    }
}
