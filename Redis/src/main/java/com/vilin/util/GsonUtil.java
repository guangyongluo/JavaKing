package com.vilin.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public final class GsonUtil {

  private static Gson gson;

  static {
    gson = new GsonBuilder().serializeNulls().create();
  }

  public static String gsonString(Object object) {
    String gsonString = null;
    if(gson != null){
      gsonString = gson.toJson(object);
    }
    return gsonString;
  }

  public static <T> T gsonToBean(String gsonString, Class<T> cls) {
    T t = null;
    if(gson != null) {
      t = gson.fromJson(gsonString, cls);
    }
    return t;
  }

  public static <T> List<T> gsonToList(String gsonString, Class<T> cls) {
    List<T> list = null;
    if(gson != null) {
      list = gson.fromJson(gsonString, new TypeToken<List<T>>(){}.getType());
    }
    return list;
  }
}
