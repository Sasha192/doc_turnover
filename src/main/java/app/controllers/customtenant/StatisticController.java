package app.controllers.customtenant;

import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.ICalendarStatistic;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.customtenant.statisticsmodule.domain.CalendarPerformerEnum;
import app.security.models.SimpleRole;
import app.security.wrappers.IPerformerWrapper;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/com/statistics")
public class StatisticController
        extends JsonSupportController {

    private static final GsonBuilder GSON_BUILDER =
            new GsonBuilder()
                    .addSerializationExclusionStrategy(
                            ExcludeStrategies
                                    .EXCLUDE_FOR_JSON_PERFORMER
                    );

    private final ICalendarStatistic statisticService;

    private final IPerformerWrapper performerWrapper;

    public StatisticController(IPerformerWrapper performerWrapper,
                               ICalendarStatistic statisticService) {
        this.performerWrapper = performerWrapper;
        this.statisticService = statisticService;
    }

    @RequestMapping(value = "/{stat_type}",
            method = RequestMethod.GET)
    public void selectAll(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable("stat_type") String statisticType)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        List<AbstractCalendarPerformerStatistic> data = null;
        if (allowOp(performer.getRoles())) {
            CalendarPerformerEnum enu = CalendarPerformerEnum
                    .valueOf(statisticType.toUpperCase());
            data = statisticService.findAllByType(enu);
        } else {
            data = new LinkedList<>();
        }
        writeToResponse(response, GSON_BUILDER, data);
    }

    private boolean allowOp(SimpleRole roles) {
        return SimpleRole.ADMIN.equals(roles)
                || SimpleRole.G_MANAGER.equals(roles);
    }
}
