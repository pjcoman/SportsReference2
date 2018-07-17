package comapps.com.sportsreference2


import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
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
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.jakewharton.rxbinding.widget.RxTextView
import org.jsoup.Jsoup
import rx.android.schedulers.AndroidSchedulers
import java.text.Normalizer
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


fun deAccent(str: String): String {
    val nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(nfdNormalizedString).replaceAll("")
}


/**
 * Created by me on 4/21/2015.
 */
class SportsItemActivityKotlin : AppCompatActivity()/*, SensorEventListener*/ {


    private val TAG = "SIAKOTLIN"

    private var sportsItemList = mutableListOf<SportsItem>()
    private var filteredSportsItems: List<DataSnapshot> = ArrayList()
    private lateinit var sportsItemRecyclerView: RecyclerView
    private lateinit var sportsItemAdapter: SportsItemAdapter
    private lateinit var lLayout: LinearLayout
    private lateinit var searchText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var linkToScrape: String

    private var i: Int = 0
    private var j: Int = 0


    private var dbRef: DatabaseReference? = null
    private var docRef: DocumentReference? = null
    private var dBaseAddress: String? = null
    private var sportsArray: Array<String>? = null
    private var sportsArrayPos: Int = 0
    private var extras: Bundle? = null
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var collegeIndex: String = "1"
    private var firestoreListener: ListenerRegistration? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sportsitemslayout)


        sportsItemRecyclerView = findViewById(R.id.sportsItemRecyclerView)
        sportsItemRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,
                false)

        searchText = findViewById(R.id.searchText)
        lLayout = findViewById(R.id.ll)
        progressBar = findViewById(R.id.progressB)


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


        dBaseAddress = "https://sportsreference2.firebaseio.com/"

        docRef = FirebaseFirestore.getInstance().document("searchData/$sport")



        sportsItemAdapter = SportsItemAdapter(sportsItemList, applicationContext,
                docRef!!)

        sportsItemRecyclerView.adapter = sportsItemAdapter


        val window = window


        if (resources.configuration.orientation <= 2) {

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }



        when (sport) {
            "baseball" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_bb)
                sportsArrayPos = 0
                dBaseAddress = "https://sportsreference2-bb.firebaseio.com/"

            }
            "football" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_fb)
                sportsArrayPos = 1
                dBaseAddress = "https://sportsreference2-fb.firebaseio.com/"

            }
            "hockey" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_h)
                sportsArrayPos = 2
                dBaseAddress = "https://sportsreference2-h.firebaseio.com/"

            }
            "basketball" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_bkb)
                sportsArrayPos = 3
                dBaseAddress = "https://sportsreference2-bkb.firebaseio.com/"

            }
            "college_football" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_cfb)
                sportsArrayPos = 4
                dBaseAddress = "https://sportsreference2-cfb$collegeIndex.firebaseio.com/"

            }
            "college_basketball" -> {
                window.setBackgroundDrawableResource(R.drawable.wallpaper_cbb)
                sportsArrayPos = 5
                dBaseAddress = "https://sportsreference2-cbb$collegeIndex.firebaseio.com/"

            }
            "soccer" -> {
                window.setBackgroundDrawableResource(R.drawable.soccerbg)
                sportsArrayPos = 6
                dBaseAddress = "https://sportsreference2-soccer.firebaseio.com/"

            }


        }



        searchText.isEnabled = true
        searchText.isCursorVisible = true

        if (sport.contains("college")) {
            searchText.hint = getString(R.string.edittext_hint)
        } else if (sport.contains("soccer")) {
            searchText.hint = getString(R.string.edittext_hint3)
        } else {
            searchText.hint = getString(R.string.edittext_hint2)
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



        RxTextView.afterTextChangeEvents(searchText)
                .debounce(prefs.timerInt.toLong() * 1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { tvChangeEvent ->


                    println("$TAG stopped typing")

                    val s = tvChangeEvent.view().text.toString().toLowerCase()

                    if ( s.trim().length > 3 ) {

                        sportsItemList.clear()

                        this.queryDatabase(deAccent(s.toLowerCase()), sport)

                        println("$TAG query ----> ${s.toLowerCase()} $sport")


                        try {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }


                }


    }


    private fun queryDatabase(query: String, sport: String) {


        println("$TAG query, sport ----> $query, $sport")


        var colPath: String


        val colPathInit = query.replace(" ", "")

        try {
            colPath = colPathInit.substring(0, 4)
            println("$TAG SportsItem colPath ----> $colPath")
        } catch (e: Exception) {
            colPath = colPathInit.substring(0, 3) + "_"
            println("$TAG SportsItem colPath ----> $colPath")
        }







        firestoreListener = docRef!!.collection(colPath)
                .addSnapshotListener(EventListener { documentSnapshots, e ->
                    if (e != null) {
                        Log.e(TAG, "Listen failed!", e)
                        return@EventListener
                    }


                    sportsItemList.clear()



                    documentSnapshots?.forEach {

                        if ((deAccent(it.toObject(SportsItem::class.java).name).toLowerCase()).contains(query)) {
                            sportsItemList.add(it.toObject(SportsItem::class.java))

                        }
                    }


                    sportsItemAdapter.notifyDataSetChanged()


                })



        docRef!!.collection(colPath).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        println("$TAG afterdoc search ${sportsItemList.size}")

                        val random = (0..5).shuffled().last()

                        println("$TAG random $random")

                        if (sportsItemList.size == 0 || random == 0) {

                            runOnUiThread {
                                lLayout.visibility = View.VISIBLE
                            }

                            if ( random == 0 ) {
                                sportsItemList.clear()
                            }



                            if (!sport.contains("filteroutsport")) {

                                if (sport.contains("college")) {

                                    val letter = Character.toLowerCase(query[0])

                                    if (letter in 'd'..'h') {
                                        collegeIndex = "2"
                                    } else if (letter in 'i'..'k') {
                                        collegeIndex = "3"
                                    } else if (letter in 'l'..'q') {
                                        collegeIndex = "4"
                                    } else if (letter > 'q') {
                                        collegeIndex = "5"
                                    }

                                    dBaseAddress = if ( sport.contains("football")) {

                                        "https://sportsreference2-cfb$collegeIndex.firebaseio.com/"

                                    } else {

                                        "https://sportsreference2-cbb$collegeIndex.firebaseio.com/"

                                    }



                                    println("$TAG slqa ----> $sport $letter $query $dBaseAddress")


                                }

                                dbRef = FirebaseDatabase.getInstance(dBaseAddress!!).reference


                                dbRef!!.addListenerForSingleValueEvent(object :
                                        ValueEventListener {


                                    override fun onCancelled(databaseError: DatabaseError) {
                                        println("$TAG databaseError.message")
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        val children = p0.children

                                        println("$TAG sportsItems size " +
                                                "${children.count()}")


                                        i = 0
                                        j = 0

                                        try {
                                            filteredSportsItems = p0.children.filter {
                                                deAccent((it.getValue(SportsItem::class.java))!!
                                                        .name.toLowerCase()).contains(query)
                                            }
                                        } catch (e: Exception) {
                                            println("$TAG error $e")
                                        }

                                        println("$TAG filteredSportsItems size " +
                                                "${filteredSportsItems.size}")



                                        filteredSportsItems.forEach {


                                            i++
                                            try {
                                                thread {

                                                    val itemDbObject = it.getValue(SportsItem::class.java)



                                                    if (sportsItemList.contains(itemDbObject)) {

                                                        println("$TAG ${itemDbObject!!.name} in sportsItemList")

                                                    } else {

                                                        sportsItemList.add(itemDbObject!!)
                                                        println("$TAG ${itemDbObject.name} added")

                                                    }



                                                    try {
                                                        runOnUiThread {
                                                            sportsItemAdapter.notifyDataSetChanged()

                                                        }

                                                    } catch (e: Exception) {
                                                    }


                                                    //************scrape*******************


                                                    /* do something */
                                                    var schoolOrTeam = ""
                                                    var position = ""
                                                    var firstSeason = ""
                                                    var lastSeason = ""


                                                    if (!itemDbObject.link.contains("www")) {

                                                        linkToScrape = when {
                                                            sport == "baseball" ->
                                                                "https://www.baseball-reference.com" + itemDbObject.link
                                                            sport == "football" ->
                                                                "https://www.pro-football-reference.com" + itemDbObject.link
                                                            sport == "basketball" ->
                                                                "https://www.basketball-reference.com" + itemDbObject.link
                                                            sport == "hockey" ->
                                                                "https://www.hockey-reference.com" + itemDbObject.link
                                                            sport == "college_basketball" ->
                                                                "https://www.sports-reference.com" + itemDbObject.link
                                                            sport == "college_football" ->
                                                                "https://www.sports-reference.com" + itemDbObject.link
                                                            sport.contains("soccer") ->
                                                                "https://www.fbref.com" + itemDbObject.link
                                                            else -> ""
                                                        }

                                                    }

                                                    try {
                                                        if ( sport == "baseball" && itemDbObject.type =="player" ) {


                                                            Jsoup.connect(linkToScrape).get().run {
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


                                                        } else  if ( itemDbObject.type == "player" ) {

                                                            Jsoup.connect(linkToScrape).get().run {
                                                                //   println("$TAG $title")
                                                                select("p:matches(Position)").forEachIndexed { index, element ->


                                                                    position = element.text()


                                                                }

                                                                select("p:matches(School:)").forEachIndexed { index, element ->


                                                                    schoolOrTeam = element.text()


                                                                }

                                                            }


                                                        }


                                                    } catch (e: Exception) {
                                                    }


                                                    val hashMap = HashMap<String, Any?>()

                                                    try {
                                                        hashMap["schoolOrTeam"] = schoolOrTeam.trim()
                                                        hashMap["firstSeason"] = firstSeason.trim()
                                                        hashMap["lastSeason"] = lastSeason.trim()
                                                        hashMap["position"] = position.trim()
                                                        hashMap["sport"] = sport.trim()

                                                        hashMap["name"] = itemDbObject.name.trim()
                                                        hashMap["link"] = itemDbObject.link.trim()
                                                        hashMap["type"] = itemDbObject.type.trim()
                                                        hashMap["DB_ID"] = it.key

                                                        hashMap["SCRAPED"] = true

                                                        println("$TAG child ${it.key.toString()} $hashMap")


                                                    } catch (e: Exception) {

                                                    }

                                                    println("$TAG link ${itemDbObject.link}")
                                                    val id: List<String> = (itemDbObject.link).split("/")
                                                    val id2: List<String>?
                                                    var idFinal = ""

                                                    var arrayIndex = 4
                                                    var arrayIndexB = 2



                                                    if (sport != "soccer") {
                                                        if (sport.contains("college")) {
                                                            arrayIndex = 4
                                                        } else {
                                                            arrayIndex = 3
                                                            arrayIndexB = 1
                                                        }
                                                    }

                                                    println("$TAG id $id")

                                                    id2 = try {
                                                        id[arrayIndex].split(".")
                                                    } catch (e: Exception) {
                                                        id[arrayIndex - 1].split(".")
                                                    }
                                                    idFinal = deAccent(id2[0].replace("-", "").toLowerCase()) +
                                                            "_" + id[arrayIndexB].substring(0, 1)

                                                    println("$TAG id2 $id2")



                                                    if ( itemDbObject.link.contains("/teams/") ||
                                                            itemDbObject.link.contains
                                                            ("/schools/") )
                                                    {  idFinal = itemDbObject.name.replace(" ",
                                                            "").toLowerCase() }


                                                    println("$TAG idFinal $idFinal")


                                                    docRef!!
                                                            .collection(colPath)
                                                            .document(idFinal)
                                                            .set(hashMap as Map<String, Any?>, SetOptions.merge())
                                                            .addOnCompleteListener { task ->
                                                                if (task.isSuccessful) {
                                                                    println("$TAG $hashMap added/updated")


                                                                } else {
                                                                    println("$TAG $hashMap add/update failed")
                                                                }


                                                            }

                                                    println("$TAG scraping")


                                                    j++

                                                    val percentComplete = ((j / (filteredSportsItems.size)
                                                            .toDouble() * 100))
                                                    val percentCompleteString =
                                                            (percentComplete.toString()).split(".")

                                                    println("$TAG j $j ${percentCompleteString[0]}%")


                                                }
                                                //*************scrape finished *********************


                                                //         println("$TAG convert successful $it")
                                            } catch (e: Exception) {
                                                println("$TAG failed to convert $it")
                                            }


                                        }



                                        if (filteredSportsItems.isEmpty()) {


                                            val searchfinal = query.replace(" ".toRegex(), "+")
                                            val httpSearch = when {
                                                sport == "baseball" -> "https://www.baseball-reference.com/search/search.fcgi?hint=&search=$searchfinal&pid=&idx="
                                                sport == "football" -> "https://www.pro-football-reference.com/search/search.fcgi?hint=$searchfinal&search=$searchfinal&pid="
                                                sport == "basketball" -> "https://www.basketball-reference.com/search/search.fcgi?hint=$searchfinal&search=$searchfinal&pid="
                                                sport == "hockey" -> "https://www.hockey-reference.com/search/search.fcgi?hint=$searchfinal&search=$searchfinal&pid="
                                                sport == "college_basketball" -> "https://www.sports-reference.com/cbb/search/search.fcgi?hint=$searchfinal&search=$searchfinal&pid="
                                                sport == "college_football" -> "https://www.sports-reference.com/cfb/search/search.fcgi?hint=$searchfinal&search=$searchfinal&pid="
                                                sport == "soccer" -> "https://fbref.com/search/search.fcgi?hint=&search=$searchfinal&pid=&idx="
                                                else -> ""
                                            }

                                            searchText.text = null





                                            val intent = Intent(this@SportsItemActivityKotlin, WebView::class.java)
                                            intent.putExtra("linkSearchText", httpSearch)
                                            intent.putExtra("sentFrom", "ACTIVITY")
                                            startActivity(intent)


                                        } else {

                                            runOnUiThread { sportsItemAdapter.notifyDataSetChanged() }


                                        }


                                        runOnUiThread { lLayout.visibility = View.GONE }

                                    }
                                })


                                //***********************************doAsync finished***********************************
                            }

                        }


                    }


                }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main_noadd, menu)
        return true
    }


}





