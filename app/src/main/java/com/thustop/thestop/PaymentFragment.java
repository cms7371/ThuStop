package com.thustop.thestop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import com.thustop.databinding.FragmentWebViewBinding;
import com.thustop.thestop.model.Ticket;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PaymentFragment extends FragmentBase{
    private FragmentWebViewBinding binding;
    private final static String TAG = "PaymentFragment";
    private String pg;
    private String method;
    private Ticket ticket;

    public static PaymentFragment newInstance(String pg, String method, Ticket ticket) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.pg = pg;
        fragment.method = method;
        fragment.ticket = ticket;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWebViewBinding.inflate(inflater);
        WebSettings webSettings = binding.wvCertification.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setDisplayZoomControls(false);
        binding.wvCertification.setWebChromeClient(new WebChromeClient());
        binding.wvCertification.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url != null && url.startsWith("intent://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Intent existPackage = requireActivity().getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        if (existPackage != null) {
                            startActivity(intent);
                        } else {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (url != null && url.startsWith("market://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            startActivity(intent);
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                view.loadUrl(url);
                return false;
            }
        });
        binding.wvCertification.addJavascriptInterface(new JsHandler(), "Android");
        binding.wvCertification.loadUrl("file:///android_asset/payment.html");

        return binding.getRoot();
    }

    private class JsHandler{
        private IamportClient iamportClient;

        public JsHandler(){
            iamportClient = new IamportClient(Constant.IAMPORT_API_KEY, Constant.IAMPORT_API_SECRET);
        }

        @JavascriptInterface
        public void onSuccess(String imp_uid) throws IOException, IamportResponseException {
            Payment result = iamportClient.paymentByImpUid(imp_uid).getResponse();
            Toast.makeText(requireContext(), "결제 결과 " + result.getStatus() + "로 성공! 메인화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
            List<Ticket> ticketList = new ArrayList<Ticket>();
            ticketList.add(ticket);
            _listener.putTickets(ticketList);
            _listener.setFragment(MainFragment.newInstance());
        }

        @JavascriptInterface
        public void onFailure(String msg) {
            Log.e(TAG, "onFailure: " + msg, new Throwable());
        }

        @JavascriptInterface
        public String getPG(){
            return pg;
        }

        @JavascriptInterface
        public String getMethod(){
            return method;
        }

        @JavascriptInterface
        public String getName(){
            return ticket.route_obj.name + " 노선 한 달 이용권";
        }

        @JavascriptInterface
        public int getPrice(){
            return ticket.route_obj.default_price;
        }

    }
}
