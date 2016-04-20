package comapps.com.sportsreference2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by me on 4/21/2015.
 */
public class CFBsearch extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String LOGTAG="SPORTSREF2";

    Context context = this;

    private AutoCompleteTextView textView;

    private TextView itextView;

    SharedPreferences prefs;


    //  private GestureDetector detector;
    private GestureDetectorCompat gestureDetector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cfblayout);

        textView = (AutoCompleteTextView) findViewById(R.id.searchtextcollegefootball);
        itextView = (TextView) findViewById(R.id.textView);

        prefs = this.getSharedPreferences(
                "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE);


        boolean hasSwiped = prefs.getBoolean("HAS_SWIPED_BEFORE", false);

        if ( hasSwiped == true ) {
            itextView.setVisibility(View.INVISIBLE);
        }


        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("College Football Reference");
        }


        this.gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);


        final List<String> names = new ArrayList<>();
        final List<String> links = new ArrayList<>();
        LinksDatabase LinksDB = new LinksDatabase(context);
        Cursor CR = LinksDB.getInformation(LinksDB, "cfb_links");
        CR.moveToFirst();

        do {

            if ( CR.getString(0) != "" ) {

                names.add(CR.getString(0));
                links.add(CR.getString(1));
            }

        } while( CR.moveToNext() );



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.simple_dropdown_item_1line, names);


        textView.setText("");

        textView.setThreshold(3);
        textView.setAdapter(adapter);



        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub

                Log.d("autocomplete", "suggestionselected");


                String selection = textView.getText().toString();



                int matchIndex = -1;

                for (int w = 0; w < names.size(); w++) {

                    matchIndex = matchIndex + 1;

                    if (names.get(w).equals(selection)) {

                        String selectionindexstring = String
                                .valueOf(matchIndex);
                        Log.d("selection index is", selectionindexstring);

                        String linkToPass = links.get(matchIndex);
                        Log.d("Link to pass ", linkToPass);

                        Intent intent = new Intent(arg1.getContext(), WebView.class);
                        intent.putExtra("link", linkToPass);
                        intent.putExtra("whichactivity", "cfb");
                        startActivity(intent);
                        textView.setText(null);

                        break;

                    }

                }

            }

        });


        // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativelayoutcfb);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                itextView.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        Button clearbutton = (Button) findViewById(R.id.clearsearchtext);
        clearbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.searchtextcollegefootball);
                et.setText("");
            }
        });

        EditText listen_for_enter = (EditText) findViewById(R.id.searchtextcollegefootball);

        listen_for_enter.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    Log.d("enter", "enter key hit");

                    EditText gettext = (EditText) findViewById(R.id.searchtextcollegefootball);
                    String search = gettext.getText().toString();
                    String searchfinal = search.replaceAll(" ", "+");

                    String searchToPass = "http://www.sports-reference.com/cfb/search.cgi?search=" + searchfinal;

                    Intent intent = new Intent(view.getContext(), WebView.class);
                    intent.putExtra("link", searchToPass);
                    intent.putExtra("whichactivity", "basketball");
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

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(LOGTAG, "Down");
        return true;

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        Log.i(LOGTAG, "Fling");

        prefs.edit().putBoolean("HAS_SWIPED_BEFORE", true).commit();



        float sensitvity = 150;

        if ((e1.getX() - e2.getX()) > sensitvity) {

            Intent intentSwipeLeft = new Intent();
            intentSwipeLeft.setClass(getApplicationContext(),
                    CBBsearch.class);
            startActivity(intentSwipeLeft);
            overridePendingTransition(R.anim.pushinfromright,R.anim.pushouttoleft);
            finish();

        } else if ((e2.getX() - e1.getX()) > sensitvity) {

            Intent intentSwipeRight = new Intent();
            intentSwipeRight.setClass(getApplicationContext(),
                    NBAsearch.class);
            startActivity(intentSwipeRight);
            overridePendingTransition(R.anim.pushinfromleft,R.anim.pushouttoright);
            finish();


        }

        return true;
    }


    @Override
    public void onLongPress(MotionEvent e) {

        Log.i(LOGTAG, "Long press");



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

        Log.i(LOGTAG, "Double tap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.i(LOGTAG, "Double tap");
        return true;

    }


    // =====================================================================================================
    // end of gestures
    // =====================================================================================================


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


   /* @Override
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
    }*/

    private static String converJsonToStringFromAssetFolder(Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open("collegefootball_links.json");

        byte[] data = new byte[file.available()];
        file.read(data);
        file.close();
        return new String(data);
    }



    @Override
    public void onBackPressed() {
        finish();
    }
}
