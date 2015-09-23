package com.jrla.client;

import com.baidu.mapapi.SDKInitializer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class activity_welcome extends Activity{
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
   	
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.layout_welcome);
        SDKInitializer.initialize(getApplicationContext());
        new Handler().postDelayed(new Runnable(){

            public void run() {
            	Intent intent = new Intent();
                intent.setClass(activity_welcome.this, activity_chat.class); 
                
                Bundle bundle = new Bundle();
				bundle.putString("first_activity", "activity_welcome");
				intent.putExtras(bundle);
				
                activity_welcome.this.startActivity(intent);
                activity_welcome.this.finish();
            }
        }, 2000);
    };
}
