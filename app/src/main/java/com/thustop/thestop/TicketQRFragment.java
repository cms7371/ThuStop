package com.thustop.thestop;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.thustop.R;
import com.thustop.databinding.FragmentTicketQrBinding;
import com.thustop.thestop.model.Ticket;

import net.glxn.qrgen.android.QRCode;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Locale;

public class TicketQRFragment extends FragmentBase {
    private FragmentTicketQrBinding binding;
    private Ticket ticket;
    private CountDownTimer timer;
    private String[] testQRList = new String[]{"whjoifhawopfhaiopfh", "wihntgopeashgoihogi", "whtgoihsgoihszf"};
    private int testQROffset = 0;

    public static TicketQRFragment newInstance(Ticket ticket) {
        TicketQRFragment fragment = new TicketQRFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.ticket = ticket;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTicketQrBinding.inflate(inflater);
        binding.setFragTicketQR(this);
        _listener.setToolbarStyle(OnFragmentInteractionListener.GREEN_BACK_EXIT, "출발 5분 전 미리 대기해주세요!");
        binding.tvFtqStart.setText(ticket.start_via_obj.stop.name);
        binding.tvFtqEnd.setText(ticket.end_via_obj.stop.name);
        binding.tvFtqTime.setText(ticket.start_via_obj.time);
        CountDownTimer();
        setTestQR();
        binding.tvFtqManage.setText(Utils.getPartialFontSpan(requireContext(),"regular","김민규 님의 티켓", new String[]{"김민규"}));

        return binding.getRoot();
    }

    public void CountDownTimer() {
        if (timer == null)
            timer = new CountDownTimer(1000 * 120, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long codeTimeCount = millisUntilFinished/1000;
                    int min = (int) codeTimeCount/60;
                    int sec = (int) codeTimeCount-(min*60);
                    binding.tvFtqTimer.setText(String.format(Locale.KOREA, "%01d분 %02d초", min, sec));
                }

                @Override
                public void onFinish() {
                    Toast.makeText(getActivity(),"인증시간이 만료되었습니다. 다시 인증해주세요",Toast.LENGTH_SHORT).show();

                }
            }.start();
        else
            timer.start();
    }

    public void setTestQR(){
        int dp196 = _listener.covertDPtoPX(196);
        binding.ivFtqQr.setImageBitmap(QRCode.from(testQRList[testQROffset]).withSize(dp196, dp196).bitmap());
        if(testQROffset == 2)
            testQROffset = 0;
        else
            ++testQROffset;
    }

    public void onRefreshClick(View view){
        binding.ivFtqQr.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            CountDownTimer();
            setTestQR();
        },500);

    }

}
