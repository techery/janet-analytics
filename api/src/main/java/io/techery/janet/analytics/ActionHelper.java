package io.techery.janet.analytics;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ActionHelper<T> {

   @NotNull
   String getAction(T action);

   @NotNull
   Map<String, Object> getData(T action);

   @NotNull
   String[] getTrackerIds();
}
