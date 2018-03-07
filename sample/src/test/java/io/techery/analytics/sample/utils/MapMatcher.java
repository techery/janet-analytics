package io.techery.analytics.sample.utils;

import org.mockito.ArgumentMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapMatcher implements ArgumentMatcher<Map<String, Object>> {

   private final List<MapAssert> args = new ArrayList<>();

   public MapMatcher(List<MapAssert> args) {
      this.args.addAll(args);
   }

   public MapMatcher(MapAssert arg) {
      this(Collections.singletonList(arg));
   }

   public MapMatcher(MapAssert... args) {
      this(Arrays.asList(args));
   }

   public static Builder builder() {
      return new Builder();
   }

   @Override
   public boolean matches(Map<String, Object> argument) {
      for (MapAssert item : args) {
         boolean matches;
         if (item.negativeCheck) {
            matches = !argument.containsKey(item.key);
         } else {
            matches = argument.containsKey(item.key) && argument.get(item.key).equals(item.value);
         }
         if (!matches) return false;
      }

      return true;
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder("{");
      Iterator<MapAssert> iterator = args.iterator();

      while (iterator.hasNext()) {
         sb.append(iterator.next().toString());
         if (iterator.hasNext()) {
            sb.append(", ");
         }
      }

      sb.append("}");

      return sb.toString();
   }

   public static class Builder {

      private final List<MapAssert> args = new ArrayList<>();

      private Builder() {
      }

      public Builder with(String key, Object value) {
         args.add(MapAssert.contains(key, value));
         return this;
      }

      public Builder without(String key) {
         args.add(MapAssert.hasNo(key));
         return this;
      }

      public MapMatcher build() {
         return new MapMatcher(args);
      }
   }
}
