package com.zhangyan.contacts.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by ku on 2015/1/11.
 */
public class DataBase extends SQLiteOpenHelper// 继承SQLiteOpenHelper类
    {

        // 数据库版本号
        private static final int DATABASE_VERSION = 1;
        /* 联系人表 */
        public static final String CONTACT = "contacts" ; //表名
        public static final String CONTACT_NAME = "cname"; //联系人姓名
        public static final String CONTACT_PHONE_ID_INDEX = "cpid"; //联系人电话id

        /* 联系人电话表 */
        private static final String CONTACT_PHONE = "phone"; //联系人电话表名
        private static final String CONTACT_PHONE_ID = "pid"; //电话id
        private static final String CONTACT_PHONE_NUMBER = "pnumber"; //电话号码
        /* sdcard */
        public static final String SDCARD_PATH = Environment.getExternalStorageDirectory() + File.separator ;
        public static final String CONTACTS_HELPER = "联系人助手" ; //一级目录
        public static final String CONTACTS_BACKUP = "backup" ;


        private SQLiteDatabase db;

        public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /**
         *
         * 在sd卡上创建数据库
         *
         * */
        public Boolean createDatabase(String dataName) {
            /* 判断sd卡是否存在 */
            if( checkSDCard() ) {
                createPath();
            } else { //
                return false;
            }
            return true;
        }
        /**
         *
         * 创建数据库在sd卡上的目录
         *
         * */
        private void createPath() {
             /* 创建一级目录 */
            File folder = new File(SDCARD_PATH + CONTACTS_HELPER);
            if( ! folder.exists() ) {
                folder.mkdir();
            }
                /* 创建二级目录 */
            folder = new File(SDCARD_PATH + CONTACTS_HELPER + File.separator + CONTACTS_BACKUP);
            if ( ! folder.exists() ) {
                folder.mkdir();
            }
        }

        /**
         *
         * 创建电话号码表
         *
         * */
        private void createPhoneTable(SQLiteDatabase db) {
            StringBuffer sBuffer = new StringBuffer();

            sBuffer.append("CREATE TABLE [" + CONTACT_PHONE + "] (");
            sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
            sBuffer.append(CONTACT_PHONE_ID + " INTEGER,");
            sBuffer.append(CONTACT_PHONE_NUMBER + " TEXT)");
            // 执行创建表的SQL语句
            db.execSQL(sBuffer.toString());
        }

        /**
         * 创建联系人表
         *
         * */
        private void createContactTable(SQLiteDatabase db) {
            StringBuffer sBuffer = new StringBuffer();

            sBuffer.append("CREATE TABLE [" + CONTACT + "] (");
            sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
            sBuffer.append(CONTACT_NAME + " TEXT,");
            sBuffer.append(CONTACT_PHONE_ID_INDEX + " INTEGER)");

            // 执行创建表的SQL语句
            db.execSQL(sBuffer.toString());
        }

        public Boolean checkSDCard() {
            if( Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED ) ) {
                return true;
            }
            return false;
        }

        public SQLiteDatabase getDb() {
            return db;
        }

        public void setDb(SQLiteDatabase db) {
            this.db = db;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
