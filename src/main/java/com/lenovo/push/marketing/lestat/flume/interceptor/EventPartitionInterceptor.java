package com.lenovo.push.marketing.lestat.flume.interceptor;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EventPartitionInterceptor implements Interceptor {
	
	static final String FLOWS = "flows";
	static final String DBNAME = "dbname";
	static final String TBLNAME = "tblname";
	static final String PARTNAME = "partname";
	static final Character SEP = '\001';
	
	private final Map<String, String> typeRuleMap;
	// partition rules: 
	// daily: ^(\\w+)\\.(\\w+)\\001(\\d{8})
	// monthly: ^(\\w+)\\.(\\w+)\\001(\\d{6})
	// none: ^(\\w+)\\.(\\w+)\\001()
	//private final String defaultRule = "^(\\w+)\\.(\\w+)\\001(\\d{8})";
	
	private static final Logger logger = LoggerFactory.getLogger(EventPartitionInterceptor.class);
	
	
	private EventPartitionInterceptor(Map<String, String> typeRuleMap) {
		this.typeRuleMap = typeRuleMap;
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
		String type = eventBody.substring(0, eventBody.indexOf(SEP));
		
		if (logger.isDebugEnabled()) {
			logger.debug("### event type {}", type);
		}
		
		Pattern rule = null;
		if (typeRuleMap.containsKey(type)) {
			rule = Pattern.compile(typeRuleMap.get(type));
		} else {
			//rule = Pattern.compile(defaultRule);
			return null;
		}
		Matcher matcher = rule.matcher(eventBody);
		if (matcher.find()) {
			if (logger.isDebugEnabled()) {				
				logger.debug("### {}:{}", DBNAME, matcher.group(1));
				logger.debug("### {}:{}", TBLNAME, matcher.group(2));
				logger.debug("### {}:{}", PARTNAME, matcher.group(3));
			}
			
			eventHeader.put(DBNAME, matcher.group(1));
			eventHeader.put(TBLNAME, matcher.group(2));
			eventHeader.put(PARTNAME, matcher.group(3));
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
	
	public static class Builder implements Interceptor.Builder {
		
		private Map<String, String> typeRuleMap;		
		
		@Override
		public void configure(Context context) {
			String flowListStr = context.getString(FLOWS);
			Preconditions.checkArgument(!StringUtils.isEmpty(flowListStr), 
					"Must supply at least one flow");
			String[] flowNames = flowListStr.split("\\s+");
			Context flowContexts =
			          new Context(context.getSubProperties(FLOWS + "."));
			typeRuleMap = Maps.newHashMap();
			for (String flowName : flowNames) {
				Context flowContext = new Context(
			            flowContexts.getSubProperties(flowName + "."));
				String type = flowContext.getString("type");
				String rule = flowContext.getString("rule");
				Preconditions.checkArgument(!StringUtils.isEmpty(rule),
			            "Supplied rule cannot be empty.");
				typeRuleMap.put(type, rule);
			}
		}

		@Override
		public Interceptor build() {
			 Preconditions.checkArgument(typeRuleMap.size() > 0,
			          "Must supply a valid type rule map");
			return new EventPartitionInterceptor(typeRuleMap);
		}
		
	}

}
