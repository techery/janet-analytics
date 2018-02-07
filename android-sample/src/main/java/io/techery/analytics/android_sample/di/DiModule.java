package io.techery.analytics.android_sample.di;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.techery.analytics.tracker.ActivityLifecycleCallback;
import io.techery.analytics.tracker.ActivityLifecycleConsumer;
import io.techery.analytics.tracker.AdobeTracker;
import io.techery.analytics.tracker.ApptentiveTracker;
import io.techery.janet.analytics.Tracker;

import javax.inject.Singleton;
import java.util.Set;

@Module
public class DiModule {

   private final Context context;

   public DiModule(Context context) {
      this.context = context;
   }

   @Provides
   @Singleton
   Context provideAppContext() {
      return context;
   }

   @Provides
   @Singleton
   AdobeTracker provideAdobeTrackerInstance() {
      return new AdobeTracker();
   }

   @Provides
   @IntoSet
   Tracker provideApptentiveTracker(Context context) {
      return new ApptentiveTracker(context);
   }

   @Provides
   @IntoSet
   Tracker provideAdobeTracker(AdobeTracker adobeTracker) {
      return adobeTracker;
   }

   @Provides
   @IntoSet
   ActivityLifecycleConsumer provideLifecycleAwareAdobeTracker(AdobeTracker adobeTracker) {
      return adobeTracker;
   }

   @Provides
   ActivityLifecycleCallback provideActivityLifecycleCallback(Set<ActivityLifecycleConsumer> lifecycleConsumers) {
      return new ActivityLifecycleCallback(lifecycleConsumers);
   }
}
