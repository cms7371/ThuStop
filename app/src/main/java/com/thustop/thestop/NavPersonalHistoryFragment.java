package com.thustop.thestop;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.thustop.R;
import com.thustop.databinding.FragmentNavPersonalHistoryBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryFragment extends FragmentBase implements MainActivity.onBackPressedListener{

    FragmentNavPersonalHistoryBinding binding;

    private final int FRAGMENT_HISTORY = 1;
    private final int FRAGMENT_TICKET = 2;

    public NavPersonalHistoryFragment() {
        // Required empty public constructor
    }


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
        _listener.setToolbarStyle(_listener.WHITE_BACK_EXIT, "이용 내역");

        _listener.setOnBackPressedListener(this);

        callFragment(FRAGMENT_HISTORY);
        return binding.getRoot();
    }

    // 클릭시 탭 색과 배경 변경(좌우 방향 있어서 메소드 사용 안함)
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

    // 탭별로 하위 프래그먼트 지정
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

    @Override
    public void onBack() {
        _listener.setFragment(MainFragment.newInstance());
        // TODO : 딜레이 넣어서 자연스럽게 만들기
        _listener.openDrawer();
    }
}