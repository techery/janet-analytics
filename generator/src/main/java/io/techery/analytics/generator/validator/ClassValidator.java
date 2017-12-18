package io.techery.analytics.generator.validator;

import io.techery.analytics.generator.model.AnalyticActionClass;
import io.techery.analytics.generator.model.ValidationError;
import io.techery.janet.analytics.annotation.AnalyticsEvent;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ClassValidator implements Validator<AnalyticActionClass> {

   @Override
   public Set<ValidationError> validate(AnalyticActionClass value) {
      final Set<ValidationError> errors = new HashSet<>();
      final TypeElement typeElement = value.typeElement;

      if (typeElement.getKind() != ElementKind.CLASS) {
         errors.add(new ValidationError(
               String.format(Locale.US, "Only classes should be annotated with %s", AnalyticsEvent.class.getSimpleName()),
               typeElement));
      }

      final String annotatedClassName = typeElement.getQualifiedName().toString();

      if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
         errors.add(new ValidationError(
               String.format(Locale.US, "Class %s should be public", annotatedClassName),
               typeElement
         ));
      }

      if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
         errors.add(new ValidationError(
               String.format(Locale.US, "Class %s should not be abstract", annotatedClassName),
               typeElement
         ));
      }

      // TODO validate parent classes - no annotation, abstract, etc

      return errors;
   }
}
