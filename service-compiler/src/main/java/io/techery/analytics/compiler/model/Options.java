package io.techery.analytics.compiler.model;

import java.util.Map;

public class Options {

   private static final String OPTION_MODULE_IS_LIBRARY = "janet.analytics.module.library";

   public final boolean isLibrary;

   public Options(Map<String, String> options) {
      this.isLibrary = Boolean.parseBoolean(options.get(OPTION_MODULE_IS_LIBRARY));
   }
}
