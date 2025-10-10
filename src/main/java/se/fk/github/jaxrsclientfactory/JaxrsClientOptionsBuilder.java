package se.fk.github.jaxrsclientfactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JaxrsClientOptionsBuilder<T>
{
   private String baseUrl;
   private Class<T> clazz;
   private final Map<String, List<Object>> headers = new TreeMap<>();
   private Integer connectTimeout = 10_000;
   private Integer readTimeout = 10_000;
   private Integer maxPayloadToLog = 10_000;
   private boolean serializeExcludeNull = true;

   public JaxrsClientOptionsBuilder()
   {
   }

   public JaxrsClientOptionsBuilder<T> setBaseUrl(String baseUrl)
   {
      this.baseUrl = baseUrl;
      return this;
   }

   public JaxrsClientOptionsBuilder<T> setClazz(Class<T> clazz)
   {
      this.clazz = clazz;
      return this;
   }

   public JaxrsClientOptionsBuilder<T> header(String key, String value)
   {
      this.headers.put(key, new ArrayList<>());
      this.headers.get(key).add(notNull("value", value));
      return this;
   }

   public JaxrsClientOptionsBuilder<T> setConnectTimeout(Integer connectTimeout)
   {
      this.connectTimeout = connectTimeout;
      return this;
   }

   public JaxrsClientOptionsBuilder<T> setReadTimeout(Integer readTimeout)
   {
      this.readTimeout = readTimeout;
      return this;
   }

   public JaxrsClientOptionsBuilder<T> setMaxPayloadToLog(Integer maxPayloadToLog)
   {
      this.maxPayloadToLog = maxPayloadToLog;
      return this;
   }

   public JaxrsClientOptionsBuilder<T> setSerializeExcludeNull(boolean serializeExcludeNull)
   {
      this.serializeExcludeNull = serializeExcludeNull;
      return this;
   }

   private static <T> T notNull(String msg, T obj)
   {
      if (obj == null)
      {
         throw new IllegalStateException(msg + " cannot be null");
      }
      return obj;
   }

   public JaxrsClientOptions<T> build()
   {
      return new JaxrsClientOptions<>(baseUrl, clazz, headers, notNull("connectTimeout", connectTimeout),
            notNull("readTimeout", readTimeout), notNull("maxPayloadToLog", maxPayloadToLog), serializeExcludeNull);
   }
}
