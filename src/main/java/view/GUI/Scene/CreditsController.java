package view.GUI.Scene;

import observer.ViewObservable;
import view.GUI.SceneController;
import javafx.scene.input.MouseEvent;

/**
 * controller of the credits panel
 * @author Stefano Morano
 */
public class CreditsController extends ViewObservable implements Controller{
    /**
     * method called when the back button is pressed: it returns the menu
     * @param mouseEvent when the mouse is clicked
     */
    @SuppressWarnings("unused")
    public void backPressed(MouseEvent mouseEvent) {
        SceneController.changeRootPane(observers,"menuPanel.fxml");
    }
}
