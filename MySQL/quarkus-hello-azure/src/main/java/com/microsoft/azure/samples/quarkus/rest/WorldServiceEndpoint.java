/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.samples.quarkus.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.microsoft.azure.samples.quarkus.controllers.CityService;
import com.microsoft.azure.samples.quarkus.controllers.CountryService;
import com.microsoft.azure.samples.quarkus.entities.City;
import com.microsoft.azure.samples.quarkus.entities.Country;


@Path("/v1")
public class WorldServiceEndpoint {
    @Inject
    CountryService countrySvc;

    @Inject
    CityService citySvc;

    @GET
    @Path(value = "/area")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContinents() {
        List<String> continents = countrySvc.findAllContinents();
        return Response.ok(continents, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/area/{continent}")
    public Response getContinent(@PathParam("continent") String continent) {
        List<Country> country = countrySvc.findItemByContinent(continent);
        GenericEntity<List<Country>> genericEntity = new GenericEntity<List<Country>>(country){};
        return Response.ok(genericEntity, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/countries/{countrycode}")
    public Response getCountry(@PathParam("countrycode") String countrycode) {
        List<City> city = citySvc.findOver1MillPopulation(countrycode);
        GenericEntity<List<City>> genericEntity = new GenericEntity<List<City>>(city){};
        return Response.ok(genericEntity, MediaType.APPLICATION_JSON).build();
    }
}