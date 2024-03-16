package com.xeta.communitykitchen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth auth;
    String[] fieldNamesOrg = {"Name", "Age", "Ward", "Address", "Quantity", "Landmark", "Mobile", "Order Date", "Created By"};
    String[] fieldNames = {"name", "age", "ward", "address", "qty", "landmark", "mobile", "order_date", "order_by"};
    FirebaseDBCollectionToExcel createExcel;
    LinearLayout normalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        createExcel = new FirebaseDBCollectionToExcel(fieldNames, fieldNamesOrg);
        auth = FirebaseAuth.getInstance();
        normalUser = (LinearLayout) findViewById(R.id.normalUser);

        if (auth.getCurrentUser() != null) {
            String user = auth.getCurrentUser().getEmail();
            if(!user.equals("admin@xetatria.com")){
                normalUser.setVisibility(LinearLayout.GONE);
            }
        }
    }

    public void createTask(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    public void connectDisha(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    public void createUser(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void generateReport(View view) {

        db.collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Date currentTime = Calendar.getInstance().getTime();
                            String fileName = "Orders_"+currentTime.toString();
                            String dirName = "Reports";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                createExcel.buildFileFromQuery(document);
                            }
                            createExcel.saveFileToStorage(fileName, dirName);
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Data exported successfully! Filename: "+fileName)
                                    .setTitle("Message")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                            builder.show();
                        } else {
                            Log.d("Data", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void logOutUser(View view) {
        if (auth.getCurrentUser() != null) {
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}