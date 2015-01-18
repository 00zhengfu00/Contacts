package com.zhangyan.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.zhangyan.contacts.data.ContactsData;

public class MainActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    private static final int IMPORT = 0;
    private static final int EXPORT = 1;

    private ContactsData contactsData;
    ProgressDialog progressDialog;
    @InjectView(R.id.export_btn)
    Button exporttn;
    @InjectView(R.id.import_btn)
    Button importBtn;
    @InjectView(R.id.quit_btn)
    Button quitBtn;
    private ProgressBar progressBar;
    private TextView msg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        contactsData = new ContactsData(this, handler);
        initView();
    }

    private void initView() {
        /* 诸如试图 */
        ButterKnife.inject(this);
        /* 为控件设置点击事件 */
        exporttn.setOnClickListener(this);
        importBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        /* 初始progressDialog */

    }
    Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch(message.what){
                case Constans.PROGRESS_MAX:
                    progressDialog.setMax(message.getData().getInt("max"));
                    break;
                case Constans.EXPORT:
                    progressDialog.incrementProgressBy(1);
                    break;
                case Constans.PROGRESS_DISMISS:
                    progressDialog.dismiss();
                    Constans.showToast(MainActivity.this, "已导出至" + ContactsData.SDCARD_PATH + ContactsData.CONTACTS_HELPER + ContactsData.CONTACTS_BACKUP);
                    break;
                default :
                    break;
            }
        }
    };
    private void exportContacts(){
       if( contactsData.openDb()) {
           if (contactsData.checkIsImport()) {
               progressDialog = getProgressBar();
               progressDialog.setMessage("正在读取联系人..");
               progressDialog.show();
               new Thread() {
                   public void run() {
                       contactsData.addContacts(ContactsOperate.getContactsFromPhone(MainActivity.this, handler));
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

            } else {
                progressDialog = getProgressBar();
                progressDialog.show();
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
        progressDialog.setMax(0);
        return progressDialog;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.export_btn:
                exportContacts();
                break;
            case R.id.import_btn:
                importContacts();
                break;
            case R.id.quit_btn:
                System.exit(0);
                break;
            default:
                break;
        }
    }
}
