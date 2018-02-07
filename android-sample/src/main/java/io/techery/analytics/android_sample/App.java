package io.techery.analytics.android_sample;

import android.app.Application;
import io.techery.analytics.android_sample.di.DaggerDiComponent;
import io.techery.analytics.android_sample.di.DiComponent;
import io.techery.analytics.android_sample.di.DiModule;
import io.techery.analytics.tracker.ActivityLifecycleCallback;

import javax.inject.Inject;

public class App extends Application {

   private DiComponent diComponent;

   @Inject
   ActivityLifecycleCallback lifecycleCallback;

   @Override
   public void onCreate() {
      super.onCreate();

      diComponent = DaggerDiComponent.builder()
            .diModule(new DiModule(getApplicationContext()))
            .build();
      diComponent.inject(this);

      registerActivityLifecycleCallbacks(lifecycleCallback);
   }

   public DiComponent diComponent() {
      return diComponent;
   }
}
