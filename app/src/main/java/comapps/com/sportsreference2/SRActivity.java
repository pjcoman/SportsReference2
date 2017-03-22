package comapps.com.sportsreference2;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SRActivity extends AppCompatActivity  {
    /** Called when the activity is first created. */

    private static final String TAG = "NORTHAMERICA";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Spinner itemHistorySpinner;
    private ArrayList itemsNamesHistory;
    private ArrayList itemsLinksHistory;

    private ArrayAdapter<String> dataAdapter;

    private Spinner spinner;
    private CheckBox checkBoxFirstName;
    private CheckBox checkBox1stLastName;



    TextView dialogTextView;

    private static FirebaseDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }

        //    overridePendingTransition(R.anim.fadeinanimation, 0);
        setContentView(R.layout.activity_main);





    //    mainScreen = findViewById(R.id.mainscreen);


        View mlbbutton = findViewById(R.id.button1);
        View nflbutton = findViewById(R.id.button2);
        View nhlbutton = findViewById(R.id.button3);
        View nbabutton = findViewById(R.id.button4);
        View cfbbutton = findViewById(R.id.button5);
        View cbbbutton = findViewById(R.id.button6);
        itemHistorySpinner = (Spinner) findViewById(R.id.spinnerHistory);
     //   olybutton = findViewById(R.id.button7);



    //    String uri = "https://api.backendless.com/B40D13D5-E84B-F009-FF57-3871FCA5AE00/v1/files/sportsreflinks.db";




        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Sports Reference");
        }

      //  detector = new GestureDetector(this);

        prefs = this.getSharedPreferences(
                "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE);
        editor = prefs.edit();


        boolean hasVisited = prefs.getBoolean("VISITED", false);

        Gson gson = new Gson();
        itemsNamesHistory = gson.fromJson(prefs.getString("NAMES_HISTORY_GSON", ""), ArrayList.class);
        itemsLinksHistory = gson.fromJson(prefs.getString("LINKS_HISTORY_GSON", ""), ArrayList.class);


        String currentDateandTime = getString();



        if( !hasVisited ) {
            prefs.edit().putBoolean("VISITED", true).commit();

            }

        prefs.edit().putString("VISITED_DATE", currentDateandTime).commit();


        if ( itemsNamesHistory == null ) {
            itemHistorySpinner.setVisibility(View.GONE);

        } else {

            dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, itemsNamesHistory);


            dataAdapter.setDropDownViewResource(R.layout.customspinner);

            itemHistorySpinner.setAdapter(dataAdapter);

            itemHistorySpinner.setSelection(Adapter.NO_SELECTION,false);


            itemHistorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    Intent intent = new Intent(getApplication().getApplicationContext(), WebView.class);
                    intent.putExtra("link", itemsLinksHistory.get(position).toString());
                 //   intent.putExtra("whichactivity", "baseball");
                    startActivity(intent);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });


        }



        mlbbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentbaseballsearch = new Intent();
                        intentbaseballsearch.setClass(getApplicationContext() ,MLB_Activity.class);
                        startActivity(intentbaseballsearch);
                        try {
                            overridePendingTransition(R.anim.pushinfromleft, R.anim.pushouttoright);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        );

        nflbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentfootballsearch = new Intent();
                        intentfootballsearch.setClass(getApplicationContext() ,NFL_Activity.class);
                        startActivity(intentfootballsearch);
                        try {
                            overridePendingTransition(R.anim.pushinfromright, R.anim.pushouttoleft);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        );

        nhlbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intenthockeysearch = new Intent();
                        intenthockeysearch.setClass(getApplicationContext() ,NHL_Activity.class);
                        startActivity(intenthockeysearch);
                        try {
                            overridePendingTransition(R.anim.pushinfromleft, R.anim.pushouttoright);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                }
        );

        nbabutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentbasketballsearch = new Intent();
                        intentbasketballsearch.setClass(getApplicationContext(), NBA_Activity.class);
                        startActivity(intentbasketballsearch);
                        try {
                            overridePendingTransition(R.anim.pushinfromright, R.anim.pushouttoleft);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    }
        );

        cfbbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentcollegefootballsearch = new Intent();
                        intentcollegefootballsearch.setClass(getApplicationContext() ,CFB_Activity.class);
                        startActivity(intentcollegefootballsearch);
                        try {
                            overridePendingTransition(R.anim.pushinfromleft, R.anim.pushouttoright);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        );

        cbbbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentcollegebasketballsearch = new Intent();
                        intentcollegebasketballsearch.setClass(getApplicationContext() ,CBB_Activity      .class);
                        startActivity(intentcollegebasketballsearch);
                        try {
                            overridePendingTransition(R.anim.pushinfromright, R.anim.pushouttoleft);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                    }

        );


    }

    private static String getString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        Log.e(TAG,"Time ----> " + currentDateandTime);
        return currentDateandTime;
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

    /*

        if( id == R.id.addplayer ) {

          *//*  Intent intentAddPlayer = new Intent();
            intentAddPlayer.setClass(getApplicationContext() , AddItem.class);
            startActivity(intentAddPlayer);
            return true;*//*
        } */

        if ( id == R.id.settings){


            AlertDialog.Builder popDialogBuilder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.customdialog, null);
        //    popDialogBuilder.setIcon(R.mipmap.ic_launcher);
        //    popDialogBuilder.setTitle("Characters for Filter");
            spinner = (Spinner) mView.findViewById(R.id.spinner);
            checkBoxFirstName = (CheckBox) mView.findViewById(R.id.checkBoxName);
            checkBox1stLastName = (CheckBox) mView.findViewById(R.id.checkBox1stLastName);
            String[] integers = {"3", "4", "5", "6", "7", "8"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SRActivity.this, android.R.layout.simple_spinner_item,
                    integers);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setSelection(prefs.getInt("FILTER_INT", 3) - 3);
            checkBoxFirstName.setChecked(prefs.getBoolean("FILTER_FIRSTNAME", false));
            checkBox1stLastName.setChecked(prefs.getBoolean("FILTER_LASTNAME", false));

            if ( checkBoxFirstName.isChecked() || checkBox1stLastName.isChecked()) {
                spinner.setEnabled(false);
            } else {
                spinner.setEnabled(true);
            }


            checkBoxFirstName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if ( isChecked ) {
                        checkBox1stLastName.setChecked(false);
                        spinner.setEnabled(false);

                    } else {
                        spinner.setEnabled(true);
                    }
                }
            });

            checkBox1stLastName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if ( isChecked ) {
                            checkBoxFirstName.setChecked(false);
                            spinner.setEnabled(false);

                        } else {
                            spinner.setEnabled(true);
                        }
                    }
                });

            // Button OK
           popDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                           if ( checkBoxFirstName.isChecked()) {

                               Toast.makeText(SRActivity.this, "filter on first name",
                                       Toast.LENGTH_SHORT).show();
                               editor.putBoolean("FILTER_FIRSTNAME", true);
                               editor.putBoolean("FILTER_LASTNAME", false);
                               editor.commit();


                           } else if ( checkBox1stLastName.isChecked()) {

                               Toast.makeText(SRActivity.this, "filter on last name (1st letter)",
                                       Toast.LENGTH_SHORT).show();
                               editor.putBoolean("FILTER_FIRSTNAME", false);
                               editor.putBoolean("FILTER_LASTNAME", true);
                               editor.commit();


                           } else if ( spinner.isEnabled()){

                               Toast.makeText(SRActivity.this,
                                       spinner.getSelectedItem().toString() + " character filter",
                                       Toast.LENGTH_SHORT).show();
                               editor.putBoolean("FILTER_FIRSTNAME", false);
                               editor.putBoolean("FILTER_LASTNAME", false);

                               Integer selected = Integer.parseInt(spinner.getSelectedItem().toString());

                               try {
                                   editor.putInt("FILTER_INT", selected);
                               } catch (Exception e) {
                                   editor.putInt("FILTER_INT", 3);
                                   e.printStackTrace();
                               }
                               editor.commit();


                           }

                           dialog.dismiss();
                        }


                   });

            popDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                }
            });


            popDialogBuilder.setView(mView);
            AlertDialog alertDialog = popDialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE  );



        }

        return super.onOptionsItemSelected(item);
    }




    private void emailAddPlayer() {
        // TODO Auto-generated method stub

        // The following code is the implementation of Email client
        Intent intentEmail = new Intent(Intent.ACTION_SEND);
        intentEmail.setType("text/plain");
        String[] address = { "email.pete@yahoo.com" };

        intentEmail.putExtra(Intent.EXTRA_EMAIL, address);
        intentEmail.putExtra(Intent.EXTRA_SUBJECT,
                "player needs to be added");
        intentEmail.putExtra(Intent.EXTRA_TEXT,
                "Could you add player ....");

        startActivityForResult((Intent.createChooser(intentEmail, "Email")), 1);

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        getPrefs();

        Gson gson = new Gson();
        itemsNamesHistory = gson.fromJson(prefs.getString("NAMES_HISTORY_GSON", ""), ArrayList.class);
        itemsLinksHistory = gson.fromJson(prefs.getString("LINKS_HISTORY_GSON", ""), ArrayList.class);
        try {
            dataAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //...Now update your objects with preference values
    }

    private void getPrefs() {
        prefs = this.getSharedPreferences(
                "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE);



    }
}
