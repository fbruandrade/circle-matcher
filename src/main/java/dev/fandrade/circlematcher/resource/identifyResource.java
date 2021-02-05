package dev.fandrade.circlematcher.resource;

import dev.fandrade.circlematcher.model.Circle;
import dev.fandrade.circlematcher.request.Identify;
import dev.fandrade.circlematcher.response.CircleResponse;
import dev.fandrade.circlematcher.service.IdentifyService;
import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.JsonLogicException;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Path("/identify")
@Tag(name = "Identify")
public class identifyResource {

    @Inject
    IdentifyService identifyService;

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
        List<Circle> queryResult = identifyService.findCircleByWorkspaceIdAndParams(identify.getWorkspaceId(), parameters);
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
                Circle defaultQueryResult = null;
                try {
                    CircleResponse response = new CircleResponse();
                    defaultQueryResult = identifyService.findCircleDefaultByWorkspaceId(identify.getWorkspaceId());
                    response.setId(String.valueOf(defaultQueryResult.getCircleId()));
                    response.setName(defaultQueryResult.getName());
                    circleResponse.add(response);
                } catch (NoResultException e) {
                    System.out.println(e);
                }
            }
        } else {
            Circle defaultQueryResult = null;
            try {
                CircleResponse response = new CircleResponse();
                defaultQueryResult = identifyService.findCircleDefaultByWorkspaceId(identify.getWorkspaceId());
                response.setId(String.valueOf(defaultQueryResult.getCircleId()));
                response.setName(defaultQueryResult.getName());
                circleResponse.add(response);
            } catch (NoResultException ignored) {}

            if (defaultQueryResult == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(circleResponse).status(Response.Status.OK).build();

        }
        return Response.ok(circleResponse).status(Response.Status.OK).build();

    }

//    public List<Circle> findCircleByWorkspaceIdAndParams(String workspaceId, String params) {
//        String queryString = "select c.* from circle c where c.workspaceid = :workspaceId and c.isdefault = false " +
//                "and c.enabled = true and jsonb_exists_any(c.parameters,array[" + params + "]) order by c.parameters_size DESC";
//        Query query = getEntityManager().createNativeQuery(queryString, Circle.class);
//        query.setParameter("workspaceId", workspaceId);
//
//        return query.getResultList();
//    }
//
//    public Circle findCircleDefaultByWorkspaceId(String workspaceId) {
//        String defaultQueryString = "select c.* from circle c where c.workspaceid = :workspaceId and c.enabled = true and c.isdefault = true";
//        Query defaultQuery = getEntityManager().createNativeQuery(defaultQueryString, Circle.class);
//        defaultQuery.setParameter("workspaceId", workspaceId);
//
//        return (Circle) defaultQuery.getSingleResult();
//    }
}