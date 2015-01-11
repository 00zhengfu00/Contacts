package com.zhangyan.contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;
import com.zhangyan.contacts.strcut.Attribute;
import com.zhangyan.contacts.strcut.Contacts;
import java.util.ArrayList;

/**
 * Created by ku on 2015/1/10.
 */
public class ContactHandler  {
    private Context mContext;
    public ContactHandler(Context mContext) {
        this.mContext = mContext;
    }
    /**
     * 获取联系人信息
     *
     * */
    public ArrayList<Contacts> getContactsFromPhone () {
        ArrayList<Contacts> contactses = new ArrayList<Contacts>();
        /* 获取所有联系人的信息 */
        Cursor cur = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Toast.makeText(mContext, cur.getCount() + "", Toast.LENGTH_SHORT).show();
        /* 遍历所有联系人 */
        while(cur.moveToNext()) {
            /* 新家一个联系人 */
            Contacts contact = new Contacts();
            /* 获取联系人id */
            contact.setId(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)));
            /* 获取联系人姓名 */
            contact.setName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            /* -----------------------获取联系人电话码号--------------------------------- */
            /* 判断有没有电话号码 */
            if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                /* 有电话号码 */
                Cursor phonesCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contact.getId(), null, null);
                while(phonesCursor.moveToNext()){
                    /* 获取联系人电话及电话类型 */
                    contact.getPhone().add(
                            new Attribute(
                                    /* 获取电话号码 */
                                    phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                                    /* 获取号码类型 */
                                    phonesCursor.getInt(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                            )
                    );
                }
            }
            /* --------------------------获取联系人邮件------------------------------- */
            Cursor emailCur = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contact.getId(), null, null);
            /* 遍历所有邮件 */
            while(emailCur.moveToNext()){
                /* 获取邮件信息 */
                contact.getEmail().add(
                        new Attribute(
                             /* 获取邮件 */
                                emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1)),
                             /* 获取邮件的类型 */
                                emailCur.getInt(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))
                        )
                );
            }
        contactses.add(contact);
        }
    return contactses;
    }

}
