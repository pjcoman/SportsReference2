package comapps.com.sportsreference2;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by me on 2/14/2017.
 */

public class AddItem extends AppCompatActivity {

    private static final String TAG = "ADDITEM";


    private DatabaseReference databaseReference;

    private Spinner sportsCategoriesSpinner;
    private String sport;
    private EditText playerName;
    private EditText webAddress;

    private String textItems;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.addplayer);


        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Add Player");
        }

        sportsCategoriesSpinner = (Spinner) findViewById(R.id.spinnerCategory);
        playerName = (EditText) findViewById(R.id.editTextName);
        webAddress = (EditText) findViewById(R.id.editTextAddress);
        Button addButton = (Button) findViewById(R.id.buttonAdd);





        playerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //SAVE THE DATA

                    String[] editTextPlayerNameArray = playerName.getText().toString().split(" ");
                    String playersLastName = editTextPlayerNameArray[1];
                    String playersFirstName = editTextPlayerNameArray[0];

                    String playersLastNameAB = null;
                    String playersFirstNameAB = null;

                    try {
                        playersLastNameAB = playersLastName.substring(0,5);
                        playersFirstNameAB = playersFirstName.substring(0,2);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    if ( sport.equals("baseball")) {

                        assert playersLastNameAB != null;
                        webAddress.setText(playersLastNameAB.toLowerCase() + editTextPlayerNameArray[0].substring(0,2).toLowerCase() + "01.shtml");

                } else if ( sport.equals("football")) {

                        assert playersLastNameAB != null;
                        playersLastNameAB = playersLastNameAB.substring(0,1).toUpperCase() + playersLastNameAB.substring(1).toLowerCase();
                        assert playersFirstNameAB != null;
                        playersFirstNameAB = playersFirstNameAB.substring(0,1).toUpperCase() + playersFirstNameAB.substring(1).toLowerCase();

                    webAddress.setText(playersLastNameAB.substring(0,4) + playersFirstNameAB.substring(0,2) + "00.htm");

                } else {

                        assert playersLastNameAB != null;
                        webAddress.setText(playersLastNameAB.toLowerCase() + editTextPlayerNameArray[0].substring(0,2).toLowerCase() + "01.html");

                }
                }

            }
        });





        Log.d(TAG, "name to add ----> " + playerName);
        Log.d(TAG, "address to add ----> " + webAddress);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sports));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        sportsCategoriesSpinner.setAdapter(spinnerArrayAdapter);

        sportsCategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                sport = sportsCategoriesSpinner.getSelectedItem().toString();

                Toast.makeText(getApplicationContext(), sport,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

//*****************************************************************************************************************************************

        long time = System.currentTimeMillis();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        databaseReference.child("update_dates").child("football").setValue(time);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("football");

        AssetManager assetManager = getAssets();
        InputStream input;
        textItems = "";

        try {
            input = assetManager.open("nflrooks1.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.close();

            // byte buffer into a string
            textItems = new String(buffer);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        String[] itemsToLoad = textItems.split("\n", 0);

        Log.d(TAG, "items to load length ----> " + itemsToLoad.length);

        for( int i = 0; i < itemsToLoad.length - 1; i++)
        {
            String[] itemFields = itemsToLoad[i].split(",", 0);

            Log.d(TAG, "item to add ----> " + itemFields[0] + " " + itemFields[1] + " " + itemFields[2]);

            SportsItem sportsItem = new SportsItem();

            sportsItem.setName(itemFields[0].trim());
            sportsItem.setLink(itemFields[1].trim());
            sportsItem.setType(itemFields[2].trim());



                databaseReference.push().setValue(sportsItem);

        }
//*****************************************************************************************************************************************




        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


                addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String linkToAdd;


                databaseReference = FirebaseDatabase.getInstance().getReference().child(sport);



                if (sport.equals("baseball")) {

                    linkToAdd = "http://www.baseball-reference.com/players/" + webAddress.getText().toString().substring(0, 1).toLowerCase() + "/" + webAddress.getText().toString();


                } else if (sport.equals("basketball")) {

                    linkToAdd = "http://www.basketball-reference.com/players/" + webAddress.getText().toString().substring(0, 1).toLowerCase() + "/" + webAddress.getText().toString();


                } else if (sport.equals("football")) {

                    linkToAdd = "http://www.pro-football-reference.com/players/" + webAddress.getText().toString().substring(0, 1).toUpperCase() + "/" + webAddress.getText().toString();


                } else if (sport.equals("hockey")) {

                    linkToAdd = "http://www.hockey-reference.com/players/" + webAddress.getText().toString().substring(0, 1) + "/" + webAddress.getText().toString();

                } else if (sport.equals("college_football")) {

                    linkToAdd = "http://www.sports-reference.com/cfb/players/" + webAddress.getText().toString();


                } else {

                    linkToAdd = "http://www.sports-reference.com/cbb/players/" + webAddress.getText().toString();

                }

                SportsItem sportsItem = new SportsItem();
                sportsItem.setType("player");
                sportsItem.setName(playerName.getText().toString());
                sportsItem.setLink(linkToAdd);


                if ( checkURL(  linkToAdd) ) {

                    databaseReference.push().setValue(sportsItem);


                    Toast toast = Toast.makeText(getBaseContext(), playerName.getText() + " added", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {

                    Toast toast = Toast.makeText(getBaseContext(), playerName.getText() + "not a valid player link", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }




            }
        });




    }

    public static boolean checkURL(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                }
            }
        }
        return isURL;
    }


    @Override
    public void onBackPressed() {

        finish();

    }


}

