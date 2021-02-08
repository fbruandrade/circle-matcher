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
import java.util.Map;
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
        List<CircleResponse> circleResponse = new ArrayList<>();
        List<Circle> queryResult = identifyService.findCircleByWorkspaceIdAndParams(identify.getWorkspaceId(), parameters);

        for (Map.Entry<String, Object> entry : identify.getRequestData().entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            List<Circle> circles = identifyService.countCSVCircle(identify.getWorkspaceId(), k);
            long count = circles.size();
            if (count != 0) {
                Circle ResultCSV;
                try {
                    CircleResponse response = new CircleResponse();
                    ResultCSV = identifyService.findCSVCircleDefaultByWorkspaceIdAndValue(identify.getWorkspaceId(), k, String.valueOf(v));
                    response.setId(String.valueOf(ResultCSV.getCircleId()));
                    response.setName(ResultCSV.getName());
                    circleResponse.add(response);
                } catch (NoResultException e) {
                    System.out.println(e);
                }
            }
        }

        if (! queryResult.isEmpty() || circleResponse.size() > 0) {
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

            if (logic == 0 && circleResponse.size() == 0) {
                Circle defaultQueryResult;
                try {
                    CircleResponse response = new CircleResponse();
                    defaultQueryResult = identifyService.findCircleDefaultByWorkspaceId(identify.getWorkspaceId());
                    response.setId(String.valueOf(defaultQueryResult.getCircleId()));
                    response.setName(defaultQueryResult.getName());
                    System.out.println("if");
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
                System.out.println("else");
                circleResponse.add(response);
            } catch (NoResultException ignored) {}

            if (defaultQueryResult == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(circleResponse).status(Response.Status.OK).build();

        }
        return Response.ok(circleResponse).status(Response.Status.OK).build();

    }
}