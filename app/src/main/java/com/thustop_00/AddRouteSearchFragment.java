package com.thustop_00;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.thustop_00.databinding.FragmentAddRouteSearchBinding;
import com.thustop_00.model.Address;
import com.thustop_00.widgets.NotoEditText;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.thustop_00.Constant.PLACES_API_KEY;

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
    private boolean isStartLastFocused = true;

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

        _listener.setToolbarStyle(_listener.WHITE_BACK, "");
        _listener.setOnBackPressedListener(this);

        //apiKey로 구글 클라이언트 시작
        if (!Places.isInitialized()) {
            Places.initialize(Objects.requireNonNull(getContext()), PLACES_API_KEY);
        }
        placesClient = Places.createClient(Objects.requireNonNull(getContext()));
        autocompleteAdapter = new LocationAutocompleteAdapter(getContext(), this);
        binding.rvLocationPrediction.setAdapter(autocompleteAdapter);
        autocompleteAdapter.notifyDataSetChanged();
        binding.etStart.setText(startLocation.getAddress());
        binding.etEnd.setText(endLocation.getAddress());
        binding.etStart.addTextChangedListener(filterTextWatcher);
        binding.etStart.setOnFocusChangeListener((view, bool) -> {
            if (bool) {
                Log.d(TAG, "onCreateView: focus on start");
                isStartLastFocused = true;
                ((NotoEditText)view).setCursorVisible(true);
            }
        });
        binding.etEnd.addTextChangedListener(filterTextWatcher);
        binding.etEnd.setOnFocusChangeListener((view, bool) -> {
            if (bool) {
                Log.d(TAG, "onCreateView: focus on end");
                isStartLastFocused = false;
                ((NotoEditText)view).setCursorVisible(true);
            }
        });
        //확인 누르면  키보드 닫히고 커서 감춤
        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                v.setCursorVisible(false);
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getActivity().getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        };
        binding.etStart.setOnEditorActionListener(editorActionListener);
        binding.etEnd.setOnEditorActionListener(editorActionListener);
        setInitialColor(binding.etStart, R.drawable.bg_outline25_green);
        setInitialColor(binding.etEnd, R.drawable.bg_outline25_red);
        binding.etDummy.requestFocus();

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
            if (binding.etStart.isFocused()) {
                binding.etStart.setBackgroundResource(R.drawable.bg_outline25_green);
            } else if (binding.etEnd.isFocused()){
                binding.etEnd.setBackgroundResource(R.drawable.bg_outline25_red);
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
        try {
            String placeId = String.valueOf(autocompleteAdapter.mResultList.get(position).placeId);
            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
            placesClient.fetchPlace(request).addOnSuccessListener(response -> {
                Log.d(TAG, "onItemSelected: etStart" + binding.etEnd.isFocused() + "End" + binding.etStart.isFocused());
                if (isStartLastFocused) {
                    transferAddress(response.getPlace(), startLocation);
                    binding.etStart.setText(startLocation.getAddress());
                    binding.etStart.setBackgroundResource(R.drawable.bg_round25_graye7);
                } else {
                    transferAddress(response.getPlace(), endLocation);
                    binding.etEnd.setText(endLocation.getAddress());
                    binding.etEnd.setBackgroundResource(R.drawable.bg_round25_graye7);
                }
                binding.etDummy.requestFocus();
                _listener.hideKeyBoard();
                binding.rvLocationPrediction.setVisibility(View.GONE);
            }).addOnFailureListener(exception -> {
                if (exception instanceof ApiException) {
                    Toast.makeText(getContext(), exception.getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            Toast.makeText(getContext(), "이런 에러가 발생했네요! 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
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
            textView.setBackgroundResource(R.drawable.bg_round25_graye7);
        } else {
            textView.setBackgroundResource(onBlank);
        }
    }

    @Override
    public void onBack() {
        _listener.addFragmentNotBackStack(AddRouteMapFragment.newInstance(startLocation, endLocation));
    }
}