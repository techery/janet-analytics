package io.techery.analytics.service;

import io.techery.janet.ActionHolder;
import io.techery.janet.ActionService;
import io.techery.janet.JanetException;
import io.techery.janet.analytics.ActionHelper;
import io.techery.janet.analytics.Tracker;
import io.techery.janet.analytics.annotation.AnalyticsEvent;

import java.util.*;

public class AnalyticsService extends ActionService {

   private final Map<String, Tracker> trackersMap = new HashMap<String, Tracker>();
   private final AnalyticActionHelperCache actionHelperCache = AnalyticActionHelperCache.getInstance();

   public AnalyticsService(Collection<Tracker> trackers) {
      for (Tracker tracker : trackers) {
         trackersMap.put(tracker.id(), tracker);
      }
   }

   @Override
   protected <A> void sendInternal(ActionHolder<A> holder) throws JanetException {
      try {
         final A action = holder.action();
         final ActionHelper actionHelper = actionHelperCache.getActionHelper(action.getClass());

         for (String trackerId : actionHelper.getTrackerIds()) {
            final Tracker tracker = trackersMap.get(trackerId);
            if (tracker == null) {
               throw new IllegalArgumentException(String.format(Locale.US, "Unsupported tracker type: %s", trackerId));
            }
            tracker.trackEvent(actionHelper.getAction(action), actionHelper.getData(action));
         }
      } catch (Exception e) {
         throw new AnalyticsServiceException(e);
      }
   }

   @Override
   protected Class getSupportedAnnotationType() {
      return AnalyticsEvent.class;
   }

   @Override
   protected <A> void cancel(ActionHolder<A> holder) {
      // effectively do nothing here
   }
}
