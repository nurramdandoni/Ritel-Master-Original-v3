package com.example.ritelmaster;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {
    private WebView webView;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //menghilangkan actionbar pada atas activity
        ActionBar action_bar = getSupportActionBar();
        action_bar.hide();

//        copas dari stackoverflow : https://stackoverflow.com/questions/14592219/how-to-call-barcode-scanner-in-webview-and-passing-it-back
        webView = (WebView) findViewById(R.id.mainWebView);    //you might need to change webView1
        webView.loadUrl("http://192.168.1.197/TesBarcodeAndroid/");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");

        webView.setWebViewClient(new WebViewClient());
//        akhir copas
    }

    public class JavaScriptInterface {

        Context mContext;
        // Instantiate the interface and set the context
        private Context c;
        JavaScriptInterface(Context c){
            mContext = c;
        }

        @JavascriptInterface
        public void scanBarcode() {
//            Toast.makeText(mContext, data, Toast.LENGTH_LONG).show();
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setPrompt(String.valueOf("Scan Barcode"));
            integrator.setBeepEnabled(true);
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.initiateScan();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            String codeContent = scanningResult.getContents();
            String codeFormat = scanningResult.getFormatName();

            //load the URL and Pass the scanned barcode
//            openUrl(POST_URL + "?barcode=" + codeContent);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
            {
                webView.evaluateJavascript("FromAndroidToWebview(\""+codeContent+"\")",
                        null);
            }
            else
            {
                webView.loadUrl("javascript:FromAndroidToWebview(\""+codeContent+"\")");
            }
            Toast toast = Toast.makeText(getApplicationContext(), codeContent, Toast.LENGTH_LONG);
            toast.show();

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



}