package app.controllers;

import app.service.interfaces.ICalendarStatistic;
import app.statisticsmodule.domain.CalendarPerformerEnum;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/statistic")
public class StatisticController
        extends JsonSupportController {

    @Autowired
    private ICalendarStatistic statisticService;

    @RequestMapping(value = "/all/monthly",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public void selectMonthly(HttpServletRequest request,
                          HttpServletResponse response) {
        statisticService.findAllByType(CalendarPerformerEnum.MONTHLY);
    }

    @RequestMapping(value = "/all/daily",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public void selectDaily(HttpServletRequest request,
                          HttpServletResponse response) {
        statisticService.findAllByType(CalendarPerformerEnum.DAILY);
    }

    @RequestMapping(value = "/all/annualy",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public void selectAnnually(HttpServletRequest request,
                          HttpServletResponse response) {
        statisticService.findAllByType(CalendarPerformerEnum.ANNUALLY);
    }

    @RequestMapping(value = "/all/weekly",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public void selectWeekly(HttpServletRequest request,
                          HttpServletResponse response) {
        statisticService.findAllByType(CalendarPerformerEnum.WEEKLY);
    }
}
