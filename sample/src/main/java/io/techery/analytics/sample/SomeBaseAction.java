package io.techery.analytics.sample;

import io.techery.janet.analytics.annotation.Attribute;
import io.techery.janet.analytics.annotation.AttributeMap;

import java.util.Map;

public abstract class SomeBaseAction extends SomeVeryBasicViewAction {

   @Attribute("attrib_from_base_action") String baseClassAttribValName;

   @AttributeMap
   Map<String, Object> lowerMap;

   public SomeBaseAction(String baseClassAttribValName) {
      this.baseClassAttribValName = baseClassAttribValName;
      lowerMap.put("1", "2");
   }
}
