package com.thustop_00.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.thustop_00.FragmentBase;
import com.thustop_00.MainFragment;
import com.thustop_00.R;
import com.thustop_00.databinding.FragmentIntroBaseBinding;

import java.util.ArrayList;
import java.util.List;

public class IntroBaseFragment extends FragmentBase {
    /* Bind fragment_intro_base as variable*/
    private FragmentIntroBaseBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_base, container, false);
        binding.setIntro(this);

        _listener.lockDrawer(true);

        /* Adapter for viewpager which wraps intro fragments and add new intro pages */
        MyAdapter pagerAdapter = new MyAdapter(getChildFragmentManager(),getLifecycle());
        pagerAdapter.add(IntroChildFragment.newInstance(getString(R.string.tv_intro_page1),R.drawable.img_intro1, 0));
        pagerAdapter.add(IntroChildFragment.newInstance(getString(R.string.tv_intro_page2),R.drawable.img_intro2, 1));
        pagerAdapter.add(IntroChildFragment.newInstance(getString(R.string.tv_intro_page3),R.drawable.img_intro3, 2));


        ViewPager2 viewpager = binding.vpIntroContainer;
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
        _listener.setFragment(MainFragment.newInstance());
    }
    public void onLaterClick(View view) { _listener.setFragment(MainFragment.newInstance()); }

    /*This method returns new instance of this fragment*/
    public static IntroBaseFragment newInstance() {
        Bundle args = new Bundle();
        IntroBaseFragment fragment = new IntroBaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /* New class for custom adaptor. Nothing special, it links 3 intro fragment*/
    private static class MyAdapter extends FragmentStateAdapter {
        private List<FragmentBase> _arrFragment = new ArrayList<>();
        MyAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
            super(fragmentManager, lifecycle); }

        @Override
        public int getItemCount() {
            return _arrFragment.size();
        }

        @NonNull
        @Override
        public FragmentBase createFragment(int position) {
            return _arrFragment.get(position);
        }

        void add(FragmentBase f) {_arrFragment.add(f);}

    }
}
