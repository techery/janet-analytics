package io.techery.analytics.tracker;

import android.app.Activity;
import android.os.Bundle;
import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import io.techery.janet.analytics.Tracker;

import java.util.Map;

public class AdobeTracker implements Tracker, ActivityLifecycleConsumer {

   public static final String KEY = "JANET_ADOBE_TRACKER";

   protected boolean appContextSet = false;

   @Override
   public String id() {
      return KEY;
   }

   @Override
   public void trackEvent(String actionKey, Map<String, Object> data) {
      Analytics.trackAction(actionKey, data);
   }

   @Override
   public void onCreate(Activity activity, Bundle bundle) {
      if (!appContextSet) {
         appContextSet = true;
         Config.setContext(activity.getApplicationContext());
      }
   }

   @Override
   public void onStart(Activity activity) {
   }

   @Override
   public void onResume(Activity activity) {
      Config.collectLifecycleData(activity);
   }

   @Override
   public void onPause(Activity activity) {
      Config.pauseCollectingLifecycleData();
   }

   @Override
   public void onStop(Activity activity) {
   }

   @Override
   public void onSaveInstanceState(Activity activity, Bundle bundle) {
   }

   @Override
   public void onDestroy(Activity activity) {
   }
}
