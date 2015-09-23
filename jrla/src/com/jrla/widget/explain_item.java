package com.jrla.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.jrla.client.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class explain_item extends LinearLayout {

	private static int CARD_PADDING = 0;
	private static int CHECKIN_PADDING = 0;
	public static float DISTANCE_FACTOR = 1.0F;
	private static int PROMO_PADDING = 0;
	private static int TUAN_PADDING = 0;

	TextView name;
	TextView date;
	Button favoriteButton;
	TextView othername;
	TextView others;
	TextView hidestring;
	Context context = null;

	public explain_item(Context context1) {
		super(context1);
		context = context1;
		// TODO Auto-generated constructor stub
	}

	public explain_item(Context context1, AttributeSet attrs) {
		super(context1, attrs);
		context = context1;
	}

	public void setPoiData(String name, String date_start, String date_end,
			String othername, String others, String id, String collect) {

		// DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		try {

			this.hidestring.setText(id);
			this.name.setText(name);
			this.othername.setText(othername);
			this.others.setText(others);
			// this.date.setText(format1.parse(date_start).toString()+format1.parse(date_end).toString());
			this.date.setText(format1.parse(date_start).getMonth() + "."
					+ format1.parse(date_start).getDate() + "-"
					+ format1.parse(date_end).getMonth() + "."
					+ format1.parse(date_start).getDate());

			if (collect.equalsIgnoreCase("1"))
				favoriteButton.setText("取消收藏");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected String showhidestring()
	{
		return hidestring.getText().toString();
	}
	
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.name = ((TextView) findViewById(R.id.explain_name));
		this.date = ((TextView) findViewById(R.id.explain_date));
		this.othername = ((TextView) findViewById(R.id.explain_othername));
		this.others = ((TextView) findViewById(R.id.explain_others));
		this.hidestring = (TextView) findViewById(R.id.explain_hide);
		hidestring.setVisibility(View.GONE);// 隐藏按钮保存id信息
//		this.favoriteButton = ((Button) findViewById(R.id.bn_explain_favorite));
//		favoriteButton.setOnClickListener(new OnClickListener() {

//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (favoriteButton.getText().toString().equalsIgnoreCase("收藏")) {
//					Toast.makeText(context,
//							"collect" + hidestring.getText().toString(),
//							Toast.LENGTH_SHORT).show();
//					favoriteButton.setText("取消收藏");
//				}
//				if (favoriteButton.getText().toString()
//						.equalsIgnoreCase("取消收藏")) {
//					Toast.makeText(context,
//							"uncollect" + hidestring.getText().toString(),
//							Toast.LENGTH_SHORT).show();
//					favoriteButton.setText("收藏");
//				}
//			}
//		});
	}

}
