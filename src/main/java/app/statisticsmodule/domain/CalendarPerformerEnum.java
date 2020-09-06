package app.statisticsmodule.domain;

public enum CalendarPerformerEnum {
    WEEKLY(Values.WEEKLY, Values.DAILY_CLASS),
    MONTHLY(Values.MONTHLY, Values.MONTHLY_CLASS),
    ANNUALLY(Values.ANNUALLY, Values.ANNUALLY_CLASS),
    DAILY(Values.DAILY, Values.DAILY_CLASS);

    private String name;
    private Class<?> clazz;

    CalendarPerformerEnum(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public static class Values {
        public static final String WEEKLY = "WEEKLY";
        public static final String MONTHLY = "MONTHLY";
        public static final String ANNUALLY = "ANNUALLY";
        public static final String DAILY = "DAILY";

        public static final Class<WeeklyPerformerStatistics> WEEKLY_CLASS;
        public static final Class<MonthlyPerformerStatistic> MONTHLY_CLASS;
        public static final Class<AnnuallyPerformerStatistics> ANNUALLY_CLASS;
        public static final Class<DailyPerformerStatistic> DAILY_CLASS;

        static {
            WEEKLY_CLASS = WeeklyPerformerStatistics.class;
            MONTHLY_CLASS = MonthlyPerformerStatistic.class;
            ANNUALLY_CLASS = AnnuallyPerformerStatistics.class;
            DAILY_CLASS = DailyPerformerStatistic.class;
        }
    }
}
