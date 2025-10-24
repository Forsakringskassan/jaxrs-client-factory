package se.fk.github.jaxrsclientfactory;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;

import java.io.IOException;

import org.slf4j.MDC;
import se.fk.github.logging.callerinfo.model.HeaderTyp;
import se.fk.github.logging.callerinfo.model.MDCKeys;

public class DefaultHeadersRequestClientFilter implements ClientRequestFilter
{
   public DefaultHeadersRequestClientFilter()
   {
   }

   @Override
   public void filter(ClientRequestContext requestContext) throws IOException
   {
      requestContext.getHeaders()
            .add(HeaderTyp.BREADCRUMB_ID.value(), MDC.get(MDCKeys.BREADCRUMBID.name()));
      requestContext.getHeaders()
            .add(HeaderTyp.PROCESSID.value(), MDC.get(MDCKeys.PROCESSID.name()));
   }
}
