package com.jrla.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class activity_setting  extends Activity implements OnClickListener{

    ImageView bn_back;
    TextView tv_check=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_setting);
        
        
        bn_back =(ImageView)findViewById(R.id.bn_setting_back);
        bn_back.setOnClickListener(this);
//        tv_check =(TextView)findViewById(R.id.tv_check_update);
//        tv_check.setOnClickListener(this);
    }

   public void onClick(View v) {
    		switch (v.getId()) {
    		case R.id.bn_setting_back:
//    			Toast.makeText(this, "1111", Toast.LENGTH_SHORT).show();
    			finish();
    			break;
    		case R.id.tv_check_update:
//    				Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); //
//    				startActivityForResult(intent,0); //设置完成后返回到原来的界面
//    				finish();
    			Toast.makeText(this, "当前已是最新版", Toast.LENGTH_SHORT).show();
    			break;
    		}
    }

}