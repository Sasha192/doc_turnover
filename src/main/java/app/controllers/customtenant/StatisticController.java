package app.controllers.customtenant;

import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.ICalendarStatistic;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.customtenant.statisticsmodule.domain.CalendarPerformerEnum;
import com.google.gson.GsonBuilder;
import java.io.IOException;
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

    @Autowired
    private ICalendarStatistic statisticService;

    @RequestMapping(value = "/{stat_type}",
            method = RequestMethod.GET)
    public void selectAll(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable("stat_type") String statisticType)
            throws IOException {
        CalendarPerformerEnum enu = CalendarPerformerEnum
                .valueOf(statisticType.toUpperCase());
        List<AbstractCalendarPerformerStatistic> data =
                statisticService.findAllByType(enu);
        writeToResponse(response, GSON_BUILDER, data);
    }
}
