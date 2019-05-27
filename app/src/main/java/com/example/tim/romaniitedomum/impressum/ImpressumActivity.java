package com.example.tim.romaniitedomum.impressum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.Util.Util;
import com.example.tim.romaniitedomum.map.MapActivity;

/**
 * created by Tim Staats 26.04.2019
 */

public class ImpressumActivity extends AppCompatActivity {

    private static final String TAG = "ImpressumActivity";
    public static final String CIVITAS_URL = "https://www.geschichte.uni-hamburg.de/arbeitsbereiche/alte-geschichte/personen/panzram.html";
    private Intent originIntent;

    private TextView tvCredits;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);
        setTitle("Impressum");
        originIntent = new Intent(ImpressumActivity.this, MapActivity.class);
        originIntent.putExtra(getResources().getString(R.string.origin), Util.IMPRESSUM_ACTIVITY);

        initImpressum();

    }

    private void initImpressum() {
        webView = findViewById(R.id.webview_impressum);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setPluginState(PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                setProgressBarIndeterminateVisibility(false);
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl(CIVITAS_URL);
        webView.setVisibility(View.VISIBLE);
        
        // visibility gone within XML
        tvCredits = findViewById(R.id.text_credits);
        String credits = "Icon made by Lucy G from www.flaticon.com \n" + "Icon made by Freepik from www.flaticon.com \n";
        tvCredits.setText(credits);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(originIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
