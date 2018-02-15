package io.techery.analytics.sample.android.action;

import io.techery.analytics.sample_common.entity.PetEntity;
import io.techery.analytics.tracker.ApptentiveTracker;
import io.techery.janet.analytics.annotation.AnalyticsEvent;

@AnalyticsEvent(actionKey = "pet_bought:$pet_type", trackerIds = ApptentiveTracker.KEY)
public class ApptentivePetSellEvent extends BasePetSellEvent {

   public ApptentivePetSellEvent(PetEntity pet) {
      super(pet);
   }
}
