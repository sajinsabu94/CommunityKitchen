package com.xeta.communitykitchen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactActivity extends Activity {
    RecyclerView rvContacts;
    String[] phones = {"8547457377"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        List<ContactListItem> contactList = new ArrayList();
        ContactListItem contactListItem;

        contactListItem = new ContactListItem();
        contactListItem.setContactName("Salin Sabu");
        contactListItem.setContactNumber("8547457377");
        contactList.add(contactListItem);

        AllContactsAdapter contactAdapter = new AllContactsAdapter(contactList, getApplicationContext());
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(contactAdapter);


        rvContacts.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rvContacts ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String phone = "tel:" + phones[position];
                        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(phone));
                        startActivity(i);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }
}
