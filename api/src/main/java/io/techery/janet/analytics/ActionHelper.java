package io.techery.janet.analytics;

import java.util.Map;

public interface ActionHelper<T> {

   String getAction(T action);
   Map<String, Object> getData(T action);
   String[] getTrackerIds();
}
