package io.techery.analytics.sample;

import java.util.Locale;
import java.util.Map;

public class MyAnalyticsSdk {

   public static void sendEvent(String actionName, Map<String, String> data) {
      // this mocks some hypothetical analytics SDK
      System.out.println(String.format(Locale.US, "Event sent: %s\ndata: %s", actionName, data));
   }
}
