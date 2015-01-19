package com.zhangyan.contacts.data;

import com.zhangyan.contacts.strcut.Contacts;

/**
 * Created by ku on 2015/1/19.
 */
public class ContactsSingle {
    private Contacts contacts;
    private static ContactsSingle single = null;
    public static synchronized ContactsSingle getInstance(){
        if(single == null) {
            single = new ContactsSingle();
        }
        return single;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }
}
