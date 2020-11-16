package com.example.lakshmimarriagebureau;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class viewBiodata extends AppCompatActivity {


    static EditText dobEditText1;
    CheckBox maleCheckBox1;
    CheckBox femaleCheckBox1;
    CheckBox divorceeCheckBox1;
    CheckBox mangalikCheckBox1;
    CheckBox buisnessCheckBox1;
    CheckBox jobChechBox1;

    AutoCompleteTextView locationAutoCompleteTextView1;

    EditText nameEditText1;
    EditText incomeEditText1;
    EditText minAgeEditText1;
    EditText maxAgeEditText1;
    EditText budgetEditText1;


    public void onClickButton(View view) {
        String name = nameEditText1.getText().toString();
        String gender = "";
        Boolean divorcee = false;
        Boolean mangalik = false;
        Boolean buisness = false;
        Boolean job = false;
        String location = locationAutoCompleteTextView1.getText().toString();
        String income = incomeEditText1.getText().toString();
        Integer minAge = 0;
        Integer maxAge = 100;
        Long budget = (long) 0;

        try {
            budget = Long.parseLong(budgetEditText1.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jobChechBox1.isChecked())
            job = true;
        if (buisnessCheckBox1.isChecked())
            buisness = true;

        if (maleCheckBox1.isChecked())
            gender = "M";
        if (femaleCheckBox1.isChecked())
            gender = "F";

        if (divorceeCheckBox1.isChecked())
            divorcee = true;

        if (mangalikCheckBox1.isChecked())
            mangalik = true;


        try {
            minAge = Integer.parseInt(minAgeEditText1.getText().toString());
        } catch (Exception e) {
            minAge = 0;
//            e.printStackTrace();
        }
        try {
            maxAge = Integer.parseInt(maxAgeEditText1.getText().toString());
        } catch (NumberFormatException e) {
            maxAge = 100;
//            e.printStackTrace();
        }


        Intent intent = new Intent(getApplicationContext(), showBiodata.class);
        intent.putExtra("name", name);
        intent.putExtra("gender", gender);
        intent.putExtra("divorcee", divorcee);
        intent.putExtra("mangalik", mangalik);
        intent.putExtra("location", location);
        intent.putExtra("income", income);
        intent.putExtra("min age", minAge);
        intent.putExtra("max age", maxAge);
        intent.putExtra("budget", budget);
        intent.putExtra("job", job);
        intent.putExtra("buisness", buisness);

        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_biodata);
        setTitle("View Profile");

        maleCheckBox1 = findViewById(R.id.maleCheckBox1);
        femaleCheckBox1 = findViewById(R.id.femaleCheckBox1);
        divorceeCheckBox1 = findViewById(R.id.divorceeCheckBox1);
        mangalikCheckBox1 = findViewById(R.id.mangalikCheckBox1);
        buisnessCheckBox1 = findViewById(R.id.buisnessCheckBox1);
        jobChechBox1 = findViewById(R.id.jobCheckBox1);
        budgetEditText1 = findViewById(R.id.budgetEditText1);
        nameEditText1 = findViewById(R.id.nameEditText1);
        incomeEditText1 = findViewById(R.id.incomeEditText1);
        minAgeEditText1 = findViewById(R.id.minAgeEditText1);
        maxAgeEditText1 = findViewById(R.id.maxAgeEditText1);
        locationAutoCompleteTextView1 = findViewById(R.id.locationAutoCompleteTextView1);

        cities cityList = new cities();
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, cities.getCity());
        locationAutoCompleteTextView1.setAdapter(cityAdapter);


    }
}
