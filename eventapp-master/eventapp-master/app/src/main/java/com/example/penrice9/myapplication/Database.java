package com.example.penrice9.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eventsDb.db";  //Database name
    private static final String TABLE_NAME_LOGIN = "loginTable";  //Tables
    private static final String TABLE_NAME_EVENT = "eventTable";
    private static final String TABLE_NAME_TAGS = "tags";
    private static final String colPassword = "password";



    private static SQLiteDatabase dbr;
    private static SQLiteDatabase dbw;
    private SQLiteDatabase db;
    private static Cursor res;
    private static String OriginalUsername;
    public  Boolean notif;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 5);
       //Constructor of Database class.
        this.dbw = getWritableDatabase();
        this.dbr = getReadableDatabase();
        this.notif = true;



    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_LOGIN + "(id integer primary key autoincrement, username String, password String)"); //Creates table with different coloums.
        db.execSQL("create table " + TABLE_NAME_TAGS + "(id integer primary key autoincrement, name String, value Integer, counter Integer)");
        db.execSQL("create table " + TABLE_NAME_EVENT + "(id integer primary key autoincrement, name String, date Integer, location String, website String, tags String, rating float, attend Integer, image String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS loginTable");
        db.execSQL("DROP TABLE IF EXISTS tags");
        db.execSQL("DROP TABLE IF EXISTS eventTable");
        onCreate(db);
    }


    void insertUserData(String GivenUsername, String GivenPassword) {
        //returns id
        res = dbr.rawQuery("select id from loginTable", null);
        //adds one to the number of columns
        int id = res.getCount() + 1;
        //inserts new details in the position pointed to by 'id' variable
        dbw.execSQL("INSERT INTO loginTable (id,username,password) VALUES ('" + id + "', '" + GivenUsername + "', '" + GivenPassword + "')");
        res.close();
    }


   void setUsername(String givenUsername){
        Log.d("Before", givenUsername);
        OriginalUsername = givenUsername;
        Log.d("After", OriginalUsername);
    }

    Boolean fetchDetails(String GivenUserName, String GivenPassword, Context c) {
        Boolean result;
        String password;
        try {
            res = dbr.query(TABLE_NAME_LOGIN, new String[]{colPassword},"username = ?", new String[]{GivenUserName}, null, null, null);
            //The query takes the table name, columns to return, conditions to be satisfied and data to be queried.
            res.moveToFirst();  //Pointer moved to first value of the cursor.
            password = res.getString(0);
            res.close();
            result = password.equals(GivenPassword);
            if(!result){
                Toast.makeText(c, "Password incorrect", Toast.LENGTH_LONG).show();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            Toast.makeText(c, "Username incorrect", Toast.LENGTH_LONG).show();//Needed to prevent a crash when data is not found.
            result = false;
        }

        return result; //Returns true/false.
    }

    ArrayList<TimeLineItem> timelineList() {
        TimeLineItem obj;
        ArrayList<TimeLineItem> results = new ArrayList<>();

        try {
            res = dbr.rawQuery("select * from " + TABLE_NAME_EVENT + " where attend = 1", null);
            if (res.moveToFirst()) {
                results = new ArrayList<>();
            }
            do {
                obj = new TimeLineItem(res.getString(1), Integer.parseInt(res.getString(2)));
                results.add(obj);
            } while (res.moveToNext());

            res.close();
        } catch (CursorIndexOutOfBoundsException e) {
            return results;
        }
        return results;
    }

    ArrayList<TimeLineItem> orderArray(ArrayList<TimeLineItem> arrayList) {
       //instantiates new TimlineComparator
        TimelineComparator comparator = new TimelineComparator();
        //Uses collections to sort the array, passing array to be sorted and comparator
        Collections.sort(arrayList, comparator);
        return arrayList;
    }


    ArrayList<BrowseElement> eventList() {
        ArrayList<BrowseElement> results = new ArrayList<>();

        try {
            res = dbr.rawQuery("select * from " + TABLE_NAME_EVENT + " where attend = 0", null);
            if (res.moveToFirst()) {
                results = new ArrayList<>();
            }
            do {
                results.add(new BrowseElement(res.getString(1),res.getString(8), res.getFloat(6)));
            } while (res.moveToNext());

            res.close();
        } catch (CursorIndexOutOfBoundsException e) {
            return results;
        }
        return results;
    }

    ArrayList<String> getImages() {
        ArrayList<String> array = null;

        try {
            //returns all data from event table
            res = dbr.rawQuery("select * from " + TABLE_NAME_EVENT, null);
            if (res.moveToFirst()) {
                array = new ArrayList<>();
            }
            do {
                //adds the images to array.
                array.add(res.getString(8));
            } while (res.moveToNext());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        return array;
    }

    Boolean checkAttend(String eventName) {
        boolean result;
        //query which returns the value in the attend column, used to set the switch either on or off.
        res = dbr.rawQuery("select attend from " + TABLE_NAME_EVENT + " where name = '" + eventName + "'", null);

        res.moveToFirst();
        //if the value in the database is 1, result is true, else it is false
        result = res.getInt(0) == 1;
        res.close();
        return result;
    }

    String[] getEventData(String eventName) {
        String[] results = {"Data not found", "", "", ""};
        res = dbr.rawQuery("select location, date, website, image from " + TABLE_NAME_EVENT + " where name = '" + eventName.trim() + "'", null);
        res.moveToFirst();
        int i = 0;
        while (i <= res.getColumnCount() - 1) {
            results[i] = res.getString(i);
            i++;
        }
        res.close();
        return results;
    }


    Boolean changeAttend(String eventName) {
        Integer changeData;
        String attend = "attend";

        res = dbw.rawQuery("select attend from " + TABLE_NAME_EVENT + " where name = '" + eventName + "'", null);
        res.moveToFirst();
        Integer result = Integer.parseInt(res.getString(0));
        if (result == 1) {
            changeData = 0;
            updateTags(getTags(eventName), false,1);
        } else {
            changeData = 1;
            updateTags(getTags(eventName), true,1);
        }
        res.close();
        ContentValues content = new ContentValues();
        content.put(attend, changeData);
        dbw.update(TABLE_NAME_EVENT, content, "name = ?", new String[]{eventName});

        updateAll();
        return true;
    }
    String getRank(String name){
        res = dbr.rawQuery("select rating from " + TABLE_NAME_EVENT + " where name = '"+name+"'", null);
        res.moveToFirst();
        String result = res.getString(0);
        res.close();
        return result;
    }

    private void updateAll() {
        res = dbr.rawQuery("select name from " + TABLE_NAME_EVENT, null);
        String[] names = new String[res.getCount()];
        Log.d("Counter", String.valueOf(res.getCount()));
        int counter = 0;
        res.moveToFirst();
       do {
            names[counter] = res.getString(0);
            counter++;
        } while (res.moveToNext());
        res.close();
        Log.d("Length:", String.valueOf(names.length));
        for (int i = 0; i < names.length-1; i++) {
            Log.d("In function:", names[i]);
            addRating(calculateRank(lookupValues(getTags(names[i])), names[i]), names[i]);
        }

    }

    Boolean findUsername(String name) {
        Boolean result;
        try {
            //returns username to check if it exists
            res = dbr.rawQuery("select username from " + TABLE_NAME_LOGIN + "where username = " + name, null);
            result = false;
        } catch (SQLiteException e) {
            e.printStackTrace();
            //if exception is found, it means the username doesn't exist.
            result = true;
        }

        return result;
    }

    private String[] getTags(String name) {
        //returns string of tags which are separated by comma
        res = dbr.rawQuery("select tags from " + TABLE_NAME_EVENT + " where name = '" + name + "'", null);
        res.moveToFirst();
        String tagString = res.getString(0);
        res.close();
        //tags split into individual elements of array
        String[] tags = tagString.split(",");
        return tags;
    }

    private int[][] lookupValues(String[] tags) {
        //takes tags as parameter
        int[][] tagValues = new int[tags.length][tags.length];
        for (int i = 0; i < tags.length; i++) {
            Log.d("Debug", tags[i]);
            //looks up value and counter from tag table
            res = dbr.rawQuery("select value, counter from " + TABLE_NAME_TAGS + " where name = '" + tags[i] + "'", null);
            res.moveToFirst();
            Log.d("Debug:", res.getString(0));
            //adds value and counter to 2d-array
            tagValues[i][0] = Integer.parseInt(res.getString(0));
            tagValues[i][1] = Integer.parseInt(res.getString(1));
            res.close();

        }
        return tagValues;
    }

    private float calculateRank(int[][] tagValues, String eventName) {
        int[] result = new int[tagValues.length];
        int sum = 0;
        float DAMPING_FACTOR = 0.95f;
        float rank;
        //'for' loop calculates the sum of all values multiplied by counter.
        for (int i = 0; i < tagValues.length; i++) {
            int answer = tagValues[i][0] * tagValues[i][1];
            result[i] = answer;
        }
        Calendar now = Calendar.getInstance();
        Log.d("Debug:", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
        //takes date of event in milliseconds and subtracts current day in milliseconds.
        long timeMultiplier = ((getDateOfEvent(String.valueOf(getEventDate(eventName))).getTimeInMillis() - now.getTimeInMillis())*-1)/1000000;
        Log.d("Time Multiplier", String.valueOf(timeMultiplier));
        //'for' loop adds sum of all tags for the event.
        for (int i = 0; i < result.length; i++) {
            sum = sum + result[i];
        }
        //calculates the rank
        rank = (DAMPING_FACTOR*sum) / timeMultiplier;
        return rank;

    }





    private int getEventDate(String eventName) {
        //returns date of event from database
        res = dbr.rawQuery("select date from " + TABLE_NAME_EVENT + " where name = '" + eventName + "'", null);
        res.moveToFirst();
        Log.d("Date:", res.getString(0));
        int date = Integer.parseInt(res.getString(0));
        res.close();
        return date;
    }

    private void addRating(float rank, String eventName) {
        ContentValues content = new ContentValues();
        //adds rank to the ContentValues object to update the database
        content.put("rating", rank);
        //database updated
        dbw.update(TABLE_NAME_EVENT, content, "name = ?", new String[]{eventName});
        Log.d("Update: ", "Operation complete");
    }

    private void updateTags(String[] tags, Boolean add, int amount) {
        for (int i = 0; i < tags.length; i++) {
            Log.d("Debug", tags[i]);
            //returns counter value
            res = dbr.rawQuery("select counter from " + TABLE_NAME_TAGS + " where name = '" + tags[i] + "'", null);
            res.moveToFirst();
            ContentValues content = new ContentValues();
            //changes the value of counter depending on boolean 'add'
            if (add) {
                content.put("counter", res.getInt(0) + amount);
            }
            else{
                content.put("counter", res.getInt(0) - amount);
            }
            res.close();
            //updates databsae
            dbw.update(TABLE_NAME_TAGS, content, "name = ?", new String[]{tags[i]});
        }

    }

    Calendar getDateOfEvent(String dateString){
        Calendar date = Calendar.getInstance();
        Log.d("Date", dateString.substring(0,2));
        //defines day value from string parameter
        date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(0,2)));
        Log.d("Date", dateString.substring(3,5));
        //defines month value from string parameter. Uses base index 0.
        date.set(Calendar.MONTH, Integer.parseInt(dateString.substring(3,5))-1);
        Log.d("Date", dateString.substring(6));
        //defines year value from string parameter
        date.set(Calendar.YEAR, Integer.parseInt(dateString.substring(6)));
        Log.d("Date", String.valueOf(date.get(Calendar.DAY_OF_MONTH)));
        return date;
    }

    ArrayList<BrowseElement> orderArraySuggestions(ArrayList<BrowseElement> arrayList) {
        SuggestionsComparator comparator = new SuggestionsComparator();
        Collections.sort(arrayList, comparator);
        return arrayList;
    }

    public Boolean settingsFunction(String password){
        Boolean complete;
        ContentValues content = new ContentValues();
        content.put("password", password);
        try {
            //updates the login table using the ContentValues and the data added to them using the 'put' method.
            dbw.update(TABLE_NAME_LOGIN, content, "username = ?", new String[]{OriginalUsername});
            Log.d("Username", OriginalUsername);
            complete = true;
        }catch (SQLiteException e){ //catches the exception to prevent a crash
            e.printStackTrace();
            complete = false;

        }

        return complete;
    }

    void notificationCheck(Switch mSwitch){
        notif = mSwitch.isChecked();
    }

    String formatDate(Integer date) {
        String dateString = date.toString();
        String days = dateString.substring(6, 8);
        String months = dateString.substring(4, 6);
        String years = dateString.substring(0, 4);

        String fDate = days + "/" + months + "/" + years;

        return fDate;
    }
    int getId(String name){
        res = dbr.rawQuery("select id from " + TABLE_NAME_EVENT +" where name = '" + name +"'", null);
        res.moveToFirst();
        String id = res.getString(0);
        return Integer.parseInt(id);
    }
    public static class updateEventTable extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Retrieves data from URL
                InputStream eventInput = new URL("https://www.bayofrum.net/~ollie/eventsdb.xml").openStream();
                Log.d("Background", "Background working");
                //XmlPullParserFactory used to create parser
                XmlPullParserFactory factory = null;
                factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                //adds the inputStream to the parser
                parser.setInput(eventInput, null);
                //ArrayList<Object> elements = null;u
                Integer id = null, date = null, attend = null;
                Float rating = null;
                String name = null, location = null, website = null, tags = null, image = null;

                int type = parser.getEventType();
                Event e = null;
                ArrayList<Event> eventArray = new ArrayList<>();

                while (type != XmlPullParser.END_DOCUMENT) {
                    String tagName = null;
                    Boolean complete = false;

                    switch (type) {
                        case XmlPullParser.START_TAG:
                            tagName = parser.getName();
                            //signifies start of new record
                            if (tagName.equals("record")) {
                                //used to signify end of record
                                complete = false;
                            } else if (eventArray != null) {
                                if (tagName.equals("ID")) {
                                    id = (Integer.parseInt(parser.nextText()));
                                } else if (tagName.equals("name")) {
                                    name = parser.nextText();
                                } else if (tagName.equals("location")) {
                                    location = parser.nextText();
                                } else if (tagName.equals("date")) {
                                    date = Integer.parseInt(parser.nextText());
                                } else if (tagName.equals("website")) {
                                    website = parser.nextText();
                                } else if (tagName.equals("tags")) {
                                    tags = parser.nextText();
                                } else if (tagName.equals("rating")) {
                                    rating = Float.valueOf(parser.nextText());
                                } else if (tagName.equals("attend")) {
                                    attend = Integer.parseInt(parser.nextText());
                                } else if (tagName.equals("image")) {
                                    image = (parser.nextText());
                                    complete = true;
                                }
                            }
                            break;
                    }
                    if (complete) {
                        e = new Event(id, name, date, location, website, rating, attend, tags, image);
                        eventArray.add(eventArray.size(), e);
                        ContentValues values;
                        res = dbr.rawQuery("SELECT id FROM " + TABLE_NAME_EVENT, null);
                        res.moveToLast();
                        //gets number of records in database
                        int start = res.getCount();
                        res.close();
                        int counter = 0, c = eventArray.size();
                        ArrayList<Event> newList = new ArrayList<>();

                        while (c > start) {
                            newList.add(counter, eventArray.get(start));
                            counter++;
                            start++;
                        }
                        //writes data to database
                        dbw.beginTransaction();
                        try {
                            for (Event items : newList) {
                                values = new ContentValues();
                                values.put("ID", items.id);
                                values.put("name", items.name);
                                values.put("date", items.date);
                                values.put("location", items.location);
                                values.put("website", items.website);
                                values.put("tags", items.tags);
                                values.put("rating", items.rating);
                                values.put("attend", items.attend);
                                values.put("image", items.image);
                                dbw.insert(TABLE_NAME_EVENT, null, values);
                            }
                            dbw.setTransactionSuccessful();
                        } finally {
                            dbw.endTransaction();
                        }
                    }
                    Log.d("debug", String.valueOf(eventArray));
                    type = parser.next();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

    return null;
    }



    }

    public static class updatetagTable extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ArrayList<tagsTableClass> tags = new ArrayList<>();
                //ArrayList<Object> elements = null;
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser tagParser = factory.newPullParser();
                InputStream tagInput = new URL("https://www.bayofrum.net/~ollie/tags.xml").openStream();
                tagParser.setInput(tagInput, null);
                //ArrayList<Object> elements = null;

                Integer id = null, counter = null, value = null;
                String name = null;

                int type = tagParser.getEventType();
                tagsTableClass t = null;

                while (type != XmlPullParser.END_DOCUMENT) {
                    String tagName = null;
                    Boolean complete = false;

                    switch (type) {
                        case XmlPullParser.START_TAG:
                            tagName = tagParser.getName();

                            if (tagName.equals("record")) {
                                complete = false;
                            } else if (tags != null) {
                                if (tagName.equals("ID")) {
                                    id = (Integer.parseInt(tagParser.nextText()));
                                } else if (tagName.equals("name")) {
                                    name = tagParser.nextText();
                                } else if (tagName.equals("value")) {
                                    value = (Integer.parseInt(tagParser.nextText()));
                                } else if (tagName.equals("counter")) {
                                    counter = (Integer.parseInt(tagParser.nextText()));
                                    complete = true;
                                }
                            }
                            break;
                    }
                    if (complete) {
                        t = new tagsTableClass(id, name, value, counter);
                        tags.add(tags.size(), t);
                        ContentValues values;
                        res = dbr.rawQuery("SELECT id FROM " + TABLE_NAME_TAGS, null);
                        res.moveToLast();
                        int start = res.getCount();
                        res.close();
                        int itemCounter = 0, c = tags.size();
                        ArrayList<tagsTableClass> newList = new ArrayList<>();

                        while (c > start) {
                            newList.add(itemCounter, tags.get(start));
                            counter++;
                            start++;
                        }
                        dbw.beginTransaction();
                        try {
                            for (tagsTableClass items : newList) {
                                values = new ContentValues();
                                values.put("ID", items.getID());
                                values.put("name", items.getName());
                                values.put("value", items.getValue());
                                values.put("counter", items.getCounter());
                                dbw.insert(TABLE_NAME_TAGS, null, values);
                            }
                            dbw.setTransactionSuccessful();
                        } finally {
                            dbw.endTransaction();
                        }
                    }
                    type = tagParser.next();
                }


            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


       /* private static Integer fetchLast(Boolean event) {
        int result;
        if (event)
            res = dbr.rawQuery("SELECT id FROM " + TABLE_NAME_EVENT, null);
        else{
            res = dbr.rawQuery("SELECT id FROM " + TABLE_NAME_TAGS, null);
        }
        res.moveToLast();
        result = res.getCount();
        res.close();
        return result;
    }*/

    public void newTopics(String name){
        switch (name){
            case "sport":
                //updates 'livesport' tag
                updateTags(new String [] {"livesport"}, true, 3);
                break;
            case "music":
                //updates 'music' tag
                updateTags(new String [] {"music"}, true, 3);
                break;
            case "comedy":
                //update 'comedy' tag
                updateTags(new String [] {"comedy"}, true, 3);
        }
    }

    public ArrayList<String> passwords(){
        ArrayList<String> result = new ArrayList<>();
        res = dbr.rawQuery("select password from " + TABLE_NAME_LOGIN, null);
        res.moveToFirst();
        int i = 0;
        while (i < res.getCount()){
            result.add(res.getString(0));
            res.moveToNext();
            i++;
        }
        res.close();
        return result;
    }
}





/*

    public String fetchLast(String eventName) {
        res = dbw.rawQuery("SELECT attend FROM '" + TABLE_NAME_EVENT + "' where name = '" + eventName + "'", null);
        res.close();
        return res.getString(0);
    }*/

  /*  public ArrayList<Integer> orderList(ArrayList<String> a) {
        int[][] array = new int[a.size()][a.size()];
        int[] forOrder = new int[a.size()];
        Boolean complete = false;

        for (int i = 0; i < a.size(); i++) {
            array[i][0] = Integer.parseInt(a.get(i));
            array[i][1] = i;
        }
        for (int i = 0; i < a.size(); i++) {
            forOrder[i] = array[i][0];
        }

        Arrays.sort(forOrder);

        for (int i = 0; i < a.size() - 1; i++) {
            array[i][1] = searchList(array, forOrder[i + 1]);
        }

        int x = 0, p = 0;
        ArrayList<Integer> finalAray = new ArrayList<>();
        while (!complete) {
            finalAray.add(p, array[x][0]);
            x = array[x][1];
            p++;
            if (finalAray.size() == array.length) {
                complete = true;
            }
        }
        return finalAray;
    }
*/
   /* private int searchList(int[][] array, int searchTerm) {
        int i;
        for (i = 0; i < array.length; i++) {
            if (array[i][0] == searchTerm) {
                break;
            }
        }
        return array[i][1];
    }*/
/*
    public ArrayList<TimeLineItem> eventList(ArrayList<String> name, ArrayList<String> date) {
         ArrayList<TimeLineItem> items = new ArrayList<>();
        for (int i = 0; i < name.size(); i++) {
            items.add(new TimeLineItem(name.get(i), Integer.parseInt(date.get(i))));
        }

        Collections.sort(items, new TimelineComparator());
        return items;
    }


    }*/


/*
    public ArrayList<String> orderedArray(ArrayList<String> a){
        int [] array = new int[a.size()];
        for (int i = 0; i < array.length; i++){
            array[i] = Integer.parseInt(a.get(i));
        }
        int [][] arrayOrder = new int[a.size()][a.size()];
        for (int s = 0; s < a.size();s++){
            arrayOrder[s][0] = array[s];
            arrayOrder[s][1] = s;
        }
        ArrayList<String> newArray = new ArrayList<>();
        Arrays.sort(array);
        for (int i = 0; i < array.length; i++){
            newArray.add(i, String.valueOf(array[i]));
        }


        return newArray;
*/


    /*
    public ArrayList<Integer> timelineQuicksort(ArrayList<String> array){
        int i = 0, value, position,counter =1;


        while (i < array.size()-1){
            value = Integer.parseInt(array.get(i));

            position = i;

            while (value < Integer.parseInt(array.get(position-1)) && position >= 0){
               array.set(position,array.get(position-1));
                position = position - 1;
            }
           array.set(position, value);
        }

        return newArray;
        }*/



/*
    public void updateDatabase() throws XmlPullParserException, IOException {
        XmlParser parser = new XmlParser();
        ArrayList<Event> dataList = parser.readXml(parser.createParser());
        Event[] data = dataList.toArray(new Event[dataList.size()]);
        int items = 0;


        while (items < data.length - 1) {
            Event e = data[items];
            Cursor res = dbw.rawQuery("UPDATE " + TABLE_NAME_EVENT + " SET ID = '" + e.id + "', name = '" + e.name + "', location = '" + e.location + "', date = '" + e.date + "', website = '" + e.website + "', tags = '" + e.tags + "', rating = '" + e.rating + "', attend = '" + e.attend + "'", null);
            items++;
        }

    }
    */

/*
    public String getCounter(String eventName){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res;
        String counter;
        res = db.rawQuery("select tags from " + TABLE_NAME_EVENT + " where name = " + eventName, null);
        res.moveToFirst();
        counter = res.getString(0);
        return counter;
    }*/





/*
    public void deleteFromDb(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_LOGIN, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME_LOGIN + "'");
    }
    */






