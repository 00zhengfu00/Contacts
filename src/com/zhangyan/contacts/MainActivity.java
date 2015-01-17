package com.zhangyan.contacts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.zhangyan.contacts.data.ContactsData;
import com.zhangyan.contacts.strcut.Contacts;

import java.util.ArrayList;

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
        contactsData = new ContactsData(this);
        initView();
    }

    private void initView() {
        ButterKnife.inject(this);
        exporttn.setOnClickListener(this);
        importBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
    }
    private void exportContacts(){
        contactsData.openDb();
        if(contactsData.checkIsImport()) {
            ArrayList<Contacts> contactses = ContactsOperate.getContactsFromPhone(this);
            showProgressBar(EXPORT, contactses.size());
            for(int i = 0; i < contactses.size(); i ++){
                contactsData.addContacts(contactses.get(i), i + 1);
                progressDialog.incrementProgressBy(1);
            }
        }
    }
    private void importContacts(){

    }
    private void showProgressBar(int type, int max){
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        if(type == IMPORT) {
            progressDialog.setMessage(getResources().getString(R.string.importing));
        } else {
            progressDialog.setMessage(getResources().getString(R.string.exporting));
        }
        progressDialog.setMax(max);
        progressDialog.show();
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
