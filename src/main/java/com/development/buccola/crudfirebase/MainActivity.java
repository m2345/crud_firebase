package com.development.buccola.crudfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
/*
    Created by: Megan Buccola
    Date: 1/30/2016
    Purpose: TO implement basic CRUD operations using Firebase

// */
public class MainActivity extends AppCompatActivity implements ListViewAdapter.customButtonListener{
    Firebase myFirebaseRef;
    ListView listView;
    ArrayList<Person> people;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://crudbuccola.firebaseio.com/");
        setPeople();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Created by Megan Buccola", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listView = (ListView) findViewById(R.id.lvPeople);
        Button add = (Button) findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Add.class));
            }
        });

    }
    /*
        Load listView using custom listViewAdapter and people ArrayList
     */
    private void loadListView(){
        ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, android.R.layout.simple_list_item_1, people);
        adapter.setCustomButtonListener(this);
        listView.setAdapter(adapter);
    }

    /*
        Load People ArrayList by retrieving information from firebase.
     */
    private void setPeople(){
        people = new ArrayList<>();
        myFirebaseRef.child("people").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        String firstName = child.child("first_name").getValue().toString();
                        String lastName = child.child("last_name").getValue().toString();
                        String zip = child.child("zip").getValue().toString();
                        String dob = child.child("dob").getValue().toString();
                        people.add(new Person(firstName, lastName, dob, zip));

                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }
                }
                loadListView();
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Delete person from firebase and notify when successful
    @Override
    public void deletePerson(int position, Person person) {
        String key = person.getFirstName() + " " + person.getLastName();
        myFirebaseRef.child("people").child(key).removeValue();
        Toast.makeText(this, key +" has been deleted", Toast.LENGTH_LONG).show();
        //reload page
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    //Edit person. Pass current person info to Add.class
    @Override
    public void editPerson(int position, Person person) {
        Intent i = new Intent(MainActivity.this, Add.class);
        Bundle extra = new Bundle();
        extra.putString("first", person.getFirstName());
        extra.putString("last", person.getLastName());
        extra.putString("zip", person.getZip());
        extra.putString("dob", person.getDob());
        i.putExtras(extra);
        startActivity(i);
    }

}
