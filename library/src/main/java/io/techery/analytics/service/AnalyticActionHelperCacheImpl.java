package io.techery.analytics.service;

import io.techery.janet.JanetInternalException;
import io.techery.janet.analytics.ActionHelper;
import io.techery.janet.analytics.AnalyticActionHelperCache;
import io.techery.janet.analytics.AnalyticActionHelperFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static io.techery.janet.analytics.AnalyticActionHelperFactory.HELPERS_FACTORY_CLASS_NAME;
import static io.techery.janet.analytics.AnalyticActionHelperFactory.HELPERS_FACTORY_CLASS_SIMPLE_NAME;

public class AnalyticActionHelperCacheImpl implements AnalyticActionHelperCache {

   private final AnalyticActionHelperFactory actionHelperFactory;
   private final Map<Class, ActionHelper> helpersCache = new HashMap<Class, ActionHelper>();

   public AnalyticActionHelperCacheImpl() {
      actionHelperFactory = loadActionHelperFactory();
   }

   @Override
   public <T> ActionHelper<T> getActionHelper(Class<T> actionClass) {
      @SuppressWarnings("unchecked")
      ActionHelper<T> helper = helpersCache.get(actionClass);
      if (helper == null) {
         helper = actionHelperFactory.provideActionHelper(actionClass);
         helpersCache.put(actionClass, helper);
      }
      return helper;
   }

   private AnalyticActionHelperFactory loadActionHelperFactory() {
      try {
         @SuppressWarnings("unchecked")
         final Class<AnalyticActionHelperFactory> factoryClass =
               (Class<AnalyticActionHelperFactory>) Class.forName(HELPERS_FACTORY_CLASS_NAME);
         return factoryClass.newInstance();
      } catch (Exception exception) {
         throw new JanetInternalException(String.format(Locale.US,
               "Cannot instantiate %s: generator failed", HELPERS_FACTORY_CLASS_SIMPLE_NAME), exception);
      }
   }
}
