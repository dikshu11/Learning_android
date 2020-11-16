package com.example.lakshmimarriagebureau;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {

    Button addBiodatabutton;
    Button viewBiodatabutton;
    static DatabaseReference mDatabase;
    static StorageReference storageReference;


    public void onClickButton(View view) {
        Intent intent = null;

        if (view == addBiodatabutton)
            intent = new Intent(getApplicationContext(), addNewData.class);
        else if (view == viewBiodatabutton)
            intent = new Intent(getApplicationContext(), viewBiodata.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBiodatabutton = (Button) findViewById(R.id.addBiodatabutton);
        viewBiodatabutton = (Button) findViewById(R.id.viewBiodatabutton);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Person");
        storageReference = FirebaseStorage.getInstance().getReference();
    }
}
