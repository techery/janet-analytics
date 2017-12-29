package io.techery.analytics.compiler.validator;

import io.techery.analytics.compiler.model.ValidationError;

import java.util.Set;

public interface Validator<T> {
   Set<ValidationError> validate(T value);
}
