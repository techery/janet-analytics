package io.techery.analytics.tracker;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import io.techery.janet.analytics.Tracker;

import java.util.Map;

public class AdobeTracker implements Tracker, Application.ActivityLifecycleCallbacks {

   public static final String KEY = "JANET_ADOBE_TRACKER";

   protected boolean appContextSet = false;

   public AdobeTracker(Application application) {
      application.registerActivityLifecycleCallbacks(this);
   }

   @Override
   public String id() {
      return KEY;
   }

   @Override
   public void trackEvent(String actionKey, Map<String, Object> data) {
      Analytics.trackAction(actionKey, data);
   }

   @Override
   public void onActivityCreated(Activity activity, Bundle bundle) {
      if (!appContextSet) {
         appContextSet = true;
         Config.setContext(activity.getApplicationContext());
      }
   }

   @Override
   public void onActivityStarted(Activity activity) {
   }

   @Override
   public void onActivityResumed(Activity activity) {
      Config.collectLifecycleData(activity);
   }

   @Override
   public void onActivityPaused(Activity activity) {
      Config.pauseCollectingLifecycleData();
   }

   @Override
   public void onActivityStopped(Activity activity) {
   }

   @Override
   public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
   }

   @Override
   public void onActivityDestroyed(Activity activity) {
   }
}
