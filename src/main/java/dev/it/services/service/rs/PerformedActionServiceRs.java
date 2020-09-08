package dev.it.services.service.rs;

import dev.it.api.service.RsRepositoryServiceV3;
import dev.it.api.util.TableKeyUtils;
import dev.it.services.management.AppConstants;
import dev.it.services.model.Action;
import dev.it.services.model.BlogPost;
import dev.it.services.model.PerformedAction;
import dev.it.services.service.S3Service;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

@Path(AppConstants.PERFORMED_ACTIONS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class PerformedActionServiceRs extends RsRepositoryServiceV3<PerformedAction, String> {

    @Inject
    S3Service s3Service;


    public PerformedActionServiceRs() {
        super(PerformedAction.class);
    }


    @Override
    protected String getDefaultOrderBy() {
        return " action asc";
    }


    @Override
    public PanacheQuery<PerformedAction> getSearch(String orderBy) throws Exception {

        PanacheQuery<PerformedAction> search;
        Sort sort = sort(orderBy);

        if (sort != null) {
            search = BlogPost.find("select a from PerformedAction a", sort);
        } else {
            search = BlogPost.find("select a from PerformedAction a");
        }
        if (nn("obj.blogpost_uuid")) {
            search
                    .filter("obj.blogpost_uuid", Parameters.with("blogpost_uuid", get("obj.blogpost_uuid")));
        }
        if (nn("obj.user_uuid")) {
            search
                    .filter("obj.user_uuid", Parameters.with("user_uuid", get("obj.user_uuid")));
        }

        return search;
    }

    @Override
    protected void prePersist(PerformedAction performedAction) throws Exception {

        performedAction.creation_date = LocalDateTime.now();
    }
}
