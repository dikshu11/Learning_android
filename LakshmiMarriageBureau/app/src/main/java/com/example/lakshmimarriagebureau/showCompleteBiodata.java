package com.example.lakshmimarriagebureau;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class showCompleteBiodata extends AppCompatActivity {

    public ImageView showImage;
    public ImageView showImage2;
    public TextView showBiodataTextView;
    public TextView showName, showHeight;
    public TextView showDateOFBirth, showTimeOfBirth, showPlaceOfBirth, showBudget ;
    public TextView showGender;
    public TextView showStatus;
    public TextView showMangalik;
    public TextView showEducation;
    public TextView showOccupation;
    public TextView showLocation;
    public TextView showIncome;
    public TextView showAge;
    public long downloadID;
    public DownloadManager downloadManager;
    final Calendar myCalendar = Calendar.getInstance();

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == id) {
                    Toast.makeText(showCompleteBiodata.this, "Download Completed", Toast.LENGTH_SHORT).show();
                    Log.i("Downloaded", "Complete");
                    Uri uri = downloadManager.getUriForDownloadedFile(downloadID);
                    if (uri != null){
                        Log.i("Uri", uri.toString());
                        Intent launchIntent = new Intent(Intent.ACTION_VIEW);
                        launchIntent.setDataAndType(uri, downloadManager.getMimeTypeForDownloadedFile(downloadID));
                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(launchIntent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };

    public void onShareImage(View view){
        Bitmap bm = null;
        if(view == showImage)
            bm = ((android.graphics.drawable.BitmapDrawable) showImage.getDrawable()).getBitmap();
        else if(view == showImage2)
            bm = ((android.graphics.drawable.BitmapDrawable) showImage2.getDrawable()).getBitmap();
        try {
            java.io.File file = new java.io.File(getExternalCacheDir() + "/image.jpg");
            java.io.OutputStream out = new java.io.FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) { e.getStackTrace();}
        Intent iten = new Intent(android.content.Intent.ACTION_SEND);
        iten.setType("*/*");
        iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(getExternalCacheDir() + "/image.jpg")));
        startActivity(Intent.createChooser(iten, "Send image"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complete_biodata);
        setTitle("Profile Details");
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());

        showName = findViewById(R.id.showName);
        showBudget = findViewById(R.id.showBudget);
        showDateOFBirth = findViewById(R.id.showDateoFBirth);
        showTimeOfBirth = findViewById(R.id.showTimeoFBirth);
        showPlaceOfBirth = findViewById(R.id.showPlaceoFBirth);
        showHeight = findViewById( R.id.showHeight);
        showImage = (ImageView) findViewById(R.id.showImage);
        showImage2 = (ImageView) findViewById(R.id.showImage2);
        showGender = (TextView) findViewById(R.id.showGender);
        showStatus = (TextView)  findViewById(R.id.showStatus);
        showEducation = (TextView) findViewById(R.id.showEducation);
        showOccupation = (TextView) findViewById(R.id.showOccupation);
        showLocation = (TextView) findViewById(R.id.showLocation);
        showIncome = (TextView) findViewById(R.id.showIncome);
        showMangalik = (TextView) findViewById(R.id.showMangalik);
        showAge = (TextView) findViewById(R.id.showAge);
        showBiodataTextView = (TextView) findViewById(R.id.showBiodataTextView);
//        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        Intent intent = getIntent();
        Integer position = intent.getIntExtra("position", -1);

        if(position == -1){
            Toast.makeText(getApplicationContext(), "Unable to display", Toast.LENGTH_LONG).show();
        }
        else{
            final Person person = showBiodata.persons.get(position);
//            Log.i("Photo URL", person.getPhotoUrl1());

            try{

                Picasso.with(this).
                        load(person.getPhotoUrl1())
                        .into(showImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try{
                Picasso.with(this)
                        .load(person.getPhotoUrl2())
                        .into(showImage2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (person.getGender() == null || person.getGender().equals(""))
                showGender.setText("");
            else if (person.getGender().equals("M"))
                showGender.setText("Male");
            else if (person.getGender().equals("F"))
                showGender.setText("Female");

            String isMangalik = ( person.isMangalik() == true )?"Mangalik":"";
            showMangalik.setText(isMangalik);

            String isDivorcee = ( person.isDivorcee() == true )?"Mangalik":"";
            showStatus.setText(isDivorcee);

            String name = ( person.getName() == null  || person.getName() == "")?"":person.getName();
            showName.setText(name);

            String tob = ( person.getTimeOfBirth() == null  || person.getTimeOfBirth().equals(""))?"":person.getTimeOfBirth() ;
            showTimeOfBirth.setText(tob);

            String height = ( person.getHeight() == null  || person.getHeight().equals(""))?"":person.getHeight() ;
            showHeight.setText( height);

            String pob = ( person.getPlaceOfBirth() == null  || person.getPlaceOfBirth() == "")?"":person.getPlaceOfBirth();
            showPlaceOfBirth.setText(pob);

            String education = ( person.getQualification() == null  || person.getQualification() == "")?"":person.getQualification();
            showEducation.setText(education);

            String occupation = ( person.getOccupation() == null  || person.getOccupation() == "")?"":person.getOccupation();
            showOccupation.setText(occupation);

            String clocation = ( person.getCurrLocation() == null  || person.getCurrLocation() == "")?"":person.getCurrLocation();
            showLocation.setText(clocation);

            Long income = person.getIncome();
            if( income > 0 ) showIncome.setText( Long.toString( income ) );

            int age = person.getAge();
            if( age > 0 ){
                showAge.setText( Integer.toString( age ) );
                myCalendar.setTime( person.getDob() );
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                showDateOFBirth.setText(sdf.format(myCalendar.getTime()));
            }

            Long budget = person.getBudget();
            if( budget > 0) showBudget.setText( Long.toString( budget));

            showBiodataTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{

                        File file=new File(getExternalFilesDir(null),"Dummy");
                        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(person.getBiodataUrl()))
                                .setTitle("Biodata")// Title of the Download Notification
                                .setDescription("Downloading")// Description of the Download Notification
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
                        downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        downloadID = downloadManager.enqueue(request);


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Can't open biodata", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
