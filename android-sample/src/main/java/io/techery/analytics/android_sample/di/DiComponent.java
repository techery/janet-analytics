package io.techery.analytics.android_sample.di;

import android.content.Context;
import dagger.Component;
import io.techery.analytics.android_sample.App;
import io.techery.analytics.android_sample.MainActivity;
import io.techery.analytics.sample_common.janet.AnalyticsInteractor;
import io.techery.analytics.tracker.ActivityLifecycleCallback;
import io.techery.analytics.tracker.ActivityLifecycleConsumer;
import io.techery.janet.Janet;
import io.techery.janet.analytics.Tracker;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
@Component(modules = {
      DiModule.class,
      JanetModule.class,
})
public interface DiComponent {

   void inject(App app);

   Context context();
   Set<Tracker> trackers();
   Set<ActivityLifecycleConsumer> lifecycleConsumers();
   ActivityLifecycleCallback activityLifecycleCallback();

   void inject(MainActivity activity);
   Janet janet();
   AnalyticsInteractor analyticsInteractor();
}
