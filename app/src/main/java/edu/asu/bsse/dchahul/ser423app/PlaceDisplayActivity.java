package edu.asu.bsse.dchahul.ser423app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
/**
 * Copyright © 2018 Darya Scheiffele,
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: An app that provides information about various places stored in
 * a json file, ability to calculate great-circle spherical distance between any
 * of the 2 places in the json file.
 * @author Darya Scheiffele dchachul@asu.edu
 *         Software Engineering, Arizona State University
 * @version April 13, 2018
 */

public class PlaceDisplayActivity extends AppCompatActivity implements ListView.OnItemClickListener,DialogInterface.OnClickListener,AdapterView.OnItemSelectedListener{
    private PlaceLibrary places;
    private TextView placeName;
    private TextView gcd;
    private TextView bearing;
    private EditText placeDescription;
    private EditText placeCategory;
    private EditText placeAddrTitle;
    private EditText placeAddr;
    private EditText placeElevation;
    private EditText placeLatitude, placeLongitude;
    private PlaceDescription currentPlace;
    private String selectedPlace;

    private String[] listPlaces;
    private String[] availPrefixes;
    private String[] colLabels;
    private int[] colIds;
    private Spinner placeSpinner;
    private List<HashMap<String,String>> fillMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.place_display);
        gcd = (TextView)findViewById(R.id.gcd_tv);
        gcd.setVisibility(TextView.INVISIBLE);
        bearing = (TextView)findViewById(R.id.bearing_tv);
        bearing.setVisibility(TextView.INVISIBLE);
        placeName = (TextView)findViewById(R.id.place_name);
        placeDescription = (EditText) findViewById(R.id.place_description);
        placeCategory = (EditText) findViewById(R.id.place_category);
        placeAddrTitle = (EditText) findViewById(R.id.address_title);
        placeAddr = (EditText) findViewById(R.id.place_address);
        placeElevation = (EditText) findViewById(R.id.place_elevation);
        placeLatitude = (EditText) findViewById(R.id.place_latitude);
        placeLongitude= (EditText) findViewById(R.id.place_longitude);

        Intent intent = getIntent();
        places = intent.getSerializableExtra("places")!=null ? (PlaceLibrary) intent.getSerializableExtra("places") :
                new PlaceLibrary(this);
        Log.d("PlaceDisplayOnCreate", places.getNames().toString());
        selectedPlace = intent.getStringExtra("selected")!=null ? intent.getStringExtra("selected") : "unknown";
        currentPlace = places.get(selectedPlace);
        placeName.setText( currentPlace.getName());
        placeDescription.setText(currentPlace.getDescription());
        placeCategory.setText(currentPlace.getCategory());
        placeAddrTitle.setText(currentPlace.getAddressTitle());
        placeAddr.setText(currentPlace.getAddress());
        placeElevation.setText(currentPlace.getElevation());
        placeLatitude.setText(currentPlace.getLatitude());
        placeLongitude.setText(currentPlace.getLongitude());

        listPlaces = places.getNames();
        // availPrefixes = this.getResources().getStringArray(R.array.course_prefixes);
        placeSpinner = (Spinner)findViewById(R.id.placeSpinner);
        ArrayAdapter<String> anAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listPlaces);
        placeSpinner.setAdapter(anAA);
        placeSpinner.setOnItemSelectedListener(this);

        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception action bar: "+ex.getLocalizedMessage());
        }
        setTitle(currentPlace.name);
    }

    // create the menu items for this activity, placed in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.place_display_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected() id: "+item.getItemId()
                +" title "+item.getTitle());
        switch (item.getItemId()) {
            // the user selected the up/home button (left arrow at left of action bar)
            case android.R.id.home:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> home");
                Intent i = new Intent();
                i.putExtra("places", places);
                this.setResult(RESULT_OK,i);
                finish();
                return true;
            // the user selected the action (garbage can) to remove the student
            case R.id.action_remove:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> remove");
                this.removePlaceAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareAdapter(){
        PlaceDescription aPlace = places.get(selectedPlace);

        // the model
        // first row is header strings for the columns
        fillMaps = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> titles = new HashMap<>();
        titles.put("Prefix","Prefix");
        titles.put("Title","Title");
        fillMaps.add(titles);
    }

    //listview.onitemclicklistener method
    // log the selection when its not the header row.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        PlaceDescription aPlace = places.get(selectedPlace);
    }

    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.selectedPlace = placeSpinner.getSelectedItem().toString();
        android.util.Log.d(this.getClass().getSimpleName(),"Spinner item "+
                placeSpinner.getSelectedItem().toString() + " selected.");
    }

    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        android.util.Log.d(this.getClass().getSimpleName(),"In onNothingSelected: No item selected");

    }

    public void greatCircleDist (View v) {
        double distance = currentPlace.GreatCircleDistance(places.get(this.selectedPlace));
        gcd.setVisibility(TextView.VISIBLE);
        gcd.setText(distance + " ft");


    }

    public void bearing (View v) {
        double bearingVal = currentPlace.InitBearing(places.get(this.selectedPlace));
        bearing.setVisibility(TextView.VISIBLE);
        bearing.setText(bearing + " degrees");

    }




    // show an alert view for the user to confirm removing the selected place
    private void removePlaceAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Remove Place "+this.selectedPlace+"?");
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Remove", this);
        dialog.show();
    }

    // DialogInterface.onClickListener method. Gets called when negative or positive button is clicked
    // in the Alert Dialog created by the removePlaceAlert method.
    //@Override
    public void onClick(DialogInterface dialog, int whichButton) {
        android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+
                (whichButton==DialogInterface.BUTTON_POSITIVE));
        if(whichButton == DialogInterface.BUTTON_POSITIVE) {
            places.remove(this.selectedPlace);
            Intent i = new Intent();
            i.putExtra("places", places);
            this.setResult(RESULT_OK,i);
            finish();
        }
    }



}
