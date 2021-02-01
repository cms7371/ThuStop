package com.thustop.thestop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thustop.databinding.FragmentRegisterVerificationBinding;

public class PaymentFragment extends FragmentBase{
    private FragmentRegisterVerificationBinding binding;

    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterVerificationBinding.inflate(inflater);

        WebSettings webSettings = binding.wvCertification.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setDisplayZoomControls(false);
        binding.wvCertification.setWebChromeClient(new WebChromeClient());
        binding.wvCertification.setWebViewClient(new WebViewClient());
        binding.wvCertification.addJavascriptInterface(new JsHandler(), "Android");
        binding.wvCertification.loadUrl("file:///android_asset/certification.html");

        return binding.getRoot();
    }

    private class JsHandler{

        @JavascriptInterface
        public void onSuccess(){}
    }
}
