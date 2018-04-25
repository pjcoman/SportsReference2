package comapps.com.sportsreference2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Created by me on 3/21/2017.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAINACTIVITY";

    private ViewPager viewPager;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private ArrayList<SportsItem> itemsHistory = new ArrayList<>();


    private MySpinnerAdapter spinnerAdapter;

    private CheckBox checkBoxFirstName;
    private CheckBox checkBox1stLastName;
    private CheckBox checkBoxAddFree;

    private Spinner spinnerMlb;
    private Spinner spinnerNfl;
    private Spinner spinnerNhl;
    private Spinner spinnerNba;


    private PopupWindow fadePopup;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.tabbedlayoutmain);
        viewPager = findViewById(R.id.simpleViewPager);
        TabLayout tabLayout = findViewById(R.id.simpleTabLayout);
        Spinner spinner = findViewById(R.id.spinnerHistory);

        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText(null); // set the Text for the first Tab
        firstTab.setIcon(R.drawable.na_icon); // set an icon for the
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText(null); // set the Text for the second Tab
        secondTab.setIcon(R.drawable.world_icon); // set an icon for the
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout


        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            ImageView imageView = new ImageView(getApplicationContext());
            if (i == 0) {
                imageView.setImageResource(R.drawable.na_icon);

            } else {
                imageView.setImageResource(R.drawable.world_icon);
            }


            //noinspection ConstantConditions
            tabLayout.getTabAt(i).setCustomView(imageView);
        }

     /*   for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.tabiconlayout);
        }*/

        prefs = this.getSharedPreferences(
                "comapps.com.thenewsportsreference.app", Context.MODE_PRIVATE);


        Type type = new TypeToken<ArrayList<SportsItem>>() {}.getType();
        itemsHistory = new Gson().fromJson(prefs.getString("SPORTSITEMS_HISTORY", ""), type);
        Log.e(TAG, "itemsHistory ----> " + itemsHistory);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        Log.e(TAG, "Time ----> " + currentDateandTime);




        prefs.edit().putString("VISITED_DATE", currentDateandTime).apply();



        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {

                Log.e(TAG, "Tab selected ----> " + tab.getPosition());

                //THIS!!
                if (viewPager != null) {
                    viewPager.setCurrentItem(tab.getPosition());

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        spinnerAdapter = new MySpinnerAdapter(getApplicationContext(), itemsHistory);


        if (itemsHistory == null || itemsHistory.size() == 0) {
            spinner.setVisibility(View.GONE);

        } else {




            spinner.setAdapter(spinnerAdapter);
            spinner.setSelected(false);
            spinner.setSelection(0, true);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getApplication().getApplicationContext(), WebView.class);


                    Log.e(TAG, "spinner position ----> " + position);
                    Log.e(TAG, "itemsHistory position ----> " + itemsHistory.get(position));
                    Log.e(TAG, "itemsHistory size ----> " + itemsHistory.size());

                    dimBackground();

                    intent.putExtra("link", itemsHistory.get(position).getLink());
                    startActivity(intent);


                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }

            });


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (prefs.getBoolean("ME", false)) {
            getMenuInflater().inflate(R.menu.menu_main_noadd, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_main_noadd, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.myteams) {


            AlertDialog.Builder popDialogBuilder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.customdialogfavorites, null);
            //    popDialogBuilder.setIcon(R.mipmap.ic_launcher);
            //    popDialogBuilder.setTitle("Characters for Filter");

            popDialogBuilder.setView(mView);
            AlertDialog alertDialog = popDialogBuilder.create();
            //noinspection ConstantConditions
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            spinnerMlb = mView.findViewById(R.id.spinnerMLB);
            spinnerNfl = mView.findViewById(R.id.spinnerNFL);
            spinnerNhl = mView.findViewById(R.id.spinnerNHL);
            spinnerNba = mView.findViewById(R.id.spinnerNBA);


            spinnerMlb.setSelection(prefs.getInt("MLBSPINNERINDEX", 0));
            spinnerNfl.setSelection(prefs.getInt("NFLSPINNERINDEX", 0));
            spinnerNhl.setSelection(prefs.getInt("NHLSPINNERINDEX", 0));
            spinnerNba.setSelection(prefs.getInt("NBASPINNERINDEX", 0));


            spinnerMlb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i != 0) {

                        Toast toast = Toast.makeText(MainActivity.this,
                                spinnerMlb.getSelectedItem().toString(),
                                Toast.LENGTH_SHORT);

                        toast.setGravity(Gravity.BOTTOM, 0, -400);
                        centerText(toast.getView());
                        toast.show();

                        prefs.edit().putString("MLBFAV", spinnerMlb.getSelectedItem().toString()).apply();
                        prefs.edit().putInt("MLBSPINNERINDEX", i).apply();


                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerNfl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i != 0) {

                        Toast toast = Toast.makeText(MainActivity.this,
                                spinnerNfl.getSelectedItem().toString(),
                                Toast.LENGTH_SHORT);


                        toast.setGravity(Gravity.BOTTOM, 0, -400);
                        centerText(toast.getView());
                        toast.show();

                        prefs.edit().putString("NFLFAV", spinnerNfl.getSelectedItem().toString()).apply();
                        prefs.edit().putInt("NFLSPINNERINDEX", i).apply();

                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerNhl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i != 0) {

                        Toast toast = Toast.makeText(MainActivity.this,
                                spinnerNhl.getSelectedItem().toString(),
                                Toast.LENGTH_SHORT);

                        toast.setGravity(Gravity.BOTTOM, 0, -400);
                        centerText(toast.getView());
                        toast.show();

                        prefs.edit().putString("NHLFAV", spinnerNhl.getSelectedItem().toString()).apply();
                        prefs.edit().putInt("NHLSPINNERINDEX", i).apply();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerNba.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i != 0) {

                        Toast toast = Toast.makeText(MainActivity.this,
                                spinnerNba.getSelectedItem().toString(),
                                Toast.LENGTH_SHORT);


                        toast.setGravity(Gravity.BOTTOM, 0, -400);
                        centerText(toast.getView());
                        toast.show();

                        prefs.edit().putString("NBAFAV", spinnerNba.getSelectedItem().toString()).apply();
                        prefs.edit().putInt("NBASPINNERINDEX", i).apply();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        }

        if (id == R.id.settings) {


            AlertDialog.Builder popDialogBuilder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.customdialogsettings, null);
            //    popDialogBuilder.setIcon(R.mipmap.ic_launcher);
            //    popDialogBuilder.setTitle("Characters for Filter");

            Button clearHistory = mView.findViewById(R.id.buttonClearHistory);
            clearHistory.setVisibility(View.GONE);

            if (itemsHistory != null) {


                clearHistory.setVisibility(View.VISIBLE);
                clearHistory.setText(R.string.ch);

            }


            clearHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemsHistory.clear();
                    spinnerAdapter.notifyDataSetChanged();
                    editor = prefs.edit();
                    editor.putString("SPORTSITEMS_HISTORY", "");
                    editor.apply();

                }
            });


            final Spinner spinnerFilter = mView.findViewById(R.id.spinnerFilterChar);

            spinnerFilter.setSelection(prefs.getInt("FILTER_INT", 2) - 1  );

            spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i >= 0) {

                        prefs.edit().putInt("FILTER_INT", i + 1).apply();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            checkBoxAddFree = mView.findViewById(R.id.checkBoxAds);
            checkBoxAddFree.setChecked(prefs.getBoolean("ADD_FREE", false));



            // Button OK
            popDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialog, int which) {

                    String toastString = null;

                    editor = prefs.edit();

                    if (checkBoxAddFree.isChecked()) {
                        editor.putBoolean("ADD_FREE", true);
                        editor.apply();
                    } else {
                        editor.putBoolean("ADD_FREE", false);
                        editor.commit();
                    }



                    if (checkBoxAddFree.isChecked()) {
                        toastString = spinnerFilter.getSelectedItem().toString() + " characters for filter" +
                                "\n(no Ads)";
                    } else {
                        toastString = spinnerFilter.getSelectedItem().toString() + " characters for filter";
                    }

                    Toast toast = Toast.makeText(MainActivity.this,
                            toastString,
                            Toast.LENGTH_SHORT);

                    toast.setGravity(Gravity.BOTTOM, 0, -800);
                    centerText(toast.getView());
                    toast.show();


                    dialog.dismiss();
                }


            });

            popDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            popDialogBuilder.setView(mView);
            AlertDialog alertDialog = popDialogBuilder.create();
            //noinspection ConstantConditions
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();


            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    protected void onResume() {
        super.onResume();

        try {
            spinnerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fadePopup != null) {
            fadePopup.dismiss();
        }


        //...Now update your objects with preference values
    }

    private void centerText(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setGravity(Gravity.CENTER);
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int n = group.getChildCount();
            for (int i = 0; i < n; i++) {
                centerText(group.getChildAt(i));
            }
        }
    }

    private void dimBackground() {

        LayoutInflater inflater = (LayoutInflater) MainActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater != null ? inflater.inflate(R.layout.fadepopup,
                (ViewGroup) findViewById(R.id.fadePopup)) : null;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        fadePopup = new PopupWindow(layout, dm.widthPixels, dm.heightPixels, false);
        fadePopup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 0);
    }


    class MySpinnerAdapter extends BaseAdapter {

        final Context context;
        final ArrayList<SportsItem> itemsHistory;

        MySpinnerAdapter(Context context, ArrayList<SportsItem> itemsHistory) {
            this.context = context;
            this.itemsHistory = itemsHistory;
        }

        @Override
        public int getCount() {
            return (itemsHistory == null) ? 0 : itemsHistory.size();
        }

        @Override
        public Object getItem(int i) {
            return itemsHistory.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                view = inflater.inflate(R.layout.itemlayouthistory, viewGroup, false);
            }
            SportsItem sportsItem = itemsHistory.get(i);

            TextView txtName = view.findViewById(R.id.textViewName);
            TextView txtSeasons = view.findViewById(R.id.textViewSeasons);
            TextView txtType = view.findViewById(R.id.textViewType);
       //     ImageView imgView = view.findViewById(R.id.imageViewIcon);


            txtName.setText(sportsItem.getName());
            if (sportsItem.getType().equals("player") || sportsItem.getType().equals("team")
                    || sportsItem.getType().equals("school") || sportsItem.getType().equals("")) {
                txtType.setVisibility(View.GONE);
            } else {
                txtType.setVisibility(View.VISIBLE);
                txtType.setText(sportsItem.getType());
            }

            txtSeasons.setText(sportsItem.getSeasons());


            try {
                if (Objects.equals(sportsItem.getSeasons(), "")) {
                    txtSeasons.setVisibility(View.GONE);
                } else {
                    txtSeasons.setVisibility(View.VISIBLE);
                    txtSeasons.setText(sportsItem.getSeasons());
                    txtSeasons.setTextSize(12);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


       /*     if (sportsItem.getLink().contains("football")) {
                imgView.setImageResource(R.drawable.football_icon_new2);
            } else if (sportsItem.getLink().contains("baseball")) {
                imgView.setImageResource(R.drawable.baseball_icon_new2);
            } else if (sportsItem.getLink().contains("basketball")) {
                imgView.setImageResource(R.drawable.basketball_icon_new2);
            } else if (sportsItem.getLink().contains("hockey")) {
                imgView.setImageResource(R.drawable.hockey_icon_new2);
            } else if (sportsItem.getLink().contains("cfb")) {
                imgView.setImageResource(R.drawable.collegefootball_icon_new2);
            } else {
                imgView.setImageResource(R.drawable.collegebasketball_icon_new2);
            }
*/

            return view;
        }

    }




}
