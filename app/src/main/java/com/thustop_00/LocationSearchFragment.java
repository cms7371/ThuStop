package com.thustop_00;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.thustop_00.databinding.FragmentLocationSearchBinding;

import java.util.Arrays;
import java.util.List;

import static com.thustop_00.Constant.apiKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationSearchFragment extends FragmentBase implements LocationAutocompleteAdapter.OnListItemSelectedInterface {
    private FragmentLocationSearchBinding binding;
    private LocationAutocompleteAdapter autocompleteAdapter;
    private PlacesClient placesClient;
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
        binding.etStart.setText(start);
        //apiKey로 구글 클라이언트 시작
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), apiKey);
        }
        placesClient = Places.createClient(getContext());
        binding.etStart.addTextChangedListener(filterTextWatcher);
        binding.etEnd.addTextChangedListener(filterTextWatcher);
        autocompleteAdapter = new LocationAutocompleteAdapter(getContext(), this);
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


        binding.etStart.setText(start);
        filled(binding.etStart, R.drawable.outline_green,R.drawable.round_gray_e7);
        filled(binding.etEnd, R.drawable.outline_red,R.drawable.round_gray_e7);
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
    public void onItemSelected(View v, int position) {
        String placeId = String.valueOf(autocompleteAdapter.mResultList.get(position).placeId);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                binding.etEnd.setText(response.getPlace().getName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ApiException) {
                    Toast.makeText(getContext(), exception.getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void filled(TextView textaddress, int drawblank, int drawfill) {
        String s = textaddress.getText().toString();
        Log.d("!!!",s);
        Log.d("!!!",s);
        if(!textaddress.isFocused()) {
            if (s.length() != 0) {
                textaddress.setBackgroundResource(drawfill);

            }
        } else {
            textaddress.setBackgroundResource(drawblank);
        }
    }
}