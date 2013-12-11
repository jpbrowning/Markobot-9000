

import java.util.HashMap;
import java.util.Map;

/**
 * Just holds a single token string.
 * This class lets you store any other strings and features with this token.
 */
public class AnnotatedToken {
  private String token;
  Map<String,String> annotations;

  public AnnotatedToken(String str) {
    token = str;
  }
  
  
  public String token() {
    return token;
  }
  
  public void add(String type, String val) {
    if( annotations == null )
      annotations = new HashMap<String,String>();
    annotations.put(type, val);
  }

  public String get(String type) {
    if( annotations == null )
      return null;
    else
      return annotations.get(type);
  }
  
  public boolean containsKey(String type) {
    if( annotations == null )
      return false;
    else
      return annotations.containsKey(type);
  }
  
  public String toString() {
    String str = token + " {";
    if( annotations != null ) {
      for( String key : annotations.keySet() )
        str += key + ":" + annotations.get(key) + " ";
    }
    str += "}";
    return str;
  }
}