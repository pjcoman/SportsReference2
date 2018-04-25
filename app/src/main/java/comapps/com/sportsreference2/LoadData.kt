package comapps.com.sportsreference2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.*
import com.google.firebase.database.*
import java.io.IOException
import java.net.URL

/**
 * Created by me on 3/2/2018.
 */

class LoadData : AppCompatActivity() {



    private var databaseReferenceSport: DatabaseReference? = null

    private lateinit var links: ArrayList<String>
    private lateinit var keys: ArrayList<String>
    private var sportsCategoriesSpinner: Spinner? = null
    private var sport: String? = null


    private lateinit var itemsKeyToObjectMap: HashMap<String, SportsItem>
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContentView(R.layout.loaddata)


        val bar = supportActionBar

        bar?.title = "Add Player"

        sportsCategoriesSpinner = findViewById<View>(R.id.spinnerCategory) as Spinner

        val loadButton = findViewById<View>(R.id.buttonLoad) as Button
        loadButton.isEnabled.not()


        var itemCount:Int

        links = ArrayList()
        keys = ArrayList()


        val spinnerArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sports))
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view

        sportsCategoriesSpinner!!.adapter = spinnerArrayAdapter

        sportsCategoriesSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, view: View, arg2: Int, arg3: Long) {

                if ( arg2 != 0 ) {

                    sport = sportsCategoriesSpinner!!.selectedItem.toString()

                    Toast.makeText(applicationContext, sport,
                            Toast.LENGTH_SHORT).show()

                    databaseReferenceSport = FirebaseDatabase.getInstance().reference.child(sport)


                    itemsKeyToObjectMap = HashMap()



                    databaseReferenceSport!!.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            println(databaseError.message)
                        }

                        override fun onDataChange(snapshot: DataSnapshot?) {
                            val children = snapshot!!.children
                            val dbSize = snapshot.childrenCount

                            itemCount = 1

                            println(message = "items in database $dbSize")
                            children.forEach {

                                val sportsItem = it.getValue(SportsItem::class.java)
                                sportsItem?.let { it1 -> itemsKeyToObjectMap.put(it.key, it1) }

                  //              println(message = "${it.key} ${itemsKeyToObjectMap.get(it.key)} count $itemCount")


                                sportsItem?.link?.let { it1 -> links.add(it1) }
                                itemCount++

                                val name: String? = sportsItem?.name
                        //        val key: String? = it.key

                        //        val splitToRemovePos = name!!.split("()")
                        //        println(name)

                                val type: String? = sportsItem?.type

                                  if ( sportsItem!!.seasons.contains("player",ignoreCase = false)){
                                    databaseReferenceSport!!.child(it.key).child("seasons").setValue(type)
                                      databaseReferenceSport!!.child(it.key).child("type").setValue("player")
                                    println(message = "item removed $it.key | $sportsItem |")
                                }

                              /*  try {
                                    databaseReferenceSport!!.child(it.key).child("position").setValue(splitToRemovePos[1])
                                } catch (e: Exception) {
                                    println("no position on item")
                                }
                                try {
                                    databaseReferenceSport!!.child(it.key).child("name").setValue(splitToRemovePos[0])
                                } catch (e: Exception) {
                                }*/

/*

                                      if (sportsItem.name.length == 0) {
                                          databaseReferenceSport!!.child(key).removeValue()
                                      }
*/

                           //     val splitName = sportsItem?.name!!.split("()")

                              /*  try {
                                    println("splitname ----> $splitName")
                                    databaseReferenceSport!!.child(it.key).child("position").setValue(splitName.get(1))
                                    databaseReferenceSport!!.child(it.key).child("name")
                                            .setValue(splitName.get(0))



                                } catch (e: Exception) {
                                }*/

                                //       links.add(link!!)

                                }

                            loadButton.isEnabled

                            }




                    })
                }

            }



            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub

            }
        }

        //*****************************************************************************************************************************************

        // *****************************************************************************************************

        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )


        loadButton.setOnClickListener {

            var itemsToLoadStream: String? = null

            try {

                itemsToLoadStream = applicationContext.assets.open("itemstoload.txt").
                        bufferedReader().use {
                    it.readText()
                }



            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

      //        println("$itemsToLoadStream")

            val itemsToLoadTypedArray = itemsToLoadStream?.split("\n".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()




            for (i in 0 until itemsToLoadTypedArray?.size!!) {
                val itemFields = itemsToLoadTypedArray[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()



                val sportsItem = SportsItem(itemFields[0], itemFields[1], itemFields[2], itemFields[3],
                        itemFields[4])
                println("sportsItem.name = ${sportsItem.name}")
                println("sportsItem.link = ${sportsItem.link}")
                println("sportsItem.seasons = ${sportsItem.seasons}")
                println("sportsItem.type = ${sportsItem.type}")
                println("sportsItem.position = ${sportsItem.position}")

                    if (links.contains(sportsItem.link)){

                      println("already in db = ${sportsItem.name}")

                    } else {

                        databaseReferenceSport?.push()?.setValue(sportsItem)
                    }

            //

            }
            //*****************************************************************************************************************************************
        }







        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )


    }


    override fun onBackPressed() {

        finish()

    }

    companion object {

        private const val TAG = "ADDITEM"

        fun checkURL(input: CharSequence): Boolean {
            if (TextUtils.isEmpty(input)) {
                return false
            }
            val URL_PATTERN = Patterns.WEB_URL
            var isURL = URL_PATTERN.matcher(input).matches()
            if (!isURL) {
                val urlString = input.toString() + ""
                if (URLUtil.isNetworkUrl(urlString)) {
                    try {
                        URL(urlString)
                        isURL = true
                    } catch (e: Exception) {
                    }

                }
            }
            return isURL
        }
    }


}

