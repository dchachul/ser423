package edu.asu.bsse.dchahul.ser423app;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;



/**
 * Created by dscheiffele on 4/8/18.
 */

public class PlaceDescription implements Serializable {
    private static final long serialVersionUID = 26288982553932835L;
    static double PI_RAD = Math.PI / 180.0;
    static double EARTH_RAD = 20925721.78;
    public String name;
    private String description;
    private String category;
    private String addressTitle;
    private String address;
    private String elevation;
    private String latitude;
    private String longitude;


    /**
     ** Default
     */
    public PlaceDescription(){
        this.name = "";
        this.description = "";
        this.category = "";
        this.addressTitle = "";
        this.address = "";
        this.elevation = "";
        this.latitude = "";
        this.longitude = "";
    }
    /**
     * @param name
     * @param description
     * @param category
     * @param addressTitle
     * @param
     */
    public PlaceDescription(String name, String description, String category, String addressTitle, String addres, String elevation, String latitude, String longitutde) {
        super();
        this.name = name;
        this.description = description;
        this.category = category;
        this.addressTitle = addressTitle;
        this.address = addres;
        this.elevation = elevation;
        this.latitude = latitude;
        this.longitude = longitutde;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Get a JSONObject representation of PlaceDescription
     * @return JSONObject
     */
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("name", name);
        jsonObj.put("description", description);
        jsonObj.put("category", category);
        jsonObj.put("address-title", addressTitle);
        jsonObj.put("address-street", address);
        jsonObj.put("elevation", elevation);
        jsonObj.put("latitude", latitude);
        jsonObj.put("longitude", longitude);
        return jsonObj;
    }
    /**
     * Constructor with JSON Object.
     * @param jsonObject
     */
    public PlaceDescription(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.description = jsonObject.getString("description");
        this.category = jsonObject.getString("category");
        this.addressTitle = jsonObject.getString("address-title");
        this.address = jsonObject.getString("address-street");
        this.elevation = jsonObject.getString("elevation");
        this.latitude = jsonObject.getString("latitude");
        this.longitude = jsonObject.getString("longitude");



    }
    /**
     * Great circle distance between 2 places
     */
    public double GreatCircleDistance(PlaceDescription place2){

        double phi1 = Double.parseDouble(this.latitude) * PI_RAD;
        double phi2 = Double.parseDouble(place2.latitude) * PI_RAD;
        double lam1 = Double.parseDouble(this.longitude) * PI_RAD;
        double lam2 = Double.parseDouble(place2.longitude) * PI_RAD;

        return EARTH_RAD * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));


    }



}
