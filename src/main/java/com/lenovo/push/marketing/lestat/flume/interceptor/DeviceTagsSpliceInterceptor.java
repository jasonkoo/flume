package com.lenovo.push.marketing.lestat.flume.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

public class DeviceTagsSpliceInterceptor implements Interceptor {
	

	static final String SEP = "\001";
	static final byte[] TABLE_NAME = "device".getBytes();
	static final byte[] COL_FAM_NAME = "dynamic_props".getBytes();
	static final byte[] COL_NAME = "tags".getBytes();
	static final String PART_NAME = "partname";
	static final String DOMESTIC_PART_NAME = "domestic";
	static final String OVERSEAS_PAET_NAME = "overseas";
	
	private HTable table;
	private Result result;
	private static final Logger logger = LoggerFactory.getLogger(DeviceTagsSpliceInterceptor.class);
	
	
	private DeviceTagsSpliceInterceptor() {
		Configuration conf = HBaseConfiguration.create();
		try {
			table = new HTable(conf, TABLE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		if (table != null)
			try {
				table.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void initialize() {
		 // NO-OP...		
	}

	@Override
	public Event intercept(Event event) {
		Map<String, String> eventHeader = event.getHeaders();
		String eventBody = new String(event.getBody(), Charsets.UTF_8);
		StringBuilder sb = new StringBuilder();
		sb.append(eventBody);
		sb.append(SEP);
		
		String tags = null;
		String[] fields = eventBody.split(SEP);
		if (fields[0].startsWith(DOMESTIC_PART_NAME)) {
			eventHeader.put(PART_NAME, DOMESTIC_PART_NAME);
		} else if (fields[0].startsWith(OVERSEAS_PAET_NAME)) {
			eventHeader.put(PART_NAME, OVERSEAS_PAET_NAME);
		} 
		
		try {
			String deviceid = fields[2];
			if (deviceid.length() > 0) {
				result = table.get(new Get(deviceid.getBytes()));
				if (result != null) {
					byte[] tags_bypes = result.getValue(COL_FAM_NAME, COL_NAME);
					if (tags_bypes != null) {
						tags = new String(tags_bypes, Charsets.UTF_8);
						logger.debug("### {}:{}", deviceid, tags);
					} 		
				} 
			} 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sb.append(tags);
		
		event.setBody(sb.toString().getBytes());
		
		return event;
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
			return new DeviceTagsSpliceInterceptor();
		}

		@Override
		public void configure(Context arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
