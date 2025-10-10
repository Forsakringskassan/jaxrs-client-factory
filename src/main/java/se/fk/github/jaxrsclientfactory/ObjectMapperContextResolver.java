package se.fk.github.jaxrsclientfactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.json.JsonMapper.Builder;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.ws.rs.ext.ContextResolver;

public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper>
{

   private final ObjectMapper mapper;

   public ObjectMapperContextResolver(JaxrsClientOptions<?> options)
   {
      Builder b = JsonMapper.builder();
      if (options.isSerializeExcludeNull())
      {
         b.serializationInclusion(Include.NON_NULL);
         b.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
      }
      b.enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION);
      b.addModule(new JavaTimeModule());
      b.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      this.mapper = b.build();
   }

   @Override
   public ObjectMapper getContext(Class<?> type)
   {
      return mapper;
   }

}
