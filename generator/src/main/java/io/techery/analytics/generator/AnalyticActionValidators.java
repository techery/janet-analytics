package io.techery.analytics.generator;

import io.techery.analytics.generator.model.AnalyticActionClass;
import io.techery.analytics.generator.model.ValidationError;
import io.techery.analytics.generator.validator.AttributesValidator;
import io.techery.analytics.generator.validator.ClassValidator;
import io.techery.analytics.generator.validator.Validator;

import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnalyticActionValidators implements Validator<AnalyticActionClass> {

   private final List<Validator<AnalyticActionClass>> validators = new ArrayList<Validator<AnalyticActionClass>>();

   public AnalyticActionValidators(Elements elementUtils) {
      validators.add(new ClassValidator());
      validators.add(new AttributesValidator(elementUtils));
      // TODO add more validators - see ticket APPCORE-355
   }

   @Override
   public Set<ValidationError> validate(AnalyticActionClass value) {
      final HashSet<ValidationError> errors = new HashSet<ValidationError>();

      for (Validator validator : validators) {
         //noinspection unchecked
         errors.addAll(validator.validate(value));
      }
      return errors;
   }
}
