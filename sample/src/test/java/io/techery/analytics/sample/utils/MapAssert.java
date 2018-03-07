package io.techery.analytics.sample.utils;

public class MapAssert {

   public final String key;
   public final Object value;
   public final boolean negativeCheck;

   private MapAssert(String key, Object value, boolean negativeCheck) {
      this.key = key;
      this.value = value;
      this.negativeCheck = negativeCheck;
   }

   public static MapAssert hasNo(String key) {
      return new MapAssert(key, null, true);
   }

   public static MapAssert contains(String key, Object value) {
      return new MapAssert(key, value, false);
   }

   @Override
   public String toString() {
      if (negativeCheck) {
         return "";
      } else {
         return "\"" + key + "\" = \"" + value + "\"";
      }
   }
}
