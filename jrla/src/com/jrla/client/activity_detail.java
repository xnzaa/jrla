package com.jrla.client;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jrla.widget.QuickAction;
import com.jrla.widget.QuickActionBar;
import com.jrla.widget.QuickActionWidget;





public class activity_detail extends Activity implements OnClickListener
{
	
	Handler handler = new Handler() 
	{
		public void handleMessage(Message paramMessage) 
		{
			if(paramMessage.what == 1)
			{
				info.findViewById(R.id.loadingbar).setVisibility(View.GONE);
				info.findViewById(R.id.serverdata).setVisibility(View.VISIBLE);
				String detail_message=paramMessage.obj.toString();
				String data[]=detail_message.split("\\|");
				
//writebuff=id+"|"+chara+"|"+phone_num+"|"+famous_activity+"|"+detail_explain+"|"+others
//+"|"+data_start+"|"+data_end;
//
//
//writebuff=id+"|"+chara+"|"+phone_num+"|"+famous_food+"|"+detail_explain+"|"+others
//+"|"+custom_tailor;
//
//writebuff=id+"|"+chara+"|"+phone_num+"|"+famous_gift+"|"+detail_explain+"|"+others
//+"|"+custom_tailor;

				if(table.equalsIgnoreCase(communicate.table_jrla_activity_list))
				{
					tv_phone_num.setText(data[2]);
					tv_fammous.setText(data[3]);
					tv_detail_explain.setText(data[4]);
					tv_others_info.setText("时间："+data[6]+" 至 "+data[7]);
				}
				if(table.equalsIgnoreCase(communicate.table_jrla_food_list))
				{
					tv_phone_num.setText(data[2]);
					tv_fammous.setText(data[3]);
					tv_detail_explain.setText(data[4]);
					if(data[6].equalsIgnoreCase("1"))
					tv_others_info.setText("接受定制");
					else {
						tv_others_info.setText("不接受定制");
					}
				}
				if(table.equalsIgnoreCase(communicate.table_jrla_gift_list))
				{
					tv_phone_num.setText(data[2]);
					tv_fammous.setText(data[3]);
					tv_detail_explain.setText(data[4]);
					if(data[6].equalsIgnoreCase("1"))
					tv_others_info.setText("接受定制");
					else {
						tv_others_info.setText("不接受定制");
					}
				}
			}
		}
	};
	
	private  LinearLayout info;
//	private Animation enterAnim;
//	private Animation exitAnim;
	private QuickActionWidget mBar;

	private ImageView iv_detail_back=null;
	private TextView tv_renjun=null;
	private TextView  tv_addr=null;
	private TextView tv_phone_num=null;
	
	private TextView tv_fammous=null;
	private TextView tv_detail_explain=null;
	private TextView tv_others_info=null;
	private TextView tv_local_map=null;
	
    static String data_detailString="";
	private TextView title_text=null;
	private Button bn_collect=null;
	
	String table="";
	
	static String id;
	static String xid;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_poidetail);

        iv_detail_back=(ImageView)findViewById(R.id.iv_detail_back);
        iv_detail_back.setOnClickListener(this);

    	
        LayoutInflater inflater = LayoutInflater.from(this);
        info = (LinearLayout)inflater.inflate(R.layout.layout_poiinfo, null);
        
        LinearLayout scroll = (LinearLayout)findViewById(R.id.lite_list);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        scroll.addView(info, layoutParams);
        
        tv_renjun=(TextView)findViewById(R.id.text2_renjun_value);
    	tv_addr=(TextView)findViewById(R.id.tv_addr);
    	tv_phone_num=(TextView)findViewById(R.id.tv_phone_num);
    	tv_phone_num.setOnClickListener(this);
    	tv_fammous=(TextView)findViewById(R.id.tv_famous);
    	tv_detail_explain=(TextView)findViewById(R.id.tv_detail_explain);
        tv_others_info=(TextView)findViewById(R.id.tv_other_info);
        tv_local_map=(TextView)findViewById(R.id.tv_local_map);
        tv_local_map.setOnClickListener(this);
        bn_collect=(Button)findViewById(R.id.bn_collect);
        bn_collect.setOnClickListener(this);
        
        Bundle bundle=getIntent().getExtras();
        title_text=(TextView)findViewById(R.id.title_bar_name);
        title_text.setText(bundle.getString("name"));
        tv_addr.setText(bundle.getString("addr"));
        tv_renjun.setText(bundle.getString("price"));
//        String xid=null;
//        if(bundle.getString("table").equalsIgnoreCase("jrla_activity_list"))
//        {
//            xid=bundle.getString("aid"); 
//        }
//        else if(bundle.getString("table").equalsIgnoreCase("jrla_food_list"))
//        {
//        	xid=bundle.getString("fid");
//        }
//        else if(bundle.getString("table").equalsIgnoreCase("jrla_gist_list"))
//        {
//        	xid=bundle.getString("gid");
//        }  
        xid=bundle.getString("xid");
        table=bundle.getString("table");
        id=bundle.getString("id");
        if(xid.equalsIgnoreCase("1"))
        {
        	bn_collect.setText("取消收藏");
        }
        else 
        	bn_collect.setText("收藏商家");
        new Thread()
        {
        	public void run()
        	{
        		data_detailString="";
        		data_detailString=get_data_detail(table,id);
        		
        		Message msg = new Message();
        		msg.what = 1;
        		handler.obtainMessage(1, data_detailString).sendToTarget();
        	}
        }.start();
 
        View btnRequest = findViewById(R.id.requestroute);
        btnRequest.setOnClickListener(this);

        
	}
	
	private String get_data_detail(String t,String i)
	{
		return communicate.get_data_detail(t,i);
			
	}
	
	
	public void onClick(View v)
	{
		Intent aim_activity=null;
		switch(v.getId())
		{

			
			case R.id.requestroute:
			{
//				mBar.show(v);
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri uri = Uri.parse("http://map.baidu.com/mobile/webapp/index/index/qt=cur&wd=%E6%AD%A6%E6%B1%89%E5%B8%82&from=maponline&tn=m01&ie=utf-8=utf-8/tab=line");
				intent.setData(uri);
				startActivity(intent);
				break;
			}
		case R.id.iv_detail_back:
			{
				finish();
				break;
			}
		case R.id.tv_local_map:
		{
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri uri = Uri.parse("http://map.baidu.com");
			intent.setData(uri);
			startActivity(intent);
			break;
		}
		case R.id.tv_phone_num:
		{
			Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_CALL);  
            //url:统一资源定位符  
            //uri:统一资源标示符（更广）  
            intent.setData(Uri.parse("tel:" + tv_phone_num.getText()));  
            //开启系统拨号器  
            startActivity(intent); 
		}
		case R.id.bn_collect :
			if(bn_collect.getText().toString().equalsIgnoreCase("收藏商家"))
			{
				String add_col=null;
				if(table.equalsIgnoreCase(communicate.table_jrla_activity_list))
				{
					add_col=communicate.table_jrla_activity_list+"|"+id+"|"+activity_login.spf_read_user_id(getApplicationContext());
				}
				if(table.equalsIgnoreCase(communicate.table_jrla_food_list))
				{
					add_col=communicate.table_jrla_food_list+"|"+id+"|"+activity_login.spf_read_user_id(getApplicationContext());
				}
				if(table.equalsIgnoreCase(communicate.table_jrla_gift_list))
				{
					add_col=communicate.table_jrla_gift_list+"|"+id+"|"+activity_login.spf_read_user_id(getApplicationContext());
				}
				communicate.socket_send(communicate.server_ip, communicate.server_port, add_col, communicate.function_add_col);
				Toast.makeText(activity_detail.this, "收藏成功", Toast.LENGTH_SHORT).show();
				bn_collect.setText("取消收藏");
			}
			
			else if(bn_collect.getText().toString().equalsIgnoreCase("取消收藏"))
			{
				String del_col=null;
				if(table.equalsIgnoreCase(communicate.table_jrla_activity_list))
				{
					del_col=communicate.table_jrla_activity_list+"|"+id+"|"+activity_login.spf_read_user_id(getApplicationContext());
				}
				if(table.equalsIgnoreCase(communicate.table_jrla_food_list))
				{
					del_col=communicate.table_jrla_food_list+"|"+id+"|"+activity_login.spf_read_user_id(getApplicationContext());
				}
				if(table.equalsIgnoreCase(communicate.table_jrla_gift_list))
				{
					del_col=communicate.table_jrla_gift_list+"|"+id+"|"+activity_login.spf_read_user_id(getApplicationContext());
				}
				communicate.socket_send(communicate.server_ip, communicate.server_port, del_col, communicate.function_del_col);
				Toast.makeText(activity_detail.this, "取消成功", Toast.LENGTH_SHORT).show();
				bn_collect.setText("收藏商家");
			}
			break;
		}
	
	}
	
	
	private void prepareQuickActionBar() {
		this.mBar = new QuickActionBar(this);
		QuickAction qa=new MyQuickAction(activity_detail.this, R.drawable.icon_car,"自驾");
		
		this.mBar.addQuickAction(qa);
		
		this.mBar.addQuickAction(new MyQuickAction(this, R.drawable.icon_bus,
				"公共交通"));
		this.mBar.addQuickAction(new MyQuickAction(this, R.drawable.icon_walk,
				"步行"));
		// this.mBar.setOnQuickActionClickListener(this.mActionListener);
	}
	

	private static class MyQuickAction extends QuickAction {
		private static final ColorFilter BLACK_CF = new LightingColorFilter(
				-16777216, -16777216);

		// public MyQuickAction(Context paramContext, int paramInt1, int
		// paramInt2)
		// {
		// super(buildDrawable(paramContext, paramInt1),
		// String.valueOf(paramInt2));
		// }
		Context context;
		public MyQuickAction(Context paramContext, int paramInt,
				CharSequence paramCharSequence) {
			super(paramContext, paramInt, paramCharSequence);
			context=paramContext;
		}

//		
//		View.OnClickListener listener=new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(context, "222", Toast.LENGTH_SHORT).show();
//			}
//		};
		// private static Drawable buildDrawable(Context paramContext, int
		// paramInt)
		// {
		// Drawable localDrawable =
		// paramContext.getResources().getDrawable(paramInt);
		// localDrawable.setColorFilter(BLACK_CF);
		// return localDrawable;
		// }
	}
}