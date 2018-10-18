package com.frankchang.atm_use_firebase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ContactActivity extends AppCompatActivity {

    // 常數
    private static final int REQUEST_CONTACT = 10;
    private RecyclerView contactList;
    private List<Contact> contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        findViews();        // 畫面元件連結

        // 檢查權限
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // 讀取連絡人資訊
            readContact();
        } else {
            // 詢問授權
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
        }
    }

    // 畫面元件連結
    private void findViews() {
        contactList = findViewById(R.id.recyclerContact);
        contactList.setHasFixedSize(true);     // 是不是固定大小
        contactList.setLayoutManager(new LinearLayoutManager(this));   // 條列式
    }

    // 讀取連絡人資訊
    private void readContact() {
        // 取得指標
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        // 取得資料
        contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            // 編號
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            // 姓名
            String name = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Contact contact = new Contact(id, name);

            // 是否有電話號碼
            int hasPhone = cursor.getInt(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            // 取得電話號碼列表
            if (hasPhone == 1) {
                Cursor c2 = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        new String[]{String.valueOf(id)}, null);
                while (c2.moveToNext()) {
                    String phone = c2.getString(
                            c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                    // 塞值
                    contact.getPhones().add(phone);
                }
            }
            // 放入集合物件
            contacts.add(contact);
        }

        // 建立Adapter
        ContactAdapter adapter = new ContactAdapter(contacts);
        contactList.setAdapter(adapter);
    }

    // 詢問授權回覆處理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 讀取連絡人資訊
                readContact();
            }
        }
    }


    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

        List<Contact> contacts;


        public ContactAdapter(List<Contact> contacts) {
            this.contacts = contacts;
        }

        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(
                    android.R.layout.simple_list_item_2, viewGroup, false);
            return new ContactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, int i) {
            Contact contact = contacts.get(i);
            contactViewHolder.tvContactName.setText(contact.getName());
            StringBuilder sb = new StringBuilder();
            for (String phone : contact.getPhones()) {
                sb.append(phone);
                sb.append(" ");
            }
            contactViewHolder.tvContactPhone.setText(sb.toString());
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }


        public class ContactViewHolder extends RecyclerView.ViewHolder {

            TextView tvContactName;
            TextView tvContactPhone;


            public ContactViewHolder(@NonNull View itemView) {
                super(itemView);

                tvContactName = itemView.findViewById(android.R.id.text1);
                tvContactPhone = itemView.findViewById(android.R.id.text2);
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_upload) {
            String userid = getSharedPreferences("ATM", MODE_PRIVATE)
                    .getString("UserId", null);
            if (userid != null) {
                FirebaseDatabase.getInstance().getReference("users")
                        .child(userid).child("contacts")
                        .setValue(contacts);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
