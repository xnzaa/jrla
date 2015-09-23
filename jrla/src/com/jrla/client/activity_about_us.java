package com.jrla.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class activity_about_us extends Activity implements OnClickListener{

    ImageView bn_back;
    TextView tv_check=null;
    TextView tv_owner=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_about_us);
        
        
        bn_back =(ImageView)findViewById(R.id.bn_about_us_back);
        bn_back.setOnClickListener(this);
        tv_check =(TextView)findViewById(R.id.tv_check_update);
        tv_check.setOnClickListener(this);
        tv_owner =(TextView)findViewById(R.id.tv_owner);
        tv_owner.setOnClickListener(this);
    }

   public void onClick(View v) {
    		switch (v.getId()) {
    		case R.id.bn_about_us_back:
//    			Toast.makeText(this, "1111", Toast.LENGTH_SHORT).show();
    			finish();
    			break;
    		case R.id.tv_check_update:
//    				Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); //
//    				startActivityForResult(intent,0); //设置完成后返回到原来的界面
//    				finish();
    			Toast.makeText(this, "当前已是最新版", Toast.LENGTH_SHORT).show();
    			break;
    		
    		case R.id.tv_owner:
    			Toast.makeText(this, "武汉理工大学本科毕业设计，转载请注明，谢谢！", Toast.LENGTH_SHORT).show();
    			break;
	}
    }

}
