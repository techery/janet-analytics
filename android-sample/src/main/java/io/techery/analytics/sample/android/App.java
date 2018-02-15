package io.techery.analytics.sample.android;

import android.app.Application;
import io.techery.analytics.sample.android.di.DaggerDiComponent;
import io.techery.analytics.sample.android.di.DiComponent;
import io.techery.analytics.sample.android.di.DiModule;

public class App extends Application {

   private DiComponent diComponent;

   @Override
   public void onCreate() {
      super.onCreate();

      diComponent = DaggerDiComponent.builder()
            .diModule(new DiModule(this))
            .build();
   }

   public DiComponent diComponent() {
      return diComponent;
   }
}
