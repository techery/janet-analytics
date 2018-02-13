package io.techery.analytics.sample;

import io.techery.janet.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

public class SomeAnalyticsTracker implements Tracker {

   public static final String ID = "MyAnalyticsSdkTrackerKey";

   @Override
   public String id() {
      return ID;
   }

   @Override
   public void trackEvent(String actionKey, Map<String, Object> data) {
      SomeAnalyticsSdk.sendEvent(actionKey, prepareData(data));
   }

   private Map<String, String> prepareData(Map<String, Object> data) {
      // final transformation - this will be special for any Tracker implementation
      // while Map<String, Object> is a more general contract.
      // E.g. you can put Integer or Boolean as a map item value
      final Map<String, String> preparedData = new HashMap<>();
      for (Map.Entry<String, Object> entry : data.entrySet()) {
         preparedData.put(entry.getKey(), entry.getValue().toString());
      }

      return preparedData;
   }
}
