package io.techery.analytics.sample.event;

import io.techery.analytics.sample.SomeAnalyticsTracker;
import io.techery.janet.analytics.annotation.AnalyticsEvent;
import io.techery.janet.analytics.annotation.Attribute;

@AnalyticsEvent(actionKey = "event_with_superclass:$superclass_name", trackerIds = { SomeAnalyticsTracker.ID })
public class SuccessorEvent extends BaseKotlinEvent {

    @Attribute("successor_attribute")
    final String successorAttribute;

    public SuccessorEvent(String attributeFromParent, String successorAttribute) {
        super(attributeFromParent);
        this.successorAttribute = successorAttribute;
    }
}
