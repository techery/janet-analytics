package io.techery.analytics.tracker;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.*;

public class ActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

   protected final List<ActivityLifecycleConsumer> lifecycleConsumers = new ArrayList<>();

   public ActivityLifecycleCallback(Collection<ActivityLifecycleConsumer> lifecycleConsumers) {
      this.lifecycleConsumers.addAll(lifecycleConsumers);
   }

   public ActivityLifecycleCallback(ActivityLifecycleConsumer... lifecycleConsumers) {
      this(Arrays.asList(lifecycleConsumers));
   }

   public void addLifecycleConsumer(ActivityLifecycleConsumer lifecycleConsumer) {
      this.lifecycleConsumers.add(lifecycleConsumer);
   }

   public void removeLifecycleConsumer(ActivityLifecycleConsumer lifecycleConsumer) {
      this.lifecycleConsumers.remove(lifecycleConsumer);
   }

   @Override
   public void onActivityCreated(Activity activity, Bundle bundle) {
      for (ActivityLifecycleConsumer consumer : lifecycleConsumers) {
         consumer.onCreate(activity, bundle);
      }
   }

   @Override
   public void onActivityStarted(Activity activity) {
      for (ActivityLifecycleConsumer consumer : lifecycleConsumers) {
         consumer.onStart(activity);
      }
   }

   @Override
   public void onActivityResumed(Activity activity) {
      for (ActivityLifecycleConsumer consumer : lifecycleConsumers) {
         consumer.onResume(activity);
      }
   }

   @Override
   public void onActivityPaused(Activity activity) {
      for (ActivityLifecycleConsumer consumer : lifecycleConsumers) {
         consumer.onPause(activity);
      }
   }

   @Override
   public void onActivityStopped(Activity activity) {
      for (ActivityLifecycleConsumer consumer : lifecycleConsumers) {
         consumer.onStop(activity);
      }
   }

   @Override
   public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
      for (ActivityLifecycleConsumer consumer : lifecycleConsumers) {
         consumer.onSaveInstanceState(activity, bundle);
      }
   }

   @Override
   public void onActivityDestroyed(Activity activity) {
      for (ActivityLifecycleConsumer consumer : lifecycleConsumers) {
         consumer.onDestroy(activity);
      }
   }
}
