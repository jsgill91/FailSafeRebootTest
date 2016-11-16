package com.example.autumn.failsafereboot;

import java.io.Serializable;

/**
 * Created by Autumn on 10/4/2016.
 */
public class FailSafe implements Serializable{
    private User user = new User();

    public FailSafe(){}

    public boolean check_credentials(String username, String password){
        if(username.equals(user.get_username()) && password.equals(user.get_password())){
            return true;
        }
        return false;
    }

    public void set_user(String uName, String pWord){
        user = new User(uName, pWord);
    }

    public void set_username(String new_username){
        user.set_username(new_username);
    }
    
    public void set_password(String new_password){
        user.set_password(new_password);
    }

    public void add_class(Gradebook newClass){
        user.add_class(newClass);
    }
}
