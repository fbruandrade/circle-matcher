package dev.fandrade.circlematcher.resource;

import com.google.gson.JsonElement;
import dev.fandrade.circlematcher.dto.CircleDTO;
import dev.fandrade.circlematcher.infra.CircleMapper;
import dev.fandrade.circlematcher.model.Circle;
import dev.fandrade.circlematcher.utils.JsonUtils;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static com.google.gson.JsonParser.parseString;

@Path("/segmentation")
@Tag(name = "Segmentation")
public class SegmentationResource {

    @Inject
    CircleMapper circleMapper;

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "201", description = "Circle created")
//    @APIResponse(responseCode = "400", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
    public Response create(@Valid CircleDTO dto) {
        Circle circle = circleMapper.toCircle(dto);
        List<String> list = new ArrayList<String>();
        String key = "var";
        JsonElement p = parseString(circle.getMatch());
        JsonUtils.check(list, key, p);
        circle.setParameters(String.valueOf(list));
        circle.setParameters_size(list.size());

        circle.persist();

        return Response.ok(circle).status(Response.Status.CREATED).build();
    }

    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{reference}")
    public Response update(@PathParam("reference") String reference) {
        JsonObject json = Json.createObjectBuilder().add("message", "update URL "+ reference).build();
        return Response.ok(json).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{reference}")
    @Transactional
    public Response delete(@PathParam("reference") String reference) {
        JsonObject json = Json.createObjectBuilder().add("message", "delete URL " + reference).build();
        return Response.ok(json).status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/import")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response importCreate() {
        JsonObject json = Json.createObjectBuilder().add("message", "Import Create URL ").build();
        return Response.ok(json).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/import")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response importUpdate() {
        JsonObject json = Json.createObjectBuilder().add("message", "Import Update URL ").build();
        return Response.ok(json).status(Response.Status.CREATED).build();
    }
}
