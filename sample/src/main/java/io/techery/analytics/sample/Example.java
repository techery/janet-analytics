package io.techery.analytics.sample;

import io.techery.analytics.sample.event.BuyPetEvent;
import io.techery.analytics.sample.event.SuccessorEvent;
import io.techery.analytics.sample_common.entity.PetEntity;
import io.techery.analytics.sample_common.entity.PetType;
import io.techery.analytics.sample_common.janet.action.BaseAnalyticsAction;
import io.techery.analytics.service.AnalyticsService;
import io.techery.janet.ActionPipe;
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
      petBirthDate.set(Calendar.YEAR, 2017);
      PetEntity pet = new PetEntity(PetType.DOG, "Moohtar", petBirthDate);
      BuyPetEvent event = new BuyPetEvent(pet);

      janet.createPipe(BuyPetEvent.class).send(event);

      // another way to handle pipes and events for janet:
      ActionPipe<BaseAnalyticsAction> analyticsPipe = janet.createPipe(BaseAnalyticsAction.class);
      analyticsPipe.send(new SuccessorEvent("parent_attribute", "successor_attribute"));
   }

   private List<Tracker> provideTrackers() {
      final List<Tracker> trackers = new ArrayList<>();
      trackers.add(new SomeAnalyticsTracker());
      return trackers;
   }
}
