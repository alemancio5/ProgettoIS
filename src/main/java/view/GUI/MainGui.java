package view.GUI;

import controller.ClientController;
import view.GUI.Scene.GameControllerScene;
import view.GUI.Scene.MenuController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * main of the Gui application
 * @author Stefano Morano
 */
public class MainGui extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * method that starts the gui
     * @param stage the stage of the gui
     * @throws Exception if the panel cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {

        Gui view = new Gui();
        ClientController clientController = new ClientController(view);
        view.addObserver(clientController);
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/fxml/menuPanel.fxml"));
        Parent rootLayout = loader.load();
        MenuController controller = loader.getController();
        controller.addObserver(clientController);

        Scene scene = new Scene(rootLayout);
        SceneController.setActiveScene(scene);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setTitle("My Shelfie");
        stage.getIcons().add(new Image("/item tiles/Gatti1.1.png"));
        //stage.centerOnScreen();
        stage.show();

    }

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}
