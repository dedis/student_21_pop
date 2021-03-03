package com.github.dedis.student20_pop.utility.security;

import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/** SHA256 Hashing Class */
public class Hash {

  public static final String TAG = Hash.class.getSimpleName();

  /**
   * Hash some objects using SHA256. Concatenate the object's string representation following the
   * protocol's directive. Then hash the obtained string
   *
   * @param strs : the objects to hash
   * @return the hashed data or null if failed to hash
   * @throws IllegalArgumentException if the data is null
   */
  public static String hash(String... strs) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      for (String str : strs) {
        String length = Integer.toString(str.length());
        digest.update(length.getBytes(StandardCharsets.UTF_8));
        digest.update(str.getBytes(StandardCharsets.UTF_8));
      }
      byte[] digestBuf = digest.digest();
      return Base64.getEncoder().encodeToString(digestBuf);
    } catch (NoSuchAlgorithmException e) {
      Log.e(TAG, "failed to hash", e);
      return "";
    }
  }
}
