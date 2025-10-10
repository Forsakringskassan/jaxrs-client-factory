package se.fk.github.jaxrsclientfactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JaxrsClientOptions<T>
{
   public static final String HEADER_X_REQUESTED_WITH = "X-Requested-With";

   private final String baseUrl;
   private final Class<T> clazz;
   private final Map<String, List<Object>> headers;
   private Integer connectTimeout = 10_000;
   private Integer readTimeout = 10_000;
   private Integer maxPayloadToLog = 10_000;
   private boolean serializeExcludeNull = true;

   public JaxrsClientOptions(String baseUrl, Class<T> clazz)
   {
      this.baseUrl = notNull("baseUrl", baseUrl);
      this.clazz = notNull("class", clazz);
      this.headers = new TreeMap<>();
   }

   JaxrsClientOptions(String baseUrl, Class<T> clazz, Map<String, List<Object>> headers, Integer connectTimeout,
         Integer readTimeout, Integer maxPayloadToLog, boolean serializeExcludeNull)
   {
      this.baseUrl = baseUrl;
      this.clazz = clazz;
      this.headers = headers;
      this.connectTimeout = connectTimeout;
      this.readTimeout = readTimeout;
      this.maxPayloadToLog = maxPayloadToLog;
      this.serializeExcludeNull = serializeExcludeNull;
   }

   public JaxrsClientOptions<T> header(String key, String value)
   {
      this.headers.put(key, new ArrayList<>());
      this.headers.get(key).add(notNull("value", value));
      return this;
   }

   public JaxrsClientOptions<T> connectTimeout(Integer connectTimeout)
   {
      this.connectTimeout = notNull("connectTimeout", connectTimeout);
      return this;
   }

   public JaxrsClientOptions<T> readTimeout(Integer readTimeout)
   {
      this.readTimeout = notNull("readTimeout", readTimeout);
      return this;
   }

   public JaxrsClientOptions<T> maxPayloadToLog(Integer maxPayloadToLog)
   {
      this.maxPayloadToLog = notNull("maxPayloadToLog", maxPayloadToLog);
      return this;
   }

   public JaxrsClientOptions<T> serializeExcludeNull(boolean serializeExcludeNull)
   {
      this.serializeExcludeNull = serializeExcludeNull;
      return this;
   }

   public boolean isSerializeExcludeNull()
   {
      return serializeExcludeNull;
   }

   public String getBaseUrl()
   {
      return baseUrl;
   }

   public Class<T> getClazz()
   {
      return clazz;
   }

   public Map<String, List<Object>> getHeaders()
   {
      return headers;
   }

   public Integer getConnectTimeout()
   {
      return connectTimeout;
   }

   public Integer getReadTimeout()
   {
      return readTimeout;
   }

   public Integer getMaxPayloadToLog()
   {
      return maxPayloadToLog;
   }

   private <T> T notNull(String msg, T obj)
   {
      if (obj == null)
      {
         throw new IllegalStateException(msg + " cannot be null");
      }
      return obj;
   }

   @Override
   public String toString()
   {
      return "JaxrsClientOptions [baseUrl=" + baseUrl + ", clazz=" + clazz + ", headers=" + headers
            + ", connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout + ", maxPayloadToLog="
            + maxPayloadToLog + "]";
   }

}
