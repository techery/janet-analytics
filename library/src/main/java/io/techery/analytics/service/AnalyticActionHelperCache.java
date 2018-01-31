package io.techery.analytics.service;

import io.techery.janet.JanetInternalException;
import io.techery.janet.analytics.ActionHelper;
import io.techery.janet.analytics.ActionHelperFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static io.techery.janet.analytics.ActionHelperFactory.HELPERS_FACTORY_CLASS_NAME;
import static io.techery.janet.analytics.ActionHelperFactory.HELPERS_FACTORY_CLASS_SIMPLE_NAME;

public class AnalyticActionHelperCache {

   private static volatile AnalyticActionHelperCache instance = null;

   private final Map<Class, ActionHelper> helpersCache = new HashMap<Class, ActionHelper>();

   private ActionHelperFactory actionHelperFactory;

   public static AnalyticActionHelperCache getInstance() {
      if (instance == null) {
         synchronized (AnalyticActionHelperCache.class) {
            if (instance == null) {
               instance = new AnalyticActionHelperCache();
               instance.loadActionHelperFactory();
            }
         }
      }
      return instance;
   }

   private AnalyticActionHelperCache() {
      if (instance != null) { // reflection call protection
         throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
      }
   }

   public <T> ActionHelper<T> getActionHelper(Class<T> actionClass) {
      @SuppressWarnings("unchecked")
      ActionHelper<T> helper = helpersCache.get(actionClass);
      if (helper == null) {
         helper = actionHelperFactory.provideActionHelper(actionClass);
         helpersCache.put(actionClass, helper);
      }
      return helper;
   }

   private void loadActionHelperFactory() {
      try {
         @SuppressWarnings("unchecked") final Class<ActionHelperFactory> factoryClass =
               (Class<ActionHelperFactory>) Class.forName(HELPERS_FACTORY_CLASS_NAME);
         actionHelperFactory = factoryClass.newInstance();
      } catch (Exception exception) {
         throw new JanetInternalException(String.format(Locale.US,
               "Cannot instantiate %s: service-compiler failed", HELPERS_FACTORY_CLASS_SIMPLE_NAME), exception);
      }
   }
}
