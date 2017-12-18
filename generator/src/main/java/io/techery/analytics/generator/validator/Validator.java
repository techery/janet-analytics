package io.techery.analytics.generator.validator;

import io.techery.analytics.generator.model.ValidationError;

import java.util.Set;

public interface Validator<T> {
   Set<ValidationError> validate(T value);
}
