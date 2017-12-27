package io.techery.analytics.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.techery.analytics.generator.model.AnalyticActionClass;
import io.techery.janet.analytics.ActionHelper;
import io.techery.janet.analytics.AnalyticActionHelperFactory;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.util.Date;
import java.util.List;

import static io.techery.janet.analytics.AnalyticActionHelperFactory.HELPERS_FACTORY_CLASS_PACKAGE;
import static io.techery.janet.analytics.AnalyticActionHelperFactory.HELPERS_FACTORY_CLASS_SIMPLE_NAME;

public class ActionHelpersFactoryGenerator extends CodeGenerator<AnalyticActionClass> {

   public ActionHelpersFactoryGenerator(Filer filer) {
      super(filer);
   }

   @Override
   void generate(List<AnalyticActionClass> classes) {
      final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(HELPERS_FACTORY_CLASS_SIMPLE_NAME)
            .addModifiers(Modifier.FINAL)
            .addJavadoc(formatJavadoc())
            .addAnnotation(AnnotationSpec.builder(Generated.class)
                  .addMember("value", "$S", AnalyticActionProcessor.class.getCanonicalName())
                  .addMember("date","$S", new Date(System.currentTimeMillis()).toString())
                  .build())
            .addSuperinterface(TypeName.get(AnalyticActionHelperFactory.class))
            .addMethod(generateConstructor())
            .addMethod(generateProvideMethod(classes));

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

   private MethodSpec generateConstructor() {
      return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .build();
   }

   private MethodSpec generateProvideMethod(List<AnalyticActionClass> classes) {
      final MethodSpec.Builder builder = MethodSpec.methodBuilder("provideActionHelper")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(Class.class, "actionClass")
            .returns(ActionHelper.class);

      for (AnalyticActionClass actionClass : classes) {
         builder.beginControlFlow("if (actionClass == $T.class)", actionClass.typeElement)
               .addStatement("return new $T()", actionClass.helperName)
               .endControlFlow();
      }
      builder.addStatement("return null");

      return builder.build();
   }
}
