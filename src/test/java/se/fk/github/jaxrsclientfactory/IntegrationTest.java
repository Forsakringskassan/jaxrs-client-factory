package se.fk.github.jaxrsclientfactory;

import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import jakarta.ws.rs.InternalServerErrorException;

public class IntegrationTest extends IntegrationTestBase
{
   @Test
   public void testThatStaticallyConfiguredHeadersAreSent()
   {
      givenWiremock(any(anyUrl()).withHeader("static-header-1", containing("static-header-1-value"))
            .willReturn(WireMock.aResponse().withStatus(204)).build());

      sut.postWhatever();

      verify(postRequestedFor(urlEqualTo("/")));
   }

   @Test
   public void testThatDefaultHeadersAreSent()
   {
      givenWiremock(any(anyUrl()).willReturn(WireMock.aResponse().withStatus(204)).build());

      sut.postWhatever();

      verify(postRequestedFor(urlEqualTo("/")));
   }

   @Test
   public void testThatErrorResponseIsHandled()
   {
      givenWiremock(any(anyUrl()).willReturn(WireMock.aResponse().withStatus(500)).build());

      InternalServerErrorException thrown = assertThrows(InternalServerErrorException.class, () -> {
         sut.postWhatever();
      });

      assertThat(thrown.getResponse().getStatus()).isEqualTo(500);
   }

   @Test
   public void testThatPostBodyContentIsLoggedWhenError()
   {
      givenWiremock(any(anyUrl()).willReturn(WireMock.aResponse().withStatus(500)).build());

      InternalServerErrorException thrown = assertThrows(InternalServerErrorException.class, () -> {
         sut.postWhateverBody(entity);
      });

      assertThat(thrown.getResponse().getStatus()).isEqualTo(500);

      verify(postRequestedFor(urlEqualTo("/"))
            .withRequestBody(WireMock.equalToJson("{\"attribute1\": \"the attribute value\"}")));
   }

   @Test
   public void testThatPostBodyContentWithNullIsLoggedWhenError()
   {
      givenWiremock(any(anyUrl()).willReturn(WireMock.aResponse().withStatus(500)).build());

      InternalServerErrorException thrown = assertThrows(InternalServerErrorException.class, () -> {
         sut.postWhateverBody(new WhateverVO(null));
      });

      assertThat(thrown.getResponse().getStatus()).isEqualTo(500);

      verify(postRequestedFor(urlEqualTo("/")).withRequestBody(WireMock.equalToJson("{}")));
   }

   @Test
   public void testThatReceivedBodyContentIsLoggendWhenError()
   {
      givenWiremock(
            any(anyUrl()).willReturn(WireMock.aResponse().withStatus(500).withBody("whatever message")).build());

      InternalServerErrorException thrown = assertThrows(InternalServerErrorException.class, () -> {
         sut.getWhateverBody();
      });

      assertThat(thrown.getResponse().getStatus()).isEqualTo(500);
   }
}
