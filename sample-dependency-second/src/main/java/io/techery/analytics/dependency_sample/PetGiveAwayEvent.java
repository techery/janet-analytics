package io.techery.analytics.dependency_sample;

import io.techery.janet.analytics.annotation.AnalyticsEvent;
import io.techery.janet.analytics.annotation.Attribute;
import io.techery.janet.analytics.annotation.AttributeMap;
import io.techery.janet.analytics.annotation.KeyPath;

import java.util.HashMap;
import java.util.Map;

@AnalyticsEvent(actionKey = "user_adopted_pet:$pet_type", trackerIds = { "tracker0" })
public class PetGiveAwayEvent {

   @KeyPath("pet_type")
   String petType;

   @Attribute("pet_birth_date")
   String petBirthDate;

   @AttributeMap
   Map<String, Object> data = new HashMap<>();

   public PetGiveAwayEvent() {
      petType = "cat";
      petBirthDate = "13/09/17";
      data.put("pet_name", "Clawy");
   }
}
