package comapps.com.sportsreference2

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * Created by me on 8/25/2015.
 */
class WebView : AppCompatActivity() {

    private val TAG = "WEBVIEW_KOTLIN"

    private var customTabsOpened = false
    private var view: View? = null


    private lateinit var itemsHistory: ArrayList<SportsItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)
        //    RelativeLayout layout = (RelativeLayout) findViewById(R.id.webLayout);

        //      prefs = this.getSharedPreferences(
        //              "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.BLACK
        }

        view = findViewById<View>(R.id.webview1)


        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)


        val width = dm.widthPixels
        val height = dm.heightPixels

        val rf = RectF(2f, 2f, 2f, 2f)

        val radii = floatArrayOf(60f, 60f, 60f, 60f, 60f, 60f, 60f, 60f)

        val rrs = android.graphics.drawable.shapes.RoundRectShape(null, rf, radii)

        val able = android.graphics.drawable.ShapeDrawable(rrs)


        val activity = this@WebView
        val orientationValue = activity.resources.configuration.orientation
        val layoutParams = window.attributes
        val mWindowParams = WindowManager.LayoutParams()
        when (orientationValue) {
            Configuration.ORIENTATION_PORTRAIT -> {
                window.setLayout((width * .95).toInt(), (height * .90).toInt())
                layoutParams.y = (height * .05).toInt()
                window.setGravity(Gravity.TOP)
                window.attributes = layoutParams
                mWindowParams.gravity = Gravity.TOP
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                window.setLayout(width, (height * .80).toInt())
                layoutParams.y = (height * .25).toInt()
                window.setGravity(Gravity.TOP)
                window.attributes = layoutParams
                mWindowParams.gravity = Gravity.TOP
            }
        }// getWindow().setAttributes(layoutParams);
        // mWindowParams.gravity = Gravity.TOP;

        println("$TAG pre GSON sIO --> " + intent.extras.getString("sportItemObject"))

        val programIntent: String = intent.extras.getString("sentFrom", "")

        var sportsItemObject: SportsItem?

        try {
            sportsItemObject = Gson().fromJson(intent.extras.getString("sportsItemObject"),
                           SportsItem::class.java)
        } catch (e: Exception) {
            sportsItemObject = null
        }

        println("$TAG programIntent --> $programIntent")
        println("$TAG post GSON sIO --> ${sportsItemObject.toString()}")





        var webAddress = when (programIntent) {

            "ACTIVITY" -> intent.extras.getString("linkSearchText")

            "FAVORITE" -> intent.extras.getString("link")

            "HISTORY" -> sportsItemObject?.link

            "ADAPTER" -> sportsItemObject?.link

            else -> "http://www.sports-reference.com"
        }

        println("$TAG webAddress --> $webAddress")


        if (programIntent == "ADAPTER") {
            sportsItemObject?.let { addToHistoryList(it) }
        }



        if ( !webAddress!!.contains("http") ) {

            if (sportsItemObject!!.sport.contains("college") || webAddress.contains("cbb")) {
                webAddress = "https://www.sports-reference.com" + webAddress
            } else if (sportsItemObject.sport == "baseball") {
                webAddress = "https://baseball-reference.com" + webAddress
            } else if (sportsItemObject.sport == "basketball") {
                webAddress = "https://basketball-reference.com" + webAddress
            } else if (sportsItemObject.sport == "hockey") {
                webAddress = "https://hockey-reference.com" + webAddress
            } else if (sportsItemObject.sport == "football") {
                webAddress = "https://pro-football-reference.com" + webAddress
            } else if (sportsItemObject.sport.contains("soccer") || sportsItemObject.link.contains
                    ("squads")) {
                webAddress = "https://fbref.com" + webAddress
            }
        }


        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(Color.BLACK)
        builder.enableUrlBarHiding()


        builder.setStartAnimations(this, R.anim.pushinfromright, R.anim.pushouttoleft)
        builder.setStartAnimations(this, R.anim.pushinfromleft, R.anim.pushouttoright)


        val customTabsIntent = builder.build()

        webAddress = webAddress.replace("`", "%27")



        Log.d("WEBVIEWKT", "url to load --> " + webAddress)




        customTabsIntent.launchUrl(this, Uri.parse(webAddress))
        customTabsOpened = true


        /*     webView = findViewById<View>(R.id.webview1) as android.webkit.WebView


             webView!!.webViewClient = WebViewClient()
             webView!!.webChromeClient = WebChromeClient()
             webView!!.background = able





             if (prefs.addFree) {
                 webView!!.settings.javaScriptEnabled = false
             } else {
                 webView!!.settings.javaScriptEnabled = true
                 webView!!.settings.domStorageEnabled = true
             }



             webView!!.settings.loadWithOverviewMode = false
             webView!!.settings.useWideViewPort = true
             webView!!.settings.setSupportZoom(true)
             webView!!.settings.builtInZoomControls = true
             webView!!.isScrollbarFadingEnabled = false
             webView!!.settings.displayZoomControls = true
             webView!!.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY


             if (savedInstanceState != null) {
                 webView!!.saveState(savedInstanceState)

             } else {

                 try {
                     webView!!.loadUrl(sportsItemObject.link)
                     addToHistoryList(sportsItemObject)
                 } catch (e: Exception) {
                     webView!!.loadUrl(sportsItemLink)
                 }

             }
     */


    }

    private fun addToHistoryList(o: SportsItem) {


        val type = object : TypeToken<java.util.ArrayList<SportsItem>>() {}.type
        itemsHistory = ArrayList()

        try {
            itemsHistory = Gson().fromJson<java.util.ArrayList<SportsItem>>(prefs.sih, type)
        } catch (e: Exception) {
        }

        println("WEBVIEWKT itemsHistory Json string --> ${prefs.sih}")
        println("WEBVIEWKT item to add to itemsHistory ----> $o")
        println("WEBVIEWKT itemsHistory.size --> ${itemsHistory.size}")

        if (prefs.sih.contains(o.link)) {
            println("WEBVIEWKT o in itemsHistory")
        } else {

            if (itemsHistory.size > 15) {
                itemsHistory.removeAt(15)
                itemsHistory.add(1, o)

            } else if (itemsHistory.size > 0) {
                itemsHistory.add(1, o)
            } else {
                itemsHistory.add(0, o)
            }

        }


        val jsonHistoryList = Gson().toJson(itemsHistory)
        prefs.sih = jsonHistoryList



        println("WEBVIEWKT itemsHistory.size --> ${itemsHistory.size}")
        println("WEBVIEWKT jsonHistoryList ---> ${prefs.sih}")

        //   mainActivityKotlin.onResume()
    }


    /*  private fun destroyWebView() {


          if (webView != null) {
              webView!!.clearHistory()
              webView!!.clearCache(true)
              webView!!.loadUrl("about:blank")
              webView!!.pauseTimers()
              webView = null

              finish()
          }

      }

      override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

          if (keyCode == KeyEvent.KEYCODE_BACK) {
              if (webView!!.canGoBack()) {
                  webView!!.goBack()
                  return true
              } else {

                  destroyWebView()
                  val intent = Intent(this, SportsItemActivityKotlin::class.java)
                  val gson = Gson()
               //   intent.putExtra("sportsItemObject", gson.toJson(listSportsItems[position]))
               //   Log.d("GSON", gson.toJson(listSportsItems[position]))
                  //    intent.putExtra("whichactivity", "baseball")
                  ContextCompat.startActivity(this, intent, null)
                  finish()
              }
          }
          return super.onKeyDown(keyCode, event)
      }

      override fun onSaveInstanceState(outState: Bundle) {
          super.onSaveInstanceState(outState)
          webView!!.saveState(outState)
      }

      override fun onRestoreInstanceState(savedState: Bundle) {
          super.onRestoreInstanceState(savedState)
          webView!!.restoreState(savedState)
      }*/

    override fun onPause() {

        finish()
        super.onPause()
    }

    override fun onBackPressed() {


    }


    companion object {

        private const val TAG = "WEBVIEW"
    }


}


