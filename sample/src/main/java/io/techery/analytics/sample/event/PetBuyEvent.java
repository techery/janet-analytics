package io.techery.analytics.sample.event;

import io.techery.analytics.sample.MyAnalyticsSdkTracker;
import io.techery.analytics.sample.model.PetEntity;
import io.techery.janet.analytics.annotation.AnalyticsEvent;
import io.techery.janet.analytics.annotation.Attribute;
import io.techery.janet.analytics.annotation.AttributeMap;
import io.techery.janet.analytics.annotation.KeyPath;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@AnalyticsEvent(actionKey = "user_bought_pet:$pet_type:$store_type", trackerIds = { MyAnalyticsSdkTracker.MYANALYTICSSDK_TRACKER_KEY })
public class PetBuyEvent implements BaseEvent {

   @KeyPath("pet_type")
   String petType;

   @KeyPath("store_type")
   String storeType = "mall";

   @Attribute("pet_birth_date")
   String petBirthDate;

   @AttributeMap
   Map<String, Object> data = new HashMap<>();

   public PetBuyEvent(PetEntity petEntity) {
      petType = petEntity.petType.name().toLowerCase(Locale.US);
      petBirthDate = DateFormat.getDateInstance().format(petEntity.birthDate.getTime());
      data.put("pet_name", petEntity.name);
   }
}
