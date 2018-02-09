package io.techery.analytics.sample.android.action;

import io.techery.analytics.sample_common.entity.PetEntity;
import io.techery.analytics.tracker.AdobeTracker;
import io.techery.janet.analytics.annotation.AnalyticsEvent;

@AnalyticsEvent(actionKey = "{$pet_type}_sold", trackerIds = AdobeTracker.KEY)
public class AdobePetSellEvent extends BasePetSellEvent {

   public AdobePetSellEvent(PetEntity pet) {
      super(pet);
   }
}
