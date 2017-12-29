package io.techery.analytics.compiler.validator;

import io.techery.analytics.compiler.model.AnalyticActionClass;
import io.techery.analytics.compiler.model.ValidationError;
import io.techery.janet.analytics.annotation.Attribute;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttributesValidator implements Validator<AnalyticActionClass> {

   private final Elements elementUtils;

   public AttributesValidator(Elements elementUtils) {
      this.elementUtils = elementUtils;
   }

   @Override
   public Set<ValidationError> validate(AnalyticActionClass value) {
      final Set<ValidationError> errors = new HashSet<ValidationError>();

      final String annotationName = Attribute.class.getSimpleName();
      final TypeElement typeElement = value.typeElement;
      final List<Element> annotatedElements = new ArrayList<Element>();

      for (Element element : elementUtils.getAllMembers(typeElement)) {
         if (element.getAnnotation(Attribute.class) != null) {
            annotatedElements.add(element);
         }
      }

      for (Element element : annotatedElements) {

         if (element.getKind() != ElementKind.FIELD) {
            errors.add(new ValidationError(element, "Only fields can be annotated with %s! Please, see class %s",
                  annotationName, value.typeName.toString()));
         }

         // TODO does not work correctly with kotlin classes - field is always private, need to check getters
//         if (element.getModifiers().contains(Modifier.PRIVATE)) {
//            errors.add(new ValidationError(element, "Attribute fields cannot have private access! Please, see class %s",
//                  value.typeName.toString()));
//         }
      }

      return errors;
   }
}
