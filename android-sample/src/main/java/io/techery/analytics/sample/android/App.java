package io.techery.analytics.sample.android;

import android.app.Application;
import io.techery.analytics.sample.android.di.DaggerDiComponent;
import io.techery.analytics.sample.android.di.DiComponent;
import io.techery.analytics.sample.android.di.DiModule;
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
