package com.thustop_00;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thustop_00.databinding.FragmentCounsellingBinding;

import io.gitple.android.sdk.Gitple;

public class CounsellingFragment extends FragmentBase {
    FragmentCounsellingBinding binding;

    public static CounsellingFragment newInstance(){
        CounsellingFragment fragment = new CounsellingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCounsellingBinding.inflate(inflater);
        binding.setCounselFrag(this);
        _listener.closeDrawer();
        _listener.setToolbarStyle(_listener.WHITE_BACK_EXIT, "1:1상담");
        return binding.getRoot();
    }

    public void onCounselClick(View view){
        //TODO 사용자 정보랑 연동하여 답변을 받을 수 있도록 해야함
        /*if (loggined) {
            Gitple.user().setId("user id")
                    .setName("user name")
                    .setMeta("City", "Seoul");
        } else {
            Gitple.user().reset(); // 로그아웃시 반드시 호출해야 합니다. 사용자 정보를 초기화합니다.
        }*/
        Gitple.launch(getContext());
    }
}
