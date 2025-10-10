package se.fk.github.jaxrsclientfactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;

public interface WhateverApi
{
   @POST
   @Consumes(
   {
         "application/json"
   })
   void postWhatever();

   @POST
   @Consumes(
   {
         "application/json"
   })
   void postWhateverWithPnr(@HeaderParam("PNR") String pnr);

   @POST
   @Consumes(
   {
         "application/json"
   })
   void postWhateverBody(WhateverVO theBody);

   @GET
   @Produces(
   {
         "application/json"
   })
   WhateverVO getWhateverBody();
}
