package comapps.com.sportsreference2

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.itemlayouthistory.view.*

/**
 * Created by me on 2/26/2018.
 */
class SportsItemAdapter(private val listSportsItems: ArrayList<SportsItem>):
        RecyclerView.Adapter<SportsItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemlayoutrecycler, parent, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(listSportsItems[position])
        
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

                itemView.textViewName.text = sportsItem.name
                when (sportsItem.type) {
                    "team", "school" , "null" -> itemView.textViewType.text = null
                    "player" -> itemView.textViewType.text = sportsItem.position
                    "manager", "coach", "GM" -> itemView.textViewType.text = sportsItem.type
                    else -> { // Note the block
                        itemView.textViewType.text = ""
                    }
                }

                if (itemView.textViewType.text == null) itemView.textViewType.text = ""

                itemView.textViewSeasons.text = sportsItem.seasons
                when (sportsItem.seasons) {
                    "" ->  itemView.textViewSeasons.visibility = View.GONE
                    " " -> itemView.textViewSeasons.visibility = View.GONE
                    "seasons" -> itemView.textViewSeasons.visibility = View.GONE
                    else -> itemView.textViewSeasons.visibility = View.VISIBLE
                }




            }
        }


    }


}


