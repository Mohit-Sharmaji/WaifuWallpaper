package com.sharmaji.wallpaperproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.sharmaji.wallpaperproject.Adapters.ExpandableListAdapter;
import com.sharmaji.wallpaperproject.Adapters.ViewPagerAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int clickCount=0;
    String TYPE="sfw";
    String CATEGORY="waifu";
    int lastVPosition;
    ViewPager2 viewPager;
    FragmentManager fragmentManager;
    ViewPagerAdapter viewPagerAdapter;
    ArrayList<String> imageUrls = new ArrayList<>();
    Toolbar toolbar;
    RequestQueue queue;
    JsonObjectRequest jsonObjectRequest;
    JSONArray jsonArrayResponse = new JSONArray();
    ExpandableListAdapter listAdapter;
    ExpandableListView expandableListView;
    List<String> headerTitles;
    HashMap<String,List<String>> childTitles;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    AppBarLayout appBarLayout;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("showExtraOption",MODE_PRIVATE);
        toggleExtraOptions(false);

        drawerLayout = findViewById(R.id.drawerLayout);
        appBarLayout = findViewById(R.id.appbarLayout);
        expandableListView = findViewById(R.id.expandableListView);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this,headerTitles,childTitles);
        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long id) {
                TYPE = headerTitles.get(i);
                CATEGORY = childTitles.get(headerTitles.get(i)).get(i1);
                makeVolleyRequest("https://api.waifu.pics/many/"+TYPE+"/"+CATEGORY);
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.closeDrawer(GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        viewPager = findViewById(R.id.viewPager);
        fragmentManager = this.getSupportFragmentManager();
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(lastVPosition < position) {
                    // handle swipe LEFT
                    appBarLayout.setVisibility(View.GONE);
                } else if(lastVPosition > position){
                    // handle swipe RIGHT
                    appBarLayout.setVisibility(View.VISIBLE);
                }
                lastVPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }
        });
        makeVolleyRequest("https://api.waifu.pics/many/"+TYPE+"/"+CATEGORY);
        navigationView = findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);

        TextView subtitle = header.findViewById(R.id.header_subtitle);
        subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount++;
                if(clickCount==7) {
                    toggleExtraOptions(true);
                    prepareListData();
                    listAdapter = new ExpandableListAdapter(MainActivity.this, headerTitles, childTitles);
                    expandableListView.setAdapter(listAdapter);
                    subtitle.setText("18+ Category: Enabled");
                    subtitle.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.red));
                    header.findViewById(R.id.header_hint).setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void makeVolleyRequest(String url) {
        Log.d("urlVolley",url);
        queue = Volley.newRequestQueue(this);
        JSONObject object = new JSONObject();
        try {
            object.put("exclude",new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("makeVolleyRequest","executed");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("onResponseMainActivity","executed");
                try {
                    for (int i = 0; i<jsonArrayResponse.length();i++){
                        jsonArrayResponse.remove(i);
                    }
                    jsonArrayResponse = response.getJSONArray("files");
                    imageUrls.clear();
                    for(int i = 0; i<jsonArrayResponse.length();i++){
                        imageUrls.add(jsonArrayResponse.get(i).toString());
                    }
                    Log.d("imageURLMainActivity",imageUrls.toString());
                    viewPagerAdapter = new ViewPagerAdapter(fragmentManager,getLifecycle(),imageUrls);
                    viewPager.setAdapter(viewPagerAdapter);
                } catch (JSONException e) {
                    Log.d("makeVolleyError",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse",error.getLocalizedMessage());
                NetworkResponse response = error.networkResponse;
                if(response!=null){
                    String res = null;
                    try {
                        res = new String(response.data, HttpHeaderParser.parseCharset(response.headers,"utf-8"));
                        JSONObject object = new JSONObject(res);
                        Log.d("VolleyError",object.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("onErrorResponseException","executed");

                    }
                }
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void prepareListData() {
        headerTitles = new ArrayList<>();
        childTitles = new HashMap<>();

        //Adding Header Data
        headerTitles.add("sfw");
        List<String> SFW = new ArrayList<>();
        SFW.add("waifu");
        SFW.add("cringe");
        SFW.add("dance");
        SFW.add("poke");
        SFW.add("wink");
        SFW.add("happy");
        SFW.add("kick");
        SFW.add("kill");
        SFW.add("slap");
        SFW.add("glomp");
        SFW.add("bite");
        SFW.add("nom");
        SFW.add("handhold");
        SFW.add("highfive");
        SFW.add("wave");
        SFW.add("smile");
        SFW.add("blush");
        SFW.add("yeet");
        SFW.add("bonk");
        SFW.add("smug");
        SFW.add("pat");
        SFW.add("lick");
        SFW.add("awoo");
        SFW.add("kiss");
        SFW.add("hug");
        SFW.add("cry");
        SFW.add("cuddle");
        SFW.add("bully");
        SFW.add("megumin");
        SFW.add("shinobu");
        SFW.add("neko");
        childTitles.put(headerTitles.get(0),SFW);

        if(isExtraOptionsVisible()){
            headerTitles.add("nsfw");
            List<String> NSFW = new ArrayList<>();
            NSFW.add("waifu");
            NSFW.add("neko");
            NSFW.add("trap");
            NSFW.add("blowjob");
            childTitles.put(headerTitles.get(1),NSFW);
        }

    }
    private void toggleExtraOptions(Boolean b){
        sharedPreferenceEditor = sharedPreferences.edit();
        sharedPreferenceEditor.putBoolean("isVisible",b);
        sharedPreferenceEditor.commit();
    }
    private Boolean isExtraOptionsVisible(){
        return sharedPreferences.getBoolean("isVisible",false);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START    );
        }else{
            super.onBackPressed();
        }
    }
}