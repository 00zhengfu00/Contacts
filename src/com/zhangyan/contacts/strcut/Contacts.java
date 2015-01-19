package com.zhangyan.contacts.strcut;

import java.util.ArrayList;

/**
 * Created by ku on 2015/1/10.
 */
public class Contacts {

    /* 联系人id */
    private String id;
    /* 联系人姓名 */
    private String name ;
    /* 联系人头像 */
//    private Photo photo;
    /* 联系人电话信息 */
    private int phoneId;
    private ArrayList<Attribute> phone;
    /* 联系人邮件信息 */
    private ArrayList<Attribute> email;
    /* 联系人地址 */
    private ArrayList<Attribute> address;
    /* 联系人分组 */
    private String group;

    public Contacts () {
        setName("");
        setPhone(new ArrayList<Attribute>());
//        setPhoto(new Photo());
        setEmail(new ArrayList<Attribute>());
        setAddress(new ArrayList<Attribute>());
        setGroup("");
        setPhoneId(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<Attribute> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<Attribute> phone) {
        this.phone = phone;
    }

    public ArrayList<Attribute> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<Attribute> email) {
        this.email = email;
    }

    public ArrayList<Attribute> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Attribute> address) {
        this.address = address;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Photo getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(Photo photo) {
//        this.photo = photo;
//    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }
}
