package com.example.officechat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

public class GroupsController implements Initializable {
    Main m = new Main();
    ObservableList<TableSetterGetter> list = FXCollections.observableArrayList();
    String user;
    DatabaseController connectNow = new DatabaseController();
    Connection connectDB = connectNow.getConnection();

    @FXML
    private ImageView backgroundImg;

    @FXML
    private ChoiceBox<String> statusBar;

    @FXML
    private TableColumn<TableSetterGetter, String> groupName;

    @FXML
    private TableColumn<TableSetterGetter, String> members;

    @FXML
    private TableView<TableSetterGetter> tableView;

    @FXML
    void createGroup(ActionEvent event) throws IOException {
        m.changeScene("CreateGroup.fxml");
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
        String st = "DROP VIEW allgroups;";
        String stet = "DROP VIEW temp1;";
        String s = "CREATE VIEW allGroups AS SELECT groupName, GROUP_CONCAT( ' ',members) as members FROM teamgroups GROUP BY groupName;";
        String sq = "CREATE VIEW temp1 AS SELECT groupName AS gn FROM teamgroups where members ='"+user+"';";
        String sql = "SELECT groupName, members FROM allgroups INNER JOIN temp1 ON temp1.gn = allgroups.groupName;";

        try{
            Statement statement = connectDB.createStatement();
            Statement ss = connectDB.createStatement();
            Statement sss = connectDB.createStatement();
            Statement stt = connectDB.createStatement();
            Statement sttt = connectDB.createStatement();
            stt.executeUpdate(st);
            sttt.executeUpdate(stet);
            ss.executeUpdate(s);
            sss.executeUpdate(sq);
            ResultSet queryResult = statement.executeQuery(sql);

            while(queryResult.next()){
                list.add(new TableSetterGetter(queryResult.getString("groupName"), queryResult.getString("members")));
                tableView.setItems(list);
                groupName.setCellValueFactory(new PropertyValueFactory<TableSetterGetter,String>("sender"));
                members.setCellValueFactory(new PropertyValueFactory<TableSetterGetter,String>("message"));
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
}
