package io.techery.janet.analytics;

public interface AnalyticActionHelperFactory {

   String HELPERS_FACTORY_CLASS_SIMPLE_NAME = AnalyticActionHelperFactory.class.getSimpleName() + "Impl";
   String HELPERS_FACTORY_CLASS_PACKAGE = "io.techery.analytics.service";
   String HELPERS_FACTORY_CLASS_NAME = HELPERS_FACTORY_CLASS_PACKAGE + "." + HELPERS_FACTORY_CLASS_SIMPLE_NAME;

   <T> ActionHelper<T> provideActionHelper(Class<T> actionClass);
}
