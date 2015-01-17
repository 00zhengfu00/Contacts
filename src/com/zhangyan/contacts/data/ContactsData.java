package com.zhangyan.contacts.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.zhangyan.contacts.Constans;
import com.zhangyan.contacts.strcut.Attribute;
import com.zhangyan.contacts.strcut.Contacts;

import java.io.File;
import java.io.IOException;

/**
 * Created by ku on 2015/1/12.
 */
public class ContactsData {
    /* 联系人表 */
    public static final String CONTACT = "contacts"; // 表名
    public static final String CONTACT_NAME = "cname"; // 联系人姓名
    public static final String CONTACT_PHONE_ID_INDEX = "cpid"; // 联系人电话id
    /* 联系人电话表 */
    public static final String CONTACT_PHONE = "phone"; // 联系人电话表名
    private static final String CONTACT_ID = "cid"; // 联系人id
    private static final String CONTACT_PHONE_TYPE = "ptype"; // 电话类型
    private static final String CONTACT_PHONE_NUMBER = "pnumber"; // 电话号码
    /* sdcard */
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory() + File.separator;
    public static final String CONTACTS_HELPER = "Contacts" + File.separator; //一级目录
    public static final String CONTACTS_BACKUP = "backup" + File.separator;

    private SQLiteDatabase db; //
    private Context mContext;
    private String dbName = "contacts.db"; // 数据库名称
    private File dbFile;

    public ContactsData(Context mContext) {
        this.mContext = mContext;
        dbFile = new File(SDCARD_PATH + CONTACTS_HELPER + CONTACTS_BACKUP + dbName);
    }

    public void openDb() {
        if(createPath()) {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            createTable();
        }
    }

    public Boolean checkIsImport() {
        Boolean check = true;
        Cursor c = db.rawQuery("SELECT count(*) FROM sqlite_sequence where name = ?", //
                new String[]{CONTACT});
        if( c.moveToNext() ){
            if ( c.getInt(0) > 0) {
                check = false;
            }
        }
        c.close();
        return check;
    }

    private void createTable() {
        createContactTable();
        createPhoneTable();
    }

    /**
     * 创建联系人表
     */
    private void createContactTable() {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("CREATE TABLE if not exists [" + CONTACT + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append(CONTACT_NAME + " TEXT,");
        sBuffer.append(CONTACT_PHONE_ID_INDEX + " INTEGER)");
        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
        Constans.showToast(mContext, "创建联系人");
    }

    /**
     * 创建电话号码表
     */
    private void createPhoneTable() {
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE if not exists [" + CONTACT_PHONE + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append(CONTACT_ID + " INTEGER,");
        sBuffer.append(CONTACT_PHONE_TYPE + " INTEGER,");
        sBuffer.append(CONTACT_PHONE_NUMBER + " TEXT)");
        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
    }

    public void addContacts(Contacts contacts, int ContactIndex) {
        db.beginTransaction();
        try {
                db.execSQL("INSERT INTO " + CONTACT + " VALUES(null, ?, ?) ",
                        new Object[]{
                                contacts.getName(),
                                contacts.getPhoneId()
                        });
                if (contacts.getPhoneId() > 0) {
                    for (Attribute phone : contacts.getPhone()) {
                        db.execSQL("INSERT INTO " + CONTACT_PHONE + " VALUES(null, ?, ?, ?)",
                                new Object[]{
                                        ContactIndex,
                                        phone.getType(),
                                        phone.getValue()
                                });
                    }
                }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 创建数据库在sd卡上的目录
     */
    public Boolean createPath() {

             /* 创建一级目录 */
        File folder = new File(SDCARD_PATH + CONTACTS_HELPER);
        if (!folder.exists()) {
            folder.mkdir();
        }
                /* 创建二级目录 */
        folder = new File(SDCARD_PATH + CONTACTS_HELPER + CONTACTS_BACKUP);
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Constans.showToast(mContext, "创建文件失败!");
                return false;
            }
        }
        return true;
    }

}
