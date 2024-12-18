package view.GUI.Scene;

import observer.ViewObservable;
import view.GUI.ErrorType;
import view.GUI.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * controller of the name panel
 * @author Stefano Morano
 */
public class NameController extends ViewObservable implements Controller {
    @FXML
    TextField usernameField;
    @FXML
    ImageView continueButton;

    private boolean isActive;

    public void initialize(){
        isActive=true;
    }

    /**
     * method called when the continue button is pressed: the name is sent to the server
     * @param mouseEvent the mouse event
     */
    @SuppressWarnings("unused")
    public void continueButtonPressed(MouseEvent mouseEvent) {
        if (isActive) {
            String Nickname = usernameField.getText();
            if (Nickname.compareTo("") == 0)
                SceneController.popUp(ErrorType.EMPTY_NICKNAME);
            else new Thread(() -> notifyObserver(obs -> obs.sendNickname(Nickname))).start();
            isActive = false;
        }
    }
}
