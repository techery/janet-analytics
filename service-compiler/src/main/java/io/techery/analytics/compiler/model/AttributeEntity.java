package io.techery.analytics.compiler.model;

public class AttributeEntity {

   public final String attributeKey;
   public final String attributeAccessibleFieldName;

   public AttributeEntity(String attributeKey, String attributeAccessibleFieldName) {
      this.attributeKey = attributeKey;
      this.attributeAccessibleFieldName = attributeAccessibleFieldName;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof AttributeEntity)) return false;

      final AttributeEntity other = (AttributeEntity) obj;
      return this.attributeKey.equals(other.attributeKey);
   }

   @Override
   public int hashCode() {
      return this.attributeKey.hashCode();
   }
}
