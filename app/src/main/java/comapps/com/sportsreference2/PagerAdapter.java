package comapps.com.sportsreference2;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by me on 3/21/2017.
 */

class PagerAdapter extends FragmentStatePagerAdapter {
    private final int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;


    }

    @Nullable
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new NorthAmericaFragment();
            case 1:
                return new WorldFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}
