package com.example.lakshmimarriagebureau;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class showBiodata extends AppCompatActivity implements MyAdapter.OnCardListener {


    static List<Person> persons;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressDialog progressDialog;
    private List<String> keys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_biodata);
        setTitle("Profiles");

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String gender = intent.getStringExtra("gender");
        final boolean divorcee = intent.getBooleanExtra("divorcee", false);
        final boolean mangalik = intent.getBooleanExtra("mangalik", false);
        final boolean job = intent.getBooleanExtra("job", false);
        final boolean buisness = intent.getBooleanExtra("buisnss", false);
        final String location = intent.getStringExtra("location");
        final String income = intent.getStringExtra("income");
        final Integer minAge = intent.getIntExtra("min age", 0);
        final Integer maxAge = intent.getIntExtra("max age", 100);
        final Long budget = intent.getLongExtra("budget", (long) 0);


        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        persons = new ArrayList<>();
        keys = new ArrayList<>();

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        MainActivity.mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                persons.clear();
                //iterating through all the values in database
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person person = postSnapshot.getValue(Person.class);
                    boolean addPerson = true;

                    if (!name.isEmpty() && person.getName() != "" && person.getName().toLowerCase() != name.toLowerCase())
                        addPerson = false;
                    if (!gender.isEmpty() && !person.getGender().equals("") && !gender.equals(person.getGender()))
                        addPerson = false;
                    if (person.isDivorcee() != divorcee && divorcee == true)
                        addPerson = false;
                    if (person.isMangalik() != mangalik && mangalik == true)
                        addPerson = false;
                    if (person.isDoesBuisness() != buisness && buisness == true)
                        addPerson = false;
                    if (person.isDoesBuisness() && job == true)
                        addPerson = false;
                    if (location != null && !location.isEmpty() && !person.getCurrLocation().equals("") && !location.equals(person.getCurrLocation()))
                        addPerson = false;
                    if (!income.isEmpty() && !person.getIncome().equals("")) {
                        try {
                            if (person.getIncome() < Long.parseLong(income))
                                addPerson = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (budget > 0 && budget < person.getBudget())
                        addPerson = false;
                    if (minAge != 0 && person.getAge() > 0 && person.getAge() < 100 && person.getAge() < minAge - 1)
                        addPerson = false;
                    if (maxAge != 100 && person.getAge() > 0 && person.getAge() < 100 && person.getAge() > maxAge + 1)
                        addPerson = false;

                    if (addPerson) {
                        keys.add(postSnapshot.getKey());
                        persons.add(person);
                    }

                }
                //creating adapter
                adapter = new MyAdapter(getApplicationContext(), persons, showBiodata.this);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onCardClick(int position) {
        Log.i("On click", String.valueOf(position));
        Intent intent = new Intent(getApplicationContext(), showCompleteBiodata.class);
        intent.putExtra("position", position);
        startActivity(intent);

    }

    private void sendEmail(String email, String subject, String message) {
        SendMail sm = new SendMail(this, email, subject, message, null);
        //Executing sendmail to send email
        sm.execute();
    }

    @Override
    public void onDeleteClick(final int position) {

        Log.i("On delete", String.valueOf(position));
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete")
                .setMessage("Are you sure to delete this biodata?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.mDatabase.child(keys.get(position)).removeValue();


                        Person tempPerson = persons.get(position);
                        if (tempPerson.getPhotoUrl1() != null && !tempPerson.getPhotoUrl1().isEmpty() && !tempPerson.getPhotoUrl1().equals("")) {
                            FirebaseStorage.getInstance().getReferenceFromUrl(tempPerson.getPhotoUrl1()).delete();
                        }

                        if (tempPerson.getPhotoUrl2() != null && !tempPerson.getPhotoUrl2().isEmpty() && !tempPerson.getPhotoUrl2().equals("")) {
                            FirebaseStorage.getInstance().getReferenceFromUrl(tempPerson.getPhotoUrl2()).delete();
                        }
                        if (tempPerson.getBiodataUrl() != null && !tempPerson.getBiodataUrl().isEmpty() && !tempPerson.getBiodataUrl().equals("")) {
                            FirebaseStorage.getInstance().getReferenceFromUrl(tempPerson.getBiodataUrl()).delete();
                        }

                        sendEmail(Config.EMAIL, "Delete Profile", "ID: " + keys.get(position) + "\n" + "Name: " + tempPerson.getName());
                        keys.remove(position);
                        persons.remove(position);
                        recyclerView.removeViewAt(position);

                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                        adapter.notifyDataSetChanged();

                    }

                })
                .setNegativeButton("No", null)
                .show();

    }
}
