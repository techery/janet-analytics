package io.techery.analytics.tracker;

import android.app.Activity;
import android.os.Bundle;

public interface ActivityLifecycleConsumer {

   void onCreate(Activity activity, Bundle bundle);
   void onStart(Activity activity);
   void onResume(Activity activity);
   void onPause(Activity activity);
   void onStop(Activity activity);
   void onSaveInstanceState(Activity activity, Bundle bundle);
   void onDestroy(Activity activity);
}
