package io.techery.analytics.sample;

import io.techery.janet.analytics.annotation.AnalyticsEvent;
import io.techery.janet.analytics.annotation.Attribute;

@AnalyticsEvent(actionKey = "no_action_part_action", trackerIds = {"ClientProjectUsedTracker", "AnotherClientProjectUsedTracker"})
public class ProfileDetailsViewAction {

   @Attribute("foo")
   String bar;

   public ProfileDetailsViewAction(String bar) {
      this.bar = bar;
   }
}
