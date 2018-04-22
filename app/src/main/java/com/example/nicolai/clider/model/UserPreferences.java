package com.example.nicolai.clider.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nicolai on 09/04/2018.
 */

public class UserPreferences {

    public UserPreferences(int age, String sex, ArrayList<String> tags) {
        this.age = age;
        this.sex = sex;
        this.tags = tags;
    }



    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int age;
    public String sex;
    public ArrayList<String>tags;


   /* public HashMap<String, Boolean> clothesPreferences;

    public HashMap<String, Boolean> getClothesPreferences() {
        return clothesPreferences;
    }

    public void setClothesPreferences(HashMap<String, Boolean> clothesPreferences) {
        this.clothesPreferences = clothesPreferences;
    }*/



    public UserPreferences() {
    }

 /*   public UserPreferences(String sex, int age, HashMap<String, Boolean> clothesPreferences) {
        this.age = age;
        this.sex = sex;
        this.clothesPreferences = clothesPreferences;
    }*/
}
