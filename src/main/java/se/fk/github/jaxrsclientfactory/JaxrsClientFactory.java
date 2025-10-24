package se.fk.github.jaxrsclientfactory;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.logging.LoggingFeature;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

public class JaxrsClientFactory
{

   public JaxrsClientFactory()
   {
   }

   public <T> T create(JaxrsClientOptions<T> options)
   {
      try
      {
         ClientConfig configuration = new ClientConfig()
               .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
               .register(new ObjectMapperContextResolver(options)).register(new ClientResponseFilterImpl())
               .register(new DefaultHeadersRequestClientFilter())
               .register(new ExtraHeadersRequestClientFilter(options.getHeaders()))
               .property(ClientProperties.CONNECT_TIMEOUT, options.getConnectTimeout())
               .property(ClientProperties.READ_TIMEOUT, options.getReadTimeout())
               .property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

         Client client = ClientBuilder.newClient(configuration); // NOPMD
         WebTarget webTarget = client.target(options.getBaseUrl());
         return WebResourceFactory.newResource(options.getClazz(), webTarget);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Kunde inte skapa klient med: " + options, e);
      }
   }
}
