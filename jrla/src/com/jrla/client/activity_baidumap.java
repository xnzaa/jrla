package com.jrla.client;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class activity_baidumap extends Activity  implements OnGetGeoCoderResultListener {

	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	ImageView bn_back=null;	
	MapView mMapView;
	BaiduMap mBaiduMap;
	private Marker mMarkerA;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);

	private InfoWindow mInfoWindow;
	
	
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	// UI相关
	boolean isFirstLoc = true;// 是否首次定位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_baidumap);
		mCurrentMode = LocationMode.NORMAL;
		bn_back=(ImageView)findViewById(R.id.map_bn_login_back);
		OnClickListener listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
				case R.id.map_bn_login_back:
					finish();
					//mLocClient.stop();
					break;
				}
			}
		};
		bn_back.setOnClickListener(listener);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);//无法定位修改这里
		 //mLocClient = new LocationClient(getApplicationContext()); 

        
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		

		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(activity_baidumap.this);
		addOverlay();
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				final LatLng ll = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				OnInfoWindowClickListener listener = null;
				if (marker == mMarkerA) {
					button.setText("聊天");
					button.setTextColor(Color.WHITE);
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
//							LatLng llNew = new LatLng(ll.latitude + 0.005,
//									ll.longitude + 0.005);
//							marker.setPosition(llNew);
//							mBaiduMap.hideInfoWindow();
							Intent aim_activity = null;
							Bundle bundle =null;
							Toast.makeText(activity_baidumap.this, "0", Toast.LENGTH_SHORT).show();
//								aim_activity = new Intent(activity_main.this, activity_main_food.class);
							aim_activity = new Intent(activity_baidumap.this, activity_chat.class);
							bundle = new Bundle();
							bundle.putString("first_activity", "activity_baidumap");
							bundle.putString("to_user", "null");
							aim_activity.putExtras(bundle);
							startActivity(aim_activity);
						}
					};
				}
				mInfoWindow = new InfoWindow(button, llInfo, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
//			Toast.makeText(activity_baidumap.this,"222", Toast.LENGTH_SHORT).show();
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
			
			
			
			if (true) {
				LatLng ptCenter = new LatLng((location.getLatitude()), location.getLongitude());
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
			} else if (false) {
//				EditText editCity = (EditText) findViewById(R.id.city);
//				EditText editGeoCodeKey = (EditText) findViewById(R.id.geocodekey);
//				// Geo搜索
//				mSearch.geocode(new GeoCodeOption().city(
//						editCity.getText().toString()).address(
//						editGeoCodeKey.getText().toString()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(activity_baidumap.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
//		mBaiduMap.clear();
//		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//				.icon(BitmapDescriptorFactory
//						.fromResource(R.drawable.icon_marka)));
//		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
//				.getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(activity_baidumap.this, strInfo, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(activity_baidumap.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
//		mBaiduMap.clear();
//		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//				.icon(BitmapDescriptorFactory
//						.fromResource(R.drawable.icon_marka)));
//		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
//				.getLocation()));
//		Toast.makeText(activity_baidumap.this, result.getAddress(),
//				Toast.LENGTH_LONG).show();
	}
	
	
	
	
	public void addOverlay() {
		// add marker overlay
		LatLng llA = new LatLng(30.599879, 114.357332);
		LatLng llB = new LatLng(39.942821, 116.369199);
		LatLng llC = new LatLng(39.939723, 116.425541);
		LatLng llD = new LatLng(39.906965, 116.401394);

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

		
	}
}
