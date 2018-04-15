package edu.asu.bsse.dchahul.ser423app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import android.widget.SearchView;
import android.database.sqlite.SQLiteDatabase;
import android.app.SearchManager;


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


public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, DialogInterface.OnClickListener{

    private ListView placeLV;
    private PlaceLibrary places;  // a collection of places (serializable)
    private String[] placeNames ;//= places.getNames();
    private EditText nameBox, descriptionBox, categoryBox, addrTitleBox, addrBox, elevationBox, latitudeBox, longitudeBox;

    private String[] colName = {"Name", "Description"};
    private int[] colIDs;
    private List<HashMap<String,String>> fillMaps;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeLV = (ListView)findViewById(R.id.placeList);

        places = new PlaceLibrary(this);
        placeNames = places.getNames();
        Log.d("Place1", placeNames[1]);

        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colName, colIDs);
        placeLV.setAdapter(sa);
        placeLV.setOnItemClickListener(this);
        Log.d("colName", colName[1]);
        Log.d("colID", "column" + colIDs[1]);
       // Log.d("Adapter", sa.getItem(2).toString());

        setTitle("Places");
    }
    // this method generates the data needed to create a new list view simple adapter.
    private void prepareAdapter(){
        Log.d("column1", colName[1]);
        colIDs = new int[] {R.id.place_name, R.id.place_description};
        this.placeNames = places.getNames();
        fillMaps = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> titles = new HashMap<>();
        // first row contains column headers
        titles.put("Name","Name");
        titles.put("Description","Description");
        fillMaps.add(titles);
        Log.d("ArrayPlace0",  placeNames[0]);
        // fill in the remaining rows with first last and student id.
       for (int i = 0; i < placeNames.length; i++) {
            //String[]firstNLast = placeNames[i].split(" ");
            HashMap<String,String> map = new HashMap<>();
            map.put("Name", placeNames[i]);
            map.put("Description", (places.get(this.placeNames[i]).getDescription()).toString());
            Log.d("PlaceDescription", map.toString());
            fillMaps.add(map);
        }
        Log.d("fillMaps", fillMaps.toString());
    }

    // create the menu items for this activity, placed in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Handle selections of action bar menu items. These are defined a menu.xml resource that is
    // inflated by the onCreateOptionsMenu method. Note that the home action bar (back arrow) is
    // placed by default in any action bar for an activity that is defined with a parent activity
    // in manifest.xml. In this example, StudentDisplayActivity's action bar has an arrow which
    // appears in the left of the action bar. Selecting that arrow is handled by the case:
    // onOptionsItemSelected method for that activity in:    case android.R.id.home:
    // The built-in icons that you can place in an action bar (and elsewhere) are
    // defined in the android.R.drawable static final class. See:
    //   https://developer.android.com/reference/android/R.drawable.html
    // these are referenced in the menu.xml, such as:
    //         android:icon="@android:drawable/ic_menu_delete"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_add:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> add");
                this.newPlaceAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //listview.onitemclicklistener method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        String[] placeNames = places.getNames();
        //Arrays.sort(placeNames);
        if(position > 0 && position <= placeNames.length) {
            android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " + placeNames[position-1]);
            Intent displayPlace = new Intent(this, PlaceDisplayActivity.class);
            displayPlace.putExtra("places", places);
            displayPlace.putExtra("selected", placeNames[position-1]);
            this.startActivityForResult(displayPlace, 1);
        }
    }

    // called when the finish() method is called in the StudentDisplayActivity. This occurs
    // when done displaying (and possibly modifying students). In case a student has been removed,
    // must update the list view (via a new adapter).
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        places = data.getSerializableExtra("places")!=null ? (PlaceLibrary) data.getSerializableExtra("places") : places;
        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colName, colIDs);
        placeLV.setAdapter(sa);
        placeLV.setOnItemClickListener(this);
    }

    private void newPlaceAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Please fill out to add new place");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        this.nameBox = new EditText(this);
        nameBox.setHint("Name");
        layout.addView(nameBox);

        this.descriptionBox = new EditText(this);
        descriptionBox.setHint("Description");
        layout.addView(descriptionBox);

        this.categoryBox = new EditText(this);
        categoryBox.setHint("Category");
        layout.addView(categoryBox);

        this.addrTitleBox = new EditText(this);
        addrTitleBox.setHint("Address Title");
        layout.addView(addrTitleBox);

        this.addrBox = new EditText(this);
        addrBox.setHint("Address");
        layout.addView(addrBox);

        this.elevationBox = new EditText(this);
        elevationBox.setHint("Elevation");
        elevationBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(elevationBox);

        this.latitudeBox = new EditText(this);
        latitudeBox.setHint("Latitude");
        latitudeBox.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(latitudeBox);

        this.longitudeBox = new EditText(this);
        longitudeBox.setHint("Longitude");
        longitudeBox.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(longitudeBox);


        dialog.setView(layout);
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Add", this);
        dialog.show();
    }

    // DialogInterface.onClickListener method. Gets called when negative or positive button is clicked
    // in the Alert Dialog created by the newStudentAlert method.
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+
                (whichButton==DialogInterface.BUTTON_POSITIVE));
        if(whichButton == DialogInterface.BUTTON_POSITIVE) {
            String name = nameBox.getText().toString();
            String description = descriptionBox.getText().toString();
            String category = categoryBox.getText().toString();
            String addrTitle = addrTitleBox.getText().toString();
            String addr = addrBox.getText().toString();
            String elev = elevationBox.getText().toString();
            String latitude = latitudeBox.getText().toString();
            String longitude = longitudeBox.getText().toString();
            //int num = idBox.getText().toString().equals("") ? 0 : Integer.parseInt(idBox.getText().toString());
            places.add(new PlaceDescription(name, description, category, addrTitle, addr, elev, latitude, longitude));
            prepareAdapter();
            SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colName, colIDs);
            placeLV.setAdapter(sa);
        }
    }


}
