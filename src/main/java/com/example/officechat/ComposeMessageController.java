package com.example.officechat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ComposeMessageController implements Initializable {

    DatabaseController connectNow = new DatabaseController();
    Connection connectDB = connectNow.getConnection();

    @FXML
    private TextArea messageBody;

    @FXML
    private TextField sendTo;

    @FXML
    private Label SentOrNot;

    @FXML
    private Label MessLength;

    @FXML
    private Button contactPage;

    @FXML
    private ImageView backgroundImg;

    @FXML
    private ChoiceBox<String> statusBar;

     Main m = new Main();
    String sender;

    @FXML
    void messageLength(KeyEvent event) {
        MessLength.setText(String.valueOf(messageBody.getText().length() + "/500"));
    }

    @FXML
    void contactPg(ActionEvent event) throws IOException{
        m.changeScene("contactList.fxml");
    }
    @FXML
    void inboxPg(ActionEvent event) throws IOException{
        m.changeScene("Inbox.fxml");
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        String sql = "update accounts set status = 'Offline' WHERE Email = '"+sender+"';";
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
    void sentPg(ActionEvent event) throws IOException {
        m.changeScene("SentMessages.fxml");
    }
    @FXML
    void sendAttempt(ActionEvent event) throws IOException{

        //display the size of the message in characters
       // MessLength.setText(messageBody.getText().length()+"/25");
        //if the message is greater than the set size let user know
        if(messageBody.getText().length()>500){
            MessLength.setText("Message is too long");
            SentOrNot.setText("Cant Send");
        }
        //else if the user didnt write anything and tries to send let them know to enter a message
        else if (messageBody.getText().length()==0) {
            MessLength.setText("Enter your message");
            SentOrNot.setText("Not Sent");
        }
        //else validate the person you are sending message to and store the message
        else {
            validate(); //validate the person youre emailing exists in the db
            //need to store message in the messages db
        }

    }

    //This method will make sure you are sending the message to someone from your company
    public void validate(){
        String senderExist = "SELECT count(1) FROM accounts WHERE Email = '" + sendTo.getText() + "'";
        String storeMessage = "INSERT INTO messages VALUES ('"+ sender +"','"+sendTo.getText()+"','"+messageBody.getText()+"','0')";
        String sqll = "SELECT count(1) FROM teamgroups WHERE groupName = '"+sendTo.getText()+"'";
        int val =0;

        try{
            Statement statement = connectDB.createStatement();
            Statement stat2 = connectDB.createStatement();
            Statement stat3 = connectDB.createStatement();
            Statement stat4 = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(senderExist);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    val = 1;
                    SentOrNot.setText("Sent");
                }else {
                    ResultSet querys = stat2.executeQuery(sqll);
                    querys.next();
                    if(querys.getInt(1)>1){
                       String sql4 = "SELECT GROUP_CONCAT(members) as members FROM teamgroups where groupName = '"+sendTo.getText()+"'";
                       ResultSet rs4 = stat3.executeQuery(sql4);
                       rs4.next();
                       String str = rs4.getString("members");
                       String [] str1 = str.split(",");

                       for(int i = 0; i<str1.length;i++){
                           String st = "INSERT INTO messages VALUES ('"+ sender +"','"+str1[i]+"','"+messageBody.getText()+"','0')";
                           stat4.executeUpdate(st);
                       }
                        SentOrNot.setText("Sent");
                    }
                }
            }
            if(val == 1)
                statement.executeUpdate(storeMessage);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backgroundImg.setImage(new Image("Background.jpg"));
        try {
            sender = getSenderName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    }

    public void getStatus(ActionEvent event){
        String st = statusBar.getValue();
        if(st == "Online"){
            String sql = "update accounts set status = 'Online' WHERE Email = '"+sender+"';";
            try{
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(sql);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
        else if(st == "Away"){
            String sql = "update accounts set status = 'Away' WHERE Email = '"+sender+"';";
            try{
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(sql);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
        else if(st == "Offline"){
            String sql = "update accounts set status = 'Offline' WHERE Email = '"+sender+"';";
            try{
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(sql);
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
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
    void loadGroups(ActionEvent event) throws IOException {
        m.changeScene("Groups.fxml");
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
