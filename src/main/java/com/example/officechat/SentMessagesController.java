package com.example.officechat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SentMessagesController implements Initializable {
    Main m = new Main();
    String user;
    DatabaseController connectNow = new DatabaseController();
    Connection connectDB = connectNow.getConnection();
    ObservableList<TableSetterGetter> list = FXCollections.observableArrayList();
    @FXML
    private TableColumn<TableSetterGetter, String> message;
    @FXML
    private TableColumn<TableSetterGetter, String> reciever;
    @FXML
    private TableView<TableSetterGetter> tableView;

    @FXML
    private ImageView backgroundImg;
    @FXML
    private ChoiceBox<String> statusBar;

    @FXML
    void mainPg(ActionEvent event) throws IOException {
        m.changeScene("Inbox.fxml");
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
        String sql = "SELECT * from messages where sender = '"+user +"'";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(sql);

            while(queryResult.next()){
                CheckBox ch = new CheckBox("");
                list.add(new TableSetterGetter(queryResult.getString("reciever"), queryResult.getString("messages")));

                tableView.setItems(list);
                reciever.setCellValueFactory(new PropertyValueFactory<TableSetterGetter,String>("sender"));
                message.setCellValueFactory(new PropertyValueFactory<TableSetterGetter,String>("message"));

            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
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



}
