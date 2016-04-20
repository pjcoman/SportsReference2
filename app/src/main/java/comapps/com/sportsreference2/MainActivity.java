package comapps.com.sportsreference2;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.backendless.Backendless;

import java.io.File;


public class MainActivity extends AppCompatActivity  {
    /** Called when the activity is first created. */

    public static final String TAG = "SPORTSREF2";

    private GestureDetector detector;
    private Boolean fileExist;
    DownloadManager downloadManager;
    String downloadFileUrl = "https://api.backendless.com/B40D13D5-E84B-F009-FF57-3871FCA5AE00/v1/files/sportsreflinks.db";
    private long myDownloadReference;
    private BroadcastReceiver receiverDownloadComplete;
    private IntentFilter intentFilter;

    View mainScreen;

    View mlbbutton;
    View nflbutton;
    View nhlbutton;
    View nbabutton;
    View cfbbutton;
    View cbbbutton;
    View olybutton;

    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //    overridePendingTransition(R.anim.fadeinanimation, 0);
        setContentView(R.layout.activity_main);

        Backendless.setUrl(Defaults.SERVER_URL );
        Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

        mainScreen = findViewById(R.id.mainscreen);



        mlbbutton = findViewById(R.id.button1);
        nflbutton = findViewById(R.id.button2);
        nhlbutton = findViewById(R.id.button3);
        nbabutton = findViewById(R.id.button4);
        cfbbutton = findViewById(R.id.button5);
        cbbbutton = findViewById(R.id.button6);
        olybutton = findViewById(R.id.button7);



    //    String uri = "https://api.backendless.com/B40D13D5-E84B-F009-FF57-3871FCA5AE00/v1/files/sportsreflinks.db";




        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Sports Reference");
        }

      //  detector = new GestureDetector(this);

        prefs = this.getSharedPreferences(
                "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE);


        boolean hasVisited = prefs.getBoolean("HAS_VISISTED_BEFORE", false);



        if(!hasVisited) {

            mlbbutton.setVisibility(View.INVISIBLE);
            nhlbutton.setVisibility(View.INVISIBLE);
            nbabutton.setVisibility(View.INVISIBLE);
            nflbutton.setVisibility(View.INVISIBLE);
            cfbbutton.setVisibility(View.INVISIBLE);
            cbbbutton.setVisibility(View.INVISIBLE);
            olybutton.setVisibility(View.INVISIBLE);


           /* //    Toast instructions = Toast.makeText(MainActivity.this,
            //            "Click for search.", Toast.LENGTH_LONG);
            //    instructions.setGravity(Gravity.CENTER, 0, -250);
            //    instructions.show();

            Toast instructions2 = Toast.makeText(MainActivity.this,
                    "Click enter with no text in search box \n " +
                            "         to go to the main site.", Toast.LENGTH_LONG);
            instructions2.setGravity(Gravity.CENTER, 0, -200);
            instructions2.show();

            //   Toast instructions3 = Toast.makeText(MainActivity.this,
            //           "Touch above search box to close keyboard.", Toast.LENGTH_LONG);
            //   instructions3.setGravity(Gravity.CENTER, 0, -150);
            //   instructions3.show();*/

            prefs.edit().putBoolean("HAS_VISISTED_BEFORE", true).commit();

            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadFileUrl));
            request.setTitle("Suggestions download.");
            request.setDescription("Suggestions database being downloaded...");

            request.setDestinationInExternalFilesDir(getApplicationContext(), "/dbase", "sportsreflinks.db");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.allowScanningByMediaScanner();
            // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "sportsreflinks.db");




      /*  if (!hasVisited) {

             request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
             request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);

        } else {

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        }*/

            String filePath = prefs.getString("filepath", "");

            Log.i(TAG, "file path is " + filePath);

            fileExist = doesFileExist(filePath);
            Log.i(TAG, "file exists is " + fileExist.toString());

            if ( fileExist == true ) {

                deleteFileIfExists(filePath);
            }

            myDownloadReference = downloadManager.enqueue(request);



            intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);


            receiverDownloadComplete = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                    if ( myDownloadReference == reference ) {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(reference);
                        Cursor cursor = downloadManager.query(query);

                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int status = cursor.getInt(columnIndex);
                        int fileNameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                        String saveFilePath = cursor.getString(fileNameIndex);

                        fileExist = doesFileExist(saveFilePath);

                        Log.i(TAG, "file exists download complete " + saveFilePath);

                        prefs.edit().putString("filepath", saveFilePath).commit();


                        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                        int reason = cursor.getInt(columnReason);

                        mlbbutton.setVisibility(View.VISIBLE);
                        nhlbutton.setVisibility(View.VISIBLE);
                        nbabutton.setVisibility(View.VISIBLE);
                        nflbutton.setVisibility(View.VISIBLE);
                        cfbbutton.setVisibility(View.VISIBLE);
                        cbbbutton.setVisibility(View.VISIBLE);
                        olybutton.setVisibility(View.VISIBLE);

                        //     mainScreen.setEnabled(true);

                        //     mainScreen.setClickable(true);




                    }

                }
            };

        }



        mlbbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentbaseballsearch = new Intent();
                        intentbaseballsearch.setClass(getApplicationContext() ,MLBsearch.class);
                        startActivity(intentbaseballsearch);

                    }
                }
        );

        nflbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentfootballsearch = new Intent();
                        intentfootballsearch.setClass(getApplicationContext() ,NFLsearch.class);
                        startActivity(intentfootballsearch);

                    }
                }
        );

        nhlbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intenthockeysearch = new Intent();
                        intenthockeysearch.setClass(getApplicationContext() ,NHLsearch.class);
                        startActivity(intenthockeysearch);

                    }
                }
        );

        nbabutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentbasketballsearch = new Intent();
                        intentbasketballsearch.setClass(getApplicationContext() ,NBAsearch.class);
                        startActivity(intentbasketballsearch);

                    }
                }
        );

        cfbbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentcollegefootballsearch = new Intent();
                        intentcollegefootballsearch.setClass(getApplicationContext() ,CFBsearch.class);
                        startActivity(intentcollegefootballsearch);

                    }
                }
        );

        cbbbutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentcollegebasketballsearch = new Intent();
                        intentcollegebasketballsearch.setClass(getApplicationContext() ,CBBsearch.class);
                        startActivity(intentcollegebasketballsearch);

                    }
                }
        );

        olybutton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentolympicssearch = new Intent();
                        intentolympicssearch.setClass(getApplicationContext() ,OLYsearch.class);
                        startActivity(intentolympicssearch);

                    }
                }
        );
    }





  /*  public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {

        View swipe_instructions = findViewById(R.id.swipe_instructions);
        swipe_instructions.setVisibility(View.GONE);

        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        float sensitvity = 150;

        if ((e1.getX() - e2.getX()) > sensitvity) {

            Intent intentbaseballsearch = new Intent();
            intentbaseballsearch.setClass(getApplicationContext(),
                    MLBsearch.class);
            startActivity(intentbaseballsearch);
            overridePendingTransition(R.anim.pushinfromright,R.anim.pushouttoleft);


        } else if ((e2.getX() - e1.getX()) > sensitvity) {

            Intent intentolympicssearch = new Intent();
            intentolympicssearch.setClass(getApplicationContext(),
                    OLYsearch.class);
            startActivity(intentolympicssearch);
            overridePendingTransition(R.anim.pushinfromleft,R.anim.pushouttoright);

        }

        return true;
    }

    public void onLongPress(MotionEvent e) {

    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {

        return false;
    }

    public void onShowPress(MotionEvent e) {

    }

    public boolean onSingleTapUp(MotionEvent e) {

        return true;
    }
*/
    private boolean doesFileExist(String filename){

        File folder1 = new File(filename);
        return folder1.exists();


    }

    private boolean deleteFileIfExists(String filename){

        File f = new File(filename);
        return f.delete();


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.updatedb) {

            prefs.edit().putBoolean("HAS_VISISTED_BEFORE", false).commit();
           this.recreate();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (receiverDownloadComplete != null) {
            registerReceiver(receiverDownloadComplete, intentFilter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiverDownloadComplete != null) {
            unregisterReceiver(receiverDownloadComplete);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
