package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pixelwallpaper.R;
import com.example.pixelwallpaper.permissionActivity;


public class locationDisabledFragment extends Fragment {

    private Button btnToMaps;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_disabled, container, false);

        btnToMaps = view.findViewById(R.id.buttonToMapsoo);

        btnToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),permissionActivity.class));
            }
        });

        return view;
    }

}