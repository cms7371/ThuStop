package com.thustop_00;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.thustop_00.databinding.FragmentLocationSearchBinding;

import static com.thustop_00.Constant.apiKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationSearchFragment extends FragmentBase implements LocationAutocompleteAdapter.ClickListener {
    private FragmentLocationSearchBinding binding;
    private LocationAutocompleteAdapter autocompleteAdapter;
    private String start;

    public LocationSearchFragment() {
        // Required empty public constructor
    }


    public static LocationSearchFragment newInstance(String start) {
        LocationSearchFragment fragment = new LocationSearchFragment();
        Bundle args = new Bundle();
        fragment.start=start;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationSearchBinding.inflate(inflater);
        View v = binding.getRoot();
        _listener.setToolbar(true,true,false);
        _listener.setTitle("");
        _listener.showActionBar(true);
        binding.tvStart.setText(start);
        //TODO 구글 주소 자동 완성 추가
        //apiKey로 구글 클라이언트 시작
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), apiKey);
        }
        binding.etEnd.addTextChangedListener(filterTextWatcher);
        autocompleteAdapter = new LocationAutocompleteAdapter(getContext());
        autocompleteAdapter.setClickListener(this);
        binding.rvLocationPrediction.setAdapter(autocompleteAdapter);
        autocompleteAdapter.notifyDataSetChanged();



/*        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getContext());
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("AutoCompleteListener", "Place: " + place.getName() + ", " + place.getId());
                binding.etEnd.setText(place.getName());
            }


            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i("AutoCompleteListener", "An error occurred: " + status);
            }
        });*/


        return binding.getRoot();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                autocompleteAdapter.getFilter().filter(s.toString());
                if (binding.rvLocationPrediction.getVisibility() == View.GONE) {binding.rvLocationPrediction.setVisibility(View.VISIBLE);}
            } else {
                if (binding.rvLocationPrediction.getVisibility() == View.VISIBLE) {binding.rvLocationPrediction.setVisibility(View.GONE);}
            }
        }
    };

    @Override
    public void click(Place place) {
        Toast.makeText(getContext(), place.getAddress()+", "+place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
    }

    /*
    private void search() {
        String s = binding.etEnd.getText().toString();
        //입력이 되었나
        if(!s.isEmpty()){
            if(getActivity() != null){
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                if(inputMethodManager != null)
                    inputMethodManager.hideSoftInputFromWindow(binding.etEnd.getWindowToken(), 0);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://dapi.kakao.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RestApi api = retrofit.create(RestApi.class);


            }

        }

    }*/
}