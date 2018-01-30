package io.techery.analytics.dependency_sample;

import io.techery.janet.analytics.annotation.ActionPart;
import io.techery.janet.analytics.annotation.AnalyticsEvent;
import io.techery.janet.analytics.annotation.Attribute;
import io.techery.janet.analytics.annotation.AttributeMap;

import java.util.HashMap;
import java.util.Map;

import static io.techery.janet.analytics.annotation.ActionPart.ACTION_PATH_PARAM;

@AnalyticsEvent(actionKey = "user_adopted_pet:" + ACTION_PATH_PARAM, trackerIds = { "tracker0" })
public class PetGiveAwayEvent {

   @ActionPart
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
