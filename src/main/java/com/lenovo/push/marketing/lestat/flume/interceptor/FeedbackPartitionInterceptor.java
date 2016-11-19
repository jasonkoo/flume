package com.lenovo.push.marketing.lestat.flume.interceptor;

import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class FeedbackPartitionInterceptor implements Interceptor {	
	private static final Logger logger = LoggerFactory.getLogger(FeedbackPartitionInterceptor.class);
	
	static final String INDEX = "index";
	static final String PATTERN = "pattern";
	static final String MATCHED = "matched";
	static final String NOTMATCHED = "notmatched";
	
	static final String DBNAME = "dbname";
	static final String TBLNAME = "tblname";
	static final String PARTNAME = "partname";
	static final String SEP = "\001";
	
	private final PatternBean pb;
	
	public FeedbackPartitionInterceptor(PatternBean pb) {
		this.pb = pb;
	}
	
	
	@Override
	public void close() {
		 // NO-OP...
	}

	@Override
	public void initialize() {
		 // NO-OP...		
	}

	@Override
	public Event intercept(Event event) {
		
		Map<String, String> eventHeader = event.getHeaders();
		String eventBody = new String(event.getBody(), Charsets.UTF_8);
		String[] parts = eventBody.split(SEP);
		String thedate = parts[1].split(" ")[0];
		if (thedate.length() != 8) {
			return null;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("### {}:{}", PARTNAME, thedate);
		}
		
		eventHeader.put(PARTNAME, thedate);
		
		String sid = parts[pb.getIndex()];
		
		if (logger.isDebugEnabled()) {
			logger.debug("### event sid {}", sid);
		}
		
		if (sid.startsWith(pb.getPattern())) {
			if (logger.isDebugEnabled()) {				
				logger.debug("### {}:{}", DBNAME, pb.getMatchedDBName());
				logger.debug("### {}:{}", TBLNAME, pb.getMatchedTBLName());
			}
			eventHeader.put(DBNAME, pb.getMatchedDBName());
			eventHeader.put(TBLNAME, pb.getMatchedTBLName());
		} else {
			if (logger.isDebugEnabled()) {				
				logger.debug("### {}:{}", DBNAME, pb.getNotMatchedDBName());
				logger.debug("### {}:{}", TBLNAME, pb.getNotMatchedTBLName());
			}
			eventHeader.put(DBNAME, pb.getNotMatchedDBName());
			eventHeader.put(TBLNAME, pb.getNotMatchedTBLName());
		}	
		
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
	
	public static class Builder implements Interceptor.Builder{
		
		private PatternBean pb;
		
		@Override
		public void configure(Context context) {
			pb = new PatternBean();
			pb.setIndex(context.getInteger(INDEX));
			pb.setPattern(context.getString(PATTERN));
			
			String matchedRoute = context.getString(MATCHED);
			pb.setMatchedDBName(matchedRoute.split("\\.")[0]);
			pb.setMatchedTBLName(matchedRoute.split("\\.")[1]);
			
			String notMatchedRoute = context.getString(NOTMATCHED);
			pb.setNotMatchedDBName(notMatchedRoute.split("\\.")[0]);
			pb.setNotMatchedTBLName(notMatchedRoute.split("\\.")[1]);
			
		}

		@Override
		public Interceptor build() {
			Preconditions.checkArgument(pb != null,  "Must supply a valid type pattern bean");
			return new FeedbackPartitionInterceptor(pb);
		}
		
	}

}
