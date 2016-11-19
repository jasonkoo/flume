package com.lenovo.push.marketing.lestat.flume.interceptor;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.lenovo.push.data.ip2location.entity.LocationBean;
import com.lenovo.push.data.ip2location.util.IpConvertUtil;
import com.lenovo.push.marketing.lestat.flume.util.ProjectUtil;
import com.lenovo.push.marketing.lestat.flume.vo.SdacApiVo;

/**
 * sdac数据去重拦截器
 * @author xuelj1
 *
 */
public class RemoveRepeatSdacInterceptor implements Interceptor {

	static final String SEP = "\001";
	static final byte[] TABLE_NAME = "sdac_device".getBytes();
	static final byte[] COL_FAM_NAME = "device_info".getBytes();
	static final byte[] COL_NAME = "occupying".getBytes();

	private HTable table;
	private IpConvertUtil util;
	private Configuration conf = null;

	private static final Logger logger = LoggerFactory.getLogger(RemoveRepeatSdacInterceptor.class);
	
	private RemoveRepeatSdacInterceptor() {
		conf = HBaseConfiguration.create();
		try {
			util = IpConvertUtil.getSingleInstance();
			table = new HTable(conf, TABLE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		if(table != null){
			try {
				table.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Event intercept(Event event) {
		String eventBody = new String(event.getBody(), Charsets.UTF_8);
		logger.debug("### inputString : " + eventBody);
		if (eventBody.split(SEP).length > 0) {
			if (ProjectUtil.KAFKA_DATA_NAME.equals(eventBody.split(SEP)[0])) {
				StringBuilder sb = new StringBuilder();
				sb.append(eventBody);
				SdacApiVo vo = new SdacApiVo().deserializeSdac(eventBody);
				if(vo != null ){
					//增加了日期
					setDay(vo.getReg_time(),sb);
					setCountyCode(sb,vo.getIp());
					if(StringUtils.isNotEmpty(vo.getImei())){
						isFristTime(sb,vo.getImei());
					}else{
						sb.append(SEP).append("false");
					}
				}else{
					sb.append(SEP).append("");
					setCountyCode(sb,"");
					sb.append(SEP).append("false");
				}
				event.setBody(sb.toString().getBytes());
				return event;
			}
		}
		return null;
	}
	//校验是否已首次上报，如果是赋值为true，并保存到去重表中，如果不是则赋值为false
	private void isFristTime(StringBuilder sb,String imei){
		String isFirstTime = "false";
		Result result = null;
		try {
			result = table.get(new Get(imei.getBytes()));
			if (result.isEmpty()) {
				Put put = new Put(Bytes.toBytes(imei));
				put.add(COL_FAM_NAME, COL_NAME,Bytes.toBytes(""));
				table.put(put);
				isFirstTime = "true";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("### {}:{}", imei, isFirstTime);
		sb.append(SEP).append(isFirstTime);
	}
	//通过ip转换国家码，以便统计使用
	private void setCountyCode(StringBuilder str,String ip){
		LocationBean bean = util.getLocationBean(ip);
		String countryCode = "";
		if(bean != null){
			countryCode = bean.getCountryCode();
		}
		str.append(SEP).append(countryCode);
	}
	
	private void setDay(String reg_time,StringBuilder sb){
		if(reg_time.length()>8){
			sb.append(SEP).append(reg_time.substring(0, 8));
		}else{
			sb.append(SEP).append("");
		}
	}

	@Override
	public List<Event> intercept(List<Event> events) {
		List<Event> intercepted = Lists.newArrayListWithCapacity(events.size());
	    for (Event event : events) {
	      Event interceptedEvent = intercept(event);
	      if (interceptedEvent != null) {
	        intercepted.add(interceptedEvent);
	      }
	    }
	    return intercepted;
	}

	public static class Builder implements Interceptor.Builder {

		@Override
		public Interceptor build() {
			return new RemoveRepeatSdacInterceptor();
		}

		@Override
		public void configure(Context arg0) {
			// TODO Auto-generated method stub
		}

	}
}
