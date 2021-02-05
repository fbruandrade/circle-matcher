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
//    @Inject
//    EntityManager entityManager;
//
//    @CacheResult(cacheName = "identifyCache")
//    public List<Circle> findCircleByWorkspaceIdAndParams(String workspaceId, String params) {
//        return entityManager.createQuery("select c from Circle c where c.workspaceId = :workspaceid and c.isDefault = false " +
//        "and c.enabled = true and jsonb_exists_any(c.parameters,array[" + params + "]) order by c.parameters_size DESC", Circle.class)
//                .setParameter("workspaceid", workspaceId).getResultList();
//    }
//
//    @CacheResult(cacheName = "identifyCache")
//    public Circle findCircleDefaultByWorkspaceId(String workspaceId) {
//        return (Circle) entityManager.createQuery("select c from circle c where c.workspaceid = :workspaceid and c.enabled = true and c.isdefault = true", Circle.class).setParameter("workspaceid", workspaceId).getSingleResult();
//    }
//    @CacheResult(cacheName = "identifyCache")
//    public int countCircleByWorkspaceIdAndParams(String workspaceId, String params) {
//        String queryString = "select c.* from circle c where c.workspaceid = :workspaceId and c.isdefault = false " +
//                "and c.enabled = true and jsonb_exists_any(c.parameters,array[" + params + "]) order by c.parameters_size DESC";
//        Query query = getEntityManager().createNativeQuery(queryString, Circle.class);
//        query.setParameter("workspaceId", workspaceId);
//
//        return query.getResultList();
//    }

    @CacheResult(cacheName = "identifyCache")
    public List<Circle> findCircleByWorkspaceIdAndParams(String workspaceId, String params) {
        String queryString = "select c.* from circle c where c.workspaceid = :workspaceId and c.isdefault = false " +
                "and c.enabled = true and jsonb_exists_any(c.parameters,array[" + params + "]) order by c.parameters_size DESC";
        Query query = getEntityManager().createNativeQuery(queryString, Circle.class);
        query.setParameter("workspaceId", workspaceId);

        return query.getResultList();
    }

    @CacheResult(cacheName = "identifyCache")
    public Circle findCircleDefaultByWorkspaceId(String workspaceId) {
        String defaultQueryString = "select c.* from circle c where c.workspaceid = :workspaceId and c.enabled = true and c.isdefault = true";
        Query defaultQuery = getEntityManager().createNativeQuery(defaultQueryString, Circle.class);
        defaultQuery.setParameter("workspaceId", workspaceId);

        return (Circle) defaultQuery.getSingleResult();
    }
}
