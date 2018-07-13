package comapps.com.sportsreference2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_world.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WorldFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WorldFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorldFragment : Fragment() {



    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val mParam1 = arguments!!.getString(ARG_PARAM1)
            val mParam2 = arguments!!.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_world, container, false)

        val button1World = v.findViewById<View>(R.id.button1World)
        val button2World = v.findViewById<View>(R.id.button2World)
        val button3World = v.findViewById<View>(R.id.button3World)
        val button4World = v.findViewById<View>(R.id.button4World)


        button1World.setOnClickListener {
            val intentplayersearch = Intent()
            intentplayersearch.setClass(activity!!.applicationContext, SportsItemActivityKotlin::class.java)
            intentplayersearch.putExtra("sport", "soccer")
            startActivity(intentplayersearch)
            //        getActivity().finish();
            try {
                activity!!.overridePendingTransition(R.anim.pushinfromright, R.anim.pushouttoleft)
                //    activity!!.finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        button2World.setOnClickListener {
            val intentclubsearch = Intent()
            intentclubsearch.setClass(activity!!.applicationContext, SportsItemActivityKotlin::class
                    .java)
            intentclubsearch.putExtra("sport", "soccer_clubs")
            startActivity(intentclubsearch)
            //        getActivity().finish();
            try {
                activity!!.overridePendingTransition(R.anim.pushinfromright, R.anim.pushouttoleft)
                //    activity!!.finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        button3World.setOnClickListener {
            val intentclubsearch = Intent()
            intentclubsearch.setClass(activity!!.applicationContext, SportsItemActivityKotlin::class
                    .java)
            intentclubsearch.putExtra("sport", "soccer_leagues")
            startActivity(intentclubsearch)
            //        getActivity().finish();
            try {
                activity!!.overridePendingTransition(R.anim.pushinfromright, R.anim.pushouttoleft)
                //    activity!!.finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        button4World.setOnClickListener {
            val intentclubsearch = Intent()
            intentclubsearch.setClass(activity!!.applicationContext, SportsItemActivityKotlin::class
                    .java)
            intentclubsearch.putExtra("sport", "soccer_wc")
            startActivity(intentclubsearch)
            //        getActivity().finish();
            try {
                activity!!.overridePendingTransition(R.anim.pushinfromright, R.anim.pushouttoleft)
                //    activity!!.finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }





        return v


    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        private const val TAG = "NORTHAMERICA"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NorthAmericaFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): WorldFragment {
            val fragment = WorldFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args



            return fragment
        }

        private val string: String
            get() {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDateandTime = sdf.format(Date())
                Log.e(TAG, "Time ----> $currentDateandTime")
                return currentDateandTime
            }
    }
}// Required empty public constructor
