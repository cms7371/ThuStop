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
        // TODO: 네비게이션에서 넘어올때 창이 안닫혀서 강제로 닫고 프래그먼트 전환해놓음, 그랬더니 백버튼 눌러도 다시 네비게이션 안열려서 x버튼이랑 차이가 없음
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
}