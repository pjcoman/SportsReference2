package comapps.com.sportsreference2


import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.sportsitemslayout.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 * Created by me on 4/21/2015.
 */
class SportsItemActivityKotlin : AppCompatActivity(), SensorEventListener  {


    private lateinit var editText: EditText
    private lateinit var textViewFilterAndResult: TextView
    private var linearLayout: LinearLayout? = null
    private var gestureDetector: Any? = null
    private lateinit var allSportsItems: ArrayList<SportsItem>
    private lateinit var queryListSportsItems: ArrayList<SportsItem>
  //  private lateinit var listSportsItemsUnique: ArrayList<SportsItem>
    private lateinit var sportsItem: SportsItem


    private var dbRef: DatabaseReference? = null
    private var sportPassed: String? = null
    private var sportsArray: Array<String>? = null
    private var sportsArrayPos: Int = 0
    private var extras: Bundle? = null

    private var sNoSpaces = ""
    private var remainingBeforeFilter = prefs.filterInt



    private lateinit var sportsItemRecyclerView: RecyclerView
    private lateinit var sportsItemAdapter: SportsItemAdapter

    private var mSensorManager : SensorManager?= null
    private var mAccelerometer : Sensor ?= null


    val sensorManager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sportsitemslayout)

        linearLayout = findViewById(R.id.linearlayoutsi)
        textViewFilterAndResult = findViewById(R.id.textViewCount)
        editText = findViewById(R.id.searchtext)

        sportsItemRecyclerView = findViewById(R.id.sportsItemRecyclerView)
        sportsItemRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        queryListSportsItems = ArrayList()
    //    listSportsItemsUnique = ArrayList()



        sportsArray = resources.getStringArray(comapps.com.sportsreference2.R.array.sports)

        gestureDetector = GestureDetector(MyGestureDetector())
        sportsItemRecyclerView.setOnTouchListener {
            v, aEvent ->
            (gestureDetector as GestureDetector).onTouchEvent(aEvent)
        }

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // focus in accelerometer
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        // setup the window

      //  sportsItemRecyclerView.adapter = sportsItemAdapter

        val Number = Random()
        val randomNumber = Number.nextInt(10)


        if ( randomNumber > 8 )   {
            editText.hint = ""

        }



        sportsItemAdapter = SportsItemAdapter(queryListSportsItems)

        //  ******************* BUNDLE ****************************
        extras = intent.extras
        sportPassed = extras!!.getString("sport").toString()

        //  ******************* BUNDLE ****************************

        when (sportPassed) {
            "baseball" -> {linearLayout!!.setBackgroundResource(R.drawable.wallpaper_bb); sportsArrayPos = 0}
            "football" -> {linearLayout!!.setBackgroundResource(R.drawable.wallpaper_fb); sportsArrayPos = 1}
            "hockey" -> {linearLayout!!.setBackgroundResource(R.drawable.wallpaper_h); sportsArrayPos = 2}
            "basketball" -> {linearLayout!!.setBackgroundResource(R.drawable.wallpaper_bkb); sportsArrayPos = 3}
            "college_football" -> {linearLayout!!.setBackgroundResource(R.drawable.wallpaper_cfb); sportsArrayPos = 4}
            "college_basketball" -> {linearLayout!!.setBackgroundResource(R.drawable.wallpaper_cbb); sportsArrayPos = 5}

        }


        if (resources.configuration.orientation <= 2) {
            val window = window
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }


        window.setBackgroundDrawableResource(R.drawable.fansbackground)



        editText.isEnabled = true
        editText.isCursorVisible = true


        progressBarLoad.visibility = View.GONE



        val bar = supportActionBar

        bar?.title = "SportsItem Reference"






        FirebaseDatabase.getInstance().reference.keepSynced(true)



        editText.setOnLongClickListener {
            // TODO Auto-generated method stub

            editText.setText("")
            textViewCount.visibility = View.GONE
            progressBarLoad.visibility = View.GONE

            queryListSportsItems.clear()


            sportsItemAdapter.notifyDataSetChanged()
            true
        }




        RxTextView.afterTextChangeEvents(editText)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { tvChangeEvent ->

                    val charFilterCount = prefs.filterInt
                    progressBarLoad.visibility = View.GONE
                    //    textViewCount.text = "$charFilterCount"
                    textViewFilterAndResult.visibility = View.VISIBLE
                    queryListSportsItems.clear()

                    val s = tvChangeEvent.view().text.toString()

                    sNoSpaces = s.replace("\\s".toRegex(), "")

                    if (charFilterCount <= sNoSpaces.length) {

                        Log.d("SIAK", "query string ----> $s")

                        this.queryDatabase(s.trimStart().trimEnd(), sportPassed!!)
                        progressBarLoad.visibility = View.VISIBLE
                        textViewFilterAndResult.visibility = View.INVISIBLE

                        try {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {

                        if ( sNoSpaces.isEmpty() ) {
                            val initialText = (remainingBeforeFilter - sNoSpaces.length).toString()
                            textViewFilterAndResult.text = initialText

                        } else {
                            textViewFilterAndResult.text = (remainingBeforeFilter - sNoSpaces.length).toString()
                        }



                    }

                }




/*


        editText.addTextChangedListener(object : TextWatcher {

            var charFilterCount = prefs.filterInt




            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                progressBarLoad.visibility = View.GONE
            //    textViewCount.text = "$charFilterCount"
                textView.visibility = View.VISIBLE
                queryListSportsItems.clear()

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                sNoSpaces = s.replace("\\s".toRegex(), "")

                if (sNoSpaces.length > charFilterCount ) {

                    Log.d("SIAK", "query string ----> " + s.toString())

                    queryDatabase(s.toString().trimStart().trimEnd(), sportPassed!!)
                    progressBarLoad.visibility = View.GONE


                    try {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }

            }

            override fun afterTextChanged(s: Editable) {

                sNoSpaces = s.replace("\\s".toRegex(), "")


                if (sNoSpaces.length >= charFilterCount ) {

                    Log.d("SIAK", "query string ----> " + s.toString())

                    queryDatabase(s.toString().trimStart().trimEnd(), sportPassed!!)
                    progressBarLoad.visibility = View.VISIBLE


                    try {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }

            }



        })

*/

        val listenforenter = findViewById<EditText>(R.id.searchtext)!!
        //   listen_for_enter.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        listenforenter.setOnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                Log.d("enter", "enter key hit")

                val gettext = findViewById<EditText>(R.id.searchtext)!!
                val search = gettext.text.toString()
                val searchfinal = search.replace(" ".toRegex(), "+")



                val httpSearch = when {
                    sportPassed.equals("baseball") -> "http://www.baseball-reference.com/search" +
                            "/search.fcgi?hint=&search=" + searchfinal + "&pid=&idx="
                    sportPassed.equals("football") -> "http://www.pro-football-reference.com/search" +
                            "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                    sportPassed.equals("basketball") -> "http://www.basketball-reference.com/search" +
                            "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                    sportPassed.equals("hockey") -> "http://www.hockey-reference.com/search/search" +
                            ".fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                    sportPassed.equals("college_basketball") -> "http://www.sports-reference.com/cbb/search" +
                            "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                    sportPassed.equals("college_football") ->  "http://www.sports-reference.com/cfb/search" +
                            "/search.fcgi?hint=" + searchfinal + "&search=" + searchfinal + "&pid="
                    else -> ""
                }


                if (!searchfinal.contentEquals("")) {

                    val intent = Intent(view.context, WebView::class.java)
                    intent.putExtra("linkSearchText", httpSearch)
                    intent.putExtra("sentFrom", "ACTIVITY")
                    startActivity(intent)

                }


                gettext.text = null

                true
            } else {
                false
            }
        }
    }




    // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx


        private val SWIPE_MIN_DISTANCE = 150
        private val SWIPE_MAX_OFF_PATH = 100
        private val SWIPE_THRESHOLD_VELOCITY = 100


    inner class MyGestureDetector : GestureDetector.SimpleOnGestureListener() {
        private var mLastOnDownEvent: MotionEvent? = null

        override fun onDown(e: MotionEvent): Boolean {
            //Android 4.0 bug means e1 in onFling may be NULL due to onLongPress eating it.
            mLastOnDownEvent = e
            return super.onDown(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if ( e1 == null || e2 == null) return false
            val dX = e2.x - e1.x
            val dY = e1.y - e2.y


            println(message = "dX $dX dY $dY sportPassed pre swipe $sportPassed")


            if ( dX < -600F ) {

                sportsArrayPos += 1

                if ( sportsArrayPos > 5) {
                    sportsArrayPos = 0
                }



            } else if ( dX > 600F) {

                sportsArrayPos -= 1

                if ( sportsArrayPos < 0) {
                    sportsArrayPos = 5
                }

            }




            val x:Int = sportsArrayPos
            sportPassed = sportsArray!![x]


            println(message = "sportPassed $sportPassed sportsArrayPos $sportsArrayPos")






            when (sportsArrayPos) {
                3 -> {
                    linearLayout!!.setBackgroundResource(R.drawable.wallpaper_bkb)}

                4 -> {
                    linearLayout!!.setBackgroundResource(R.drawable.wallpaper_cfb)}

                5 -> {
                    linearLayout!!.setBackgroundResource(R.drawable.wallpaper_cbb)}

                0 -> {
                    linearLayout!!.setBackgroundResource(R.drawable.wallpaper_bb)}

                1 -> {
                    linearLayout!!.setBackgroundResource(R.drawable.wallpaper_fb)}

                2 -> {
                    linearLayout!!.setBackgroundResource(R.drawable.wallpaper_h)}


            }


            return false
        }
    }




    // ******************************************************************************************************************

    private fun updateCharactersLeft() {

        textViewFilterAndResult.text = (remainingBeforeFilter - sNoSpaces.length).toString()

    }




    private fun queryDatabase(query: String, sport: String) {


        var snapShotDbSize: Long
        var countFiltered = 0
        var updateAllSportsItems = false

        doAsync {
            // Do something in a secondary thread

            val type = object : TypeToken<java.util.ArrayList<SportsItem>>() {}.type
            allSportsItems = ArrayList()

            try {

                when {
                    sportPassed.equals("baseball") ->
                        allSportsItems = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.bbi, type)
                    sportPassed.equals("football") ->
                        allSportsItems = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.fbi, type)
                    sportPassed.equals("basketball") ->
                        allSportsItems = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.bkbi, type)
                    sportPassed.equals("hockey") ->
                        allSportsItems = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.hi, type)
                    sportPassed.equals("college_basketball") ->
                        allSportsItems = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.cbkbi, type)
                    sportPassed.equals("college_football") ->
                        allSportsItems = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.cfbi, type)
                }
            } catch (e: Exception) {
            }



            dbRef = FirebaseDatabase.getInstance().reference.child(sport)
            dbRef!!.keepSynced(true)
            dbRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    println(databaseError.message)
                }

                override fun onDataChange(snapshot: DataSnapshot?) {
                    val children = snapshot!!.children
                    snapShotDbSize = snapshot.childrenCount

                    println(message = "allSportsItems ${allSportsItems.size} compare to snapshot size $snapShotDbSize")

                    if ( allSportsItems.size != snapShotDbSize.toInt()) {
                        allSportsItems.clear()
                        updateAllSportsItems = true
                    }


                   /* queryListSportsItems.clear()
                    sportsItemAdapter.notifyDataSetChanged()*/


                    countFiltered = 0

                    children.forEach {
                        sportsItem = it.getValue(SportsItem::class.java)!!

                 //       println(message = "|\"${it.key}|${it.child("name").value}|${it.child("link").value}|" +
                 //               "${it.child("seasons").value}|${it.child("type").value}|${it.child("position").value}|$count")

                //        println("sportsItem = ${sportsItem.toString()}")



                        val itemName: String = sportsItem.name




                        if (itemName.contains(query, true)) {

                            queryListSportsItems.add(sportsItem)
                            println(sportsItem.name)
                            countFiltered++

                            println("SportsItem added $sportsItem count is $countFiltered")

                        }

                        if ( updateAllSportsItems ) {
                            allSportsItems.add(sportsItem)
                        }




                    }



                    queryListSportsItems.sortedWith(compareBy({ it.name }))

                    uiThread {
                        // Back to the main thread

                        textViewFilterAndResult.text = "$countFiltered of $snapShotDbSize links"
                        textViewFilterAndResult.visibility = View.VISIBLE
                        progressBarLoad.visibility = View.GONE

                        sportsItemRecyclerView.adapter = sportsItemAdapter
                        sportsItemAdapter.notifyDataSetChanged()



                    }

                    println("$sportPassed allSportsItems size is ${allSportsItems.size}")
                    val jsonAllSportsItems = Gson().toJson(allSportsItems)

                    when {
                        sportPassed.equals("baseball") ->  prefs.bbi = jsonAllSportsItems
                        sportPassed.equals("football") -> prefs.fbi = jsonAllSportsItems
                        sportPassed.equals("basketball") -> prefs.bkbi = jsonAllSportsItems
                        sportPassed.equals("hockey") -> prefs.hi = jsonAllSportsItems
                        sportPassed.equals("college_basketball") -> prefs.cbkbi = jsonAllSportsItems
                        sportPassed.equals("college_football") ->  prefs.cfbi = jsonAllSportsItems
                    }

                }


            })



        }





    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main_noadd, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {

        val startTime = event!!.timestamp

        val alpha = 0.8
        val gravity = DoubleArray(3)
        val linear_acceleration = DoubleArray(3)

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event!!.values[0]
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

        linear_acceleration[0] = event.values[0] - gravity[0]
        linear_acceleration[1] = event.values[1] - gravity[1]
        linear_acceleration[2] = event.values[2] - gravity[2]

      /*  println(event.values.zip("XYZ".toList()).fold("") { acc, pair ->
            "$acc${pair.second}: ${pair.first}\n"
        })
        println(gravity.zip("GGG".toList()).fold("") { acc, pair ->
            "$acc${pair.second}: ${pair.first}\n"
        })*/
        println(linear_acceleration.zip("XYZ".toList()).fold("") { acc, pair ->
            "$acc${pair.second}: ${pair.first} "
        })




        println(message = "sport pre tilt $sportPassed $sportsArrayPos")

       if ( linear_acceleration[0] < -5 ) {

           sportsArrayPos++

           if ( sportsArrayPos == 6 ) {
               sportsArrayPos = 0
           }


       }


        if ( linear_acceleration[0] > 5 ) {



            sportsArrayPos--

            if ( sportsArrayPos == -1 ) {
                sportsArrayPos = 5
            }
        }

        when (sportsArrayPos) {
            3 -> {
                linearLayout!!.setBackgroundResource(R.drawable.wallpaper_bkb)}

            4 -> {
                linearLayout!!.setBackgroundResource(R.drawable.wallpaper_cfb)}

            5 -> {
                linearLayout!!.setBackgroundResource(R.drawable.wallpaper_cbb)}

            0 -> {
                linearLayout!!.setBackgroundResource(R.drawable.wallpaper_bb)}

            1 -> {
                linearLayout!!.setBackgroundResource(R.drawable.wallpaper_fb)}

            2 -> {
                linearLayout!!.setBackgroundResource(R.drawable.wallpaper_h)}


        }

        sportPassed = sportsArray!![sportsArrayPos]


    }





}





