package org.apache.ambari.views.service.resource;


import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.swagger.annotations.*;
import org.apache.ambari.views.service.api.ApiError;
import org.apache.ambari.views.service.api.FileList;
import org.apache.ambari.views.service.api.Instance;
import org.apache.ambari.views.service.api.Mkdir;
import org.apache.ambari.views.service.db.DbService;
import org.apache.ambari.views.service.services.HDFSService;
import org.apache.ambari.views.service.services.HdfsApiException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@Singleton
@Path("/files/")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/files/", description = "HDFS file operations API")
public class FileOperationsResource {

    public static final String SLASH = "/";
    @Inject
    DbService dbService;

    @Inject
    HDFSService hdfsService;

    @GET
    @Path("{instanceID}")
    @ApiOperation(
            value = "List directory",
            notes = "Fetches all files for the supplied path",
            response = FileList.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid InstanceId",response = ApiError.class),
            @ApiResponse(code = 404, message = "InstanceId not found",response = ApiError.class),
            @ApiResponse(code = 200, message = "List of files or directories",response = FileList.class)
    })
    @Timed
    public Response list(@ApiParam(value = "instanceID", required = true) @PathParam("instanceID") String instanceID,
                         @ApiParam(value = "path",defaultValue = "/") @QueryParam("path") String path) {

        String listPath = Strings.isNullOrEmpty(path)? SLASH:path;

        //load the instance
        Optional<Instance> instance = dbService.loadInstance(instanceID);
        if(!instance.isPresent()) {
            return badInstance();
        }

        try {
            Optional<FileList> fileList = hdfsService.listFile(instance.get(),listPath);
            return Response.ok(fileList.get()).build();
        } catch (HdfsApiException e) {
            ApiError err = new ApiError(400);
            err.setDescription(e.getMessage());
            return Response.status(400).entity(err).build();
        }



    }


    @POST
    @Path("{instanceID}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Upload a file at the specified path",
            notes = "Uploads a file, if lastFile was specified, respond with a list of all files at this path, else true/false",
            response = FileList.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid InstanceId",response = ApiError.class),
            @ApiResponse(code = 404, message = "InstanceId not found",response = ApiError.class),
            @ApiResponse(code = 500, message = "Cannot save file",response = ApiError.class),
            @ApiResponse(code = 200, message = "New List of files at the current path",response = FileList.class)
    })
    @Timed
    public Response upload(@ApiParam(value = "instanceID", required = true) @PathParam("instanceID") String instanceID,
                           @ApiParam(value = "file", required = true) @FormDataParam("file") InputStream uploadedInputStream,
                           @ApiParam(value = "file", required = true) @FormDataParam("file") FormDataContentDisposition contentDisposition,
                           @ApiParam(value = "target-path", required = true) @HeaderParam("target-path") String path
                           ) {

        //load the instance
        Optional<Instance> instance = dbService.loadInstance(instanceID);
        if(!instance.isPresent()) {
            return badInstance();
        }

        try {
            hdfsService.uploadFile(instance.get(),uploadedInputStream,contentDisposition,path);
            return Response.ok().build();
        } catch (Exception e) {
            ApiError err = new ApiError(500);
            err.setDescription(e.getMessage());
            return Response.status(500).entity(err).build();
        }


    }



    @POST
    @Path("{instanceID}/mkdir")
    @ApiOperation(
            value = "Make a new directory",
            notes = "Fetches all files for the supplied path",
            response = FileList.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid InstanceId",response = ApiError.class),
            @ApiResponse(code = 404, message = "InstanceId not found",response = ApiError.class),
            @ApiResponse(code = 200, message = "List of files or directories",response = FileList.class)
    })
    @Timed
    public Response mkdir(@ApiParam(value = "instanceID", required = true) @PathParam("instanceID") String instanceID,
                         @ApiParam(value = "path",required = true) Mkdir mkdir) {

        if(Strings.isNullOrEmpty(mkdir.getPath())){
            ApiError err = new ApiError(400);
            err.setDescription("The file path is empty or null");
            return Response.status(400).entity(err).build();
        }

        //load the instance
        Optional<Instance> instance = dbService.loadInstance(instanceID);
        if(!instance.isPresent()) {
            return badInstance();
        }

        try {
            Optional<FileList> fileList = hdfsService.mkdir(instance.get(),mkdir.getPath());
            return Response.ok(fileList.get()).build();
        } catch (HdfsApiException e) {
            ApiError err = new ApiError(400);
            err.setDescription(e.getMessage());
            return Response.status(400).entity(err).build();
        }

    }

    private Response badInstance() {
        Response.Status badRequest = Response.Status.BAD_REQUEST;
        ApiError error = new ApiError(badRequest.getStatusCode());
        error.setDescription("No Instance with this name found");
        return Response.status(badRequest).entity(error).build();
    }


}
