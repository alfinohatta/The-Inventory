package com.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.inventory.util.ConfigManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize configuration
        ConfigManager.initialize();
        
        // Load main FXML
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
        primaryStage.setTitle("Inventory Management System - Supabase");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);

        // Ensure JavaFX runtime components are loaded
        System.setProperty("javafx.runtime.components", "true");

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Cleanup if needed
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}