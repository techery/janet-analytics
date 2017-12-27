package io.techery.analytics.generator;

import io.techery.analytics.generator.model.AttributeEntity;
import io.techery.janet.analytics.annotation.Attribute;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.lang.annotation.Annotation;
import java.util.*;

public class ActionClassUtils {

   public static boolean checkIsKotlinClass(Elements elementUtils, TypeElement typeElement) {
      // simpler check is possible (getAnnotation(kotlin.Metadata.class) != null) but we do not want to add kotlin dependency
      boolean isKotlinClass = false;
      for (AnnotationMirror annotationMirror : elementUtils.getAllAnnotationMirrors(typeElement)) {
         if (((TypeElement) annotationMirror.getAnnotationType().asElement()).getQualifiedName().toString().equals("kotlin.Metadata"))
            isKotlinClass = true;
      }

      return isKotlinClass;
   }

   public static boolean checkHasAnnotatedField(Elements elementUtils, TypeElement typeElement, Class<? extends Annotation> annotationClass) {
      boolean hasAnnotatedField = false;

      for (Element element : elementUtils.getAllMembers(typeElement)) {
         if (element.getKind() == ElementKind.FIELD) {
            if (element.getAnnotation(annotationClass) != null) {
               hasAnnotatedField = true;
            }
         }
      }

      return hasAnnotatedField;
   }

   public static String getAnnotatedFieldName(Elements elementUtils, TypeElement typeElement, Class<? extends Annotation> annotationClass) {
      String fieldName = null;
      for (Element element : elementUtils.getAllMembers(typeElement)) {
         if (element.getKind() == ElementKind.FIELD) {
            if (element.getAnnotation(annotationClass) != null) {
               fieldName = element.getSimpleName().toString();
               break;
            }
         }
      }

      if (fieldName == null) // should never happen - validators go first
         throw new IllegalStateException(String.format(Locale.US, "No field with such annotation: %s", annotationClass.getSimpleName()));

      return resolveAccessibleFieldName(elementUtils, typeElement, fieldName);
   }

   public static Set<String> getAnnotatedFieldNames(Elements elementUtils, TypeElement typeElement, Class<? extends Annotation> annotationClass) {
      Set<String> names = new HashSet<String>();
      for (Element element : elementUtils.getAllMembers(typeElement)) {
         if (element.getKind() == ElementKind.FIELD) {
            if (element.getAnnotation(annotationClass) != null) {
               names.add(resolveAccessibleFieldName(elementUtils, typeElement, element.getSimpleName().toString()));
            }
         }
      }

      return names;
   }

   public static Set<AttributeEntity> getAttributes(Elements elementUtils, TypeElement typeElement) {
      final Set<AttributeEntity> attributes = new HashSet<AttributeEntity>();

      for (Element element : elementUtils.getAllMembers(typeElement)) {
         if (element.getKind() == ElementKind.FIELD) {
            if (element.getAnnotation(Attribute.class) != null) {
               attributes.add(new AttributeEntity(
                     element.getAnnotation(Attribute.class).value(),
                     resolveAccessibleFieldName(elementUtils, typeElement, element.getSimpleName().toString())
               ));
               break;
            }
         }
      }

      return attributes;
   }

   public static String resolveAccessibleFieldName(Elements elementUtils, TypeElement typeElement, String fieldName) {
      if (ActionClassUtils.checkIsKotlinClass(elementUtils, typeElement)) {
         if (fieldName.startsWith("is")) {
            return fieldName;
         } else {
            return String.format(Locale.US, "get%s()", capitalize(fieldName));
         }
      } else {
         return fieldName;
      }
   }

   public static String capitalize(String value) {
      return value.substring(0, 1).toUpperCase(Locale.US) + value.substring(1);
   }

   public static TypeElement getSuperclass(TypeElement typeElement) {
      TypeMirror superTypeMirror = typeElement.getSuperclass();

      if (superTypeMirror instanceof NoType) {
         return null;
      }

      TypeElement superTypeElement =
            (TypeElement) ((DeclaredType) superTypeMirror).asElement();

      if (superTypeElement.getQualifiedName().toString().equals(java.lang.Object.class.getCanonicalName())) {
         return null;
      }

      return superTypeElement;
   }
}
