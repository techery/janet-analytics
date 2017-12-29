package io.techery.analytics.compiler.model;

import javax.lang.model.element.Element;
import java.util.Locale;

public class ValidationError {

   public final String message;
   public final Element element;

   public ValidationError(String message, Element element) {
      this.message = message;
      this.element = element;
   }

   public ValidationError(Element element, String format, String... args) {
      this(String.format(Locale.US, format, (Object[]) args), element);
   }
}
