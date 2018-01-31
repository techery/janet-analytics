package io.techery.analytics.dependency_sample;

import io.techery.janet.analytics.annotation.ActionPart;
import io.techery.janet.analytics.annotation.AnalyticsEvent;
import io.techery.janet.analytics.annotation.Attribute;
import io.techery.janet.analytics.annotation.AttributeMap;

import java.util.HashMap;
import java.util.Map;

import static io.techery.janet.analytics.annotation.ActionPart.ACTION_PATH_PARAM;

@AnalyticsEvent(actionKey = "user_sold_pet:" + ACTION_PATH_PARAM, trackerIds = { "tracker0" })
public class PetSellEvent {

   @ActionPart
   String petType;

   @Attribute("pet_birth_date")
   String petBirthDate;

   @AttributeMap
   Map<String, Object> data = new HashMap<>();

   public PetSellEvent() {
      petType = "crocodile";
      petBirthDate = "12/01/17";
      data.put("pet_name", "Dundee");
   }
}
