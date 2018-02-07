package io.techery.analytics.sample_common.janet;

import io.techery.analytics.sample_common.janet.action.BaseAnalyticsAction;
import io.techery.janet.ActionPipe;
import io.techery.janet.Janet;
import rx.schedulers.Schedulers;

public class AnalyticsInteractor {

   private final ActionPipe<BaseAnalyticsAction> analyticsActionPipe;

   public AnalyticsInteractor(Janet janet) {
      analyticsActionPipe = janet.createPipe(BaseAnalyticsAction.class, Schedulers.io());
   }

   public ActionPipe<BaseAnalyticsAction> analyticsActionPipe() {
      return analyticsActionPipe;
   }
}
