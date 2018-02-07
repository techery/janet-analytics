package io.techery.analytics.android_sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.techery.analytics.android_sample.janet.action.AdobePetSellEvent;
import io.techery.analytics.android_sample.janet.action.ApptentivePetSellEvent;
import io.techery.analytics.androidsample.R;
import io.techery.analytics.sample_common.entity.PetEntity;
import io.techery.analytics.sample_common.entity.PetType;
import io.techery.analytics.sample_common.janet.AnalyticsInteractor;

import javax.inject.Inject;
import java.util.Calendar;

public class MainActivity extends Activity {

   @Inject
   AnalyticsInteractor analyticsInteractor;

   @BindView(R.id.pet_name)
   EditText petNameView;

   @BindView(R.id.pet_type_radio_group)
   RadioGroup petTypeView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      ButterKnife.bind(this);

      ((App) getApplication()).diComponent().inject(this);
   }

   @OnClick(R.id.button)
   void buttonClicked() {
      final String petName = petNameView.getText().toString();
      PetType petType;
      switch (petTypeView.getCheckedRadioButtonId()) {
         case R.id.radio_type_cat: petType = PetType.CAT; break;
         case R.id.radio_type_dog: petType = PetType.CAT; break;
         default: petType = PetType.UNKNOWN;
      }
      Calendar petBirthDate = Calendar.getInstance();
      petBirthDate.set(Calendar.YEAR, 2017);

      PetEntity pet = new PetEntity(petType, petName, petBirthDate);
      analyticsInteractor.analyticsActionPipe().send(new AdobePetSellEvent(pet));
//      analyticsInteractor.analyticsActionPipe()
//            .createObservable(new AdobePetSellEvent(pet))
//      .subscribe(new Subscriber<ActionState<BaseAnalyticsAction>>() {
//         @Override
//         public void onCompleted() {
//            // do nothing
//         }
//
//         @Override
//         public void onError(Throwable e) {
//            // this is highly expected because none of analytic SDK are properly set
//            // do nothing
//         }
//
//         @Override
//         public void onNext(ActionState<BaseAnalyticsAction> baseAnalyticsActionActionState) {
//            // do nothing
//         }
//      });
      analyticsInteractor.analyticsActionPipe().send(new ApptentivePetSellEvent(pet));
   }
}
