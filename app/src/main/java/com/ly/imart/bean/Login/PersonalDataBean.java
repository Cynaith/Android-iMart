package com.ly.imart.bean.Login;

public class PersonalDataBean {
    private String username;
    private String mail;
    private int age;
    private String address;

    public PersonalDataBean() {
    }

    public PersonalDataBean(String username, String mail, int age, String address) {
        this.username = username;
        this.mail = mail;
        this.age = age;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNameAndMailAndAge(String username,String mail,int age){
        setUsername(username);
        setMail(mail);
        setAge(age);
    }
}
