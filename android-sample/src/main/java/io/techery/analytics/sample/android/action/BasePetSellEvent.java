package io.techery.analytics.sample.android.action;

import io.techery.analytics.sample_common.entity.PetEntity;
import io.techery.analytics.sample_common.janet.action.BaseAnalyticsAction;
import io.techery.janet.analytics.annotation.Attribute;
import io.techery.janet.analytics.annotation.KeyPath;

import java.text.DateFormat;
import java.util.Locale;

public class BasePetSellEvent implements BaseAnalyticsAction {

   @KeyPath("pet_type")
   String petType;

   @Attribute("pet_name")
   String name;

   @Attribute("pet_birthdate")
   String birthDate;

   public BasePetSellEvent(PetEntity pet) {
      this.petType = pet.petType.name().toLowerCase(Locale.US);
      this.name = pet.name;
      this.birthDate = DateFormat.getDateInstance().format(pet.birthDate.getTime());
   }
}
