package app;

import controller.ClientController;
import view.CLI.Cli;
import view.GUI.MainGui;
import javafx.application.Application;
import java.util.Scanner;

/**
 * main of the client application
 */
public class ClientApp {

    /**
     * main method in which the client choose between Cli or GUI view
     * @param args input stream
     */

    public static void main(String[] args) {
        System.out.print("Choose the modality: 'c' for CLI or 'g' for GUI: ");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();

        if(response.equalsIgnoreCase("c")) {
            Cli view = new Cli();
            ClientController clientController = new ClientController(view);
            view.addObserver(clientController);
            view.init();
        }
        else if (response.equalsIgnoreCase("g")) {
            Application.launch(MainGui.class);
        }
        else {
            System.err.println("Invalid argument! Please run the executable again");
        }
    }
}
