package com.example.nicolai.clider.model;

/**
 * Created by Nicolai on 09/04/2018.
 */

public class UserPreferences {
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

    public int age;
    public String sex;

    public UserPreferences() {
    }

    public UserPreferences(String sex) {
        //this.age = age;
        this.sex = sex;
    }
}
