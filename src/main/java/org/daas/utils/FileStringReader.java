package org.daas.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import com.google.common.io.CharStreams;

public class FileStringReader {
  public static String readFileString(String filePath) throws IOException {
    String text = "";
    FileInputStream fileIStream = null;

    try {
      fileIStream = new FileInputStream(filePath);
      final Reader reader = new InputStreamReader(fileIStream);
      text = CharStreams.toString(reader);
    } finally {
      fileIStream.close();
    }

    return text;
  }

  public static byte[] readFileBytes(String filePath) throws IOException {
    return readFileString(filePath).getBytes();
  }


}
