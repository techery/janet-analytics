package io.techery.analytics.sample;

import io.techery.analytics.sample.event.BaseEvent;
import io.techery.analytics.service.AnalyticsService;
import io.techery.janet.ActionPipe;
import io.techery.janet.Janet;
import io.techery.janet.analytics.Tracker;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseTest {

   protected Janet janet;
   protected Tracker tracker;
   protected ActionPipe<BaseEvent> analyticsPipe;

   @Before
   public void setUp() throws Exception {
      tracker = mock(Tracker.class);
      when(tracker.getKey()).thenReturn(MyAnalyticsSdkTracker.MYANALYTICSSDK_TRACKER_KEY);

      janet = new Janet.Builder().addService(new AnalyticsService(provideTrackers())).build();
      analyticsPipe = janet.createPipe(BaseEvent.class);
   }

   protected List<Tracker> provideTrackers() {
      return new ArrayList<Tracker>() {{
         add(tracker);
      }};
   }
}
