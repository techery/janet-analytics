package io.techery.janet.analytics;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface Tracker {

   @NotNull
   String id();

   void trackEvent(@NotNull String actionKey, @NotNull Map<String, Object> data);
}
