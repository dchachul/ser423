package edu.asu.bsse.dchahul.ser423app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.res.AssetManager;
import android.text.style.ReplacementSpan;
import android.util.Log;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.OutputStream;
import java.sql.SQLException;
/**
 *
 * Created by dscheiffele on 4/8/18.
 */

public class PlaceLibrary extends Object implements Serializable {
    private static final String jsonFileName = "places.json";

    public Hashtable<String, PlaceDescription> places;
    private static final boolean debugOn = false;

    /**
     * Default constructor. Will attempt to read a library file that has been saved.
     */
    public PlaceLibrary(Activity parent) {
        places = new Hashtable<String, PlaceDescription>();
        try {
            this.restoreFromFile(parent);
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "error resetting from students json file" + ex.getMessage());
        }

    }


    /**
     * Restores a place library from the last saved or opened json library.
     *
     * @return boolean
     */
    public boolean restoreFromFile(Activity parent) {
        boolean restored = false;
        //this.eventMap = new HashMap<String, PlaceDescription>();
        InputStream inputStream = null;
        try {
            inputStream = parent.getApplicationContext().getResources().openRawResource(R.raw.places);
            if (inputStream != null) {
                Log.d("File open", "Places description library found under: " + jsonFileName);
                System.out.println("Places description library found under: " + jsonFileName);
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                String result = sb.toString();
                JSONObject obj = new JSONObject(result);
                List<String> keyList = new ArrayList<String>();
                for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                    String key = it.next();
                    keyList.add(key);
                }
                String[] places = keyList.toArray(new String[keyList.size()]);
                for (int i = 0; i < places.length; i++) {
                    PlaceDescription placeDesc = new PlaceDescription((JSONObject) obj.getJSONObject(places[i]));
                    if (placeDesc != null)
                        this.places.put(placeDesc.getName(), placeDesc);
                        Log.d("Place", placeDesc.getName());
                }
                restored = true;
            } else {
                System.out.println("Error loading library file.");
            }
        } catch (Exception e) {
            System.out.println("Library File Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        return restored;
    }

    public String[] getNames() {
        String[] ret = {};
      //  debug("getting " + places.size() + " place names.");
        if (places.size() > 0) {
            ret = (String[]) (places.keySet()).toArray(new String[0]);
        }
        return ret;
    }

    public String[] getDescriptions() {
        String[] ret = {};
        //  debug("getting " + places.size() + " place names.");
        if (places.size() > 0) {
            ret = (String[]) (places.keySet()).toArray(new String[1]);
        }
        return ret;
    }

    public boolean add(PlaceDescription aPlace) {
        boolean ret = true;
        //debug("adding student named: " + ((aStud == null) ? "unknown" : aStud.name));
        try {
            places.put(aPlace.name, aPlace);
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }

    public boolean remove(String aPlace) {
        //debug("removing place named: " + aName);
        return ((places.remove(aPlace) == null) ? false : true);
    }

    public PlaceDescription get(String aName) {
        PlaceDescription ret = new PlaceDescription();
        PlaceDescription aPlace = places.get(aName);
        if (aPlace != null) {
            ret = aPlace;
        }
        return ret;
    }





}
