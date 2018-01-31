package io.techery.analytics.compiler.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import io.techery.analytics.compiler.ActionClassUtils;
import io.techery.janet.analytics.annotation.AnalyticsEvent;
import io.techery.janet.analytics.annotation.AttributeMap;
import io.techery.janet.analytics.annotation.KeyPath;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.HashSet;
import java.util.Set;

import static io.techery.analytics.compiler.ActionHelperGenerator.ACTION_HELPER_SUFFIX;

public final class AnalyticActionClass {

   public final TypeElement typeElement;
   public final TypeName typeName;
   public final String packageName;
   public final ClassName helperName;

   public final String action;
   public final String[] trackerIds;
   public final boolean containsActionPathParam;

   public Set<KeyPathEntity> keyPathEntities = new HashSet<KeyPathEntity>();
   public boolean containsAttributeMap;
   public Set<String> attributeMapAccessorNames = new HashSet<String>();
   public Set<AttributeEntity> attributeEntities = new HashSet<AttributeEntity>();

   public AnalyticActionClass(Elements elementUtils, TypeElement typeElement) {
      this.typeElement = typeElement;

      typeName = TypeName.get(typeElement.asType());
      packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
      helperName = ClassName.get(packageName, typeElement.getSimpleName().toString() + ACTION_HELPER_SUFFIX);

      action = typeElement.getAnnotation(AnalyticsEvent.class).actionKey();
      trackerIds = typeElement.getAnnotation(AnalyticsEvent.class).trackerIds();
      containsActionPathParam = ActionClassUtils.checkHasKeyParams(action);

      obtainAttributes(elementUtils, typeElement);
      containsAttributeMap = !attributeMapAccessorNames.isEmpty();
   }

   private void obtainAttributes(Elements elementUtils, TypeElement typeElement) {
      final TypeElement superTypeElement = ActionClassUtils.getSuperclass(typeElement);
      if (superTypeElement != null) {
         obtainAttributes(elementUtils, superTypeElement);
      }

      if (containsActionPathParam) {
         if (ActionClassUtils.checkHasAnnotatedField(elementUtils, typeElement, KeyPath.class)) {
            keyPathEntities.addAll(ActionClassUtils.getKeyPathEntities(elementUtils, typeElement));
         }
      }
      if (ActionClassUtils.checkHasAnnotatedField(elementUtils, typeElement, AttributeMap.class)) {
         attributeMapAccessorNames.addAll(ActionClassUtils.getAnnotatedFieldNames(elementUtils, typeElement, AttributeMap.class));
      }
      attributeEntities.addAll(ActionClassUtils.getAttributes(elementUtils, typeElement));
   }
}
