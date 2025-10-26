package se.fk.github.jaxrsclientfactory;

import org.junit.jupiter.api.AfterEach;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.MDC;

public abstract class IntegrationTestBase
{
   protected WhateverApi sut;
   protected WireMockServer server;
   protected final WhateverVO entity = new WhateverVO("the attribute value");
   protected String baseUrl;

   protected void givenWiremock(StubMapping anyRequestMapping)
   {
      WireMockConfiguration config = new WireMockConfiguration().notifier(new Slf4jNotifier(true)).dynamicPort();
      server = new WireMockServer(config);
      server.addStubMapping(anyRequestMapping);
      server.start();
      WireMock.configureFor(server.port());

      baseUrl = "http://localhost:" + server.port();
      sut = new JaxrsClientFactory().create(JaxrsClientOptionsBuilders.createClient(baseUrl, WhateverApi.class)
            .header("static-header-1", "static-header-1-value").build());
   }

   @BeforeEach
   public void clearMdc()
   {
      MDC.clear();
   }

   @AfterEach
   public void after()
   {
      server.stop();
   }
}
