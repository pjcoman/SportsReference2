package comapps.com.sportsreference2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by me on 3/21/2017.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAINACTIVITY";

    ViewPager simpleViewPager;
    TabLayout tabLayout;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private ArrayList itemsNamesHistory;
    private ArrayList itemsLinksHistory;

    private ArrayAdapter<String> dataAdapter;

    private Spinner spinner;
    private Spinner spinnerHistory;
    private CheckBox checkBoxFirstName;
    private CheckBox checkBox1stLastName;



    TextView dialogTextView;

    private static FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }

        setContentView(R.layout.tabbedlayoutmain);

        simpleViewPager = (ViewPager) findViewById(R.id.simpleViewPager);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        spinnerHistory = (Spinner) findViewById(R.id.spinnerHistory);

        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText(null); // set the Text for the first Tab
        firstTab.setIcon(R.drawable.na_icon); // set an icon for the
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText(null); // set the Text for the second Tab
        secondTab.setIcon(R.drawable.world_icon); // set an icon for the
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout

        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            ImageView imageView = new ImageView(getApplicationContext());
            if ( i == 0 ) {
                imageView.setImageResource(R.drawable.na_icon);
            } else {
                imageView.setImageResource(R.drawable.world_icon);
            }

            tabLayout.getTabAt(i).setCustomView(imageView);
        }

        prefs = this.getSharedPreferences(
                "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE);
        editor = prefs.edit();


        boolean hasVisited = prefs.getBoolean("VISITED", false);

        Gson gson = new Gson();
        itemsNamesHistory = gson.fromJson(prefs.getString("NAMES_HISTORY_GSON", ""), ArrayList.class);
        itemsLinksHistory = gson.fromJson(prefs.getString("LINKS_HISTORY_GSON", ""), ArrayList.class);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        Log.e(TAG ,"Time ----> " + currentDateandTime);


        if( !hasVisited ) {
            prefs.edit().putBoolean("VISITED", true).commit();

        }

        prefs.edit().putString("VISITED_DATE", currentDateandTime).commit();

        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);

        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        if ( itemsNamesHistory == null ) {
            spinnerHistory.setVisibility(View.GONE);

        } else {

            dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, itemsNamesHistory);


            dataAdapter.setDropDownViewResource(R.layout.customspinner);

            spinnerHistory.setAdapter(dataAdapter);

            spinnerHistory.setSelection(Adapter.NO_SELECTION,false);


            spinnerHistory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            spinner = (Spinner) mView.findViewById(R.id.spinnerDialog);
            checkBoxFirstName = (CheckBox) mView.findViewById(R.id.checkBoxName);
            checkBox1stLastName = (CheckBox) mView.findViewById(R.id.checkBox1stLastName);
            String[] integers = {"3", "4", "5", "6", "7", "8"};
        //    ArrayAdapter<String> adapterDialog = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
        //              integers);
        //    adapterDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //    spinner.setAdapter(adapterDialog);

       //     spinner.setSelection(prefs.getInt("FILTER_INT", 3) - 3);
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

                        Toast.makeText(MainActivity.this, "filter on first name",
                                Toast.LENGTH_SHORT).show();
                        editor.putBoolean("FILTER_FIRSTNAME", true);
                        editor.putBoolean("FILTER_LASTNAME", false);
                        editor.commit();


                    } else if ( checkBox1stLastName.isChecked()) {

                        Toast.makeText(MainActivity.this, "filter on last name (1st letter)",
                                Toast.LENGTH_SHORT).show();
                        editor.putBoolean("FILTER_FIRSTNAME", false);
                        editor.putBoolean("FILTER_LASTNAME", true);
                        editor.commit();


                    } else if ( spinner.isEnabled()){

                        Toast.makeText(MainActivity.this,
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
