package comapps.com.sportsreference2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by me on 4/21/2015.
 */
public class Splash extends Activity implements Animation.AnimationListener {

    public static final String LOGTAG="SPORTSREF2 SPLASH";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Animation a = AnimationUtils
                .loadAnimation(this, R.anim.startanimation);
        a.setAnimationListener(this);

        ImageView iv = (ImageView) findViewById(R.id.imageView);
        //	iv.clearAnimation();
        iv.startAnimation(a);


    }

    @Override
    public void onAnimationEnd(Animation animation) {


        Intent mainIntent = new Intent().setClass(Splash.this,
                MainActivity.class);

        overridePendingTransition(R.anim.fadeinanimation, 0);

        startActivity(mainIntent);

        finish();

        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }
}
