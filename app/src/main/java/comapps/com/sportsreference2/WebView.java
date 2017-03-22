package comapps.com.sportsreference2;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by me on 8/25/2015.
 */
public class WebView extends AppCompatActivity {

    private static final String TAG = "WEBVIEW";

    private android.webkit.WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
    //    RelativeLayout layout = (RelativeLayout) findViewById(R.id.webLayout);

        Button closeButton = (Button) findViewById(R.id.closeButton);



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;


        Activity activity = WebView.this;
        int orientationValue = activity.getResources().getConfiguration().orientation;
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
        switch(orientationValue) {
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().setLayout((int) (width * .95), (int) (height * .80));
                layoutParams.y = (int) (height * .15);
                getWindow().setGravity(Gravity.TOP);
                getWindow().setAttributes(layoutParams);
                mWindowParams.gravity = Gravity.TOP;

                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().setLayout(width, (int) (height * .80));
                layoutParams.y = (int) (height * .25);
                getWindow().setGravity(Gravity.TOP);
                getWindow().setAttributes(layoutParams);
                mWindowParams.gravity = Gravity.TOP;

                break;

        }





        Bundle extras = getIntent().getExtras();





        String url = extras.getString("link");

        Log.d(TAG, "url is " + url);

        webView = (android.webkit.WebView) findViewById(R.id.webview1);


        webView.setWebViewClient(new WebViewClient());




        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);


       webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
       webView.setScrollbarFadingEnabled(false);

        if (savedInstanceState != null) {
            webView.saveState(savedInstanceState);

        } else {

            if ( getResources().getConfiguration().orientation == 2 ) {
                webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
                webView.loadUrl(url);
            } else {
                webView.loadUrl(url);
            }



        }





        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    destroyWebView();

            }
        });




    }

    private void destroyWebView() {



        if(webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
            webView.pauseTimers();
            webView = null;

            finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            if(webView.canGoBack()){
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        webView.restoreState(savedState);
    }





}

