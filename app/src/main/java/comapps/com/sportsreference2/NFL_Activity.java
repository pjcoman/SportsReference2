package comapps.com.sportsreference2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

/**
 * Created by me on 4/21/2015.
 */
public class NFL_Activity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String TAG = "NFL";


    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;

    private ArrayList<String> names;
    private ArrayList<String> links;
    private ArrayList<String> namesHistory;
    private ArrayList<String> linksHistory;
    private long time;


    private long firebaseUpdate;
    private long userUpdate;
    private int i;
    private double percentLoaded;


    private TextView iTextView;
    private TextView lcTextView;
    private CheckBox checkBox;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;

    private boolean inProgress = false;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private DatabaseReference databaseReferenceData;
    private DatabaseReference databaseReferenceItems;

    //  private GestureDetector detector;
    private GestureDetectorCompat gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nfllayout);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.searchtextfootball);
        iTextView = (TextView) findViewById(R.id.textView);
        lcTextView = (TextView) findViewById(R.id.textViewLinksCount);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBarLoadStatus2);


        autoCompleteTextView.setEnabled(false);
        autoCompleteTextView.setCursorVisible(false);


        prefs = this.getSharedPreferences(
                "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE);
        editor = prefs.edit();

        Random Number = new Random();
        int rNumber = Number.nextInt(10);

        lcTextView.setText(prefs.getInt("FOOTBALL_LINKS_COUNT", 0) + " links");
        Log.d(TAG, "random " + rNumber);

        Log.d(TAG, "SHOWINSTRUCTIONS -----> " + prefs.getBoolean("SHOWINSTRUCTIONS", true));


        if (rNumber > 8 && prefs.getBoolean("SHOWINSTRUCTIONS", true)) {
            autoCompleteTextView.setHint(R.string.edittext_hint);
            assert iTextView != null;
            iTextView.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);

        } else {
            iTextView.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            autoCompleteTextView.setHint(null);

        }


        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Football Reference");
        }


        this.gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);


        names = new ArrayList<>();
        links = new ArrayList<>();

        if (prefs.getBoolean("FIRSTRUNNFL", true)) {

            progressBar2.setVisibility(View.VISIBLE);

            inProgress = true;

            databaseReferenceItems = FirebaseDatabase.getInstance().getReference().child("football")    ;



            databaseReferenceItems.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    i = 0;

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        Map<String, Object> newChild = (Map<String, Object>) child.getValue();

                        names.add(newChild.get("name").toString());
                        links.add(newChild.get("link").toString());

                        i++;

                        int size = (int) dataSnapshot.getChildrenCount();

                        percentLoaded = ((double)i/size)*100;
                     //   Log.d(TAG, "percentLoaded ----> " + (int)percentLoaded);

                      /*  new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while ( (int)percentLoaded <= 100  ) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            if ( progressBar.getProgress() != (int)percentLoaded
                                                    && (int)percentLoaded % 10 == 0 ) {
                                                progressBar.setProgress((int)percentLoaded);
                                            }

                                        }
                                    });
                                    try {
                                        Thread.sleep(100);
                                    }
                                    catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }).start();*/




                    }


                    time = System.currentTimeMillis();
                    editor.putLong("FOOTBALL_LASTUPDATE", time);
                    editor.commit();


                    Gson gson = new Gson();
                    String jsonNames = gson.toJson(names);
                    editor.putString("FOOTBALL_ITEMS_NAMES_GSON", jsonNames);
              //      Log.d(TAG, "names " + jsonNames);
                    String jsonLinks = gson.toJson(links);
                    editor.putString("FOOTBALL_ITEMS_LINKS_GSON", jsonLinks);
             //       Log.d(TAG, "links " + jsonLinks);
                    editor.commit();


                    autoCompleteTextView.setEnabled(true);
                    autoCompleteTextView.setCursorVisible(true);
                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, names);
                    autoCompleteTextView.setText("");
                    autoCompleteTextView.setThreshold(3);
                    autoCompleteTextView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    progressBar2.setVisibility(View.GONE);

                    Log.d(TAG, "firebase used, number of items -----> " + names.size());
                    editor.putInt("FOOTBALL_LINKS_COUNT", names.size());
                    editor.commit();
                    lcTextView.setText(names.size() + " links");

                    inProgress = false;

                }




                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });





            editor.putBoolean("FIRSTRUNNFL", false);
            editor.commit();

        } else {

            Gson gson = new Gson();
            names = gson.fromJson(prefs.getString("FOOTBALL_ITEMS_NAMES_GSON", ""), ArrayList.class);
            links = gson.fromJson(prefs.getString("FOOTBALL_ITEMS_LINKS_GSON", ""), ArrayList.class);

        //    progressBar.setProgress(100);


            autoCompleteTextView.setEnabled(true);
            autoCompleteTextView.setCursorVisible(true);
            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, names);
            autoCompleteTextView.setText("");
            autoCompleteTextView.setThreshold(3);
            autoCompleteTextView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


//            Log.d(TAG, "GSON used, number of items -----> " + names.size());


        }


//************************************************************ check/get update *******************************************************************************
        databaseReferenceData = FirebaseDatabase.getInstance().getReference().child("data");
        databaseReferenceData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    Map<String, Long> newUpdateChild = (Map<String, Long>) child.getValue();


                    firebaseUpdate = newUpdateChild.get("football");


                }


                userUpdate = prefs.getLong("FOOTBALL_LASTUPDATE", 0);


                if (firebaseUpdate > userUpdate) {

                    Log.d(TAG, "firebase date newer firebase:" + getDate(firebaseUpdate) +
                            " user:" + getDate(userUpdate));


                    databaseReferenceItems = FirebaseDatabase.getInstance().getReference().child("football");
                    databaseReferenceItems.keepSynced(true);


                    databaseReferenceItems.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            names.clear();
                            links.clear();

                            progressBar2.setVisibility(View.VISIBLE);

                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                Map<String, Object> newChild = (Map<String, Object>) child.getValue();


                                names.add(newChild.get("name").toString());
                                links.add(newChild.get("link").toString());


                            }


                            Gson gson = new Gson();
                            String jsonNames = gson.toJson(names);
                            editor.putString("FOOTBALL_ITEMS_NAMES_GSON", jsonNames);
                            editor.commit();
                            //   Log.d(TAG, "names " + jsonNames);
                            String jsonLinks = gson.toJson(links);
                            editor.putString("FOOTBALL_ITEMS_LINKS_GSON", jsonLinks);
                            //   Log.d(TAG, "links " + jsonLinks);
                            editor.commit();


                            time = System.currentTimeMillis();
                            editor.putLong("FOOTBALL_LASTUPDATE", time);
                            editor.commit();


                            Log.d(TAG, "user database updated " + dataSnapshot.getChildrenCount());
                            lcTextView.setText(names.size() + " links");
                            editor.putInt("FOOTBALL_LINKS_COUNT", names.size());
                            editor.commit();



                            Toast toast = Toast.makeText(getBaseContext(), "football links updated", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP | Gravity.RIGHT, 20, 20);
                            toast.show();

                            progressBar2.setVisibility(View.GONE);

                            adapter.notifyDataSetChanged();


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if (userUpdate == 0) {

                    Log.d(TAG, "dates irrelevent first run");

                } else {

                    Log.d(TAG, "user date newer user:" + getDate(userUpdate) +
                            " firebase:" + getDate(firebaseUpdate));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//**************************************************************************************************************************************************************

        checkBox.setChecked(prefs.getBoolean("SHOWINSTRUCTIONS", true));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                editor.putBoolean("SHOWINSTRUCTIONS", buttonView.isChecked());
                editor.commit();

                Log.d(TAG, "checkbox checked " + checkBox.isChecked());


            }


        });

        // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub

                Log.d("autocomplete", "suggestionselected");


                String selection = autoCompleteTextView.getText().toString();


                int matchIndex = -1;

                for (int w = 0; w < names.size(); w++) {

                    matchIndex = matchIndex + 1;

                    if (names.get(w).equals(selection)) {

                        String selectionindexstring = String
                                .valueOf(matchIndex);
                        Log.d("selection index is", selectionindexstring);
                        String nameSelected= names.get(matchIndex);
                        String linkToPass = links.get(matchIndex);
                        Log.d("Link to pass ", linkToPass);

                        addNameToHistoryList(nameSelected);
                        addLinkToHistoryList(linkToPass);

                        Intent intent = new Intent(arg1.getContext(), WebView.class);
                        intent.putExtra("link", linkToPass);
                        intent.putExtra("whichactivity", "football");
                        startActivity(intent);
                        autoCompleteTextView.setText(null);

                        break;

                    }

                }

            }

        });


        // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx


        autoCompleteTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
// TODO Auto-generated method stub
                EditText et = (EditText) findViewById(R.id.searchtextfootball);
                assert et != null;
                et.setText("");
                return false;
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String character = s.toString();

                if (count == 1 && !Character.isLetter(character.charAt(0))) {

                    autoCompleteTextView.setText(null);

                }

                if (count == prefs.getInt("FILTER_INT", 3) && (!prefs.getBoolean("FILTER_FIRSTNAME", false) &&
                        !prefs.getBoolean("FILTER_LASTNAME", false))) {


                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (prefs.getBoolean("FILTER_FIRSTNAME", false)) {

                    if (s.length() != 0) {
                        if (s.toString().charAt(s.toString().length() - 1) == ' ') {

                            try {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                } else if (prefs.getBoolean("FILTER_LASTNAME", false)) {

                    if (s.length() > 1) {
                        if (s.toString().charAt(s.toString().length() - 2) == ' ') {

                            try {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        });


        EditText listen_for_enter = (EditText) findViewById(R.id.searchtextfootball);
        //   listen_for_enter.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        assert listen_for_enter != null;
        listen_for_enter.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {


                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    Log.d("enter", "enter key hit");

                    EditText gettext = (EditText) findViewById(R.id.searchtextfootball);
                    assert gettext != null;
                    String search = gettext.getText().toString();
                    String searchfinal = search.replaceAll(" ", "+");

                    String searchToPass = "http://www.pro-football-reference.com/search/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid=";

                    // http://www.pro-football-reference.com/search/search.fcgi?hint=tom+Ackerman&search=tom&pid=


                    Intent intent = new Intent(view.getContext(), WebView.class);
                    intent.putExtra("link", searchToPass);
                    intent.putExtra("whichactivity", "football");
                    startActivity(intent);


                    gettext.setText(null);

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    private String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(TAG, "Down");
        return true;

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        Log.i(TAG, "Fling");


        float sensitvity = 150;
        float sensitvityY = 500;

        if ((e1.getX() - e2.getX()) > sensitvity) {

            Intent intentSwipeLeft = new Intent();
            intentSwipeLeft.setClass(getApplicationContext(),
                    NHL_Activity.class);
            startActivity(intentSwipeLeft);
            try {
                overridePendingTransition(R.anim.pushinfromright, R.anim.pushouttoleft);
            } catch (Exception e) {
                e.printStackTrace();
            }




        } else if ((e2.getX() - e1.getX()) > sensitvity) {

            Intent intentSwipeRight = new Intent();
            intentSwipeRight.setClass(getApplicationContext(),
                    MLB_Activity.class);
            startActivity(intentSwipeRight);
            try {
                overridePendingTransition(R.anim.pushinfromleft, R.anim.pushouttoright);
            } catch (Exception e) {
                e.printStackTrace();
            }




        } else if (Math.abs((e2.getY() - e1.getY())) > sensitvityY) {

            int e2e1 = Math.round((e2.getY()) - (e1.getY()));
            int e1e2 = Math.round((e1.getY()) - (e2.getY()));

            Log.i(TAG, "y swipe float is ----> " + e1e2 + " " + e2e1);
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            try {
                if (e1e2 < 0) {
                    overridePendingTransition(R.anim.pushinfromtop, R.anim.pushouttobottom);
                } else {
                    overridePendingTransition(R.anim.pushinfrombottom, R.anim.pushouttotop);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.finish();

        return true;
    }


    @Override
    public void onLongPress(MotionEvent e) {

        Log.i(TAG, "Long press");


    }

    private void addNameToHistoryList(Object obj){

        Gson gson = new Gson();


        namesHistory = gson.fromJson(prefs.getString("NAMES_HISTORY_GSON", null), ArrayList.class);

        if ( namesHistory == null ) {
            namesHistory = new ArrayList<>();
            namesHistory.add(0, obj.toString());
        } else if (namesHistory.size() < 20) {
            namesHistory.add(0,obj.toString());
        }else{
            namesHistory.add(0,obj.toString());
            namesHistory.remove(20);
        }
        String jsonHistoryNames = gson.toJson(namesHistory);
        editor.putString("NAMES_HISTORY_GSON", jsonHistoryNames);



        editor.commit();
    }


    private void addLinkToHistoryList(Object obj){

        Gson gson = new Gson();


        linksHistory = gson.fromJson(prefs.getString("LINKS_HISTORY_GSON", null), ArrayList.class);

        if ( linksHistory == null ) {
            linksHistory = new ArrayList<>();
            linksHistory.add(0, obj.toString());
        } else if (linksHistory.size() < 20) {
            linksHistory.add(0,obj.toString());
        }else{
            linksHistory.add(0,obj.toString());
            linksHistory.remove(20);
        }
        String jsonHistoryLinks = gson.toJson(linksHistory);
        editor.putString("LINKS_HISTORY_GSON", jsonHistoryLinks);



        editor.commit();
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {

        Log.i(TAG, "Double tap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.i(TAG, "Double tap");
        return true;

    }


    // =====================================================================================================
    // end of gestures
    // =====================================================================================================
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!inProgress)
            return super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
    }



    @Override
    public void onBackPressed() {
        //finish();
    }
}

