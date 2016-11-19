package com.lenovo.push.marketing.lestat.flume.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ProjectUtil.class);

	public static Map<String, String> FMAP = new HashMap<String, String>();

	public static Map<String, Long> thresholdMap = new HashMap<String, Long>();

	public static final String DOMESTIC = "domestic";// 国内
	public static final String OVERSEAS = "overseas";// 海外

	public static final String SEPARATOR = "\001";
	public static final String TIMESTAMP_FORMAT = "yyyyMMdd HH:mm:ss";
	public static final String NULL_VALUE = "\000";
	public static final String KAFKA_DATA_NAME = "sdac.sdac";
	
	public static final String EXPIRY_DATE = "20151210";

	static {
		// 国内：PLUS是老设备, PRC是phone, PUSH是tablet
		// 海外: ROW是老设备，GEO是phone, TGEO是tablet
		FMAP.put("PLUS", DOMESTIC);
		FMAP.put("PRC", DOMESTIC);
		FMAP.put("PUSH", DOMESTIC);

		FMAP.put("ROW", OVERSEAS);
		FMAP.put("GEO", OVERSEAS);
		FMAP.put("TGEO", OVERSEAS);
	}

	// 通过注册方式，获取国内还是海外标识
	public static String getProvince(String fStr) {
		return FMAP.get(fStr);
	}

	public static String dateToStr(Date data) {
		if (data != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			return sdf.format(data);
		}
		return "";
	}
	
	public static Date stringToDate(String strDate){
		Date date = null;
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try{
			date = df.parse(strDate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return date;
	}
	
	public static boolean compareDate(String dateOne,String dateTow){
		if(stringToDate(dateOne).getTime()>=stringToDate(dateTow).getTime()){
			return true;
		}else{
			return false;
		}
	}
}
