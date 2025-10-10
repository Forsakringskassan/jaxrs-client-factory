package se.fk.github.jaxrsclientfactory;

import org.junit.jupiter.api.Test;

public class JaxrsClientOptionsBuilderTest
{
   @Test
   public void client_ok()
   {
      JaxrsClientOptionsBuilders.createClient("baseUrl", Object.class)
            .header("static-header-1", "static-header-1-value").build();
   }

}
