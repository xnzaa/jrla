package com.jrla.client;

import android.R.string;
import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class get_location implements OnGetGeoCoderResultListener {

	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	Context context=null;
	GeoCoder mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	// UI���
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	String parent_activity_name=null;
	
	public static String latitude="";
	public static String longtitude="";
	public get_location(Context parent,String activity_name)
	{
		context=parent;
		parent_activity_name=activity_name;
		mLocClient = new LocationClient(context);//�޷���λ�޸�����
		//mLocClient = new LocationClient(getApplicationContext()); 

		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
	}
	
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(context, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG).show();
			return;
		}
		String strInfo = String.format("γ�ȣ�%f ���ȣ�%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(context, strInfo, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(context, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG).show();
			return;
		}
//		Toast.makeText(context, result.getAddress(),Toast.LENGTH_LONG).show();
		if(parent_activity_name=="activity_main")
		{
			activity_main.location=result.getAddress();
		}
			
	}

	
	
	
	private static double rad(double d) { 
        return d * Math.PI / 180.0; 
    }
	
	private static double EARTH_RADIUS = 6378.137; 
//	static double DEF_PI180= 0.01745329252;
//    static double DEF_R =6370693.5;
	public static String getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
        Double lat1 = Double.parseDouble(lat1Str);
        Double lng1 = Double.parseDouble(lng1Str);
        Double lat2 = Double.parseDouble(lat2Str);
        Double lng2 = Double.parseDouble(lng2Str);
//        double ew1, ns1, ew2, ns2;
        double distance;
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;
        
//        ew1 = lng1 * DEF_PI180;
//        ns1 = lat1 * DEF_PI180;
//        ew2 = lng2 * DEF_PI180;
//        ns2 = lat2 * DEF_PI180;
//        // ���Բ�ӻ����������еĽ�(����)
//        distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
//        // ������[-1..1]��Χ�ڣ��������
//        if (distance > 1.0)
//            distance = 1.0;
//        else if (distance < -1.0)
//             distance = -1.0;
//        // ���Բ�ӻ�����
//        distance = DEF_R * Math.acos(distance);
        
        
        String distanceStr = distance+"";
        distanceStr = distanceStr.
            substring(0, distanceStr.indexOf("."));
          
        return distanceStr;
    }
	
	
	
	
	
/**
 * ��λSDK��������
 */
public class MyLocationListenner implements BDLocationListener {

	@Override
	public void onReceiveLocation(BDLocation location) {
		// map view ���ٺ��ڴ����½��յ�λ��
		if (location == null)
			return;
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
//		mBaiduMap.setMyLocationData(locData);
		
		latitude=location.getLatitude()+"";
		longtitude=location.getLongitude()+"";
		
		if (isFirstLoc) {
			isFirstLoc = false;
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//			mBaiduMap.animateMapStatus(u);
		}
		
		if (true) {
			LatLng ptCenter = new LatLng((location.getLatitude()), location.getLongitude());
			// ��Geo����
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
		} 
	}

	public void onReceivePoi(BDLocation poiLocation) {
	}
}
}

