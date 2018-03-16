package io.techery.janet.analytics;

import org.jetbrains.annotations.Nullable;

public interface ActionHelperFactory {

   String HELPERS_FACTORY_CLASS_SIMPLE_NAME = ActionHelperFactory.class.getSimpleName() + "Impl";
   String HELPERS_FACTORY_CLASS_PACKAGE = "io.techery.analytics.service";
   String HELPERS_FACTORY_CLASS_NAME = HELPERS_FACTORY_CLASS_PACKAGE + "." + HELPERS_FACTORY_CLASS_SIMPLE_NAME;

   @Nullable
   <T> ActionHelper<T> provideActionHelper(Class<T> actionClass);
}
