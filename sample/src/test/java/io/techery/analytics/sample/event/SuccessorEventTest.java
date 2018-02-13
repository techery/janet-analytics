package io.techery.analytics.sample.event;

import org.junit.Test;

import io.techery.analytics.sample.BaseTest;

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
              argThat(argument ->
                      argument.containsKey("successor_attribute") &&
                              argument.get("successor_attribute").equals("SUCCESSOR") &&
                              argument.containsKey("attribute_from_parent") &&
                              argument.get("attribute_from_parent").equals("PARENT")
              ));
   }
}
