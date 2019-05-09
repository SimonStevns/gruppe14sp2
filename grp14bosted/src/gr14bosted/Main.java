package gr14bosted;

import Domain.Facade;
import Domain.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        try {
            showLogin();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static void showLogin() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("FXML.fxml"));
        AnchorPane layout = loader.load();
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void showMain(Facade f) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("MainFXML.fxml"));
        AnchorPane layout = loader.load();
        
        MainFXMLController mainController = loader.getController();
        mainController.setFacade(f);
        
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void showAdmin(Facade f) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("admin.fxml"));
        TabPane layout = loader.load();
        
        AdminController ac = loader.getController();
        ac.setFacade(f);
        
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();    
    }
    
    

}
