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
 * Created by dscheiffele on 4/8/18.
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
                this.removeStudentAlert();
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
        // now add the data for the remaining rows
     /*   Vector<Course> takes = this.sortTakes(aStud.takes);
        for (int i = 0; i < takes.size(); i++) {
            Course aCourse = takes.get(i);
            HashMap<String,String> map = new HashMap<>();
            Log.d(this.getClass().getSimpleName(),"mapping: "+aCourse.prefix+" "+aCourse.title);
            map.put("Prefix", aCourse.prefix);
            map.put("Title", aCourse.title);
            fillMaps.add(map);
        } */
    }

    //listview.onitemclicklistener method
    // log the selection when its not the header row.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        PlaceDescription aPlace = places.get(selectedPlace);
       /* if (position > 0 && position < aPlace.takes.size()+1) {
            String prefix = aStud.takes.get(position - 1).prefix;
            String title = aStud.takes.get(position - 1).title;
            android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " +
                    prefix + " " + title);
        } */
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

    public void addClicked (View v) {
        double distance = currentPlace.GreatCircleDistance(places.get(this.selectedPlace));
        gcd.setVisibility(TextView.VISIBLE);
        gcd.setText(distance + "ft");
        //this.prepareAdapter();
        //SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.course_list_item, colLabels, colIds);
        //courseLV.setAdapter(sa);

    }

    /*// remove the course selected from all courses spinner from the student's takes, if present
    public void removeClicked (View v) {
        int isWhere = this.findPrefix(students.get(this.selectedStud).takes,this.selectedCourse);
        // the course is not unknown and then the course is in takes
        if(!this.selectedCourse.equalsIgnoreCase("unknown") && isWhere > -1){
            students.get(this.selectedStud).takes.remove(isWhere);
        }
        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.course_list_item, colLabels, colIds);
        courseLV.setAdapter(sa);
    } */

    // returns -1 if the course prefix is not found in takes. Otherwise, the index into the vector
    // where it is found.
   /* private int findPrefix(Vector<Course> takes, String prefix) {
        int ret = -1;
        for(int i=0; i<takes.size(); i++){
            Course aCrs = takes.get(i);
            if (aCrs.prefix.equalsIgnoreCase(prefix)){
                ret = i;
            }
        }
        return ret;
    } */


    // show an alert view for the user to confirm removing the selected student
    private void removeStudentAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Remove Student "+this.selectedPlace+"?");
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Remove", this);
        dialog.show();
    }

    // DialogInterface.onClickListener method. Gets called when negative or positive button is clicked
    // in the Alert Dialog created by the newStudentAlert method.
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
