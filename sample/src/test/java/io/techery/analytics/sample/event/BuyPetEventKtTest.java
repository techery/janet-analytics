package io.techery.analytics.sample.event;

import org.junit.Test;

import java.util.Calendar;

import io.techery.analytics.sample.BaseTest;
import io.techery.analytics.sample.utils.MapMatcher;
import io.techery.analytics.sample_common.entity.PetEntity;
import io.techery.analytics.sample_common.entity.PetType;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class BuyPetEventKtTest extends BaseTest {

   @Test
   public void eventKotlinSentWithCorrectData() {
      Calendar petBirthDate = Calendar.getInstance();
      petBirthDate.set(2015, 4, 13); // formatted date will be "May 13, 2015"
      PetEntity pet = new PetEntity(PetType.DOG, "Moohtar", petBirthDate);
      BuyPetEventKt event = new BuyPetEventKt(pet);

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
