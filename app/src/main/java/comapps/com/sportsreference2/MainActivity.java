package comapps.com.sportsreference2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    /** Called when the activity is first created. */

    public static final String LOGTAG="SPORTSREF2";

    private GestureDetector detector;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //    overridePendingTransition(R.anim.fadeinanimation, 0);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Sports Reference");
        }

        detector = new GestureDetector(
                this);



        SharedPreferences prefs = this.getSharedPreferences(
                "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE);
        boolean hasVisited = prefs.getBoolean("HAS_VISISTED_BEFORE", false);
        if(!hasVisited) {


            //    Toast instructions = Toast.makeText(MainActivity.this,
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
            //   instructions3.show();

            prefs.edit().putBoolean("HAS_VISISTED_BEFORE", true).commit();

        }

        View mlbbutton = findViewById(R.id.button1);
        View nflbutton = findViewById(R.id.button2);
        View nhlbutton = findViewById(R.id.button3);
        View nbabutton = findViewById(R.id.button4);
        View cfbbutton = findViewById(R.id.button5);
        View cbbbutton = findViewById(R.id.button6);
        View olybutton = findViewById(R.id.button7);

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





    public boolean onTouchEvent(MotionEvent event) {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
