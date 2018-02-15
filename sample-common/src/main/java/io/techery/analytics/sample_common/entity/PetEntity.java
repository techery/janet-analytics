package io.techery.analytics.sample_common.entity;

import java.util.Calendar;

public class PetEntity {

   public final PetType petType;
   public final String name;
   public final Calendar birthDate;

   public PetEntity(PetType petType, String name, Calendar birthDate) {
      this.petType = petType;
      this.name = name;
      this.birthDate = birthDate;
   }
}
