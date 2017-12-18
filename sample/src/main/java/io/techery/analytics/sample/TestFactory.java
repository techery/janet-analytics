package io.techery.analytics.sample;

import io.techery.analytics.service.AnalyticActionHelperCacheImpl;
import io.techery.janet.analytics.ActionHelper;
import io.techery.janet.analytics.AnalyticActionHelperCache;

import java.util.Locale;

public class TestFactory {

   public static void main(String[] args) {
      final AnalyticActionHelperCache helpersCache = new AnalyticActionHelperCacheImpl();
      final ActionHelper helper = helpersCache.getActionHelper(FeedDetailsViewAction.class);
      final FeedDetailsViewAction action =
            new FeedDetailsViewAction("a_path", "mapAttrib", "singleAttrib");

      System.out.println(String.format(Locale.US,
            "actionKey: %s\ntrackerIds are: %s\ndata: %s",
            helper.getAction(action),
            printArray(helper.getTrackerIds()),
            helper.getData(action).toString()
            ));
   }

   private static String printArray(String[] array) {
      StringBuilder stringBuilder = new StringBuilder("[");
      for (String element : array) {
         stringBuilder.append("\"").append(element).append("\", ");
      }
      stringBuilder.append("]");
      return stringBuilder.toString().replace(", ]", "]");
   }
}
