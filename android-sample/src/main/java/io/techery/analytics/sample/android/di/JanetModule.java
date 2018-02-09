package io.techery.analytics.sample.android.di;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.techery.analytics.sample_common.janet.AnalyticsInteractor;
import io.techery.analytics.service.AnalyticsService;
import io.techery.janet.ActionService;
import io.techery.janet.Janet;
import io.techery.janet.analytics.Tracker;

import javax.inject.Singleton;
import java.util.Set;

@Module
public class JanetModule {

   @Provides
   @Singleton
   Janet provideJanet(Set<ActionService> actionServices) {
      Janet.Builder builder = new Janet.Builder();
      for (ActionService actionService : actionServices) {
         builder.addService(actionService);
      }
      return builder.build();
   }

   @Provides
   @IntoSet
   ActionService provideAnalyticsSerivice(Set<Tracker> trackers) {
      return new AnalyticsService(trackers);
   }

   @Provides
   @Singleton
   AnalyticsInteractor provideAnalyticsInteractor(Janet janet) {
      return new AnalyticsInteractor(janet);
   }
}
