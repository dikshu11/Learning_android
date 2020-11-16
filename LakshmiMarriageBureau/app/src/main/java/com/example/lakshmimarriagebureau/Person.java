package com.example.lakshmimarriagebureau;

import java.util.Calendar;
import java.util.Date;

public class Person {

    private String name="";
    private String placeOfBirth = "";
    private String gender = "";
    private String currLocation = "";
    private String qualification = "";

    private String height= "";
    private Long income = (long) -1;

    private Date dob;
    private String timeOfBirth = "";

    private String contact = "", whatsapp_contact = "";

    private boolean isDivorcee = false;
    private boolean isMangalik = false;
    private boolean doesBuisness = false;
    private String occupation = "";
    private Long budget = (long) -1;
    private String photoUrl1 = null, photoUrl2 = null, biodataUrl = null;

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public boolean isDivorcee() {
        return isDivorcee;
    }

    public void setDivorcee(boolean divorcee) {
        isDivorcee = divorcee;
    }

    public boolean isMangalik() {
        return isMangalik;
    }

    public void setMangalik(boolean mangalik) {
        isMangalik = mangalik;
    }

    public boolean isDoesBuisness() {
        return doesBuisness;
    }

    public void setDoesBuisness(boolean doesBuisness) {
        this.doesBuisness = doesBuisness;
    }

    public String getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(String currLocation) {
        this.currLocation = currLocation;
    }

    public Long getIncome() {
        return income;
    }

    public void setIncome(Long income) {
        this.income = income;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getWhatsapp_contact() {
        return whatsapp_contact;
    }

    public void setWhatsapp_contact(String whatsapp_contact) {
        this.whatsapp_contact = whatsapp_contact;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }


    public String getPhotoUrl2() {
        return photoUrl2;
    }


    public void setPhotoUrl2(String photoUrl2) {
        this.photoUrl2 = photoUrl2;
    }

    public String getBiodataUrl() {
        return biodataUrl;
    }

    public void setBiodataUrl(String biodataUrl) {
        this.biodataUrl = biodataUrl;
    }

    public int getAge() {

        Calendar dob = Calendar.getInstance();
        dob.setTime( this.dob );
        Calendar today = Calendar.getInstance();

        try{
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//
//            Log.i("Age: ", Integer.toString( today.get(Calendar.YEAR)) );
//            Log.i("Age: ", Integer.toString( dob.get(Calendar.YEAR)) );
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
                age--;
            return age;
        }catch (Exception e){
            return  -1;
        }

    }

    public String getPhotoUrl1() {
        return photoUrl1;
    }

    public void setPhotoUrl1(String photoUrl1) {
        this.photoUrl1 = photoUrl1;
    }
}
