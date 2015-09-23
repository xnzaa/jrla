package com.jrla.client;


import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
  
  
public class activity_explain_detail extends Activity {  
    private WebView myWebView ;  
    ImageView bn_back=null;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.layout_explain_detail);  
        //��ȡwebview�ؼ�  
        
        Bundle bundle = getIntent().getExtras();
        myWebView = (WebView) findViewById(R.id.webview);  
        //���ط������ϵ�ҳ��  
        myWebView.loadUrl(bundle.getString("url"));  
        //���ر����е�html   
        //����������δ������ʹ��ҳ�е����Ӳ���������ķ�ʽ��  
        myWebView.setWebViewClient(new HelloWebViewClient());  
        //�õ�webview����  
        WebSettings webSettings = myWebView.getSettings();    
        //����ʹ��javascript  
        webSettings.setJavaScriptEnabled(true);  
        //��WebAppInterface��javascript��  
//        myWebView.addJavascriptInterface(new HelloWebViewClient(this), "Android");
        bn_back=(ImageView)findViewById(R.id.bn_explain_detail_back);
		OnClickListener listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
				case R.id.bn_explain_detail_back:
					finish();
					break;
				}
			}
		};
		bn_back.setOnClickListener(listener); 
    }  
      
      
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        // Check if the key event was the Back button and if there's history  
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {  
            myWebView.goBack();  
            return true;  
        }  
      //  return true;  
        // If it wasn't the Back key or there's no web page history, bubble up to the default  
        // system behavior (probably exit the activity)  
        return super.onKeyDown(keyCode, event);  
    }  
  
    //Web��ͼ 
    private class HelloWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    } 
      
}  