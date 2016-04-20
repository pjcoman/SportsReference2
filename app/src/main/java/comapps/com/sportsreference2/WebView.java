package comapps.com.sportsreference2;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebViewClient;

/**
 * Created by me on 8/25/2015.
 */
public class WebView extends AppCompatActivity {

    private static final String TAG = "SPORTSREF2";
    Activity activity;
    int orientationValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
    //    RelativeLayout layout = (RelativeLayout) findViewById(R.id.webLayout);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;


        activity = WebView.this;
        orientationValue = activity.getResources().getConfiguration().orientation;
        switch(orientationValue) {
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().setLayout((int) (width * .95), (int) (height * .85));
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().setLayout((int) (width * .95), (int) (height * .75));
                break;

        }


        getWindow().setGravity(Gravity.BOTTOM);




        WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity = Gravity.BOTTOM;



        Bundle extras = getIntent().getExtras();





        String link = extras.getString("link");

        String sendingActivity = extras.getString("whichactivity");



   /*     switch (sendingActivity) {
            case "baseball":
                layout.setBackgroundResource(R.drawable.wallpaper_bb); break;
            case "hockey":
                layout.setBackgroundResource(R.drawable.wallpaper_h); break;
            case "football":
                layout.setBackgroundResource(R.drawable.wallpaper_fb); break;
            case "basketball":
                layout.setBackgroundResource(R.drawable.wallpaper_basketb); break;
            case "cbb":
                layout.setBackgroundResource(R.drawable.walpaper_cbb); break;
            case "cfb":
                layout.setBackgroundResource(R.drawable.wallpaper_cfb); break;
            case "oly":
                layout.setBackgroundResource(R.drawable.wallpaper_o); break;


            default:

                break;

        }  */



//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        android.webkit.WebView webView = (android.webkit.WebView) findViewById(R.id.webview1);


        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setJavaScriptEnabled(false);


        // make sure your pinch zoom is enabled
        webView.getSettings().setBuiltInZoomControls(true);

// don't show the zoom controls
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl(link);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }







    public void close(View v) {


        finish();

    }


}

