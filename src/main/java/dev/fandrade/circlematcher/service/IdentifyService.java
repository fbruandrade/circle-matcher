package dev.fandrade.circlematcher.service;

import dev.fandrade.circlematcher.model.Circle;
import io.quarkus.cache.CacheResult;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import java.util.List;

import static io.quarkus.hibernate.orm.panache.Panache.getEntityManager;


@RequestScoped
@Traced
public class IdentifyService {

    @CacheResult(cacheName = "identifyCache")
    public Object[] findCSVCircleDefaultByWorkspaceIdAndValueObj(String workspaceId, String key, String value) {
        String defaultQueryString = "select c.circleid, c.name from Circle c where c.workspaceid = :workspaceId " +
                "and c.enabled = true and c.isdefault = false and c.csv = true " +
                "and c.matchcsv @> '\"" + value + "\"' and jsonb_exists(c.parameters,'" + key + "')";
        Query defaultQuery = getEntityManager().createNativeQuery(defaultQueryString);
        defaultQuery.setParameter("workspaceId", workspaceId);
        return (Object[]) defaultQuery.getSingleResult();
    }

    @CacheResult(cacheName = "identifyCache")
    public Circle findCSVCircleDefaultByWorkspaceIdAndValue(String workspaceId, String key, String value) {
        String defaultQueryString = "select c.* from Circle c where c.workspaceid = :workspaceId " +
                "and c.enabled = true and c.isdefault = false and c.csv = true " +
                "and c.matchcsv->>'" + key + "' = '" + value + "'" ;
        Query defaultQuery = getEntityManager().createNativeQuery(defaultQueryString, Circle.class);
        defaultQuery.setParameter("workspaceId", workspaceId);
        return (Circle) defaultQuery.getSingleResult();
    }

    @CacheResult(cacheName = "identifyCache")
    public List<Circle> findCircleByWorkspaceIdAndParams(String workspaceId, String params) {
        String queryString = "select c.* from circle c where c.workspaceid = :workspaceId and c.isdefault = false " +
                "and c.csv = false and c.enabled = true and jsonb_exists_any(c.parameters,array[" + params + "])";
        Query query = getEntityManager().createNativeQuery(queryString, Circle.class);
        query.setParameter("workspaceId", workspaceId);

        return query.getResultList();
    }

    @CacheResult(cacheName = "identifyCache")
    public Circle findCircleDefaultByWorkspaceId(String workspaceId) {
        String defaultQueryString = "select c.* from circle c where c.workspaceid = :workspaceId and c.enabled = true " +
                "and c.isdefault = true";
        Query defaultQuery = getEntityManager().createNativeQuery(defaultQueryString, Circle.class);
        defaultQuery.setParameter("workspaceId", workspaceId);

        return (Circle) defaultQuery.getSingleResult();
    }
}
