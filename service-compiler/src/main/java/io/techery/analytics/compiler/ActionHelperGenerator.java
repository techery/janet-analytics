package io.techery.analytics.compiler;

import com.squareup.javapoet.*;
import io.techery.analytics.compiler.model.AnalyticActionClass;
import io.techery.analytics.compiler.model.AttributeEntity;
import io.techery.analytics.compiler.model.KeyPathEntity;
import io.techery.janet.analytics.ActionHelper;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import java.util.*;

public class ActionHelperGenerator extends CodeGenerator<AnalyticActionClass> {

   public static final String ACTION_HELPER_SUFFIX = "Helper";

   private static final String GENERATED_ACTION_KEY_VAR_NAME = "formattedActionKey";

   public ActionHelperGenerator(Filer filer) {
      super(filer);
   }

   @Override
   void generate(List<AnalyticActionClass> classes) {
      for (AnalyticActionClass actionClass : classes) {
         generateHelperClass(actionClass);
      }
   }

   private void generateHelperClass(AnalyticActionClass actionClass) {
      final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(actionClass.helperClassName.simpleName())
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addAnnotation(AnnotationSpec.builder(Generated.class)
                  .addMember("value", "$S", AnalyticActionProcessor.class.getCanonicalName())
                  .addMember("date","$S", new Date(System.currentTimeMillis()).toString())
                  .build())
            .addJavadoc(formatJavadoc(actionClass))
            .addTypeVariables(getTypeVariables(actionClass.typeElement))
            .addSuperinterface(ParameterizedTypeName.get(
                  ClassName.get(ActionHelper.class), actionClass.typeName
                ))
            .addMethod(generateGetActionMethod(actionClass))
            .addMethod(generateGetDataMethod(actionClass))
            .addMethod(generateGetTrackerIdsMethod(actionClass));

      saveClass(actionClass.packageName, classBuilder.build());
   }

   private String formatJavadoc(AnalyticActionClass actionClass) {
      return new StringBuilder()
            .append(String.format(Locale.US, "Helper class generated based on %s\n", actionClass.typeName.toString()))
            .append("Utilizes analytic event class content in a format, suitable for {@link AnalyticsService}\n")
            .toString();
   }

   private Iterable<TypeVariableName> getTypeVariables(TypeElement typeElement) {
      final ArrayList<TypeVariableName> typeVariableNames = new ArrayList<TypeVariableName>();

      for (TypeParameterElement parameterElement: typeElement.getTypeParameters()) {
         typeVariableNames.add((TypeVariableName) TypeVariableName.get(parameterElement.asType()));
      }

      return typeVariableNames;
   }

   private MethodSpec generateGetActionMethod(AnalyticActionClass actionClass) {
      return MethodSpec.methodBuilder("getAction")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(actionClass.typeName, "action")
            .returns(String.class)
            .addCode(makeGetActionCodeBlock(actionClass))
            .build();
   }

   private CodeBlock makeGetActionCodeBlock(AnalyticActionClass actionClass) {
      final CodeBlock.Builder builder = CodeBlock.builder();

      if (actionClass.containsActionPathParam) {
         if (actionClass.keyPathEntities.size() == 1) {
            KeyPathEntity keyPath = actionClass.keyPathEntities.iterator().next();
            builder.addStatement("return $S.replace($S, action.$L)", actionClass.action, keyPath.annotationValue, keyPath.fieldAccessibleName);
         } else {
            builder.addStatement("$T $L = $S", String.class, GENERATED_ACTION_KEY_VAR_NAME, actionClass.action);
            for (KeyPathEntity keyPathEntity : actionClass.keyPathEntities) {
               builder.addStatement("$L = $L.replace($S, action.$L)",
                     GENERATED_ACTION_KEY_VAR_NAME,
                     GENERATED_ACTION_KEY_VAR_NAME,
                     keyPathEntity.annotationValue,
                     keyPathEntity.fieldAccessibleName);
            }
            builder.addStatement("return $L", GENERATED_ACTION_KEY_VAR_NAME);
         }
      } else {
         builder.addStatement("return $S", actionClass.action);
      }
      return builder.build();
   }

   private MethodSpec generateGetDataMethod(AnalyticActionClass actionClass) {
      return MethodSpec.methodBuilder("getData")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(actionClass.typeName, "action")
            .returns(ParameterizedTypeName.get(
                  ClassName.get(Map.class),
                  ClassName.get(String.class),
                  ClassName.get(Object.class)))
            .addCode(makeDataMapCodeBlock(actionClass))
            .build();
   }

   private CodeBlock makeDataMapCodeBlock(AnalyticActionClass actionClass) {
      final CodeBlock.Builder builder = CodeBlock.builder()
       .addStatement("$T<$T, $T> data = new $T<>()", HashMap.class, String.class, Object.class, HashMap.class);

      if (actionClass.containsAttributeMap) {
         builder.addStatement("//noinspection CollectionAddAllCanBeReplacedWithConstructor");
         for (String mapAccessibleName : actionClass.attributeMapAccessorNames) {
            builder.addStatement("data.putAll(action.$L)", mapAccessibleName);
         }
      }

      for (AttributeEntity attributeEntity : actionClass.attributeEntities) {
         builder.addStatement("data.put($S, action.$L)", attributeEntity.attributeKey, attributeEntity.attributeAccessibleFieldName);
      }

      builder.addStatement("return data");

      return builder.build();
   }

   private MethodSpec generateGetTrackerIdsMethod(AnalyticActionClass actionClass) {
      return MethodSpec.methodBuilder("getTrackerIds")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(ArrayTypeName.of(ClassName.get(String.class)))
            .addCode(makeStringArrayCodeBlock(actionClass))
            .build();
   }

   private CodeBlock makeStringArrayCodeBlock(AnalyticActionClass actionClass) {
      final String[] trackerIds = actionClass.trackerIds;
      final int trackerIdsSize = trackerIds.length;
      final CodeBlock.Builder builder = CodeBlock.builder()
            .addStatement("$T[] trackerIds = new $T[$L]", String.class, String.class, trackerIdsSize);

      for (int i = 0; i < trackerIdsSize; i++) {
         builder.addStatement("trackerIds[$L] = $S", i, trackerIds[i]);
      }

      builder.addStatement("return trackerIds");

      return builder.build();
   }
}
