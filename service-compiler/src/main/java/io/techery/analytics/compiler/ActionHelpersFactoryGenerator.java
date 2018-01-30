package io.techery.analytics.compiler;

import com.squareup.javapoet.*;
import io.techery.analytics.compiler.model.AnalyticActionClass;
import io.techery.janet.analytics.ActionHelper;
import io.techery.janet.analytics.AnalyticActionHelperFactory;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.techery.janet.analytics.AnalyticActionHelperFactory.HELPERS_FACTORY_CLASS_PACKAGE;
import static io.techery.janet.analytics.AnalyticActionHelperFactory.HELPERS_FACTORY_CLASS_SIMPLE_NAME;

public class ActionHelpersFactoryGenerator extends CodeGenerator<AnalyticActionClass> {

   private final List<String> factoryClassNames;
   private final String currentFactoryClassName;
   private final boolean shouldIncludeOtherFactories;

   private ActionHelpersFactoryGenerator(Filer filer, List<String> factoryClassNames, String currentFactoryClassName) {
      super(filer);
      this.currentFactoryClassName = currentFactoryClassName;
      this.factoryClassNames = factoryClassNames;
      this.shouldIncludeOtherFactories = factoryClassNames != null && !factoryClassNames.isEmpty();
   }

   public ActionHelpersFactoryGenerator(Filer filer, List<String> factoryClassNames) {
      this(filer, factoryClassNames, HELPERS_FACTORY_CLASS_SIMPLE_NAME);
   }

   public ActionHelpersFactoryGenerator(Filer filer, String currentFactoryClassName) {
      this(filer, new ArrayList<String>(), currentFactoryClassName);
   }

   @Override
   void generate(List<AnalyticActionClass> classes) {
      final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(currentFactoryClassName)
            .addModifiers(Modifier.FINAL)
            .addJavadoc(formatJavadoc())
            .addAnnotation(AnnotationSpec.builder(Generated.class)
                  .addMember("value", "$S", AnalyticActionProcessor.class.getCanonicalName())
                  .addMember("date","$S", new Date(System.currentTimeMillis()).toString())
                  .build())
            .addSuperinterface(TypeName.get(AnalyticActionHelperFactory.class));

      final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC);

      final MethodSpec.Builder provideMethodBuilder = MethodSpec.methodBuilder("provideActionHelper")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(Class.class, "actionClass")
            .returns(ActionHelper.class);

      for (AnalyticActionClass actionClass : classes) {
         provideMethodBuilder.beginControlFlow("if (actionClass == $T.class)", actionClass.typeElement)
               .addStatement("return new $T()", actionClass.helperClassName)
               .endControlFlow();
      }

      if (shouldIncludeOtherFactories) {
         ParameterizedTypeName listType = ParameterizedTypeName.get(ArrayList.class, AnalyticActionHelperFactory.class);
         classBuilder.addField(FieldSpec.builder(listType, "otherFactories", Modifier.PRIVATE)
               .initializer("new $T()", listType)
               .build());

         for (String factoryClassName : factoryClassNames) {
            ClassName className = ClassName.get(HELPERS_FACTORY_CLASS_PACKAGE, factoryClassName);
            constructorBuilder.addStatement("otherFactories.add(new $T())", className);
         }

         provideMethodBuilder.addStatement("$T helper = null", ActionHelper.class);
         provideMethodBuilder.beginControlFlow("for ($T factory : otherFactories)", AnalyticActionHelperFactory.class)
               .addStatement("helper = factory.provideActionHelper(actionClass)")
               .addStatement("if (helper != null) return helper")
               .endControlFlow();
      }

      provideMethodBuilder.addStatement("return null");

      classBuilder.addMethod(constructorBuilder.build());
      classBuilder.addMethod(provideMethodBuilder.build());

      saveClass(HELPERS_FACTORY_CLASS_PACKAGE, classBuilder.build());
   }

   private String formatJavadoc() {
      return new StringBuilder()
            .append("Factory to provide generated helper-classes.\n")
            .append("For {@link AnalyticsService}'s internal use only,\n")
            .append("in case if client code needs to make use of\n")
            .append("helper (e.g. for wrapper service) - use {@link AnalyticActionHelperCacheImpl}\n")
            .toString();
   }
}
