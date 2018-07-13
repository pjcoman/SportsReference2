package comapps.com.sportsreference2

import android.content.Context
import android.content.SharedPreferences.Editor
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson

/**
 * Created by me on 8/29/2017.
 */

internal object Util {

    private const val TAG = "UTIL"


    fun toast(context: Context, message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, length).show()
    }


    fun addToHistoryList(item: SportsItem) {

        val itemsHistory = ArrayList<SportsItem>()

        val editor: Editor? = null


        try {
            Log.i(TAG, "itemHistory size " + itemsHistory.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (itemsHistory.size < 20) {
            if (!itemsHistory.contains(item)) {
                itemsHistory.add(item)
            }
        } else {
            if (!itemsHistory.contains(item)) {
                itemsHistory.add(item)
                // itemsHistory.remove(20);
            }
        }


        val jsonHistoryList = Gson().toJson(itemsHistory)
        editor!!.putString("SPORTSITEMS_HISTORY", jsonHistoryList)
        editor.commit()


        val itr = itemsHistory.iterator()
        while (itr.hasNext()) {
            val element = itr.next()
            Log.i(TAG, "itemHistory " + element.toString())
        }


    }

    fun <T> uniquefy(myList: ArrayList<T>): ArrayList<T> {

        val uniqueArrayList = ArrayList<T>()
        for (i in myList.indices) {
            if (!uniqueArrayList.contains(myList[i])) {
                uniqueArrayList.add(myList[i])
            }
        }

        return uniqueArrayList
    }


}