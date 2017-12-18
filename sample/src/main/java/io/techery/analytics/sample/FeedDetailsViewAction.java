package io.techery.analytics.sample;

import io.techery.janet.analytics.annotation.ActionPart;
import io.techery.janet.analytics.annotation.AnalyticsEvent;
import io.techery.janet.analytics.annotation.Attribute;
import io.techery.janet.analytics.annotation.AttributeMap;

import java.util.HashMap;
import java.util.Map;

@AnalyticsEvent(actionKey = "blah-blah:@action_path", trackerIds = {"ClientProjectUsedTracker", "bla"})
public class FeedDetailsViewAction {

   @ActionPart
   final String action;

   @AttributeMap
   final Map<String, String> attributeMap = new HashMap<>();

   @AttributeMap
   final Map<String, String> attributeMapToo = new HashMap<>();

   @Attribute("single_attribute")
   String bar;

   public FeedDetailsViewAction(String actionPath, String attributeForMap, String singleAttribute) {
      action = actionPath;
      attributeMap.put("attributeForMap", attributeForMap);
      bar = singleAttribute;
   }
}
