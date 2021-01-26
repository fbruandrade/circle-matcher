package dev.fandrade.circlematcher.resource;

import dev.fandrade.circlematcher.model.Circle;
import dev.fandrade.circlematcher.request.Identify;
import dev.fandrade.circlematcher.response.CircleResponse;
import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.JsonLogicException;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.persistence.Query;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.quarkus.hibernate.orm.panache.Panache.getEntityManager;

@Path("/identify")
@Tag(name = "Identify")
public class identifyResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "WorkspaceId founded, and apply the match")
    @APIResponse(responseCode = "404", description = "WorkspaceId not found")
    public Response identifyCircle(@Valid Identify identify) throws JsonLogicException {
        String parameters = identify.getRequestData().keySet()
                .stream()
                .map(name -> ("'" + name + "'"))
                .collect(Collectors.joining(","));
        String queryString = "select c.* from circle c where c.workspaceid = :workspaceId and c.name != :default and jsonb_exists_any(c.parameters,array[" + parameters + "]) order by c.parameters_size DESC";
        Query query = getEntityManager().createNativeQuery(queryString, Circle.class);
        query.setParameter("workspaceId", identify.getWorkspaceId())
             .setParameter("default", "Default");
        List<Circle> queryResult = query.getResultList();
        List<CircleResponse> circleResponse = new ArrayList<>();

        if (! queryResult.isEmpty()) {
            int logic = 0;
            for (Circle circle : queryResult) {
                JsonLogic jsonLogic = new JsonLogic();
                if ((boolean) jsonLogic.apply(circle.getMatch(), identify.getRequestData())) {
                    CircleResponse response = new CircleResponse();
                    response.setId(String.valueOf(circle.getCircleId()));
                    response.setName(circle.getName());
                    circleResponse.add(response);
                    logic++;
                }
            }

            if (logic == 0) {
                String defaultQueryString = "select c.* from circle c where c.name = :default";
                Query defaultQuery = getEntityManager().createNativeQuery(defaultQueryString, Circle.class);
                defaultQuery.setParameter("default", "Default");
                Circle defaultQueryResult = (Circle) defaultQuery.getSingleResult();
                CircleResponse response = new CircleResponse();
                response.setId(String.valueOf(defaultQueryResult.getCircleId()));
                response.setName(defaultQueryResult.getName());
                circleResponse.add(response);
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(circleResponse).status(Response.Status.OK).build();

    }
}