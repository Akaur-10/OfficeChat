package com.example.officechat;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    private Button login;

    @FXML
    private PasswordField password;

    @FXML
    public  TextField username;

    @FXML
    private Label wrongAttempt;

    @FXML
    private ImageView logo;

    Main m = new Main();

    @FXML
    void loginAttempt(ActionEvent event) throws IOException {
        if(username.getText().isBlank() == false && password.getText().isBlank() == false){
            validateLogin();
        }
        else if (username.getText().isEmpty() && password.getText().isEmpty()) {
            wrongAttempt.setText("Enter your login credentials");
        }
        else{
            wrongAttempt.setText("Incorrect");
        }
    }

    public void validateLogin(){
        DatabaseController connectNow = new DatabaseController();
        Connection connectDB = connectNow.getConnection();
        String verifyLogin = "SELECT count(1) FROM accounts WHERE Email = '" + username.getText() + "' AND Pass = '" + password.getText() +"'";
        String setStatus = "update accounts set status = 'Online' WHERE Email = '"+username.getText()+"';";
        try{
            Statement statement = connectDB.createStatement();
            Statement stat2 = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    storeName();                            //store the logged in user into a txt file
                    stat2.executeUpdate(setStatus);
                    m.changeScene("Inbox.fxml");
                }else {
                wrongAttempt.setText("invalid login");
                }
            }
            checkNotif();                           //start background checks once user logged in
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    private void checkNotif() {
        NotificationRunner notif = new NotificationRunner();
        notif.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                alert.setTitle("Notification");
                alert.setContentText("New Message!");
                alert.show();
            }
        });
        Thread th = new Thread(notif);
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logo.setImage(new Image("Logo.png"));
    }

    public void storeName() throws IOException {
        FileWriter myWriter = new FileWriter("loggedIn.txt");
        myWriter.write(username.getText());
        myWriter.close();
    }

}

