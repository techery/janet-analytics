package io.techery.janet.analytics;

import java.util.Map;

public interface Tracker {

   String id();

   void trackEvent(String actionKey, Map<String, Object> data);
}
