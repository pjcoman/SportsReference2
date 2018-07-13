package comapps.com.sportsreference2


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by me on 3/21/2017.
 */

internal class PagerAdapter(fm: FragmentManager, private val mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return NorthAmericaFragment()
            1 -> return WorldFragment()

            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }


}
