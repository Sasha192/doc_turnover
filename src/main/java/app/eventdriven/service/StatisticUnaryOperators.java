package app.eventdriven.service;

import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.util.function.UnaryOperator;

public class StatisticUnaryOperators {

    public static final UnaryOperator<AbstractCalendarPerformerStatistic> INC_NEW;
    public static final UnaryOperator<AbstractCalendarPerformerStatistic> DEC_NEW;

    public static final UnaryOperator<AbstractCalendarPerformerStatistic> INC_INPROGRESS;
    public static final UnaryOperator<AbstractCalendarPerformerStatistic> DEC_INPROGRESS;

    public static final UnaryOperator<AbstractCalendarPerformerStatistic> INC_OVERDUE;
    public static final UnaryOperator<AbstractCalendarPerformerStatistic> DEC_OVERDUE;

    public static final UnaryOperator<AbstractCalendarPerformerStatistic> INC_COMPLETED;
    public static final UnaryOperator<AbstractCalendarPerformerStatistic> DEC_COMPLETED;

    public static final UnaryOperator<AbstractCalendarPerformerStatistic> INC_ONHOLD;
    public static final UnaryOperator<AbstractCalendarPerformerStatistic> DEC_ONHOLD;

    public static final UnaryOperator<AbstractCalendarPerformerStatistic> INC_ACTIVE;
    public static final UnaryOperator<AbstractCalendarPerformerStatistic> DEC_ACTIVE;

    static {
        INC_NEW = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                statistic.incrementNew();
                return statistic;
            }
        };
        DEC_NEW = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                statistic.decrementNew();
                return statistic;
            }
        };
        INC_INPROGRESS = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                statistic.incrementInProgress();
                return statistic;
            }
        };
        DEC_INPROGRESS = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                statistic.decrementInProgress();
                return statistic;
            }
        };

        INC_OVERDUE = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                statistic.incrementOverdue();
                return statistic;
            }
        };
        DEC_OVERDUE = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                statistic.decrementOverdue();
                return statistic;
            }
        };
        INC_COMPLETED = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                statistic.incrementInProgress();
                return statistic;
            }
        };
        DEC_COMPLETED = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                return statistic;
            }
        };
        INC_ONHOLD = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                statistic.incrementOnhold();
                return statistic;
            }
        };
        DEC_ONHOLD = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                statistic.decrementOnhold();
                return statistic;
            }
        };
        INC_ACTIVE = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                return statistic;
            }
        };
        DEC_ACTIVE = new UnaryOperator<AbstractCalendarPerformerStatistic>() {
            @Override
            public AbstractCalendarPerformerStatistic apply(
                    AbstractCalendarPerformerStatistic statistic) {
                return statistic;
            }
        };
    }
}
