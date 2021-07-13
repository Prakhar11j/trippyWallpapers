package com.example.pixelwallpaper;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.IntentSender;
import android.location.Location;
import android.view.MenuItem;
import android.view.View;

import com.example.pixelwallpaper.Model.User;
import com.example.pixelwallpaper.Model.UserLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import fragments.ProfileFragment;
import fragments.SavedFragment;
import fragments.SearchFragment;
import fragments.locationDisabledFragment;

public class myProfile extends AppCompatActivity implements OnMapReadyCallback {

    //   BottomNavigationView bottomNavigationView;
    ChipNavigationBar chipNavigationBar;
    Fragment selectedFragment = null;
    SupportMapFragment mapFragment;
    LatLng abc,abcO;
    Double lat,lng, latO,lngO;
    String t,s,tO,sO;

    GoogleMap mGoogleMap;
    private final float DEFAULT_ZOOM = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        chipNavigationBar = findViewById(R.id.bottomNavigation);
        chipNavigationBar.setItemSelected(R.id.nav_profile, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment()).commit();
        bottomMenu();

        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);

    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                selectedFragment = null;
                switch (i) {
                    case R.id.nav_home:
                        selectedFragment = null;
                        finish();
                        break;

                    case R.id.nav_searchFriend:
                        selectedFragment = new SearchFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                        break;


                    case R.id.nav_saved:

                        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("locations");

                        r.orderByChild("userid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()){
                                    selectedFragment = new locationDisabledFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                                }
                                else{
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mapFragment).commit();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                       // getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mapFragment).commit();
                        break;

                    case R.id.nav_profile:
                        getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
                        selectedFragment = new ProfileFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                        break;
                }

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
        assert current != null;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("locations");
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("users");

        ref.child(current.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserLocation userLocation = dataSnapshot.getValue(UserLocation.class);
                lat = userLocation.getLatitude();
                lng = userLocation.getLongitude();
                t = userLocation.getUsername();
                abc = new LatLng(lat,lng);

                MarkerOptions markerOptions =  new MarkerOptions().title("My Location").position(abc)
                        .icon(bitmapDescriptor(getApplicationContext(),R.drawable.ic_owl_logo));
                mGoogleMap.addMarker(markerOptions);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(abc,DEFAULT_ZOOM));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserLocation userLocation = snapshot.getValue(UserLocation.class);
                    if(!userLocation.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        latO = userLocation.getLatitude();
                        lngO = userLocation.getLongitude();
                        tO = userLocation.getUsername();
                        sO = userLocation.getUserid();
                        abcO = new LatLng(latO,lngO);

                        MarkerOptions markerOptions = new MarkerOptions().title(tO).position(abcO).snippet(sO)
                                .icon(bitmapDescriptor(getApplicationContext(),R.drawable.ic_owl_logo));
                        mGoogleMap.addMarker(markerOptions);

                    }
                }
            }

            @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
        });


      //  lat = 27.5747505;
      //   lng = 81.6028243;

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                getApplicationContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().putString("profileId", marker.getSnippet()).apply();
                selectedFragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();

              //  getApplicationContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().putString("profileId",marker.getSnippet()).apply();
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment()).commit();
            }
        });

    }

    private BitmapDescriptor bitmapDescriptor(Context context,int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}