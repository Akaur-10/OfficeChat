package com.example.officechat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CreateGroupController implements Initializable {
    Main m = new Main();

    String user;
    DatabaseController connectNow = new DatabaseController();
    Connection connectDB = connectNow.getConnection();
    @FXML
    private ImageView backgroundImg;

    @FXML
    private TextField groupMembers;

    @FXML
    private TextField groupName;
    @FXML
    private Label Label;
    @FXML
    private ChoiceBox<String> statusBar;

    @FXML
    void createGroup(ActionEvent event) throws IOException {
        String str = groupMembers.getText();
        String [] splitMembers = str.split(",");

        for(int i=0;i<splitMembers.length;i++) {
            String sql = "Insert into teamgroups value ('" + groupName.getText() + "','" + splitMembers[i] + "');";
            try {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(sql);
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        Label.setText("Group Created");
    }

    @FXML
    void loadArchive(ActionEvent event) throws IOException {
        m.changeScene("Archive.fxml");
    }

    @FXML
    void loadCompose(ActionEvent event) throws IOException {
        m.changeScene("ComposeMessage.fxml");
    }

    @FXML
    void loadContacts(ActionEvent event) throws IOException {
        m.changeScene("contactList.fxml");
    }

    @FXML
    void loadGroups(ActionEvent event) throws IOException {
        m.changeScene("Groups.fxml");
    }

    @FXML
    void loadInbox(ActionEvent event) throws IOException {
        m.changeScene("Inbox.fxml");
    }

    @FXML
    void loadLogout(ActionEvent event) throws IOException {
        String sql = "update accounts set status = 'Offline' WHERE Email = '"+user+"';";
        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        m.changeScene("Login.fxml");
    }

    @FXML
    void loadSent(ActionEvent event) throws IOException {
        m.changeScene("SentMessages.fxml");
    }

    public void getStatus(ActionEvent event){
        String st = statusBar.getValue();
        if(st == "Online"){
            String sql = "update accounts set status = 'Online' WHERE Email = '"+user+"';";
            try{
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(sql);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
        else if(st == "Away"){
            String sql = "update accounts set status = 'Away' WHERE Email = '"+user+"';";
            try{
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(sql);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
        else if(st == "Offline"){
            String sql = "update accounts set status = 'Offline' WHERE Email = '"+user+"';";
            try{
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(sql);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backgroundImg.setImage(new Image("Background.jpg"));
        statusBar.getItems().addAll(null,"Online","Away","Offline");
        statusBar.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String s) {
                return (s == null)? "Status" : s;
            }

            @Override
            public String fromString(String s) {
                return null;
            }
        });
        statusBar.setOnAction(this::getStatus);
        try {
            user = getSenderName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSenderName() throws IOException {
        String data = "";
        try {
            File myObj = new File("loggedIn.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }
}
