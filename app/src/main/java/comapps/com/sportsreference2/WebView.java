package comapps.com.sportsreference2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

/**
 * Created by me on 8/25/2015.
 */
public class WebView extends AppCompatActivity {

    private static final String LOGTAG="THEGRANADATHEATER";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.webLayout);




        Bundle extras = getIntent().getExtras();





        String link = extras.getString("link");

        String sendingActivity = extras.getString("whichactivity");



        switch (sendingActivity) {
            case "baseball":
                layout.setBackgroundResource(R.drawable.baseballbk); break;
            case "hockey":
                layout.setBackgroundResource(R.drawable.wallpaperhockey); break;
            case "football":
                layout.setBackgroundResource(R.drawable.footballbk); break;
            case "basketball":
                layout.setBackgroundResource(R.drawable.wallpaperbasketball); break;
            case "cbb":
                layout.setBackgroundResource(R.drawable.wallpapercollegebasketball); break;
            case "cfb":
                layout.setBackgroundResource(R.drawable.cfootballbk5); break;
            case "oly":
                layout.setBackgroundResource(R.drawable.olympicsbk2); break;


            default:

                break;

        }



//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        android.webkit.WebView webView = (android.webkit.WebView) findViewById(R.id.webview1);
        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
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

