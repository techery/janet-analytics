package io.techery.analytics.sample.android.di;

import android.content.Context;
import dagger.Component;
import io.techery.analytics.sample.android.MainActivity;
import io.techery.analytics.sample_common.janet.AnalyticsInteractor;
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

   Context context();
   Set<Tracker> trackers();

   void inject(MainActivity activity);
   Janet janet();
   AnalyticsInteractor analyticsInteractor();
}
