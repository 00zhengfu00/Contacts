package com.zhangyan.contacts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import com.zhangyan.contacts.data.ContactsSingle;
import com.zhangyan.contacts.strcut.Attribute;
import com.zhangyan.contacts.strcut.Contacts;

import java.util.ArrayList;

/**
 * Created by ku on 2015/1/10.
 */
public class ContactsOperate {
    /**
     * 获取联系人信息
     *
     * */
    public static ArrayList<Contacts> getContactsFromPhone (Context mContext, Handler handler) {
        ArrayList<Contacts> contactses = new ArrayList<Contacts>();
        /* 获取所有联系人的信息 */
        Cursor cur = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        /* 遍历所有联系人 */
        while(cur.moveToNext()) {
            /* 新家一个联系人实例 */
            Contacts contact = new Contacts();
            /* 获取联系人id */
            contact.setId(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)));
            /* 获取联系人姓名 */
            contact.setName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
//            /* 获取头像id */
//            Cursor photoCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Photo.CONTACT_ID + "=" + contact.getId(), null, null);
            /* -----------------------获取联系人电话码号--------------------------------- */
            /* 判断有没有电话号码 */
            contact.setPhoneId(cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (contact.getPhoneId() > 0) {
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
                phonesCursor.close();
            }
//            /* --------------------------获取联系人邮件------------------------------- */
//            Cursor emailCur = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contact.getId(), null, null);
//            /* 遍历所有邮件 */
//            while(emailCur.moveToNext()){
//                /* 获取邮件信息 */
//                contact.getEmail().add(
//                        new Attribute(
//                             /* 获取邮件 */
//                                emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1)),
//                             /* 获取邮件的类型 */
//                                emailCur.getInt(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))
//                        )
//                );
//            }
        contactses.add(contact);
        ContactsSingle.getInstance().setContacts(contact);
        Constans.sendMessage(Constans.AUPDATA_LIST, handler);
        }
    return contactses;
    }

    public static void saveContacts(Context mContext, ArrayList<Contacts> contactses, Handler handler){
        for(Contacts contacts : contactses) {
            ContentValues contentValues = new ContentValues();
            //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
            Uri rawContactUri = mContext.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
            long rawContactId = ContentUris.parseId(rawContactUri);
            /* 存入联系人的姓名 */
            contentValues.clear();
            contentValues.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
            contentValues.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contacts.getName());
            mContext.getContentResolver().insert(
                    android.provider.ContactsContract.Data.CONTENT_URI, contentValues);
            /* 存入联系人电话 */
            /* 判断该联系人是否有电话 */
            if (contacts.getPhoneId() > 0) { //
                for (Attribute phone : contacts.getPhone()) {
                    contentValues.clear();
                    contentValues.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                    contentValues.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    // 设置录入联系人电话信息
                    contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getValue());
                    contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phone.getType());
                    // 往data表入电话数据
                    mContext.getContentResolver().insert(
                            android.provider.ContactsContract.Data.CONTENT_URI, contentValues);
                }
            }

            Constans.sendMessage(Constans.PROGRESS_INC, handler);
        }
        Constans.sendMessage(Constans.PROGRESS_DISMISS, handler);
    }
}
