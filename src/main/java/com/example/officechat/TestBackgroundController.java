package com.example.officechat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestBackgroundController implements Initializable {

    @FXML
    private ImageView backgroundImg;
    Main m = new Main();


    @FXML
    void goCompose(ActionEvent event) {

    }

    @FXML
    void goInbox(ActionEvent event) {

    }

    @FXML
    void goSent(ActionEvent event) throws IOException {
        m.changeScene("SentMessages.fxml");
    }

    @FXML
    void logout(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       backgroundImg.setImage(new Image("Background.jpg"));
    }
}
