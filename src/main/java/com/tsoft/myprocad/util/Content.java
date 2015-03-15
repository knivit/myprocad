package com.tsoft.myprocad.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Content for files, images
 */
public interface Content extends Serializable {
  /**
   * Returns an input stream to a content.
   * @throws IOException If the input stream can't be opened.
   */
  InputStream openStream() throws IOException;
}
