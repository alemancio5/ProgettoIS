package observer;

import network.message.Message;

/**
 * observer interface that supports the method update
 */
public interface Observer {

    /**
     * called by the observable, update the observer
     * @param message for updating
     */
    void update(Message message);
}