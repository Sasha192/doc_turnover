package app.customtenant.models.basic;

import app.customtenant.eventdriven.service.StatisticUnaryOperators;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.util.HashMap;
import java.util.function.UnaryOperator;

public enum TaskStatus {
    COMPLETED("COMPLETED") {
        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
            return StatisticUnaryOperators.INC_COMPLETED;
        }

        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
            return StatisticUnaryOperators.DEC_COMPLETED;
        }

        @Override
        public String message() {
            return "Завершено";
        }
    },ONHOLD("ONHOLD") {
        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
            return StatisticUnaryOperators.INC_ONHOLD;
        }

        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
            return StatisticUnaryOperators.DEC_ONHOLD;
        }

        @Override
        public String message() {
            return "Відкладено";
        }
    }, INPROGRESS("INPROGRESS") {
        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
            return StatisticUnaryOperators.INC_INPROGRESS;
        }

        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
            return StatisticUnaryOperators.DEC_INPROGRESS;
        }

        @Override
        public String message() {
            return "На виконанні";
        }
    }, OVERDUE("OVERDUE") {
        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
            return StatisticUnaryOperators.INC_OVERDUE;
        }

        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
            return StatisticUnaryOperators.DEC_OVERDUE;
        }

        @Override
        public String message() {
            return "На доопрацювання";
        }
    }, NEW("NEW") {
        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
            return StatisticUnaryOperators.INC_NEW;
        }

        @Override
        public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
            return StatisticUnaryOperators.DEC_NEW;
        }

        @Override
        public String message() {
            return "Новий";
        }
    };

    public static HashMap<String, TaskStatus> statusesMap;

    static {
        statusesMap = new HashMap<>();
        statusesMap.put(NEW.getName(), NEW);
        statusesMap.put(INPROGRESS.getName(), INPROGRESS);
        statusesMap.put(OVERDUE.getName(), OVERDUE);
        statusesMap.put(COMPLETED.getName(), COMPLETED);
        statusesMap.put(ONHOLD.getName(), ONHOLD);
    }

    private String name;

    TaskStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TaskStatus getByName(String name) {
        return statusesMap.get(name);
    }

    public abstract UnaryOperator<AbstractCalendarPerformerStatistic> increment();

    public abstract UnaryOperator<AbstractCalendarPerformerStatistic> decrement();

    protected abstract String message();

    public String getMessage() {
        return "Статус задачі було змінено : " + message();
    }
}
