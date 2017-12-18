package io.techery.janet.analytics;

public interface AnalyticActionHelperCache {

   <T> ActionHelper<T> getActionHelper(Class<T> actionClass);
}
