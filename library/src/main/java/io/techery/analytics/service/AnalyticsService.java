package io.techery.analytics.service;

import io.techery.janet.ActionHolder;
import io.techery.janet.ActionService;
import io.techery.janet.JanetException;
import io.techery.janet.analytics.ActionHelper;
import io.techery.janet.analytics.AnalyticActionHelperCache;
import io.techery.janet.analytics.Tracker;
import io.techery.janet.analytics.annotation.AnalyticsEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AnalyticsService extends ActionService {

   private final Map<String, Tracker> trackersMap = new HashMap<>();
   private final AnalyticActionHelperCache actionHelperCache;

   public AnalyticsService(List<Tracker> trackers) {
      this(trackers, new AnalyticActionHelperCacheImpl());
   }

   public AnalyticsService(List<Tracker> trackers, AnalyticActionHelperCache actionHelperCache) {
      for (Tracker tracker : trackers) {
         trackersMap.put(tracker.getKey(), tracker);
      }
      this.actionHelperCache = actionHelperCache;
   }

   @Override
   protected <A> void sendInternal(ActionHolder<A> holder) throws JanetException {
      try {
         final A action = holder.action();
//         final ActionHelper<A> actionHelper = actionHelperCache.getActionHelper(action.getClass());
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
