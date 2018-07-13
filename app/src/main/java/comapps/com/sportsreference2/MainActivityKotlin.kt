package comapps.com.sportsreference2

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.itemlayoutrecycler_constraint.view.*
import java.util.*

/**
 * Created by me on 3/21/2017.
 */

class MainActivityKotlin : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private var itemsHistory: ArrayList<SportsItem>? = ArrayList()
    private val type = object : TypeToken<java.util.ArrayList<SportsItem>>() {}.type
    private var spinnerHistoryAdapter: HistorySpinnerAdapter? = null
    private var checkBoxAddFree: CheckBox? = null
    private var spinnerMlb: Spinner? = null
    private var spinnerNfl: Spinner? = null
    private var spinnerNhl: Spinner? = null
    private var spinnerNba: Spinner? = null

    private var spinnerHistory: Spinner? = null

    private var userIsInteracting: Boolean = false

    private var loaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tabbedlayoutmain)

        spinnerHistory = findViewById(R.id.spinnerHistory) as Spinner

        viewPager = findViewById(R.id.simpleViewPager)
        val tabLayout = findViewById<TabLayout>(R.id.simpleTabLayout)


        val firstTab = tabLayout.newTab()
        firstTab.text = null // set the Text for the first Tab
        firstTab.setIcon(R.drawable.na_icon) // set an icon for the
        tabLayout.addTab(firstTab) // add  the tab at in the TabLayout

        val secondTab = tabLayout.newTab()
        secondTab.text = null // set the Text for the second Tab
        secondTab.setIcon(R.drawable.world_icon) // set an icon for the
        tabLayout.addTab(secondTab) // add  the tab  in the TabLayout


        for (i in 0 until tabLayout.tabCount) {

            val imageView = ImageView(applicationContext)
            if (i == 0) {
                imageView.setImageResource(R.drawable.na_icon)

            } else {
                imageView.setImageResource(R.drawable.world_icon)
            }



            tabLayout.getTabAt(i)!!.customView = imageView
        }

        /*   for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.tabiconlayout);
        }*/

        /*  prefs = this.getSharedPreferences(
                  "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE)*/


        val adapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                Log.e(TAG, "Tab selected ----> " + tab.position)

                //THIS!!

                viewPager.currentItem = tab.position


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        itemsHistory = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.sih, type)

        if (itemsHistory != null) {
            loadHistory()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        menuInflater.inflate(R.menu.menu_main_noadd, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.loadData) {

            val intentLoadData = Intent()
            intentLoadData.setClass(this.applicationContext, LoadData::class.java)
            startActivity(intentLoadData)

        }


        if (id == R.id.myteams) {


            val popDialogBuilder = AlertDialog.Builder(this)
            val mView = layoutInflater.inflate(R.layout.customdialogfavorites, null)
            //    popDialogBuilder.setIcon(R.mipmap.ic_launcher);
            //    popDialogBuilder.setTitle("Characters for Filter");

            popDialogBuilder.setView(mView)
            val alertDialog = popDialogBuilder.create()

            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.BLACK))
            alertDialog.show()

            spinnerMlb = mView.findViewById<View>(R.id.spinnerMLB) as Spinner
            spinnerNfl = mView.findViewById<View>(R.id.spinnerNFL) as Spinner
            spinnerNhl = mView.findViewById<View>(R.id.spinnerNHL) as Spinner
            spinnerNba = mView.findViewById<View>(R.id.spinnerNBA) as Spinner


            spinnerMlb!!.setSelection(prefs.mlbfav)
            spinnerNfl!!.setSelection(prefs.nflfav)
            spinnerNhl!!.setSelection(prefs.nhlfav)
            spinnerNba!!.setSelection(prefs.nbafav)


            spinnerMlb!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                    if (i != 0) {
                        prefs.mlbfav = i
                    }


                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }

            spinnerNfl!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                    if (i != 0) {

                        //    prefs!!.edit().putString("NFLFAV", spinnerNfl!!.selectedItem.toString()).apply()
                        prefs.nflfav = i

                    }
                }


                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }

            spinnerNhl!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                    if (i != 0) {


                        //    prefs!!.edit().putString("NHLFAV", spinnerNhl!!.selectedItem.toString()).apply()
                        prefs.nhlfav = i

                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }

            spinnerNba!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                    if (i != 0) {


                        //    prefs!!.edit().putString("NBAFAV", spinnerNba!!.selectedItem.toString()).apply()
                        prefs.nbafav = i
                    }

                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }


        }

        if (id == R.id.settings) {


            val popDialogBuilder = AlertDialog.Builder(this)
            val mView = layoutInflater.inflate(R.layout.customdialogsettings, null)
            //    popDialogBuilder.setIcon(R.mipmap.ic_launcher);
            //    popDialogBuilder.setTitle("Characters for Filter");

            val clearHistory = mView.findViewById<Button>(R.id.buttonClearHistory)


            if (itemsHistory?.size != 0) {

                clearHistory.visibility = View.VISIBLE
                clearHistory.setText(R.string.ch)

            } else {
                clearHistory.visibility = View.GONE
                spinnerHistory?.visibility = View.GONE
            }







            clearHistory.setOnClickListener {

                var i = 0
                itemsHistory!!.forEach { println("The item is $it"); i++ }

                prefs.sih = ""
                itemsHistory!!.clear()
                spinnerHistoryAdapter!!.notifyDataSetChanged()

                val toast = Toast.makeText(this@MainActivityKotlin,
                        "HISTORY CLEARED",
                        Toast.LENGTH_SHORT)

                toast.setGravity(Gravity.TOP, 0, 400)
                centerText(toast.view)
                toast.show()


            }


            val spinnerFilter = mView.findViewById<Spinner>(R.id.spinnerFilterChar)

            spinnerFilter.setSelection(prefs.timerInt - 1)

            spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                    if (i >= 0) {

                        prefs.timerInt = i + 1
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }

            checkBoxAddFree = mView.findViewById(R.id.checkBoxAds)
            checkBoxAddFree!!.isChecked = prefs.addFree


            // Button OK
            popDialogBuilder.setPositiveButton("Ok") { dialog, _ ->
                val toastString: String? = if (checkBoxAddFree!!.isChecked) {
                    spinnerFilter.selectedItem.toString() + " characters for filter" +
                            "\n(no Ads)"
                } else {
                    spinnerFilter.selectedItem.toString() + " seconds auto-enter"
                }



                prefs.addFree = checkBoxAddFree!!.isChecked


                val toast = Toast.makeText(this@MainActivityKotlin,
                        toastString,
                        Toast.LENGTH_SHORT)

                toast.setGravity(Gravity.TOP, 0, 400)
                centerText(toast.view)
                toast.show()


                dialog.dismiss()
            }

            popDialogBuilder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }


            popDialogBuilder.setView(mView)
            val alertDialog = popDialogBuilder.create()

            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.BLACK))
            alertDialog.show()


            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE)


        }

        return super.onOptionsItemSelected(item)
    }


    private fun centerText(view: View) {
        if (view is TextView) {
            view.gravity = Gravity.CENTER
        } else if (view is ViewGroup) {
            val n = view.childCount
            for (i in 0 until n) {
                centerText(view.getChildAt(i))
            }
        }
    }

    private fun loadHistory() {

        /*  try {
              itemsHistory = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.sih, type)
          } catch (e: Exception) {
              val dummySportsItem = SportsItem(name = "SEARCH HISTORY", link = "",
                      seasons = "", type = "", position = "")
              //  itemsHistory = ArrayList()
              itemsHistory?.add(dummySportsItem)
              val jsonHistoryList = Gson().toJson(itemsHistory)
              prefs.sih = jsonHistoryList
          } finally {
              spinnerHistoryAdapter = HistorySpinnerAdapter(applicationContext, itemsHistory)
              spinnerHistoryAdapter!!.notifyDataSetInvalidated()
              spinnerHistoryAdapter!!.notifyDataSetChanged()
          }*/

        spinnerHistory?.visibility = View.VISIBLE

        println("itemsHistory size ${itemsHistory?.size}")

        itemsHistory = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.sih, type)

        if (!(prefs.sih).contains("SEARCH HISTORY")) {

            val dummySportsItem = SportsItem(name = "SEARCH HISTORY", link = "",
                    type = "", position = "", sport = "")
            //  itemsHistory = ArrayList()
            itemsHistory?.add(0, dummySportsItem)
            val jsonHistoryList = Gson().toJson(itemsHistory)
            prefs.sih = jsonHistoryList


        }



        spinnerHistoryAdapter = HistorySpinnerAdapter(applicationContext, itemsHistory)
        spinnerHistoryAdapter!!.notifyDataSetInvalidated()
        spinnerHistoryAdapter!!.notifyDataSetChanged()


        spinnerHistory!!.adapter = spinnerHistoryAdapter

        //      if ( userIsInteracting ) {spinnerHistory!!.isSelected = true} else {spinnerHistory!!.isSelected = false}
        spinnerHistory!!.setSelection(0, true)


        spinnerHistory!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                println("MAK onItemSelectedListener $userIsInteracting $position")
                //    dimBackground()

                val intent: Intent

                if (userIsInteracting) {
                    intent = Intent(application.applicationContext, WebView::class.java)
                    val gson = Gson()
                    intent.putExtra("sportsItemObject", gson.toJson(itemsHistory!![position]))
                    intent.putExtra("sentFrom", "HISTORY")
                    println("MAK Intent ${gson.toJson(itemsHistory!![position])} $userIsInteracting $position")
                    userIsInteracting = false
                    startActivity(intent)


                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {


            }

        }
    }


    inner class HistorySpinnerAdapter(internal val context: Context, private val itemsHistory:
    ArrayList<SportsItem>?) : BaseAdapter() {

        override fun getCount(): Int {
            return itemsHistory?.size ?: 0
        }

        override fun getItem(i: Int): SportsItem? {
            return itemsHistory?.get(i)
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            var view = view
            if (view == null) {
                val inflater = context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(R.layout.itemlayouthistory, viewGroup, false)
            }
            val itemFromHistory = itemsHistory!![i]

            val txtName = view!!.findViewById<TextView>(R.id.textViewNameHistory)
            val txtSeasons = view.findViewById<TextView>(R.id.textViewSeasonsHistory)
            val txtType = view.findViewById<TextView>(R.id.textViewTypeHistory)
            val imgView = view.findViewById<ImageView>(R.id.imageViewIconHistory)


            txtName.text = itemFromHistory.name
            if (itemFromHistory.type == "player" || itemFromHistory.type == "team"
                    || itemFromHistory.type == "school" || itemFromHistory.type == "") {
                txtType.visibility = View.GONE
            } else {
                txtType.visibility = View.VISIBLE
                txtType.text = "(${itemFromHistory.type})"
            }

         val year: Int

            year = Calendar.getInstance().get(Calendar.YEAR)

            var careerSpan = ""


            if (itemFromHistory.lastSeason == year.toString() && itemFromHistory.firstSeason != "") {

                careerSpan = itemFromHistory.firstSeason + "-"

            } else {

                careerSpan = itemFromHistory.firstSeason + "-" + itemFromHistory.lastSeason

            }


            println("${ContentValues.TAG} sportsItem careerSpan $careerSpan")

            try {
                if ( careerSpan.startsWith("-")) {
                    careerSpan = careerSpan.substring(1, careerSpan.length)
                }
            } catch (e: Exception) {

                careerSpan = ""

            }



            txtSeasons.text = careerSpan




            if (itemFromHistory.sport.contains("football")) {
                imgView.setImageResource(R.drawable.football_icon_new2)
            } else if (itemFromHistory.sport.contains("baseball")) {
                imgView.setImageResource(R.drawable.baseball_icon_new2)
            } else if (itemFromHistory.sport.contains("soccer")) {
                imgView.setImageResource(R.drawable.fbreficonp)
            } else if (itemFromHistory.sport.contains("basketball")) {
                imgView.setImageResource(R.drawable.basketball_icon_new2)
            } else if (itemFromHistory.sport.contains("hockey")) {
                imgView.setImageResource(R.drawable.hockey_icon_new2)
            } else if (itemFromHistory.sport.contains("college_football")) {
                imgView.setImageResource(R.drawable.collegefootball_icon_new2)
            } else if (itemFromHistory.sport.contains("college_basketball")) {
                imgView.setImageResource(R.drawable.collegebasketball_icon_new2)
            } else if (itemFromHistory.name.contains("SEARCH HISTORY")) {
                imgView.setImageResource(R.drawable.sricon)
                //  view.visibility = View.GONE
            }

            return view
        }

    }

    public override fun onResume() {
        super.onResume()

        if (!loaded) {
            loaded = true
        } else {
            itemsHistory?.clear()
            loadHistory()

            userIsInteracting = false

            println("ON RESUME")

        }

    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        userIsInteracting = true
    }


    companion object {

        private const val TAG = "MAINACTIVITY"
    }


}


