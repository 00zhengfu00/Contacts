package com.zhangyan.contacts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.zhangyan.contacts.data.ContactsData;
import com.zhangyan.contacts.data.ContactsSingle;
import com.zhangyan.contacts.strcut.Contacts;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    private static final int IMPORT = 0;
    private static final int EXPORT = 1;

    private ContactsData contactsData;
    private MyAdapter adapter ;
    private ProgressDialog progressDialog;

    private ArrayList<Contacts> contactList;
    private ProgressBar progressBar;
    private TextView msg;
    private int opreate ;
    /* 控件 */
    @InjectView(R.id.listView) ListView listView;
    @InjectView(R.id.contacts_count) TextView contactsCount;
    @InjectView(R.id.import_btn) TextView import_btn;
    @InjectView(R.id.export_btn) TextView export_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        contactsData = new ContactsData(this, handler);
        initView();
        readContacts();
    }
    private void initView() {
        /* 注入视图 */
        ButterKnife.inject(this);
        /* 为控件设置点击事件 */
        /*  */
        import_btn.setOnClickListener(this);
        export_btn.setOnClickListener(this);
        adapter = new MyAdapter(this, null);
        listView.setAdapter(adapter);
    }
    private void readContacts() {
        new Thread() {
            public void run() {
                contactList = ContactsOperate.getContactsFromPhone(MainActivity.this, handler);
            }
        }.start();
    }
    Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch(message.what){
                case Constans.PROGRESS_MAX:
                    progressDialog.setMax(message.getData().getInt("max"));
                    break;
                case Constans.EXPORT:

                    break;
                case Constans.IMPORT:
                    progressDialog.setMessage("正在导入联系人..");
                    break;
                case Constans.PROGRESS_INC:
                    progressDialog.incrementProgressBy(1);
                    break;
                case Constans.PROGRESS_DISMISS:
                    progressDialog.dismiss();
                    if(opreate == IMPORT) {
                        Constans.showToast(MainActivity.this, "导入成功！");
                    } else {
                        Constans.showToast(MainActivity.this, "已导出至" + ContactsData.SDCARD_PATH + ContactsData.CONTACTS_HELPER + ContactsData.CONTACTS_BACKUP);
                    }
                    break;
                case Constans.AUPDATA_LIST:
                    adapter.add(ContactsSingle.getInstance().getContacts());
                    ContactsSingle.getInstance().getContacts().clear();
                    contactsCount.setText("共" + ContactsSingle.getInstance().getCount() + "位联系人");
                    break;
                default :
                    break;
            }
        }
    };
    private void exportContacts(){
       if( contactsData.openDb()) {
           if (contactsData.checkIsImport()) {
               opreate = EXPORT;
               progressDialog = getProgressBar();
               progressDialog.show();
               new Thread() {
                   public void run() {
                       contactsData.addContacts(contactList);
                   }
               }.start();
           } else {
               showDialog("联系人已经导出，是否需要更新？");
           }
       }
    }
    private void importContacts(){
        if(contactsData.openDb()) {
            if (contactsData.checkIsImport()) {
                showDialog("联系人还未导出，是否导出？");
            } else {
                opreate = IMPORT;
                progressDialog = getProgressBar();
                progressDialog.show();
                new Thread() {
                    public void run() {
                       ContactsOperate.saveContacts(MainActivity.this, contactsData.getContactsFromDb(), handler);
                    }
                }.start();
            }
        }
    }
    private void showDialog(String title){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactsData.deleteTable();
                        exportContacts();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
    private ProgressDialog getProgressBar(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在读取联系人..");
        return progressDialog;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.import_btn:
                importContacts();
                break;
            case R.id.export_btn:
                exportContacts();
                break;
            default:
                break;
        }
    }

    public class MyAdapter extends BaseAdapter{
        private Context mContext;
        private ArrayList<Contacts> contactses;
        public MyAdapter(Context mContext, ArrayList<Contacts> contactses){
            this.mContext = mContext;
            this.contactses = contactses == null ? new ArrayList<Contacts>() : contactses;
        }
        @Override
        public int getCount() {
            return contactses.size();
        }

        @Override
        public Contacts getItem(int position) {
            return contactses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public void add(ArrayList<Contacts> contacts){
            if(contacts != null) {
                for(Contacts contacts1 : contacts)
                this.contactses.add(contacts1);
            }
            notifyDataSetChanged();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.contacts_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.setValues(getItem(position));
            return convertView;
        }

        class ViewHolder {
            @InjectView(R.id.imageView) ImageView img;
            @InjectView(R.id.contact_name) TextView name;
            @InjectView(R.id.contact_phone) TextView phone;
            public ViewHolder(View view){
                ButterKnife.inject(this, view);
            }
            public void setValues(Contacts contacts){
                name.setText(contacts.getName());
                if(contacts.getPhoneId() > 0){
                    phone.setText(contacts.getPhone().get(0).getValue());
                }
            }
        }
    }
}
