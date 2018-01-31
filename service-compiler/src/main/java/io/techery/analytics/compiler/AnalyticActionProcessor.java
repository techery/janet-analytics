package io.techery.analytics.compiler;

import com.google.auto.service.AutoService;
import io.techery.analytics.compiler.model.AnalyticActionClass;
import io.techery.analytics.compiler.model.Options;
import io.techery.analytics.compiler.model.ValidationError;
import io.techery.janet.analytics.ActionHelperFactory;
import io.techery.janet.analytics.annotation.AnalyticsEvent;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.*;

import static io.techery.janet.analytics.ActionHelperFactory.HELPERS_FACTORY_CLASS_PACKAGE;
import static io.techery.janet.analytics.ActionHelperFactory.HELPERS_FACTORY_CLASS_SIMPLE_NAME;

@AutoService(Processor.class)
public class AnalyticActionProcessor extends AbstractProcessor {

   private Elements elementUtils;
   private Types typesUtil;
   private Messager messager;
   private ActionHelperGenerator actionHelperGenerator;
   private Options options;

   private AnalyticActionValidators validators;
   private final List<AnalyticActionClass> actionClasses = new ArrayList<AnalyticActionClass>();

   private boolean factoryGenerated = false;

   @Override
   public synchronized void init(ProcessingEnvironment processingEnv) {
      super.init(processingEnv);

      options = new Options(processingEnv.getOptions());
      elementUtils = processingEnv.getElementUtils();
      typesUtil = processingEnv.getTypeUtils();
      messager = processingEnv.getMessager();
      actionHelperGenerator = new ActionHelperGenerator(processingEnv.getFiler());
      validators = new AnalyticActionValidators(elementUtils);
   }

   @Override
   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
      final Set<ValidationError> errors= new HashSet<ValidationError>();
      final Set<? extends Element> eventElements = roundEnv.getElementsAnnotatedWith(AnalyticsEvent.class);

      if (!eventElements.isEmpty()) {
         for (Element element : eventElements) {
            final TypeElement typeElement = (TypeElement) element;
            if (helperExists(typeElement)) continue;

            final AnalyticActionClass actionClass = new AnalyticActionClass(elementUtils, typeElement);
            errors.addAll(validators.validate(actionClass));
            actionClasses.add(actionClass);
         }

         if (!errors.isEmpty()) {
            printErrors(new ArrayList<ValidationError>(errors));
            return true;
         }

         actionHelperGenerator.generate(actionClasses);
         return true;
      } else {
         if (actionClasses.isEmpty()) {
            // nothing was provided to generate upon
            return true;
         }

         if (!factoryGenerated) {
            ActionHelpersFactoryGenerator factoryGenerator;
            if (options.isLibrary) {
               factoryGenerator = new ActionHelpersFactoryGenerator(processingEnv.getFiler(), composeNextFactoryName());
            } else {
               factoryGenerator = new ActionHelpersFactoryGenerator(processingEnv.getFiler(), obtainFactoryElements());
            }
            factoryGenerator.generate(actionClasses);
            factoryGenerated = true;
            return true;
         }

         return true;
      }
   }

   /**
    * Suffix hardcoded factory name with last 6 digit of current timestamp in milliseconds
    * + 4 symbols of random UUID
    */
   private String composeNextFactoryName() {
      final String timestamp = String.valueOf(System.currentTimeMillis());
      final String timestampSuffix = timestamp.substring(timestamp.length() - 6);
      final int uuidSuffixStartIndex = Integer.parseInt(timestamp.substring(timestamp.length() - 1));
      final String uuidSuffix = UUID.randomUUID().toString()
            .substring(uuidSuffixStartIndex,uuidSuffixStartIndex + 4).replace("-", "");
      return HELPERS_FACTORY_CLASS_SIMPLE_NAME + timestampSuffix + uuidSuffix;
   }

   private List<String> obtainFactoryElements() {
      final List<String> factoryElementNames = new ArrayList<String>();
      PackageElement packageElement = elementUtils.getPackageElement(HELPERS_FACTORY_CLASS_PACKAGE);
      if (packageElement == null) return Collections.emptyList();

      TypeElement factoryInterfaceElement = elementUtils.getTypeElement(ActionHelperFactory.class.getCanonicalName());
      for (Element packageContentElement : packageElement.getEnclosedElements()) {
         for (TypeMirror typeMirror : ((TypeElement) packageContentElement).getInterfaces()) {
            if (typesUtil.isAssignable(typeMirror, factoryInterfaceElement.asType())) {
               factoryElementNames.add(((TypeElement) packageContentElement).getQualifiedName().toString());
               break;
            }
         }
      }

      return factoryElementNames;
   }

   private boolean helperExists(TypeElement annotatedElement) {
      boolean helperExists = false;

      for (AnalyticActionClass actionClass : actionClasses) {
         helperExists = actionClass.typeElement.getQualifiedName().equals(annotatedElement.getQualifiedName());

         if (helperExists) break;
      }

      return helperExists;
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
