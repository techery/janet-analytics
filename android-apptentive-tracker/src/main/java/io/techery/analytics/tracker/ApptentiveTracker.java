package io.techery.analytics.tracker;

import android.content.Context;
import com.apptentive.android.sdk.Apptentive;
import io.techery.janet.analytics.Tracker;

import java.util.Map;

public class ApptentiveTracker implements Tracker {

   public static final String KEY = "JANET_APPTENTIVE_TRACKER";

   protected final Context context;

   public ApptentiveTracker(Context context) {
      this.context = context;
   }

   @Override
   public String id() {
      return KEY;
   }

   @Override
   public void trackEvent(String actionKey, Map<String, Object> data) {
      Apptentive.engage(context, actionKey, data);
   }
}
