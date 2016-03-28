package org.apache.ambari.views.service.resource;


import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.swagger.annotations.*;
import org.apache.ambari.views.service.api.ApiError;
import org.apache.ambari.views.service.api.Instance;
import org.apache.ambari.views.service.api.InstanceList;
import org.apache.ambari.views.service.db.DbService;
import org.apache.ambari.views.service.db.InstanceDao;
import org.apache.ambari.views.service.services.AmbariService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

@Singleton
@Path("/instances/")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/instances", description = "Manage file view instances")
public class InstanceResource {

    @Inject
    private DbService dbService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "List all file view instances",
            notes = "Fetches all instances of the file view",
            response = InstanceList.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of Instances",response = InstanceList.class)})
    @Timed
    public Response list() {
        return  Response.ok(new InstanceList(Arrays.asList(new Instance("test", "test", "test")))).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "new File instance",
            notes = "Create a new file instance",
            response = Instance.class)
    @ApiResponses(value = {  @ApiResponse(code = 400, message = "Invalid display name",response = ApiError.class),
            @ApiResponse(code = 200, message = "New instance",response = Instance.class)})
    @Timed
    public Response newInstance(@ApiParam(value = "instance", required = true) Instance instance){
        //persist instance
        Instance persistedInstance = dbService.persistInstance(instance);
        return Response.ok(persistedInstance).build();
    }


    @DELETE
    @Path("{instanceID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Delete a file instance",
            notes = "Delete a file instance by its ID",
            response = Instance.class)
    @ApiResponses(value = {  @ApiResponse(code = 400, message = "Invalid instance name",response = ApiError.class),
            @ApiResponse(code = 200, message = "Deleted instance",response = Instance.class)})
    @Timed
    public Response deleteInstance(@ApiParam(value = "instanceID", required = true) @PathParam("instanceID") String displayName){
        return Response.ok(new Instance(displayName,"test","test")).build();
    }


    @PUT
    @Path("{instanceID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Update a file instance",
            notes = "Update a file instance by its ID",
            response = Instance.class)
    @ApiResponses(value = {  @ApiResponse(code = 400, message = "Invalid instance name",response = ApiError.class),
            @ApiResponse(code = 200, message = "Deleted instance",response = Instance.class)})
    @Timed
    public Response uodateInstance(@ApiParam(value = "instanceID", required = true) @PathParam("instanceID") String displayName){
        return Response.ok(new Instance(displayName,"test","test")).build();
    }


}
