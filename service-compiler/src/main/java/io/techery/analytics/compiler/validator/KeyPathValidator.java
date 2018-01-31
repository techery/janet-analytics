package io.techery.analytics.compiler.validator;

import io.techery.analytics.compiler.model.AnalyticActionClass;
import io.techery.analytics.compiler.model.KeyPathEntity;
import io.techery.analytics.compiler.model.ValidationError;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KeyPathValidator implements Validator<AnalyticActionClass> {

   @Override
   public Set<ValidationError> validate(AnalyticActionClass value) {
      // TODO: implement checking number of placeholders in actionKey and KeyPathEntities
      // TODO: WIP code below
      final Set<KeyPathEntity> keyPathEntities = new HashSet<KeyPathEntity>(value.keyPathEntities);
      for (KeyPathEntity entity : value.keyPathEntities) {
         if (!value.action.contains(entity.annotationValue)) {
            keyPathEntities.remove(entity);
         }
      }

      // TODO: validate redundant actionKey placeholders (placeholders > KeyPath entities);
      // TODO: validate redundant keyPaths (KeyPath entities > placeholders);
      // TODO: validate several identical placeholders
      return Collections.emptySet();
   }
}
