package comapps.com.sportsreference2


import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.jakewharton.rxbinding.view.clickable
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.abc_alert_dialog_material.*
import kotlinx.android.synthetic.main.sportsitemslayout.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.onUiThread
import org.jsoup.Jsoup
import rx.android.schedulers.AndroidSchedulers
import java.sql.Ref
import java.text.Normalizer
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.coroutines.experimental.suspendCoroutine


fun deAccent(str: String): String {
    val nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(nfdNormalizedString).replaceAll("")
}



/**
 * Created by me on 4/21/2015.
 */
class SportsItemActivityKotlin : AppCompatActivity()/*, SensorEventListener*/ {


    private val TAG = "SIA_KOTLIN"

    private var sportsItemList = mutableListOf<SportsItem>()

    private lateinit var sportsItemRecyclerView: RecyclerView
    private lateinit var sportsItemAdapter: SportsItemAdapter
    private lateinit var lLayout: LinearLayout
    private lateinit var searchText: EditText
   private lateinit var linkToScrape: String

    private var i: Int = 0
    private var j: Int = 0

    private var pStatus = 0.0


    private var dbRef: DatabaseReference? = null
    private var docRef: DocumentReference? = null
    private var sportsArray: Array<String>? = null
    private var sportsArrayPos: Int = 0
    private var extras: Bundle? = null
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var SCRAPED = false
    private var firestoreListener: ListenerRegistration? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sportsitemslayout)


        sportsItemRecyclerView = findViewById(R.id.sportsItemRecyclerView)
        sportsItemRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,
                false)

        searchText = findViewById<EditText>(R.id.searchText)
        lLayout = findViewById<LinearLayout>(R.id.ll)


      sportsItemRecyclerView.setHasFixedSize(true)

        sportsArray = resources.getStringArray(comapps.com.sportsreference2.R.array.sports)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // focus in accelerometer
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        // setup the window


        //  ******************* BUNDLE ****************************
        extras = intent.extras
        val sport = extras!!.getString("sport").toString()

        //  ******************* BUNDLE ****************************






        docRef = FirebaseFirestore.getInstance().document("searchData/" + sport)



        sportsItemAdapter = SportsItemAdapter(sportsItemList, applicationContext,
                docRef!!)

        sportsItemRecyclerView.adapter = sportsItemAdapter



        val window = window


        if (resources.configuration.orientation <= 2) {

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }


        when (sport) {
            "baseball" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_bb);
                sportsArrayPos = 0

            }
            "football" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_fb);
                sportsArrayPos = 1
            }
            "hockey" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_h);
                sportsArrayPos = 2
            }
            "basketball" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_bkb);
                sportsArrayPos = 3
            }
            "college_football" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_cfb);
                sportsArrayPos = 4
            }
            "college_basketball" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_cbb);
                sportsArrayPos = 5
            }
            "soccer" -> {
                window.setBackgroundDrawableResource(R.drawable.soccerbg);
                sportsArrayPos = 6
            }
            "soccer_clubs" -> {
                window.setBackgroundDrawableResource(R.drawable.soccerbg);
                sportsArrayPos = 7
            }
            "soccer_leagues" -> {
                window.setBackgroundDrawableResource(R.drawable.soccerbg);
                sportsArrayPos = 7
            }
            "soccer_wc" -> {
                window.setBackgroundDrawableResource(R.drawable.soccerbg);
                sportsArrayPos = 7
            }

        }



        searchText.isEnabled = true
        searchText.isCursorVisible = true

        if (!sport.contains("college")) {
            searchText.hint = getString(R.string.edittext_hint2)
        } else if (sport.contains("soccer_")) {
            searchText.hint = getString(R.string.edittext_hint3)
        }


        val bar = supportActionBar

        bar?.title = "SportsItem Reference"

  searchText.setOnLongClickListener {
            // TODO Auto-generated method stub

            try {
                firestoreListener!!.remove()
            } catch (e: Exception) {
            }


            searchText.setText("")

            //    sportsItemRecyclerView.invalidate()
            sportsItemList.clear()
            sportsItemAdapter.notifyDataSetChanged()




            true
        }

        if ( sport.contains("soccer_")) {

            sportsItemList.clear()


            this.queryDatabase("show_all", sport)

            lLayout.visibility = View.GONE
            searchText.visibility = View.GONE

        } else {

            RxTextView.afterTextChangeEvents(searchText)
                    .debounce(prefs.timerInt.toLong() * 1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { tvChangeEvent ->


                        println("$TAG stopped typing")

                        val s = tvChangeEvent.view().text.toString().toLowerCase()

                        if ((s.trim().length > 3 && !sport.contains("college")) ||
                                (s.trim().length > 5 && s.trim().contains(" "))) {

                            sportsItemList.clear()

                            this.queryDatabase(deAccent(s.toLowerCase()), sport)

                            println("$TAG query ----> ${s.toLowerCase()} $sport")

                            lLayout.visibility = View.VISIBLE


                            try {
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }


                    }



        }





    }


    private fun queryDatabase(query: String, sport: String) {


        println("$TAG query, sport ----> $query, $sport")


        var colPath: String

        if ( query == "show_all") {
            
            colPath = "show_all"
            
        } else {

            val colPathInit = query.replace(" ", "")
            
            try {
                colPath = colPathInit.substring(0, 4)
                println("$TAG SportsItem colPath ----> $colPath")
            } catch (e: Exception) {
                colPath = colPathInit.substring(0, 3)
                println("$TAG SportsItem colPath ----> $colPath")
            }


        }

     


        firestoreListener = docRef!!.collection(colPath)
                .addSnapshotListener(EventListener { documentSnapshots, e ->
                    if (e != null) {
                        Log.e(TAG, "Listen failed!", e)
                        return@EventListener
                    }


                    sportsItemList.clear()

                    if ( query == "show_all") {

                        documentSnapshots?.forEach { sportsItemList.add(it.toObject(SportsItem::class.java))  }

                    } else {

                        documentSnapshots?.forEach {

                                 if ((deAccent(it.toObject(SportsItem::class.java).name).toLowerCase()).contains(query)) {
                                     sportsItemList.add(it.toObject(SportsItem::class.java))

                            }
                        }

                    }
                    sportsItemAdapter.notifyDataSetChanged()


                })



        docRef!!.collection(colPath).get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {

                        println("$TAG afterdoc search ${sportsItemList.size}")

                        val random = (0..2).shuffled().last()

                        if ( sportsItemList.size == 0 || random == 0) {

                            if (!sport.contains("college_football")) {


                                dbRef = FirebaseDatabase.getInstance().reference.child(sport)

                                if (sport.contains("college")) {
                                    dbRef!!.orderByChild("name").startAt(query.subSequence(0, 1)
                                            .toString()).limitToFirst(500)
                                }

                                println("$TAG child ----> $sport")



                                dbRef!!.addListenerForSingleValueEvent(object :
                                        ValueEventListener {


                                    override fun onCancelled(databaseError: DatabaseError) {
                                        println("$TAG databaseError.message")
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        val children = p0.children
                                        val numberOfItemsLeft = p0.childrenCount.toBigInteger()


                                        i = 0
                                        j = 0


                                        var filteredSportsItems: List<DataSnapshot> = ArrayList<DataSnapshot>()
                                        try {
                                            filteredSportsItems = children.filter {
                                                deAccent((it.getValue(SportsItem::class.java))!!.name.toLowerCase()).contains(query) }
                                        } catch (e: Exception) {
                                            println("$TAG error $e")
                                        }



                                        filteredSportsItems.forEach {


                                            i++
                                            val remainder = (numberOfItemsLeft .toInt() - i) %
                                                    10

                                            if (remainder == 0) {


                                                val iDouble = i.toDouble()
                                                pStatus = (iDouble / p0.childrenCount) * 100
                                                val percentText = pStatus.toInt().toString() + "%"

                                                println("$TAG children count (i,j) " +
                                                        "${p0.childrenCount} ($i,$j) $percentText")




                                            }





                                            try {

                                                val itemDbObject = it.getValue(SportsItem::class.java)
                                                val nameToLC = itemDbObject!!.name.toLowerCase()



                                                if (deAccent(nameToLC).contains(query) || query == "show_all") {


                                                    j++

                                                    thread {



                                                        //************scrape*******************


                                                        /* do something */
                                                        var schoolOrTeam = itemDbObject.schoolOrTeam
                                                        var position = itemDbObject.position
                                                        var firstSeason = itemDbObject.firstSeason
                                                        var lastSeason = itemDbObject.lastSeason
                                                        val link = itemDbObject.link.replace("`",
                                                                "%27").trim()
                                                        val type = itemDbObject.type
                                                        val name = itemDbObject.name
                                                        SCRAPED = itemDbObject.SCRAPED


                                                        if (!link.contains("www")) {

                                                            linkToScrape = when {
                                                                sport.equals("baseball") ->
                                                                    "https://www.baseball-reference.com" + link
                                                                sport.equals("football") ->
                                                                    "https://www.pro-football-reference.com" + link
                                                                sport.equals("basketball") ->
                                                                    "https://www.basketball-reference.com" + link
                                                                sport.equals("hockey") ->
                                                                    "https://www.hockey-reference.com" + link
                                                                sport.equals("college_basketball") ->
                                                                    "https://www.sports-reference.com" + link
                                                                sport.equals("college_football") ->
                                                                    "https://www.sports-reference.com" + link
                                                                sport.contains("soccer") ->
                                                                    "https://www.fbref.com" + link
                                                                else -> ""
                                                            }

                                                        }

                                                        try {
                                                            if (sport.contains("college")) {

                                                                Jsoup.connect(linkToScrape).get().run {
                                                                    //   println("$TAG $title")
                                                                    select("p:matches(School:)").forEachIndexed { index, element ->


                                                                        schoolOrTeam = element.text()


                                                                    }


                                                                }
                                                                Jsoup.connect(linkToScrape).get().run {
                                                                    //   println("$TAG $title")
                                                                    select("p:matches(Position:)")
                                                                            .forEachIndexed { index, element ->

                                                                                position = element.text()


                                                                            }

                                                                    select("p:matches(Positions:)")
                                                                            .forEachIndexed { index, element ->


                                                                                position = element.text()


                                                                            }


                                                                }


                                                            } else {

                                                                if (sport == "baseball" &&
                                                                        link.contains("players")) {


                                                                    Jsoup.connect(linkToScrape).get()
                                                                            .run {
                                                                                //   println("$TAG $title")
                                                                                select("p:matches(Positions:)")
                                                                                        .forEachIndexed { index, element ->

                                                                                            position = element.text().replace("Positions:", "")

                                                                                        }

                                                                                select("p:matches(Position:)")
                                                                                        .forEachIndexed { index, element ->


                                                                                            position = element.text().replace("Position:", "")


                                                                                        }

                                                                                select("p:matches(Team:)").forEachIndexed { index, element ->


                                                                                    schoolOrTeam = element.text()

                                                                                }





                                                                                select("p:matches(Debut:)").forEachIndexed { index, element ->

                                                                                    val debutSeason = element.text().replace("Debut: ", "")
                                                                                    val debutSeasonSplit = debutSeason.split(",")

                                                                                    val debutForDocSplit = debutSeasonSplit[1].split(" ")

                                                                                    firstSeason = debutForDocSplit[1].trim()


                                                                                }



                                                                                select("p:matches(Last Game:)")
                                                                                        .forEachIndexed { index, element ->

                                                                                            val serviceTime = element.text()
                                                                                            val serviceTimeSplit = serviceTime.split(",", "(")
                                                                                            lastSeason = serviceTimeSplit[1].trim()


                                                                                        }


                                                                            }


                                                                } else {

                                                                    if ( !sport.contains
                                                                            ("soccer_")) {
                                                                        Jsoup.connect(linkToScrape).get().run {
                                                                            //   println("$TAG $title")
                                                                            select("p:matches(Position)").forEachIndexed { index, element ->


                                                                                position = element.text()


                                                                            }
                                                                        }

                                                                    }
                                                                }


                                                            }
                                                        } catch (e: Exception) {
                                                        }


                                                        val hashMap = HashMap<String, Any?>()

                                                        try {
                                                            hashMap.put("name", name.trim())
                                                            hashMap.put("link", link.trim())
                                                            hashMap.put("position", position.trim())
                                                            hashMap.put("type", type.trim())
                                                            hashMap.put("schoolOrTeam", schoolOrTeam.trim())
                                                            hashMap.put("firstSeason", firstSeason.trim())
                                                            hashMap.put("lastSeason", lastSeason.trim())
                                                            hashMap.put("years", firstSeason + "-" + lastSeason.trim())
                                                            hashMap.put("sport", sport.trim())
                                                            hashMap.put("SCRAPED", true)

                                                            /*           dbRef!!.child(it.key
                                                                               .toString())
                                                                       .setValue(hashMap)*/

                                                            println("$TAG child ${it.key.toString()} $hashMap")


                                                        } catch (e: Exception) {

                                                        }

                                                        var docId: String

                                                        if (sport == "soccer") {

                                                            val linkSplit = link.split("/")
                                                            docId = linkSplit[3].toUpperCase()

                                                        } else {


                                                            var addressSplit: CharSequence = ""

                                                            if (type == "team") {


                                                                addressSplit = (name).replace(" ", "")
                                                                //       println("$TAG docId create $addressSplit")


                                                            } else {

                                                                try {
                                                                    addressSplit = (link).subSequence(
                                                                            (link).lastIndexOf("/"),
                                                                            (link).lastIndexOf("."))

                                                                } catch (e: Exception) {
                                                                    addressSplit = (name).replace(" ", "")

                                                                }


                                                            }




                                                            try {
                                                                docId = "$addressSplit${(type)[0]}".replace("-", "").trim()
                                                                        .toUpperCase()
                                                                //            println("$TAG docId create (try) " +
                                                                //                    "$addressSplit")
                                                            } catch (e: Exception) {

                                                                docId = addressSplit.toString().replace("-", "").toUpperCase()
                                                                //            println("$TAG docId create (catch) " +
                                                                //                    "$addressSplit")
                                                            }

                                                        }


                                                        docRef!!
                                                                .collection(colPath)
                                                                .document(docId)
                                                                .set(hashMap as Map<String, Any?>)
                                                                .addOnCompleteListener { task ->
                                                                    if (task.isSuccessful) {
                                                                        println("$TAG $hashMap added/updated")


                                                                    } else {
                                                                        println("$TAG $hashMap add/update failed")
                                                                    }


                                                                }

                                                        println("$TAG scraping")




                                                    }
                                                    //*************scrape finished *********************

                                                    runOnUiThread { lLayout
                                                            .visibility = View.GONE }


                                                }





                                                //         println("$TAG convert successful $it")
                                            } catch (e: Exception) {
                                                println("$TAG failed to convert $it")
                                            }




                                        }

                                        lLayout.visibility = View.GONE



                                        if (j == 0 && sportsItemList.size == 0) {


                                            println("$TAG j=$j sportsItemList size is " +
                                                    "${sportsItemList.size}")

                                            val searchfinal = query.replace(" ".toRegex(), "+")


                                            val httpSearch = when {
                                                sport.equals("baseball") -> "https://www" +
                                                        ".baseball-reference.com/search" +
                                                        "/search.fcgi?hint=&search=" + searchfinal + "&pid=&idx="
                                                sport.equals("football") -> "https://www" +
                                                        ".pro-football-reference.com/search" +
                                                        "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                                                sport.equals("basketball") -> "https://www" +
                                                        ".basketball-reference.com/search" +
                                                        "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                                                sport.equals("hockey") -> "https://www" +
                                                        ".hockey-reference.com/search/search" +
                                                        ".fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                                                sport.equals("college_basketball") ->
                                                    "https://www.sports-reference.com/cbb/search" +
                                                            "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                                                sport.equals("college_football") ->
                                                    "https://www.sports-reference.com/cfb/search" +
                                                            "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                                                sport.contains("soccer") ->
                                                    "https://fbref.com/search/search" +
                                                            ".fcgi?hint=&search=" + searchfinal + "&pid=&idx="
                                                else -> ""
                                            }


                                            searchText.text = null


                                            val intent = Intent(this@SportsItemActivityKotlin, WebView::class.java)
                                            intent.putExtra("linkSearchText", httpSearch)
                                            intent.putExtra("sentFrom", "ACTIVITY")
                                            startActivity(intent)


                                        }

                                    }
                                })



                                //***********************************doAsync finished***********************************
                            } else {

                                val searchfinal = query.replace(" ".toRegex(), "+")


                                val httpSearch = when {
                                    sport.equals("college_basketball") ->
                                        "https://www.sports-reference.com/cbb/search" +
                                                "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                                    sport.equals("college_football") ->
                                        "https://www.sports-reference.com/cfb/search" +
                                                "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                                    else -> ""
                                }


                                searchText.text = null
                                lLayout.visibility = View.GONE



                                val intent = Intent(this@SportsItemActivityKotlin, WebView::class.java)
                                intent.putExtra("linkSearchText", httpSearch)
                                intent.putExtra("sentFrom", "ACTIVITY")
                                startActivity(intent)


                            }

                        } else {

                            lLayout.visibility = View.GONE


                        }





                    }


                })



    }











    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main_noadd, menu)
        return true
    }


}





