package com.wdk.sports.domain;

import lombok.*;

import java.io.Serializable;
public class User implements Serializable {

    private int sex; // 性别
    private int age; // 年龄
    private int height; // 身高
    private int weight; // 体重
    private int waistline; // 腰围

    private String error;

    public User(int sex, int age, int height, int weight, int waistline,String error) {
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.waistline = waistline;
        this.error = error;
    }

    public User(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "User{" +
                "sex=" + sex +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", waistline=" + waistline +
                ", error='" + error + '\'' +
                '}';
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWaistline() {
        return waistline;
    }

    public void setWaistline(int waistline) {
        this.waistline = waistline;
    }

}
