package io.techery.analytics.compiler;

import com.google.auto.service.AutoService;
import io.techery.analytics.compiler.model.AnalyticActionClass;
import io.techery.analytics.compiler.model.ValidationError;
import io.techery.janet.analytics.annotation.AnalyticsEvent;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class AnalyticActionProcessor extends AbstractProcessor {

   private Elements elementUtils;
   private Messager messager;
   private ActionHelperGenerator actionHelperGenerator;
   private ActionHelpersFactoryGenerator actionHelpersFactoryGenerator;

   private boolean processed = false;

   private AnalyticActionValidators validators;

   @Override
   public synchronized void init(ProcessingEnvironment processingEnv) {
      super.init(processingEnv);

      elementUtils = processingEnv.getElementUtils();
      messager = processingEnv.getMessager();
      actionHelperGenerator = new ActionHelperGenerator(processingEnv.getFiler());
      actionHelpersFactoryGenerator = new ActionHelpersFactoryGenerator(processingEnv.getFiler());
      validators = new AnalyticActionValidators(elementUtils);
   }

   @Override
   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
      if (processed) return false;
      else processed = true;

      final Set<ValidationError> errors= new HashSet<ValidationError>();
      final List<AnalyticActionClass> actionClasses = new ArrayList<AnalyticActionClass>();
      for (Element element : roundEnv.getElementsAnnotatedWith(AnalyticsEvent.class)) {
         final TypeElement typeElement = (TypeElement) element;
         final AnalyticActionClass actionClass = new AnalyticActionClass(elementUtils, typeElement);
         errors.addAll(validators.validate(actionClass));
         actionClasses.add(actionClass);
      }

      if (!errors.isEmpty()) {
         printErrors(new ArrayList<ValidationError>(errors));
         return true;
      }

      actionHelperGenerator.generate(actionClasses);
      actionHelpersFactoryGenerator.generate(actionClasses);

      return false;
   }

   private void printErrors(List<ValidationError> errors) {
      for (ValidationError error : errors) {
         messager.printMessage(Diagnostic.Kind.ERROR, error.message, error.element);
      }
   }

   @Override
   public SourceVersion getSupportedSourceVersion() {
      return SourceVersion.latestSupported();
   }

   @Override
   public Set<String> getSupportedAnnotationTypes() {
      final Set<String> types = new HashSet<String>();
      types.add(AnalyticsEvent.class.getCanonicalName());
      return types;
   }
}
