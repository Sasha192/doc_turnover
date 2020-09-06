package app.statisticsmodule.domain;

public enum CalendarPerformerEnum {
    WEEKLY(Values.WEEKLY),
    MONTHLY(Values.MONTHLY),
    ANNUALLY(Values.ANNUALLY),
    DAILY(Values.DAILY);

    private String name;

    CalendarPerformerEnum(String name) {
        this.name = name;
    }

    public static class Values {
        public static final String WEEKLY = "WEEKLY";
        public static final String MONTHLY = "MONTHLY";
        public static final String ANNUALLY = "ANNUALLY";
        public static final String DAILY = "DAILY";
    }
}