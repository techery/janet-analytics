package io.techery.analytics.sample;

import java.util.Locale;
import java.util.Map;

public class MyAnalyticsSdk {

   public static void sendEvent(String actionName, Map<String, String> data) {
      // this mocks some hypothetical analytics SDK
      System.out.println(String.format(Locale.US, "\nEvent sent: %s\n\nMap content:\n%s", actionName, formatMap(data)));
   }

   private static String formatMap(Map<String, String> data) {
      final StringBuilder stringBuilder = new StringBuilder("\n");
      for (Map.Entry<String, String> entry : data.entrySet()) {
         stringBuilder
                 .append("Key: ").append(entry.getKey())
                 .append("\t\t Value: ").append(entry.getValue()).append("\n");
      }

      return stringBuilder.toString();
   }
}
