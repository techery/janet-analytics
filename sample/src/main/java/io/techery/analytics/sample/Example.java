package io.techery.analytics.sample;

import io.techery.analytics.sample.event.PetBuyEvent;
import io.techery.analytics.sample_common.entity.PetEntity;
import io.techery.analytics.sample_common.entity.PetType;
import io.techery.analytics.service.AnalyticsService;
import io.techery.janet.ActionService;
import io.techery.janet.Janet;
import io.techery.janet.analytics.Tracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Example {

   public static void main(String[] args) {
      new Example().go();
   }

   public void go() {
      ActionService actionService = new AnalyticsService(provideTrackers());
      Janet janet = new Janet.Builder().addService(actionService).build();

      Calendar petBirthDate = Calendar.getInstance();
      petBirthDate.set(Calendar.YEAR, 2015);
      PetEntity pet = new PetEntity(PetType.DOG, "Moohtar", petBirthDate);
      PetBuyEvent event = new PetBuyEvent(pet);

      janet.createPipe(PetBuyEvent.class).send(event);
   }

   private static String printArray(String[] array) {
      StringBuilder stringBuilder = new StringBuilder("[");
      for (String element : array) {
         stringBuilder.append("\"").append(element).append("\", ");
      }
      stringBuilder.append("]");
      return stringBuilder.toString().replace(", ]", "]");
   }

   private List<Tracker> provideTrackers() {
      final List<Tracker> trackers = new ArrayList<>();
      trackers.add(new MyAnalyticsSdkTracker());
      return trackers;
   }
}
