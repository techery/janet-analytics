package io.techery.analytics.sample.event;

import io.techery.analytics.sample.SomeAnalyticsTracker;
import io.techery.analytics.sample_common.entity.PetEntity;
import io.techery.analytics.sample_common.janet.action.BaseAnalyticsAction;
import io.techery.janet.analytics.annotation.AnalyticsEvent;
import io.techery.janet.analytics.annotation.Attribute;
import io.techery.janet.analytics.annotation.AttributeMap;
import io.techery.janet.analytics.annotation.KeyPath;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@AnalyticsEvent(actionKey = "user_bought_pet:$pet_type:$store_type", trackerIds = { SomeAnalyticsTracker.ID})
public class BuyPetEvent implements BaseAnalyticsAction {

   @KeyPath("pet_type")
   String petType;

   @KeyPath("store_type")
   String storeType = "mall"; // multiple actionKey params supported

   @Attribute("pet_birth_date")
   String petBirthDate;

   @AttributeMap
   Map<String, Object> data = new HashMap<>();

   public BuyPetEvent(PetEntity petEntity) {
      petType = petEntity.petType.name().toLowerCase(Locale.US);
      petBirthDate = DateFormat.getDateInstance().format(petEntity.birthDate.getTime());
      data.put("pet_name", petEntity.name);
   }
}
