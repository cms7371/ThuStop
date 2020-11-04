package com.thustop_00;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentDoneBinding;
import com.thustop_00.databinding.FragmentNavPersonalHistoryBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryFragment extends FragmentBase {

    FragmentNavPersonalHistoryBinding binding;

    private final int FRAGMENT_HISTORY = 1;
    private final int FRAGMENT_TICKET = 2;

    public NavPersonalHistoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NavPersonalHistoryFragment newInstance() {
        NavPersonalHistoryFragment fragment = new NavPersonalHistoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavPersonalHistoryBinding.inflate(inflater);
        binding.setNavPersonalHistoryFrag(this);
        _listener.showToolbarVisibility(true);
        _listener.setToolbarStyle(true,true);
        _listener.setTitle(false, "이용 내역");
        callFragment(FRAGMENT_HISTORY);

        return binding.getRoot();
    }

    public void onHistoryClick(View view) {
        binding.btHistory.setBackgroundResource(R.drawable.bg_tap_right);
        binding.btHistory.setTextColor(getResources().getColor(R.color.Primary));
        binding.btTicket.setBackgroundColor(Color.parseColor("#00000000"));
        binding.btTicket.setTextColor(getResources().getColor(R.color.TextBlack));
        callFragment(FRAGMENT_HISTORY);
    }

    public void onTicketClick(View view) {
        binding.btHistory.setBackgroundColor(Color.parseColor("#00000000"));
        binding.btHistory.setTextColor(getResources().getColor(R.color.TextBlack));
        binding.btTicket.setBackgroundResource(R.drawable.bg_tap_left);
        binding.btTicket.setTextColor(getResources().getColor(R.color.Primary));
        callFragment(FRAGMENT_TICKET);
    }

    private void callFragment(int fragmentName) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragmentName){
            case 1:
                NavPersonalHistoryHistoryFragment fragmentHistory = new NavPersonalHistoryHistoryFragment();
                fragmentTransaction.replace(R.id.frame1, fragmentHistory);
                fragmentTransaction.commit();
                break;

            case 2:
                NavPersonalHistoryTicketFragment fragmentTicket = new NavPersonalHistoryTicketFragment();
                fragmentTransaction.replace(R.id.frame1, fragmentTicket);
                fragmentTransaction.commit();
                break;

        }
    }
}