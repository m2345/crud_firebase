package com.development.buccola.crudfirebase;

import android.util.Log;

/**
 * Created by megan on 1/30/16.
 *
 */
public class Person {
    String firstName, lastName, dob, zip;
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getYear(){
        Log.e("getyear", this.dob);
        return Integer.parseInt((dob.split(" ")[2]).trim());
    }
    public int getDay(){
        return Integer.parseInt((dob.split(" ")[1]).trim());
    }
    public String getMonth(){
        return dob.split(" ")[0].trim();
    }

    Person(String firstname,String lastname, String dob, String zip){
        Log.e("person constructor", dob);
        this.firstName = firstname;
        this.lastName = lastname;
        this.dob = dob;
        this.zip = zip;
    }


}
