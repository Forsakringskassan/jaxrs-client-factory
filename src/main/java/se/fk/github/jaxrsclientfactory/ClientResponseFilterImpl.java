package se.fk.github.jaxrsclientfactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

public class ClientResponseFilterImpl implements ClientResponseFilter
{
   private static final Set<MediaType> READABLE_APP_MEDIA_TYPES = new HashSet<>()
   {
      { // NOPMD
         add(MediaType.APPLICATION_ATOM_XML_TYPE);
         add(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
         add(MediaType.APPLICATION_JSON_TYPE);
         add(MediaType.APPLICATION_SVG_XML_TYPE);
         add(MediaType.APPLICATION_XHTML_XML_TYPE);
         add(MediaType.APPLICATION_XML_TYPE);
      }
   };
   private static final Logger LOG = LoggerFactory.getLogger(ClientResponseFilter.class);

   @Override
   public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException
   {
      logRequest(requestContext, responseContext.getStatus());
      logResponse(responseContext, responseContext.getStatus());
   }

   private void logRequest(ClientRequestContext request, int statusCode)
   {
      if (shouldLog(statusCode))
      {
         String message = "FK REST Request: URI={0}, method={1}, headers={2}{3}";
         String uri = request.getUri().toString();
         String method = request.getMethod();
         String headers = formatHeadersObject(request.getHeaders());
         String bodyLog = formatBody(getRequestBody(request));
         Object[] args =
         {
               uri, method, headers, bodyLog
         };
         doLog(statusCode, message, args);
      }
   }

   private void logResponse(ClientResponseContext response, int statusCode) throws IOException
   {
      if (shouldLog(statusCode))
      {
         // Det är enbart möjligt att logga bodyn när det är en BufferedResponse (annars
         // stängs inputstream)
         String responseText = response.getStatusInfo().getReasonPhrase();
         String headers = formatHeaders(response.getHeaders());

         String message = "FK REST Response: status={0}, headers={1}";
         Object[] args =
         {
               responseText, headers
         };
         doLog(statusCode, message, args);
      }
   }

   private String getRequestBody(ClientRequestContext request)
   {
      if (!request.hasEntity())
      {
         return "";
      }
      MediaType mediaType = request.getMediaType();
      if (!isReadable(mediaType))
      {
         return "";
      }
      try
      {
         return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request.getEntity());
      }
      catch (Exception e)
      {
         return request.getEntity().toString();
      }
   }

   /**
    * https://github.com/eclipse-ee4j/jersey/blob/master/core-common/src/main/java/org/glassfish/jersey/logging/LoggingInterceptor.java#L87
    */
   private boolean isReadable(MediaType mediaType)
   {
      if (mediaType == null)
      {
         return true;
      }
      for (MediaType readableMediaType : READABLE_APP_MEDIA_TYPES)
      {
         if (readableMediaType.isCompatible(mediaType))
         {
            return true;
         }
      }
      return false;
   }

   private String formatBody(String body)
   {
      if (body == null || body.isEmpty())
      {
         return "";
      }
      return MessageFormat.format(", body=\n\n{0}\n", body);
   }

   private String formatHeaders(MultivaluedMap<String, String> httpHeaders)
   {
      return httpHeaders.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining(", "));
   }

   private String formatHeadersObject(MultivaluedMap<String, Object> httpHeaders)
   {
      return httpHeaders.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining(", "));
   }

   private boolean shouldLog(int statusCode)
   {
      return LOG.isDebugEnabled() || !is2xx(statusCode);
   }

   private boolean is2xx(int statusCode)
   {
      return statusCode >= 200 && statusCode <= 299;
   }

   private boolean is4xx(int statusCode)
   {
      return statusCode >= 400 && statusCode <= 499;
   }

   private void doLog(int statusCode, String message, Object... args)
   {
      if (is2xx(statusCode))
      {
         LOG.debug(message, args);
         return;
      }

      if (is4xx(statusCode))
      {
         LOG.warn(message, args);
         return;
      }

      LOG.error(message, args);
   }
}
