package comapps.com.sportsreference2

import android.app.Application

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger

/**
 * Created by me on 8/6/2017.
 */


class MApplication : Application() {

    val prefs: Prefs by lazy {
        App.prefs!!
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)


        super.onCreate()
    }


}
