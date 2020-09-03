package com.thustop_00;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

import com.thustop_00.databinding.FragmentAgreeTermsBinding;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Agree_TermsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Agree_TermsFragment extends FragmentBase implements CompoundButton.OnCheckedChangeListener {
    FragmentAgreeTermsBinding binding;


    public Agree_TermsFragment() {
        // Required empty public constructor
    }
    //TODO 약관 보이는 뷰 클릭 연결하기!

    public static Agree_TermsFragment newInstance() {
        Agree_TermsFragment fragment = new Agree_TermsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAgreeTermsBinding.inflate(inflater);
        ButterKnife.bind(this,binding.getRoot());

        _listener.setToolbar(true, true, false);
        _listener.setTitle("");

        binding.cbAgreeAll.setOnCheckedChangeListener(this);
        binding.cbAgree1.setOnCheckedChangeListener(this);
        binding.cbAgree2.setOnCheckedChangeListener(this);
        binding.cbAgree3.setOnCheckedChangeListener(this);
        binding.cbAgree4.setOnCheckedChangeListener(this);
        binding.cbAgree5.setOnCheckedChangeListener(this);
        binding.cbAgree6.setOnCheckedChangeListener(this);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onAgreeClick(View view) {_listener.setFragment(IdVerificationFragment.newInstance());}

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.cb_agree_all :
                if(binding.cbAgreeAll.isChecked()) {
                    binding.cbAgree1.setChecked(true);
                    binding.cbAgree2.setChecked(true);
                    binding.cbAgree3.setChecked(true);
                    binding.cbAgree4.setChecked(true);
                    binding.cbAgree5.setChecked(true);
                    binding.cbAgree6.setChecked(true);
                    Clickable(true);
                } else {
                    if(binding.cbAgreeAll.isPressed()){
                        binding.cbAgree1.setChecked(false);
                        binding.cbAgree2.setChecked(false);
                        binding.cbAgree3.setChecked(false);
                        binding.cbAgree4.setChecked(false);
                        binding.cbAgree5.setChecked(false);
                        binding.cbAgree6.setChecked(false);
                        Clickable(false);
                    }
                }
                break;
            default:
                    binding.cbAgreeAll.setChecked(binding.cbAgree1.isChecked()
                            && binding.cbAgree2.isChecked()
                            && binding.cbAgree3.isChecked()
                            && binding.cbAgree4.isChecked()
                            && binding.cbAgree5.isChecked()
                            && binding.cbAgree6.isChecked());
                    Clickable(binding.cbAgree1.isChecked()
                            && binding.cbAgree2.isChecked()
                            && binding.cbAgree3.isChecked()
                            && binding.cbAgree4.isChecked()
                            && binding.cbAgree5.isChecked());
        }
    }

    public void Clickable(boolean b) {
        if(b) {
            binding.btAgree.setBackgroundResource(R.drawable.button_round_green);
            binding.btAgree.setEnabled(true);
        } else {
            binding.btAgree.setBackgroundResource(R.drawable.button_round_c4);
            binding.btAgree.setEnabled(false);
        }

    }
}