package com.example.lakshmimarriagebureau;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class addNewData extends AppCompatActivity {

    // variables
    private static final int PICK_IMAGE_REQUEST1 = 234;
    private static final int PICK_IMAGE_REQUEST2 = 235;
    private static final int PICK_DATA_REQUEST1 = 236;
    // Setting date fragments
    final Calendar myCalendar = Calendar.getInstance();
    ArrayList<File> attachFiles;
    AutoCompleteTextView locationAutoCompleteTextView;
    CheckBox maleCheckBox, femaleCheckBox;
    CheckBox divorceeCheckBox;
    CheckBox mangalikCheckBox;
    CheckBox buisnessCheckBox, jobCheckBox;
    EditText nameEditText;
    EditText dobEditText, pobEditText, tobEditText;
    EditText heightEditText;
    EditText contactEditText, whatsappEditText;
    EditText qualificationEditText;
    EditText occupationEditText;
    EditText incomeEditText, budgetEditText;
    Button photo1Button;
    Button photo2Button;
    Button biodataButton;
    Button submitButton;

    // filePath for three files/pics/biodata
    private Uri filePath1 = null;
    private Uri filePath2 = null;
    private Uri filePath3 = null;

    // get file extension
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // sending email with attach files
    private void sendEmail(String email, String subject, String message) {
        SendMail sm = new SendMail(this, email, subject, message, attachFiles);
        sm.execute(); //Executing sendmail to send email
        attachFiles.clear();
    }

    // reset the filled data
    public void reset() {
        nameEditText.setText("");
        dobEditText.setText("");
        pobEditText.setText("");
        tobEditText.setText("");
        heightEditText.setText("");
        contactEditText.setText("");
        whatsappEditText.setText("");
        qualificationEditText.setText("");
        occupationEditText.setText("");
        incomeEditText.setText("");
        budgetEditText.setText("");
        filePath1 = null;
        filePath2 = null;
        filePath3 = null;
        locationAutoCompleteTextView.setText("");

        maleCheckBox.setChecked(false);
        femaleCheckBox.setChecked(false);
        divorceeCheckBox.setChecked(false);
        mangalikCheckBox.setChecked(false);
        jobCheckBox.setChecked(false);
        buisnessCheckBox.setChecked(false);
    }

    // get Person Object
    public Person getPersonObject() {
        Person person = new Person();

        person.setName(nameEditText.getText().toString());

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = format.parse(dobEditText.getText().toString());
            person.setDob(date);
        } catch (ParseException e) {
            Date date = new Date(2050, 01, 1);
            person.setDob(date);
        }

        person.setPlaceOfBirth(pobEditText.getText().toString());
        person.setTimeOfBirth(tobEditText.getText().toString());
        person.setHeight(heightEditText.getText().toString());

        if (maleCheckBox.isChecked()) person.setGender("M");
        if (femaleCheckBox.isChecked()) person.setGender("F");

        Log.i("Gender", person.getGender());
        if (mangalikCheckBox.isChecked()) person.setMangalik(true);
        if (divorceeCheckBox.isChecked()) person.setDivorcee(true);

        if (buisnessCheckBox.isChecked()) person.setDoesBuisness(true);
        else person.setDoesBuisness(false);

        person.setCurrLocation(locationAutoCompleteTextView.getText().toString());
        person.setContact(contactEditText.getText().toString());
        person.setWhatsapp_contact(whatsappEditText.getText().toString());
        person.setQualification(qualificationEditText.getText().toString());
        person.setOccupation(occupationEditText.getText().toString());
        try {
            person.setIncome(Long.parseLong(incomeEditText.getText().toString()));
        } catch (NumberFormatException e) {
            person.setIncome((long) -1);
        }
        try {
            person.setBudget(Long.parseLong(budgetEditText.getText().toString()));
        } catch (NumberFormatException e) {
            person.setBudget((long) -1);
        }
        return person;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_data);
        setTitle("Add a New Profile");

        maleCheckBox = (CheckBox) findViewById(R.id.maleCheckBox);
        femaleCheckBox = (CheckBox) findViewById(R.id.femaleCheckBox);
        divorceeCheckBox = (CheckBox) findViewById(R.id.divorceeCheckBox);
        mangalikCheckBox = (CheckBox) findViewById(R.id.mangalikCheckBox);
        jobCheckBox = (CheckBox) findViewById(R.id.jobCheckBox);
        buisnessCheckBox = (CheckBox) findViewById(R.id.buisnessCheckBox);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        dobEditText = (EditText) findViewById(R.id.dobEditText);
        pobEditText = (EditText) findViewById(R.id.pobEditText);
        tobEditText = (EditText) findViewById(R.id.tobEditText);
        contactEditText = (EditText) findViewById(R.id.contactEditText);
        whatsappEditText = (EditText) findViewById(R.id.whatsappEditText);
        budgetEditText = (EditText) findViewById(R.id.budgetEditText);
        heightEditText = (EditText) findViewById(R.id.heightEditText);
        qualificationEditText = (EditText) findViewById(R.id.qualificationEditText);
        occupationEditText = (EditText) findViewById(R.id.occupationEditText);
        incomeEditText = (EditText) findViewById(R.id.incomeEditText);
        locationAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.locationAutoCompleteTextView);

        photo1Button = (Button) findViewById(R.id.photo1Button);
        photo2Button = (Button) findViewById(R.id.photo2Button);
        biodataButton = (Button) findViewById(R.id.biodataButton);
        submitButton = (Button) findViewById(R.id.submitButton);
        attachFiles = new ArrayList<java.io.File>();

        cities cityList = new cities();
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, cityList.getCity());
        locationAutoCompleteTextView.setAdapter(cityAdapter);

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int year = 2000;
                int month = 8;
                int day = 15;
                DatePickerDialog picker = new DatePickerDialog(addNewData.this, android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, month);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd/MM/yyyy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                dobEditText.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, year, month, day
                );

                picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                picker.show();
            }
        });

        tobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minutes = myCalendar.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog picker = new TimePickerDialog(addNewData.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String timeSet = "AM";
                                String minutes = Integer.toString(sMinute);
                                if (sHour >= 12) {
                                    sHour -= (sHour == 12) ? 0 : 12;
                                    timeSet = "PM";
                                } else if (sHour == 0) {
                                    sHour += 12;
                                    timeSet = "AM";
                                }
                                if (sMinute < 10) minutes = "0" + sMinute;

                                tobEditText.setText(sHour + ":" + minutes + " " + timeSet);
                            }
                        }, hour, minutes, false);
                picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                picker.show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String key = MainActivity.mDatabase.push().getKey();
                final Person person = getPersonObject();

                sendEmail(Config.EMAIL, "Add Profile: " + person.getName() + " " + person.getGender(), "ID: " + key);
                MainActivity.mDatabase.child(key).setValue(person);

                if (filePath1 != null) {
                    final StorageReference sRef = MainActivity.storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(filePath1));
                    UploadTask uploadTask = sRef.putFile(filePath1);

                    final ProgressDialog progressDialog = new ProgressDialog(addNewData.this);
                    progressDialog.setMessage("Uploading photo 1");
                    progressDialog.show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Toast.makeText(addNewData.this, "Uploading failed! Please try again", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
//                                throw Objects.requireNonNull(task.getException());
                            }
                            // Continue with the task to get the download URL
                            return sRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                Uri downloadUri = task.getResult();
                                person.setPhotoUrl1(downloadUri.toString());
                                MainActivity.mDatabase.child(key).child("photoUrl1").setValue(downloadUri.toString());

                            }
                        }
                    });

                }


                if (filePath2 != null) {

                    final ProgressDialog progressDialog2 = new ProgressDialog(addNewData.this);
                    progressDialog2.setMessage("Uploading photo 2");
                    progressDialog2.show();

                    final StorageReference sRef = MainActivity.storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(filePath2));
                    UploadTask uploadTask = sRef.putFile(filePath2);

                    Task<Uri> urlTask1 = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Toast.makeText(addNewData.this, "Uploading failed! Please try again", Toast.LENGTH_LONG).show();
                                progressDialog2.dismiss();
//                                throw task.getException();
                            }
                            return sRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                progressDialog2.dismiss();
                                Uri downloadUri = task.getResult();
                                person.setPhotoUrl2(downloadUri.toString());
                                MainActivity.mDatabase.child(key).child("photoUrl2").setValue(downloadUri.toString());
                            }
                        }
                    });

                }

                if (filePath3 != null) {

                    final StorageReference sRef = MainActivity.storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(filePath3));
                    UploadTask uploadTask = sRef.putFile(filePath3);
                    final ProgressDialog progressDialog3 = new ProgressDialog(addNewData.this);
                    progressDialog3.setMessage("Uploading biodata");
                    progressDialog3.show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                progressDialog3.dismiss();
                                Toast.makeText(addNewData.this, "Uploading failed! Please try again", Toast.LENGTH_LONG).show();

//                                throw task.getException();
                            }
                            return sRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                progressDialog3.dismiss();
                                Uri downloadUri = task.getResult();
                                person.setBiodataUrl(downloadUri.toString());
                                MainActivity.mDatabase.child(key).child("biodataUrl").setValue(downloadUri.toString());
                            }
                        }
                    });
                }
                reset();

            }
        });

    }


    public void addFile(Uri filePath) {
        if (attachFiles == null) attachFiles = new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(filePath);
            try {
                File file = new File(addNewData.this.getCacheDir(), "profile" + System.currentTimeMillis() + "." + getFileExtension(filePath));
                try (OutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) output.write(buffer, 0, read);
                    output.flush();
                    attachFiles.add(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Choose file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath1 = data.getData();
            addFile(filePath1);
        } else if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath2 = data.getData();
            addFile(filePath2);
        } else if (requestCode == PICK_DATA_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath3 = data.getData();
            addFile(filePath3);
        }
    }

    public void onClickButton(View view) {
        int pickRequest = (view == photo1Button) ? PICK_IMAGE_REQUEST1 : PICK_IMAGE_REQUEST2;
        String intent_type = "image/*";

        if (view == biodataButton) {
            intent_type = "*/*";
            pickRequest = PICK_DATA_REQUEST1;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(intent_type);
        startActivityForResult(Intent.createChooser(intent, "Select File to Upload"), pickRequest);
    }
}
