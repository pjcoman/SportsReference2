package comapps.com.sportsreference2;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by me on 3/21/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;


    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NorthAmericaFragment tab1 = new NorthAmericaFragment();
                return tab1;
            case 1:
                WorldFragment tab2 = new WorldFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}
