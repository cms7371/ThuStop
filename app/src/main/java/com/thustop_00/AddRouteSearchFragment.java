package com.thustop_00;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.thustop_00.databinding.FragmentAddRouteSearchBinding;
import com.thustop_00.model.Address;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.thustop_00.Constant.apiKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRouteSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRouteSearchFragment extends FragmentBase implements LocationAutocompleteAdapter.OnListItemSelectedInterface, MainActivity.onBackPressedListener {
    private FragmentAddRouteSearchBinding binding;
    private LocationAutocompleteAdapter autocompleteAdapter;
    private PlacesClient placesClient;
    private static final String TAG = "LocationSearchFragment";
    private Address startLocation;
    private Address endLocation;

    public AddRouteSearchFragment() {
        // Required empty public constructor
    }


    public static AddRouteSearchFragment newInstance(Address startPosition, Address endPosition) {
        AddRouteSearchFragment fragment = new AddRouteSearchFragment();
        Bundle args = new Bundle();
        fragment.startLocation = startPosition;
        fragment.endLocation = endPosition;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRouteSearchBinding.inflate(inflater);

        _listener.setToolbar(true, true, false);
        _listener.setTitle("");
        _listener.showActionBar(true);
        _listener.setOnBackPressedListener(this);

        //apiKey로 구글 클라이언트 시작
        if (!Places.isInitialized()) {
            Places.initialize(Objects.requireNonNull(getContext()), apiKey);
        }
        placesClient = Places.createClient(Objects.requireNonNull(getContext()));
        autocompleteAdapter = new LocationAutocompleteAdapter(getContext(), this);
        binding.rvLocationPrediction.setAdapter(autocompleteAdapter);
        autocompleteAdapter.notifyDataSetChanged();

        binding.etStart.setText(startLocation.getAddress());
        binding.etEnd.setText(endLocation.getAddress());
        binding.etStart.addTextChangedListener(filterTextWatcher);
        binding.etEnd.addTextChangedListener(filterTextWatcher);
        setInitialColor(binding.etStart, R.drawable.outline_green);
        setInitialColor(binding.etEnd, R.drawable.outline_red);

        return binding.getRoot();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        @Override
        public void afterTextChanged(Editable s) {
            if (binding.etStart.isFocused()) {
                binding.etStart.setBackgroundResource(R.drawable.outline_green);
            } else {
                binding.etEnd.setBackgroundResource(R.drawable.outline_red);
            }
            //텍스트가 입력되어 공백이 아니면
            if (!s.toString().equals("")) {
                //자동 완성을 진행하고 RecyclerView 보이도록 만듭니다.
                autocompleteAdapter.getFilter().filter(s.toString());
                if (binding.rvLocationPrediction.getVisibility() == View.GONE) {
                    binding.rvLocationPrediction.setVisibility(View.VISIBLE);
                }
            //공백이라면 안보이게 만듭니다.
            } else {
                if (binding.rvLocationPrediction.getVisibility() == View.VISIBLE) {
                    binding.rvLocationPrediction.setVisibility(View.GONE);
                }
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
                if (binding.etStart.isFocused()) {
                    transferAddress(response.getPlace(), startLocation);
                    binding.etStart.setText(startLocation.getAddress());
                    binding.etStart.setBackgroundResource(R.drawable.round_gray_e7);
                    binding.etDummy.requestFocus();
                    _listener.hideKeyBoard();
                } else if (binding.etEnd.isFocused()) {
                    transferAddress(response.getPlace(), endLocation);
                    binding.etEnd.setText(endLocation.getAddress());
                    binding.etEnd.setBackgroundResource(R.drawable.round_gray_e7);
                    binding.etDummy.requestFocus();
                    _listener.hideKeyBoard();
                }
                binding.rvLocationPrediction.setVisibility(View.GONE);


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
    //Places API의 Place 클래스의 값을 Address 클래스로 옮겨주는 method
    private void transferAddress(Place place, Address position) {
        position.setAddress(place.getAddress().replace("대한민국 ", ""));
        position.setLatitude(place.getLatLng().latitude);
        position.setLongitude(place.getLatLng().longitude);
    }

    private void setInitialColor(TextView textView, int onBlank) {
        String s = textView.getText().toString();
        if (s.length() != 0) {
            textView.setBackgroundResource(R.drawable.round_gray_e7);
        } else {
            textView.setBackgroundResource(onBlank);
        }
    }

    @Override
    public void onBack() {
        _listener.addFragmentNotBackStack(AddRouteMapFragment.newInstance(startLocation, endLocation, true));
    }
}