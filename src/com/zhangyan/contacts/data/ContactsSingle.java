package com.zhangyan.contacts.data;

import com.zhangyan.contacts.strcut.Contacts;

import java.util.ArrayList;

/**
 * Created by ku on 2015/1/19.
 */
public class ContactsSingle {
    private ArrayList<Contacts> contacts;
    private int count;
    private static ContactsSingle single = null;
    public static synchronized ContactsSingle getInstance(){
        if(single == null) {
            single = new ContactsSingle();
        }
        return single;
    }
    private ContactsSingle(){
        contacts = new ArrayList<Contacts>();
        setCount(0);
    }
    public void add(Contacts contacts){
        this.contacts.add(contacts);
        count ++ ;
    }
    public void clear(){
        this.contacts.clear();
    }
    public ArrayList<Contacts> getContacts() {
        return contacts;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
