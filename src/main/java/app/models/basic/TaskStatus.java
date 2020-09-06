package app.models.basic;

import app.eventdriven.service.StatisticUnaryOperators;
import app.models.abstr.IdentityBaseEntity;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "custom_status")
public class TaskStatus
        extends IdentityBaseEntity
        implements Serializable {

    public static enum DefaultStatus {
        NEW("new") {
            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
                return StatisticUnaryOperators.INC_NEW;
            }

            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
                return StatisticUnaryOperators.DEC_NEW;
            }
        }, INPROGRESS("inprogress") {
            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
                return StatisticUnaryOperators.INC_INPROGRESS;
            }

            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
                return StatisticUnaryOperators.DEC_INPROGRESS;
            }
        }, OVERDUE("overdue") {
            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
                return StatisticUnaryOperators.INC_OVERDUE;
            }

            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
                return StatisticUnaryOperators.DEC_OVERDUE;
            }
        },
        COMPLETED("completed") {
            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
                return StatisticUnaryOperators.INC_COMPLETED;
            }

            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
                return StatisticUnaryOperators.DEC_COMPLETED;
            }
        }, ONHOLD("onhold") {
            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
                return StatisticUnaryOperators.INC_ONHOLD;
            }

            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
                return StatisticUnaryOperators.DEC_ONHOLD;
            }
        }, ACTIVE("active") {
            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> increment() {
                return StatisticUnaryOperators.INC_ACTIVE;
            }

            @Override
            public UnaryOperator<AbstractCalendarPerformerStatistic> decrement() {
                return StatisticUnaryOperators.DEC_ACTIVE;
            }
        };

        public static HashMap<String, DefaultStatus> statusesMap;

        static {
            statusesMap = new HashMap<>();
            statusesMap.put(NEW.getName(), NEW);
            statusesMap.put(INPROGRESS.getName(), INPROGRESS);
            statusesMap.put(OVERDUE.getName(), OVERDUE);
            statusesMap.put(COMPLETED.getName(), COMPLETED);
            statusesMap.put(ONHOLD.getName(), ONHOLD);
            statusesMap.put(ACTIVE.getName(), ACTIVE);
        }

        private String name;

        DefaultStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static DefaultStatus getByName(String name) {
            return statusesMap.get(name);
        }

        public abstract UnaryOperator<AbstractCalendarPerformerStatistic> increment();

        public abstract UnaryOperator<AbstractCalendarPerformerStatistic> decrement();
    }

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id")
    private Performer performer;

    @Column(name = "is_custom")
    private Boolean custom;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH})
    private List<Task> taskList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DefaultStatus getStatus() {
        if (name != null) {
            return DefaultStatus.getByName(name);
        } else {
            return null;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Performer getPerformer() {
        return this.performer;
    }

    public void setPerformer(final Performer performer) {
        this.performer = performer;
    }

    public Boolean getCustom() {
        return this.custom;
    }

    public void setCustom(final Boolean custom) {
        this.custom = custom;
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public void setTaskList(final List<Task> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task task) {
        if (taskList == null) {
            taskList = new LinkedList<>();
        }
        taskList.add(task);
    }
}
