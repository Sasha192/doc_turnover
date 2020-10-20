package app.controllers.customtenant.toChange;

import app.controllers.customtenant.JsonSupportController;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/statistic")
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

    @RequestMapping(value = "/all/monthly",
            method = RequestMethod.GET)
    public void selectMonthly(HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {
        List<AbstractCalendarPerformerStatistic> data =
                statisticService.findAllByType(CalendarPerformerEnum.MONTHLY);
        writeToResponse(response, GSON_BUILDER, data);
    }

    @RequestMapping(value = "/all/daily",
            method = RequestMethod.GET)
    public void selectDaily(HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {
        List<AbstractCalendarPerformerStatistic> data =
                statisticService.findAllByType(CalendarPerformerEnum.DAILY);
        writeToResponse(response, GSON_BUILDER, data);
    }

    @RequestMapping(value = "/all/annualy",
            method = RequestMethod.GET)
    public void selectAnnually(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {
        List<AbstractCalendarPerformerStatistic> data =
                statisticService.findAllByType(CalendarPerformerEnum.ANNUALLY);
        writeToResponse(response, GSON_BUILDER, data);
    }

    @RequestMapping(value = "/all/weekly",
            method = RequestMethod.GET)
    public void selectWeekly(HttpServletRequest request,
                             HttpServletResponse response)
            throws IOException {
        List<AbstractCalendarPerformerStatistic> data =
                statisticService.findAllByType(CalendarPerformerEnum.WEEKLY);
        writeToResponse(response, GSON_BUILDER, data);
    }

    @RequestMapping(value = "/all",
            method = RequestMethod.GET)
    public void selectAll(HttpServletRequest request,
                             HttpServletResponse response)
            throws IOException {
        List<AbstractCalendarPerformerStatistic> data =
                statisticService.findAllByType(CalendarPerformerEnum.ALL_TIME);
        writeToResponse(response, GSON_BUILDER, data);
    }
}
