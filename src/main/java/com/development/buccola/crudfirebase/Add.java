package com.development.buccola.crudfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by megan on 1/30/16.
 *
 */
public class Add extends AppCompatActivity {
    Firebase myFirebaseRef;
    Spinner spinnerYear, spinnerMonth, spinnerDay;
    EditText etFirstName, etLastName, etZip;
    String originalKey = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://crudbuccola.firebaseio.com/");
        setContentView(R.layout.dialog_add);

        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        etFirstName = (EditText) findViewById(R.id.etfirst_name);
        etLastName = (EditText) findViewById(R.id.etlast_name);
        etZip = (EditText) findViewById(R.id.etzip);
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForm();
            }
        });
        //loadYears
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getYears()); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(spinnerArrayAdapter);
        //Load Days
        ArrayAdapter<Integer> spinnerArray = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getDays()); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(spinnerArray);

        //Load Months
        ArrayAdapter<CharSequence> spinner_Adapter =ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(spinner_Adapter);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            originalKey = extras.getString("first") + " " + extras.getString("last");
            loadPage(extras.getString("first"),  extras.getString("last"),  extras.getString("zip"),  extras.getString("dob"));
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Add.this, MainActivity.class));
    }


    /*
    Get index of Birth month to set dropdown list
    Returns: int - index
     */
    private int getMonthIndex(String month){
        switch(month){
            case "Jan":
                return 0;
            case "Feb":
                return 1;
            case "March":
                return 2;
            case "April" :
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "Aug":
                return 7;
            case "Sept":
                return 8;
            case "Oct":
                return 9;
            case "Nov":
                return 10;
            case "Dec":
            default:
                return 11;
        }
    }

    /*
        Get index of birth year to set dropdown list
        Returns: int - index
     */
    private int getYearIndex(int year){
        return getCurrentYear()-year;
    }

    /*
        Get index of birth day of month to set dropdown list
        Returns: int-index
    */
    private int getDayIndex(int day){
        return --day;
    }

    /*
       Load page with the information of the person being edited
     */
    private void loadPage(String first, String last, String zip, String dob){
        Person person = new Person(first, last, dob, zip);
        spinnerYear.setSelection(getYearIndex(person.getYear()));
        spinnerMonth.setSelection(getMonthIndex(person.getMonth()));
        spinnerDay.setSelection(getDayIndex(person.getDay()));
        etFirstName.setText(person.getFirstName());
        etLastName.setText(person.getLastName());
        etZip.setText(person.getZip());

    }


    /*
        Validate form.
        Make sure all fields have been filled in and zip code is a valid length
     */
    private void checkForm() {
        // Reset errors.
        etFirstName.setError(null);
        boolean error = false;
        View focusView = null;

        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String zip = etZip.getText().toString();
        if(TextUtils.isEmpty((zip))){
            etZip.setError(getString(R.string.error_zip_required));
            focusView = etZip;
            error = true;
        }else if(zip.length() != 5){
            etZip.setError(getString(R.string.error_zip_invalid));
            focusView = etZip;
            error = true;
        }

        if(TextUtils.isEmpty(lastName)){
            etLastName.setError(getString(R.string.error_last_name_required));
            focusView = etLastName;
            error= true;
        }

        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError(getString(R.string.error_first_name_required));
            focusView = etFirstName;
            error = true;
        }

        if(error) {
            focusView.requestFocus();
        }else{
            addPerson(firstName, lastName, zip, getBDAY());
        }
    }

    /*
        If originalKey is null add a new person by creating a new key
        else edit the original person
     */
    private void addPerson(String first, String last, String zip, String dob){
        String message;
        if(originalKey == null) {
            Firebase usersRef = myFirebaseRef.child("people").child(first + " " + last);
            usersRef.child("first_name").setValue(first);
            usersRef.child("last_name").setValue(last);
            usersRef.child("zip").setValue(zip);
            usersRef.child("dob").setValue(dob);
            message = "Added " + first + " " + last + " successfully";
            Toast.makeText(Add.this, message, Toast.LENGTH_LONG).show();
        }else{
            Firebase usersRef = myFirebaseRef.child("people").child(originalKey);
            usersRef.child("first_name").setValue(first);
            usersRef.child("last_name").setValue(last);
            usersRef.child("zip").setValue(zip);
            usersRef.child("dob").setValue(dob);
            message = "Edited " + first + " " + last + " successfully";
            Toast.makeText(Add.this, message, Toast.LENGTH_LONG).show();
        }
        clearPage();
    }

    /*
         Reset textViews on page
     */
    private void clearPage() {
        etLastName.setText("");
        etFirstName.setText("");
        etZip.setText("");
    }

    /*
        Get selected items from the dropdown list to form the birtday as a string.
        RETURNS: String birthday format
     */
    private String getBDAY(){
        String month = spinnerMonth.getSelectedItem().toString();
        String year = spinnerYear.getSelectedItem().toString();
        String day = spinnerDay.getSelectedItem().toString();

        return month + " " + day + " " + year;
    }

    /*
        Create arrayList of days for the dropdown list
        Returns: ArrayList<Integer>
     */
    private ArrayList<Integer> getDays(){
        ArrayList<Integer> days = new ArrayList<>();
        for(int i=1; i<32; i++){
            days.add(i);
        }
        return days;
    }

    private int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /*
        Create ArrayList of years for the drop downList.
        Returns: ArrayList<Integer>
     */
    private ArrayList<Integer> getYears(){
        ArrayList<Integer> years = new ArrayList<>();
        for(int i=getCurrentYear(); i>1900; i--){
            years.add(i);
        }
        return years;
    }
}
