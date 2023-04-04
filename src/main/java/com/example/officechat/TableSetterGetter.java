package com.example.officechat;

import javafx.scene.control.CheckBox;

public class TableSetterGetter {

    String sender;
    String message;
    CheckBox checkBox;

    public TableSetterGetter(String sender, String message, CheckBox checkBox) {
        this.sender = sender;
        this.message = message;
        this.checkBox = checkBox;
    }

    public  TableSetterGetter(String sender, String message){
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }




}
