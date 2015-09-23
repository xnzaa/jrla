package com.jrla.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class activity_gps_dialog extends Activity implements OnClickListener{

    Button bn_cancel;
    Button bt_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_gps);
        
        
        bn_cancel =(Button)findViewById(R.id.dialog_button_cancel);
        bn_cancel.setOnClickListener(this);
        bt_ok =(Button)findViewById(R.id.dialog_button_ok);
        bt_ok.setOnClickListener(this);
    }

   public void onClick(View v) {
    		switch (v.getId()) {
    		case R.id.dialog_button_cancel:
//    			Toast.makeText(this, "1111", Toast.LENGTH_SHORT).show();
    			finish();
    			break;
    		case R.id.dialog_button_ok:
    				Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); //
    				startActivityForResult(intent,0); //设置完成后返回到原来的界面
    				finish();
    			break;
    		}
    }

}


