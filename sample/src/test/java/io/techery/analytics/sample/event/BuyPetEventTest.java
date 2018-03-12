package io.techery.analytics.sample.event;

import io.techery.analytics.sample.BaseTest;
import io.techery.analytics.sample.utils.MapAssert;
import io.techery.analytics.sample.utils.MapMatcher;
import io.techery.analytics.sample_common.entity.PetEntity;
import io.techery.analytics.sample_common.entity.PetType;
import org.junit.Test;

import java.util.Calendar;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class BuyPetEventTest extends BaseTest {

   @Test
   public void eventSentWithCorrectData() {
      Calendar petBirthDate = Calendar.getInstance();
      petBirthDate.set(2015, 4, 13); // formatted date will be "May 13, 2015"
      PetEntity pet = new PetEntity(PetType.DOG, "Moohtar", petBirthDate);
      BuyPetEvent event = new BuyPetEvent(pet);

      analyticsPipe.send(event);
      verify(tracker).trackEvent(eq("user_bought_pet:dog:mall"),
            argThat(MapMatcher.builder()
                  .with("pet_birth_date", "May 13, 2015")
                  .with("pet_name", "Moohtar")
                  .with("pet_gender", "female")
                  .without("nullable_attribute")
                  .build()
            ));
   }
}
