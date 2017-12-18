package io.techery.janet.analytics;

import java.util.Map;

public interface Tracker {

   String getKey();

   void trackEvent(String action, Map<String, Object> data);
}
