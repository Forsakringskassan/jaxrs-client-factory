package se.fk.github.jaxrsclientfactory;

import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import com.github.tomakehurst.wiremock.client.WireMock;

import jakarta.ws.rs.InternalServerErrorException;
import se.fk.github.logging.callerinfo.model.HeaderTyp;
import se.fk.github.logging.callerinfo.model.MDCKeys;

public class MdcIntegrationTest extends IntegrationTestBase
{
   @Test
   public void testThatResponseWithBreadcrumbAndProcessIdHeadersIsHandled()
   {
      String breadcrumbId = "test-breadcrumb-123";
      String processId = "test-process-456";

      givenWiremock(any(anyUrl())
            .willReturn(WireMock.aResponse().withStatus(204)
                  .withHeader(HeaderTyp.BREADCRUMB_ID.value(), breadcrumbId)
                  .withHeader(HeaderTyp.PROCESSID.value(), processId))
            .build());

      sut.postWhatever();
      verify(postRequestedFor(urlEqualTo("/")));
   }

   @Test
   public void testThatResponseWithOnlyBreadcrumbIdHeaderIsHandled()
   {
      String breadcrumbId = "test-breadcrumb-789";

      givenWiremock(any(anyUrl())
            .willReturn(WireMock.aResponse().withStatus(204)
                  .withHeader(HeaderTyp.BREADCRUMB_ID.value(), breadcrumbId))
            .build());

      sut.postWhatever();

      verify(postRequestedFor(urlEqualTo("/")));
   }

   @Test
   public void testThatResponseWithOnlyProcessIdHeaderIsHandled()
   {
      String processId = "test-process-999";

      givenWiremock(any(anyUrl())
            .willReturn(WireMock.aResponse().withStatus(204)
                  .withHeader(HeaderTyp.PROCESSID.value(), processId))
            .build());

      sut.postWhatever();

      verify(postRequestedFor(urlEqualTo("/")));
   }

   @Test
   public void testThatResponseWithoutMdcHeadersIsHandled()
   {
      givenWiremock(any(anyUrl())
            .willReturn(WireMock.aResponse().withStatus(204))
            .build());

      sut.postWhatever();

      verify(postRequestedFor(urlEqualTo("/")));
   }

   @Test
   public void testThatResponseWithEmptyMdcHeadersIsHandled()
   {
      givenWiremock(any(anyUrl())
            .willReturn(WireMock.aResponse().withStatus(204)
                  .withHeader(HeaderTyp.BREADCRUMB_ID.value(), "")
                  .withHeader(HeaderTyp.PROCESSID.value(), ""))
            .build());

      sut.postWhatever();

      verify(postRequestedFor(urlEqualTo("/")));
   }

   @Test
   public void testThatResponseWithDifferentHeaderValuesIsHandled()
   {
      String responseBreadcrumbId = "response-breadcrumb-abc";
      String responseProcessId = "response-process-def";

      givenWiremock(any(anyUrl())
            .willReturn(WireMock.aResponse().withStatus(204)
                  .withHeader(HeaderTyp.BREADCRUMB_ID.value(), responseBreadcrumbId)
                  .withHeader(HeaderTyp.PROCESSID.value(), responseProcessId))
            .build());

      sut.postWhatever();

      verify(postRequestedFor(urlEqualTo("/")));
   }

   @Test
   public void testThatResponseHeadersAreReceivedOnErrorStatus()
   {
      String breadcrumbId = "error-breadcrumb-123";
      String processId = "error-process-456";

      givenWiremock(any(anyUrl())
            .willReturn(WireMock.aResponse().withStatus(500)
                  .withHeader(HeaderTyp.BREADCRUMB_ID.value(), breadcrumbId)
                  .withHeader(HeaderTyp.PROCESSID.value(), processId))
            .build());

      InternalServerErrorException thrown = assertThrows(InternalServerErrorException.class, () -> {
         sut.postWhatever();
      });

      assertThat(thrown.getResponse().getStatus()).isEqualTo(500);
      verify(postRequestedFor(urlEqualTo("/")));
   }
}
