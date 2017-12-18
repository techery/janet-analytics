package io.techery.analytics.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.List;

public abstract class CodeGenerator<T> {

   private final Filer filer;

   public CodeGenerator(Filer filer) {
      this.filer = filer;
   }

   abstract void generate(List<T> classes);

   protected void saveClass(String packageName, TypeSpec typeSpec) {
      try {
         JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
      } catch (IOException exception) {
         exception.printStackTrace();
         throw new RuntimeException(exception.getMessage());
      }
   }
}
