package com.xeta.communitykitchen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by apple on 26-03-2020.
 */

public class AddActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    String name, age, landmark, ward, order_date, address, mobile, qty, created_by;
    EditText editTextName, editTextAge, editTextAddress, editTextMobile, editTextWard, editTextLand, editTextQty;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            created_by = auth.getCurrentUser().getEmail();
            created_by.replace("@xetatria.com","");
        }

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextLand = (EditText) findViewById(R.id.landMark);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextWard = (EditText) findViewById(R.id.orderWard);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        editTextQty = (EditText) findViewById(R.id.editTextQty);
    }

    public void addItemToDB(View view) {
        if(checkValidation()){
            name = editTextName.getText().toString();
            age = editTextAge.getText().toString();
            address = editTextAddress.getText().toString();
            mobile = editTextMobile.getText().toString();
            ward = editTextWard.getText().toString();
            landmark = editTextLand.getText().toString();
            qty = editTextQty.getText().toString();

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            order_date = df.format(c);

            Order order = new Order(name, age, landmark, ward, order_date, address, mobile, qty, created_by);

            db.collection("orders").add(order)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                            builder.setMessage("Data added successfully!")
                                    .setTitle("Message")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    });
                            builder.show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getApplicationContext(), "Error while adding the data! Please make sure your data is ON.", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                            builder.setMessage("Error while adding the data! Please make sure your data is ON.")
                                    .setTitle("Message")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                            builder.show();
                        }
                    });
        }

    }
    public  boolean checkValidation() {
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();
        String address = editTextAddress.getText().toString();
        String mobile = editTextMobile.getText().toString();
        String ward = editTextWard.getText().toString();
        String qty = editTextQty.getText().toString();

        if (name.length() <= 0) {
            editTextName.requestFocus();
            editTextName.setError("Enter Name");
            return false;
        } else if (age.length() <= 0) {
            editTextAge.requestFocus();
            editTextAge.setError("Enter Age");
            return false;

        } else if (ward.length() <= 0) {
            editTextWard.requestFocus();
            editTextWard.setError("Enter Ward Number");
            return false;
        }
        else if (address.length() <= 0) {
            editTextAddress.requestFocus();
            editTextAddress.setError("Enter Address");
            return false;

        } else if (qty.length() <= 0) {
            editTextQty.requestFocus();
            editTextQty.setError("Enter Quantity");
            return false;
        }
        else if (mobile.length() <= 0 || mobile.length() != 10) {
            editTextMobile.requestFocus();
            editTextMobile.setError("Enter Valid Mobile Number");
            return false;

        }   else {
            return true;
        }
    }
}