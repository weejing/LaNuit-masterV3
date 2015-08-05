package com.example.asus.lanuit;

import java.util.ArrayList;

/**
 * Created by shihui on 30/6/2015.
 */
public class User {

    private String name;
    private String password;
    private String role;
    private int id;

    public static ArrayList<User> listOfUser = new ArrayList<User>();
    public static int arrayIndicator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String name, String password, String role, int id) {
        super();
        this.name = name;
        this.password = password;
        this.role = role;
        this.id = id;
    }

    public User() {
        super();
    }

    public User(String name, String role) {
        super();
        this.name = name;
        this.role = role;
    }


   /* public static User createUser(String name,String password,String role,int id) {

        User user1 = new User();
        user1.setId(id);
        user1.setName(name);
        user1.setRole(role);
        user1.setPassword(password);

        List messages = new ArrayList();

        for (int i = 0; i < 5; i++) {
            MyObjectMessage objMessage = new MyObjectMessage();
            objMessage.setId_message(i);
            objMessage.setValue(i * 10 + 1);
            objMessage.setText("value");

            messages.add(objMessage);
        }

        myObj.setListMessages(messages);

        return myObj;
    }*/
}
