package com.example.pixelwallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FloatingActionButton logO;
    Vibrator vibrator;
    EditText editText;

    RecyclerView recyclerView;
    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModelList;
    int pageNumber = 1;

    Boolean isScrolling = false;
    int currentItems,totalItems,scrollOutItems;
    String url = "https://api.pexels.com/v1/curated/?page="+pageNumber+"&per_page=80";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.search_text);

        logO = findViewById(R.id.logout01);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        logO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hold to Logout from Trippy. ", Toast.LENGTH_SHORT).show();
            }
        });

        logO.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.vibrate(300);
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),loginActivity.class));
                finish();
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this,wallpaperModelList);

        recyclerView.setAdapter(wallpaperAdapter);
        final StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();

                int[] firstVisibleItems = null;
                firstVisibleItems = gridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                    scrollOutItems = firstVisibleItems[0];
                }

                if(isScrolling && ((currentItems+scrollOutItems) >=totalItems)){
                    isScrolling = false;
                    fetchWallpaper();
                }


            }
        });

        fetchWallpaper();

    }


    public void fetchWallpaper(){

        StringRequest request = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("photos");

                    int length = jsonArray.length();

                    for(int i=0;i<length;i++){

                        JSONObject object = jsonArray.getJSONObject(i);

                        int id = object.getInt("id");

                        JSONObject objectImages = object.getJSONObject("src");

                        String orignalUrl = objectImages.getString("original");
                        String mediumUrl = objectImages.getString("medium");

                        WallpaperModel wallpaperModel = new WallpaperModel(id,orignalUrl,mediumUrl);
                        wallpaperModelList.add(wallpaperModel);



                    }

                    wallpaperAdapter.notifyDataSetChanged();
                    pageNumber++;

                }catch (JSONException e){
                    Toast.makeText(MainActivity.this, "ERROR007", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","563492ad6f91700001000001eaf3b08c778b4230ab9e564818b6cef3");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

    public void SearchByCat(View view) {
        String query = editText.getText().toString().toLowerCase();

        if(query.isEmpty()){
            Toast.makeText(this, "No category searched", Toast.LENGTH_SHORT).show();
        }

        url = "https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query="+query;
        wallpaperModelList.clear();
        fetchWallpaper();
    }

    public void myProfile(View view) {

        startActivity(new Intent(this,myProfile.class));
    }

    public void Settings(View view) {
        startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
    }

    public void logOut(View view) {
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(),loginActivity.class));
        finish();
    }

}