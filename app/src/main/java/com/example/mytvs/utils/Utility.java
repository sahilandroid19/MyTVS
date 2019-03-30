package com.example.mytvs.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.mytvs.model.Person;
import com.example.mytvs.retrofit.DataResponse;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utility {

    public static final String TOAST_MSG = "\"username\":\"test\", \n" + "\"password\":\"123456\" \n ";
    public static final String INTENT_USERNAME = "username";
    public static final String INTENT_PASSWORD = "password";

    public static final String INTENT_PERSON = "person";

    public static final String INTENT_MAP = "map";
    public static final String INTENT_LOCATION = "location";

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    /*
    Method to fetch person objects from JSON data
     */
    public static List<Person> getPersonList(DataResponse dataResponse){
        List<List<String>> dataList = dataResponse.getDataList();
        List<Person> personList = new ArrayList<>();

        for(List<String> list : dataList){
            Person person = new Person();
            for(int i=0; i<list.size(); i++){
                switch (i) {
                    case 0:
                        person.setName(list.get(i));
                        break;
                    case 1:
                        person.setOccupation(list.get(i));
                        break;
                    case 2:
                        person.setLocation(list.get(i));
                        break;
                    case 3:
                        person.setPinCode(list.get(i));
                        break;
                    case 4:
                        person.setDate(list.get(i));
                        break;
                    case 5:
                        person.setSalary(list.get(i));
                        break;
                }
            }
            personList.add(person);
        }
        return personList;
    }

    /*
    Returns lat lng from location name
     */
    public static ArrayList<LatLng> getLatLong(Context context, String location){
        ArrayList<LatLng> latLngs = new ArrayList<>();
        if(Geocoder.isPresent()){
            try {
                Geocoder geocoder = new Geocoder(context);
                List<Address> addresses = geocoder.getFromLocationName(location, 5);

                latLngs = new ArrayList<>(addresses.size());
                for(Address address : addresses){
                    if(address.hasLatitude() && address.hasLongitude()){
                        latLngs.add(new LatLng(address.getLatitude(), address.getLongitude()));
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return latLngs;
    }

    /*
    Returns date in specified format
     */
    public static String getDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        return format.format(Date.parse(date));
    }

    /*
    Checks internet connectivity
     */
    public static boolean isInternetAvailable() {
        final String command = "ping -c 1 google.com";
        try {
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
