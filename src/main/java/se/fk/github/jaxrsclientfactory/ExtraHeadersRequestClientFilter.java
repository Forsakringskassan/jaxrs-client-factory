package se.fk.github.jaxrsclientfactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;

public class ExtraHeadersRequestClientFilter implements ClientRequestFilter
{
   private final Map<String, List<Object>> headers;

   public ExtraHeadersRequestClientFilter(Map<String, List<Object>> headers)
   {
      this.headers = headers;
   }

   @Override
   public void filter(ClientRequestContext requestContext) throws IOException
   {
      for (Entry<String, List<Object>> h : headers.entrySet())
      {
         for (Object value : h.getValue())
         {
            requestContext.getHeaders().add(h.getKey(), value);
         }
      }
   }
}
