package io.techery.analytics.compiler.model;

public class KeyPathEntity {

   public final String annotationValue;
   public final String fieldAccessibleName;

   public KeyPathEntity(String annotationValue, String fieldAccessibleName) {
      this.annotationValue = annotationValue;
      this.fieldAccessibleName = fieldAccessibleName;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof KeyPathEntity)) return false;

      final KeyPathEntity other = (KeyPathEntity) obj;
      return this.annotationValue.equals(other.annotationValue);
   }

   @Override
   public int hashCode() {
      return this.annotationValue.hashCode();
   }
}
