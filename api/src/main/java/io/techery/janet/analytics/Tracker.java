package io.techery.janet.analytics;

import java.util.Map;

public interface Tracker {

   String getKey(); // TODO: rename to id()

   void trackEvent(String action, Map<String, Object> data);
}
