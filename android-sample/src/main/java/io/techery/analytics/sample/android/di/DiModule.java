package io.techery.analytics.sample.android.di;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.techery.analytics.tracker.AdobeTracker;
import io.techery.analytics.tracker.ApptentiveTracker;
import io.techery.janet.analytics.Tracker;

import javax.inject.Singleton;

@Module
public class DiModule {

   private final Application application;

   public DiModule(Application application) {
      this.application = application;
   }

   @Provides
   @Singleton
   Context provideAppContext() {
      return application.getApplicationContext();
   }

   @Provides
   @Singleton
   Application provideApplication() {
      return application;
   }

   @Provides
   @IntoSet
   Tracker provideApptentiveTracker(Context context) {
      return new ApptentiveTracker(context);
   }

   @Provides
   @IntoSet
   Tracker provideAdobeTracker(Application application) {
      return new AdobeTracker(application);
   }
}
