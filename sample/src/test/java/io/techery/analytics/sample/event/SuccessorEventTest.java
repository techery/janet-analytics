package io.techery.analytics.sample.event;

import org.junit.Test;

import io.techery.analytics.sample.BaseTest;
import io.techery.analytics.sample.utils.MapMatcher;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class SuccessorEventTest extends BaseTest {

   @Test
   public void successorEventSentWithCorrectData() {
      final String parentAttribute = "PARENT";
      final String successorAttribute = "SUCCESSOR";
      SuccessorEvent event = new SuccessorEvent(parentAttribute, successorAttribute);

      analyticsPipe.send(event);
      verify(tracker).trackEvent(eq("event_with_superclass:SuccessorEvent"),
              argThat(MapMatcher.builder()
                    .with("successor_attribute", successorAttribute)
                    .with("attribute_from_parent", parentAttribute)
                    .build()));
   }
}
