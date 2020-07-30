package com.thustop_00.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private int pre_position = 0;
    private FragmentIntroBaseBinding binding;
    private FragmentStateAdapter pagerAdapter;
    private ViewPager2 viewpager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_base, container, false);

        pagerAdapter = new MyAdapter(this);
        viewpager = binding.vpIntroContainer;
        viewpager.setAdapter(pagerAdapter);

        binding.indicator.setViewPager(viewpager);
        binding.indicator.createIndicators(3, 0);

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ConstraintLayout.LayoutParams introLayoutParams = (ConstraintLayout.LayoutParams) binding.indicator.getLayoutParams();
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (position == 0) {
                    binding.ivIntroPrev.setVisibility(View.INVISIBLE);
                } else if(position == 1) {
                    binding.ivIntroPrev.setVisibility(View.VISIBLE);
                    binding.ivIntroNext.setVisibility(View.VISIBLE);
                    binding.btIntroLater.setVisibility(View.VISIBLE);
                    binding.btIntroStart.setVisibility(View.INVISIBLE);

                    if(pre_position == 1) {
                        introLayoutParams.bottomMargin -= 40;
                        binding.ivIntroPrev.setLayoutParams(introLayoutParams);
                        binding.indicator.setLayoutParams(introLayoutParams);
                        pre_position = 0;
                    }
                } else if(position == 2) {
                    binding.ivIntroNext.setVisibility(View.INVISIBLE);
                    binding.btIntroLater.setVisibility(View.INVISIBLE);
                    binding.btIntroStart.setVisibility(View.VISIBLE);

                    introLayoutParams.bottomMargin += 40;
                    binding.ivIntroPrev.setLayoutParams(introLayoutParams);
                    binding.indicator.setLayoutParams(introLayoutParams);
                    pre_position = 1;
                }
            }
        });

        return binding.getRoot();
    }

    /*This method returns new instance of this fragment*/
    public static IntroBaseFragment newInstance() {
        Bundle args = new Bundle();
        IntroBaseFragment fragment = new IntroBaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private class MyAdapter extends FragmentStateAdapter {

        public MyAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0 : return new IntroPage1Fragment();
                case 1 : return new IntroPage2Fragment();
                case 2 : return new IntroPage3Fragment();
                default : return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
