package com.thustop_00.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.thustop_00.R;
import com.thustop_00.databinding.FragmentIntroBaseBinding;

public class IntroBaseFragment extends Fragment {
    /* Bind fragment_intro_base as variable*/
    private FragmentIntroBaseBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_base, container, false);
        binding.setIntro(this); //
        /* Adapter for viewpager which wraps intro fragments */
        FragmentStateAdapter pagerAdapter = new MyAdapter(this);
        /* Link viewpager in fragment_intro_base */
        ViewPager2 viewpager = binding.vpIntroContainer;
        /* Assign the adapter to the viewpager*/
        viewpager.setAdapter(pagerAdapter);
        /* Link indicator and viewpager and create indicating dots*/
        binding.indicator.setViewPager(viewpager);
        binding.indicator.createIndicators(3, 0);

        /* Set viewpager listener for page changing action(Set visibility of buttons) */
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                /* Change visibility of buttons depend on page num. Initially, only prev button and start button are invisible*/
                if (position == 0) {
                    binding.ivIntroPrev.setVisibility(View.INVISIBLE);
                } else if(position == 1) {
                    binding.ivIntroPrev.setVisibility(View.VISIBLE);
                    binding.ivIntroNext.setVisibility(View.VISIBLE);
                    binding.btIntroLater.setVisibility(View.VISIBLE);
                    binding.btIntroStart.setVisibility(View.INVISIBLE);
                } else if(position == 2) {
                    binding.ivIntroNext.setVisibility(View.INVISIBLE);
                    binding.btIntroLater.setVisibility(View.INVISIBLE);
                    binding.btIntroStart.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();
    }
    /*Method for button listener test */
    public void onStartClick(View view) {
        binding.btIntroStart.setText("눌렸음");
    }

    /*This method returns new instance of this fragment*/
    public static IntroBaseFragment newInstance() {
        Bundle args = new Bundle();
        IntroBaseFragment fragment = new IntroBaseFragment();
        fragment.setArguments(args);
        return fragment;
    }
    /* New class for custom adaptor. Nothing special, it links 3 intro fragment*/
    private static class MyAdapter extends FragmentStateAdapter {

        public MyAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0 : return new IntroPage1Fragment();
                case 1 : return new IntroPage2Fragment();
                default : return new IntroPage3Fragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
