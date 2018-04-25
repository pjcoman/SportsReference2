package comapps.com.sportsreference2

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by me on 3/14/2018.
 */
class Prefs (context: Context) {
    private val PREFS_FILENAME = "comapps.com.sportsreference2.prefs"
    private val SPORTS_ITEM_HISTORY = "sih"
    private val BB_ITEMS = "bbi"
    private val FB_ITEMS = "fbi"
    private val H_ITEMS = "hi"
    private val BKB_ITEMS = "bkbi"
    private val CFB_ITEMS = "cbkbi"
    private val CBKB_ITEMS = "cfbi"
    private val MLBFAVSPIN = "mlb_spin"
    private val NHLFAVSPIN = "nhl_spin"
    private val NBAFAVSPIN = "nba_spin"
    private val NFLFAVSPIN = "nfl_spin"
    private val FILTER_INT = "filter_integer"
    private val ADD_FREE = "add_free"
    private val SHOW_INSTRUCTIONS = "show_instructions"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var sih: String
        get() = prefs.getString(SPORTS_ITEM_HISTORY, "")
        set(value) = prefs.edit().putString(SPORTS_ITEM_HISTORY, value).apply()
    var bbi: String
        get() = prefs.getString(BB_ITEMS, "")
        set(value) = prefs.edit().putString(BB_ITEMS, value).apply()
    var fbi: String
        get() = prefs.getString(FB_ITEMS, "")
        set(value) = prefs.edit().putString(FB_ITEMS, value).apply()
    var hi: String
        get() = prefs.getString(H_ITEMS, "")
        set(value) = prefs.edit().putString(H_ITEMS, value).apply()
    var bkbi: String
        get() = prefs.getString(BKB_ITEMS, "")
        set(value) = prefs.edit().putString(BKB_ITEMS, value).apply()
    var cfbi: String
        get() = prefs.getString(CFB_ITEMS, "")
        set(value) = prefs.edit().putString(CFB_ITEMS, value).apply()
    var cbkbi: String
        get() = prefs.getString(CBKB_ITEMS, "")
        set(value) = prefs.edit().putString(CBKB_ITEMS, value).apply()

    var mlbfav: Int
        get() = prefs.getInt(MLBFAVSPIN, 0)
        set(value) = prefs.edit().putInt(MLBFAVSPIN, value).apply()

    var nhlfav: Int
        get() = prefs.getInt(NHLFAVSPIN, 0)
        set(value) = prefs.edit().putInt(NHLFAVSPIN, value).apply()

    var nbafav: Int
        get() = prefs.getInt(NBAFAVSPIN, 0)
        set(value) = prefs.edit().putInt(NBAFAVSPIN, value).apply()

    var nflfav: Int
        get() = prefs.getInt(NFLFAVSPIN, 0)
        set(value) = prefs.edit().putInt(NFLFAVSPIN, value).apply()

    var filterInt: Int
        get() = prefs.getInt(FILTER_INT, 4)
        set(value) = prefs.edit().putInt(FILTER_INT, value).apply()

    var addFree: Boolean
        get() = prefs.getBoolean(ADD_FREE, false)
        set(value) = prefs.edit().putBoolean(ADD_FREE, value).apply()

    var showInstructions: Boolean
        get() = prefs.getBoolean(SHOW_INSTRUCTIONS, true)
        set(value) = prefs.edit().putBoolean(SHOW_INSTRUCTIONS, value).apply()
}