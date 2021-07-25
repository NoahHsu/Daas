package org.daas.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FilePathNameUtils {
  public static String parsePackage(String packageName) {
    return packageName.replace(".", "/");
  }

  public static String toFielPath(String... folders) {
    return Arrays.stream(folders).collect(Collectors.joining("/"));
  }
}
