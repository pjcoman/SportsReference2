package comapps.com.sportsreference2

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentReference
import com.google.gson.Gson
import kotlinx.android.synthetic.main.itemlayoutrecycler_constraint.view.*
import java.util.*

/**
 * Created by me on 2/26/2018.
 */
class SportsItemAdapter(private val listSportsItems: MutableList<SportsItem>, applicationContext: Context, docRef: DocumentReference) :
        RecyclerView.Adapter<SportsItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout
                .itemlayoutrecycler_constraint,
                parent, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(listSportsItems.get(position))

        holder.itemView.setOnClickListener {


            val intent = Intent(it.context, WebView::class.java)
            val gson = Gson()
            intent.putExtra("sportsItemObject", gson.toJson(listSportsItems[position]))
            intent.putExtra("sentFrom", "ADAPTER")
            Log.d("GSON", gson.toJson(listSportsItems[position]))
            //    intent.putExtra("whichactivity", "baseball")
            startActivity(it.context, intent, null)
        }
    }


    override fun getItemCount() = listSportsItems.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bindForecast(sportsItem: SportsItem) {
            with(sportsItem) {

                itemView.textViewName.text = deAccent(sportsItem.name)

                /* if ( sportsItem.sport.contains("college")) {
                     itemView.textViewSchoolOrTeam.visibility = View.VISIBLE
                 }
 */
                if (sportsItem.sport == "basketball" || sportsItem.sport == "football" ||
                        sportsItem.sport == "hockey") {

                    itemView.textViewSchoolOrTeam.visibility = View.GONE

                } else {

                    if (sportsItem.schoolOrTeam.contains("High") && sportsItem.sport.contains
                            ("college")) {
                        itemView.textViewSchoolOrTeam.text = sportsItem.schoolOrTeam
                    } else {

                        if (sportsItem.schoolOrTeam.contains("Schools:")) {
                            itemView.textViewSchoolOrTeam.text = sportsItem.schoolOrTeam
                                    .removePrefix("Schools: ")
                        } else if (sportsItem.schoolOrTeam.contains("School:")) {
                            itemView.textViewSchoolOrTeam.text = sportsItem.schoolOrTeam.removePrefix("School: ")


                        }

                    }


                }

                position = (sportsItem.position).replace("Positions:", "").trim()


                position = (sportsItem.position).replace("Position:", "").trim()

                itemView.textViewType.visibility = View.VISIBLE


                when (sportsItem.type) {
                    "team", "school", "null" -> itemView.textViewType.text = null
                    "player" -> itemView.textViewType.text = position
                    "" -> itemView.textViewType.text = position
                    "manager", "coach", "GM" -> {
                        itemView.textViewType.text = sportsItem.type
                        itemView.textViewSeasons.text = ""
                    }
                    else -> { // Note the block
                        itemView.textViewType.text = null
                        itemView.textViewType.visibility = View.GONE
                    }
                }

                /* if (itemView.textViewType.text == null) itemView.textViewType.text = ""


 */              if (sportsItem.sport == "baseball" && sportsItem.type == "player") {

                val year: Int

                year = Calendar.getInstance().get(Calendar.YEAR)

                var careerSpan = ""


                if (sportsItem.lastSeason == year.toString() && sportsItem.firstSeason != "") {

                    careerSpan = sportsItem.firstSeason + "-"

                } else {

                    careerSpan = sportsItem.firstSeason + "-" + sportsItem.lastSeason

                }


                println("$TAG sportsItem careerSpan $careerSpan")

                try {
                    if ( careerSpan.startsWith("-")) {
                        careerSpan = careerSpan.substring(1, careerSpan.length)
                    }
                } catch (e: Exception) {

                    careerSpan = ""

                }

                if ( careerSpan.length < 4 ) {
                    itemView.textViewSeasons.visibility = View.GONE
                } else {
                    itemView.textViewSeasons.visibility = View.VISIBLE

                }



                itemView.textViewSeasons.text = careerSpan



            }


            }
        }


    }


}


