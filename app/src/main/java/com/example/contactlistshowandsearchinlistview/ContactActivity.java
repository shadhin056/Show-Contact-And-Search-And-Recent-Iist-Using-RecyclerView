package com.example.contactlistshowandsearchinlistview;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.contactlistshowandsearchinlistview.R;
import com.example.contactlistshowandsearchinlistview.SQLite.DatabaseHelper;
import com.example.contactlistshowandsearchinlistview.SQLite.SQLiteData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.example.contactlistshowandsearchinlistview.SQLite.SQLiteData.TABLE_NAME;

public class ContactActivity extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener, ContactsAdapterRecent.ContactsAdapterListener {
    private RecyclerView recyclerView,recycler_view_recent;
    Button submit;
    private List<Contact> contactList;
    private List<Contact> contactList2;
    private ContactsAdapter mAdapter;
    private ContactsAdapterRecent mAdapterRecent;
    private EditText searchView;
    private EditText inputSearch;
    String name, phonenumber,image_uri,count ;
    Cursor cursor ;
    DatabaseHelper databaseHelper;
    private String nameSelected;
    private String numberSelected="";
    private String imageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view);
        inputSearch = findViewById(R.id.inputSearch);
        submit = findViewById(R.id.submit);
        recycler_view_recent = findViewById(R.id.recycler_view_recent);
        contactList = new ArrayList<>();
        contactList2 = new ArrayList<>();
        mAdapter = new ContactsAdapter(ContactActivity.this, contactList, this);
        mAdapterRecent = new ContactsAdapterRecent(ContactActivity.this, contactList2, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);
        whiteNotificationBar(recycler_view_recent);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

       RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        recycler_view_recent.setLayoutManager(mLayoutManager1);
        recycler_view_recent.setItemAnimator(new DefaultItemAnimator());
        recycler_view_recent.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recycler_view_recent.setAdapter(mAdapterRecent);
        fetchContacts();
        fetchRecentContacts();
        search();
        searchRecent();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberSelected.equals(inputSearch.getText().toString())){
                    insertOrUpdate(numberSelected,nameSelected,imageSelected,1);
                }else {
                    insertOrUpdate(inputSearch.getText().toString(),"","",1);
                }

            }
        });
    }



    private void fetchRecentContacts() {



        /*if (response == null) {
            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
            return;

        }*/
        List<SQLiteData> number = databaseHelper.getNumbers();
        contactList2.clear();

        for(int i = 0; i<number.size(); i++){
            name=number.get(i).getName();
            count= String.valueOf(number.get(i).getPriority());
            phonenumber=number.get(i).getNumber();
            image_uri=number.get(i).getImage();
            Contact a = new Contact(name,image_uri, phonenumber,count);
            contactList2.add(a);
            mAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }


    private void search() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (EditText) findViewById(R.id.inputSearch);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAdapter.getFilter().filter(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void searchRecent() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (EditText) findViewById(R.id.inputSearch);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAdapterRecent.getFilter().filter(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void fetchContacts() {


        /*if (response == null) {
            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
            return;
        }*/
        String order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, order);
        contactList.clear();
        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            image_uri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            String removeSpecialChar= phonenumber.replaceAll("[-+.^:*#_/, ]","");
            String mobileNumber = null;
            if(removeSpecialChar.length()==13){
                mobileNumber= removeSpecialChar.substring(2, 13);
                //searchView.setText(mobileNumber);
                phonenumber=mobileNumber;
                Contact a = new Contact(name, image_uri, phonenumber,"");

                contactList.add(a);
                /*if(databaseHelper.hasNumber(phonenumber)){
                    contactList2.add(a);
                }else {
                    contactList.add(a);
                }*/
            }else if(removeSpecialChar.length()==11){
                //searchView.setText(removeSpecialChar);
                phonenumber=removeSpecialChar;
                Contact a = new Contact(name, image_uri, phonenumber,"");
                contactList.add(a);
                /*if(databaseHelper.hasNumber(phonenumber)){
                    contactList2.add(a);
                }else {
                    contactList.add(a);
                }*/
            }
            // refreshing recycler view
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
    @Override
    public void onContactSelected(Contact contact) {
        searchView.setText(contact.getPhone());
        nameSelected=contact.getName();
        numberSelected=contact.getPhone();
        imageSelected=contact.getImage();

        Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void insertOrUpdate(String numberSelected, String nameSelected, String imageSelected, int i){
        long idInsert = 0;
        long idUpdate = 0;
        int idGet = databaseHelper.getID(numberSelected);
        if(idGet==-1){
            idInsert= databaseHelper.insertNumber(numberSelected,nameSelected,imageSelected,1);
        } else{
            idUpdate=databaseHelper.update(numberSelected,nameSelected,imageSelected);
        }

        if (idInsert > 0) {
            Toast.makeText(getApplicationContext(), "Data is added and id : " + idInsert, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Can not inserted : ", Toast.LENGTH_LONG).show();
        }
        if (idUpdate > 0) {
            Toast.makeText(getApplicationContext(), "Data is updated", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Can not updated : ", Toast.LENGTH_LONG).show();
        }
    }
}

