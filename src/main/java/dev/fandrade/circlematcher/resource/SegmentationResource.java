package dev.fandrade.circlematcher.resource;

import com.google.gson.JsonElement;
import dev.fandrade.circlematcher.dto.CircleDTO;
import dev.fandrade.circlematcher.infra.CircleMapper;
import dev.fandrade.circlematcher.model.Circle;
import dev.fandrade.circlematcher.utils.JsonUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static com.google.gson.JsonParser.parseString;
import static io.quarkus.hibernate.orm.panache.Panache.getEntityManager;

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
        List<String> list = new ArrayList<>();
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
    @Path("{circleId}")
//    @APIResponse(responseCode = "200", description = "resource created", content = @Content(schema = @Schema(implementation = Entity.class)))
    @APIResponse(responseCode = "204", description = "Circle updated")
    @APIResponse(responseCode = "404", description = "Circle not found")
    @APIResponse(responseCode = "409", description = "Circle already exists")
    @Operation(summary = "Update Circle by CircleId")
    public Response update(@PathParam("circleId") String circleId) {
        JsonObject json = Json.createObjectBuilder().add("message", "update URL "+ circleId).build();
        return Response.ok(json).status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{circleId}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "204", description = "Circle deleted")
    @APIResponse(responseCode = "404", description = "Circle not found")
    @Operation(summary = "Delete Circle by CircleId")
    public Response delete(@PathParam("circleId") String circleId) {
        String queryString = "select c.* from circle c where c.circleid = :circleId";
        Query query = getEntityManager().createNativeQuery(queryString, Circle.class);
        query.setParameter("circleId", circleId);
        Circle queryResult = null;

        try {
            queryResult = (Circle) query.getSingleResult();
            if (queryResult.getDefault()){
                JsonObject json = Json.createObjectBuilder().add("message", "Can not delete Default Circle: " + queryResult.getCircleId()).build();
                return Response.ok(json).status(Response.Status.BAD_REQUEST).build();
            }
            queryResult.delete();
        } catch (NoResultException ignored) {}

        if (queryResult == null) {
            return Response.ok().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }

//    @POST
//    @Path("/import")
//    @Transactional
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response importCreate() {
//        JsonObject json = Json.createObjectBuilder().add("message", "Import Create URL ").build();
//        return Response.ok(json).status(Response.Status.CREATED).build();
//    }
//
//    @PUT
//    @Path("/import")
//    @Transactional
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response importUpdate() {
//        JsonObject json = Json.createObjectBuilder().add("message", "Import Update URL ").build();
//        return Response.ok(json).status(Response.Status.CREATED).build();
//    }
}
