package view.GUI.Scene;

import controller.ClientController;
import observer.ViewObservable;
import view.GUI.ErrorType;
import view.GUI.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * controller of the connection panel for the client connection
 * @author Stefano Morano
 */
public class ConnectionController extends ViewObservable implements Controller {

    @FXML
    TextField addressField;
    @FXML
    TextField serverPortField;
    @FXML
    ComboBox<String> serverType;
    private int type;
    int error;

    public void initialize(){
        ObservableList<String> parameters = FXCollections.observableArrayList();
        parameters.add("Socket");
        parameters.add("RMI");
        serverType.setItems(parameters);
        serverType.setValue("Socket");
    }

    /**
     * method called when the continue button is pressed: the client try to connect to the server
     * @param mouseEvent when the mouse is clicked
     */
    @SuppressWarnings("unused")
    public void continue_pressed(MouseEvent mouseEvent) {
        String address;
        String port;
        String defaultPort_socket = "12345";
        String defaultPort_rmi = "1099";
        address = addressField.getText();
        port = serverPortField.getText();
        final String correctAddress;
        final String correctPort;

            error = 0;
            if (port.compareTo("") == 0) {
                if (serverType.getValue().equals("Socket")){
                    type=1;
                    correctPort = defaultPort_socket;
                } else {
                    correctPort = defaultPort_rmi;
                    type=2;
                }

            } else if (ClientController.isValidPort(port)) {
                correctPort = port;

            } else {
                error = 1;
                correctPort = null;
            }

            if (address.compareTo("") == 0)
                correctAddress = "localhost";
            else if (ClientController.isValidAddress(address)) {
                correctAddress = address;
            } else {
                correctAddress = null;
                if (error == 0)
                    error = 2;
                else error = 3;
            }

            switch (error) {
                case 0 -> new Thread(() -> notifyObserver(obs -> obs.createConnection(correctAddress, correctPort, type))).start();
                case 1 -> SceneController.popUp(ErrorType.WRONG_PORT);
                case 2 -> SceneController.popUp(ErrorType.WRONG_ADDRESS);
                case 3 -> SceneController.popUp(ErrorType.WRONG_PORT_ADDRESS);
            }
    }
}
