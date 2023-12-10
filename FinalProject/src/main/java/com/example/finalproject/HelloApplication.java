package com.example.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Dungeon Mania");
        stage.setScene(scene);
        stage.show();
    }
//Got stop() function from here https://stackoverflow.com/questions/37819508/how-to-do-something-when-my-javafx-close
    @Override
    public void stop() {
        int iterate = 0;
        while(iterate <= 100){
            File gold = new File("gold" + iterate + ".txt");
            File room = new File("room" + iterate + ".txt");
            File monster = new File("monster" + iterate + ".txt");
            if (gold.exists()){
                gold.delete();
            }
            if (room.exists()){
                room.delete();
            }
            if (monster.exists()){
                monster.delete();
            }
            iterate++;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}