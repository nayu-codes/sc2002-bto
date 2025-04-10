package bto.database;

import bto.model.user.User;

import java.util.HashMap;

public class UserDB {
    private HashMap<String, User> userList;

    public UserDB() {
        userList = new HashMap<>();
    }
    public void addUser(User user) {
        userList.put(user.getUserId(), user);
    }
    public User getUser(String userId) {
        return userList.get(userId);
    }
    public void removeUser(String userId) {
        userList.remove(userId);
    }
    public boolean userExists(String userId) {
        return userList.containsKey(userId);
    }
    public HashMap<String, User> getUserList() {
        return userList;
    }
    public void setUserList(HashMap<String, User> userList) {
        this.userList = userList;
    }
    public void clearUserList() {
        userList.clear();
    }
    public int getUserCount() {
        return userList.size();
    }
    public boolean isEmpty() {
        return userList.isEmpty();
    }
    public void printUserList() {
        for (User user : userList.values()) {
            System.out.println(user);
        }
    }
    public void updateUser(User user) {
        if (userList.containsKey(user.getUserId())) {
            userList.put(user.getUserId(), user);
        } else {
            System.out.println("User not found in the database.");
        }
    }
}
