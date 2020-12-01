(function (Factory) {
    class BoardsHandler {

        constructor() {
            this.Modal = Factory.getClass("Modal");

            this.Data = Factory.getClass("Data");
            this.Loader = Factory.getClass("Loader");
            this.Http = Factory.getClass("Http");
            this.Dropdown = Factory.getClass("Dropdown");
            this.Alert = Factory.getClass("Alert");
            this._table = document.querySelector(".body tbody");
            this.Lang = Factory.getClass("Lang")

            this.init("#newTodo", () => this.Modal.render("newTodo"));
            this.Data.get("Todos").then(data => this.render(data));

            Factory.getClass("Pagination").init(".pagination", "/com//task/list", "BoardsHandler");
            Factory.getClass("Notifications").render(document.querySelector(".tool-bar .notifications"));

            this.Data.get("Departments").then(data => {
                let list = document.querySelector("#departmentsList");
                data.forEach(d => {
                    $(list).prepend(`
                        <button class="dropdown-item" href="#">${d.name}</button>
                    `); list.querySelector(".dropdown-item").onclick = () => {
                        this.Loader.show("infinity");
                        list.closest(".dropdown").querySelector("#selected-option").innerHTML = d.name;
                        this.Http.get(`/com/task/list/1?depId=${d.id}`, data => this.render(data));
                        Factory.getClass("Pagination").init(".pagination", `/com/task/list?depId=${d.id}`, "BoardsHandler");
                    };
                });
                list.querySelector('[data-event="alldeps"]').onclick = () => {
                    this.Loader.show("infinity");
                    list.closest(".dropdown").querySelector("#selected-option").innerHTML = "Усі";
                    this.Data.get("Todos").then(data => this.render(data));
                    Factory.getClass("Pagination").init(".pagination", "/com/task/list", "BoardsHandler");
                };
            });

            const filterByStatus = status => {
                this.Http.get(`/com/task/list${status ? `/${status}` : ``}`, data => this.render(data));
                Factory.getClass("Pagination").init(".pagination", `/com/task/list${status ? `/${status}` : ``}`, "BoardsHandler");
            };

            let statusFilter = this._table.closest(".body").querySelector("thead #boardFilterByStatus");
            statusFilter.querySelector('[value="new"]').onclick = () => filterByStatus("NEW");
            statusFilter.querySelector('[value="inProgress"]').onclick = () => filterByStatus("INPROGRESS");
            statusFilter.querySelector('[value="ovedue"]').onclick = () => filterByStatus("OVERDUE");
            statusFilter.querySelector('[value="completed"]').onclick = () => filterByStatus("COMPLETED");
            statusFilter.querySelector('[value="onhold"]').onclick = () => filterByStatus("ONHOLD");
            statusFilter.querySelector('[value="all"]').onclick = () => filterByStatus();
        }

        init(selector, callback) {
            document.querySelector(`${selector}`).addEventListener("click", () => callback());
        }

        render(data = []) {
            this._table.innerHTML = "";
            data.forEach(t => {
                let manager = t.owner;
                $(this._table).append(`
                <tr class="table-row todo" data-todo-id="${t.id}">
                    <td>
                        <div class="td-wrapper">
                            <img data-placement="auto" data-toggle="tooltip" title="${manager.name}" data-name="manager" data-src="${manager.imgPath}" alt="" />
                            <button title="натисніть для більш детальної інформації" class="name">
                               ${t.name}
                            </button>
                        </div>
                    </td>
                    <td data-name="date" class="control">
                        <div class="calendar drop-down">
                            <button data-event="toggle">
                                <span>
                                    <span data-name="selectedDate">00.00.0000</span>
                                </span>
                            </button>
                            <div style="display:none" class="drop-down_menu">
                                <div class="control w-100 d-flex justify-content-between align-items-center"
                                    style="padding: 12px">
                                    <i class="fa fa-caret-left" data-event="previous"></i>
                                    <div class="d-flex">
                                        <span data-name="pickerMonth" class="mr-1">Квітень</span>
                                        <span data-name="pickerYear" class="ml-1">2020</span>
                                    </div>
                                    <i class="fa fa-caret-right" data-event="next"></i>
                                </div>
                                <table class="date-picker" valign="center">
                                    <thead>
                                        <tr>
                                            <td class="week-day">
                                                <div>Пн</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Вт</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Ср</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Чт</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Пт</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Сб</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Нд</div>
                                            </td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div class="date-item active">1</div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </td>
                    <td data-name="date" class="deadline">           
                        <div class="calendar drop-down">
                            <button data-event="toggle">
                                <span>
                                    <span data-name="selectedDate">00.00.0000</span>
                                </span>
                            </button>
                            <div style="display:none" class="drop-down_menu">
                                <div class="control w-100 d-flex justify-content-between align-items-center"
                                    style="padding: 12px">
                                    <i class="fa fa-caret-left" data-event="previous"></i>
                                    <div class="d-flex">
                                        <span data-name="pickerMonth" class="mr-1">Квітень</span>
                                        <span data-name="pickerYear" class="ml-1">2020</span>
                                    </div>
                                    <i class="fa fa-caret-right" data-event="next"></i>
                                </div>
                                <table class="date-picker" valign="center">
                                    <thead>
                                        <tr>
                                            <td class="week-day">
                                                <div>Пн</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Вт</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Ср</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Чт</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Пт</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Сб</div>
                                            </td>
                                            <td class="week-day">
                                                <div>Нд</div>
                                            </td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div class="date-item active">1</div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </td>
                    <td data-name="status">
                        <div class="status ${t.status}"></div>
                        <span class="status-name">${this.Lang.get(t.status)}</span>
                    </td>
                    <td>
                        <div data-name="performers">
                        </div>
                    </td>
                </tr>`);

                let todo = this._table.querySelector('.table-row:last-child [data-name="performers"]')
                t.performerIds.forEach((id, i) => {
                    Factory.getClass("User").get(id).then(user => {
                        let p = user
                        if (i < 5) {
                            $(todo).append(
                                `<div class="performer" data-placement="auto" data-toggle="tooltip" title="${p.name}"><img data-src="${p.imgPath}"/></div>`
                            )
                        }
                        if (i == t.performerIds.length - 1 && i > 5) {
                            $(todo).append(
                                `<span class="ml-3">${t.performerIds.length - 5} + </span>`
                            )
                        }

                        $(this._table).find('.performer[data-toggle="tooltip"]').tooltip()

                        $(this._table).find('.performer [data-src]').Lazy({
                            effect: 'fadeIn',
                            effectTime: 200,
                            threshold: this._table.scrollHeight,
                            visibleOnly: false,
                            onError: function (element) {
                                console.log('error loading ' + element.data('src'));
                            },
                            autoDestroy: true
                        });

                    })
                })

                let deadline = Factory.getClass("Datepicker", this._table.querySelector(`[data-todo-id="${t.id}"] .deadline .calendar`), t.deadlineDate, target => {
                    this.Alert.render("confirm", "Дату дедлайну буде змінено. Ви впевнені?", {
                        confirm: () => {
                            this.Loader.show("infinity");
                            this.Http.post("/com/task/modify/deadline", { date: deadline.getDate(), taskId: t.id }, res => {
                                this.Loader.hide(() => {
                                    console.log({ date: deadline.getDate() });
                                    if (res.success) {
                                        this.Alert.render("success", "Дату змінено.");
                                    } else {
                                        this.Alert.render("danger", "Сталася помилка." + res.msg.substr(0, 32) + "...");
                                        deadline.set(t.deadlineDate);
                                    }
                                });
                            });
                        },
                        unConfirm: () => {
                            deadline.set(t.deadlineDate);
                        }

                    });
                });
                let control = Factory.getClass("Datepicker", this._table.querySelector(`[data-todo-id="${t.id}"] .control .calendar`), t.controlDate, target => {
                    this.Alert.render("confirm", "Дату контролю буде змінено. Ви впевнені?", {
                        confirm: () => {
                            this.Loader.show("infinity");
                            this.Http.post("/com/task/modify/control", { date: control.getDate(), taskId: t.id }, res => {
                                this.Loader.hide(() => {
                                    console.log({ date: control.getDate() });
                                    if (res.success) {
                                        this.Alert.render("success", "Дату змінено.");
                                    } else {
                                        this.Alert.render("danger", "Сталася помилка." + res.msg.substr(0, 32) + "...");
                                        control.set(t.controlDate);
                                    }
                                });
                            });
                        },
                        unConfirm: () => {
                            control.set(t.controlDate);
                        }

                    });
                });
                this._table.querySelector(`[data-todo-id="${t.id}"] .td-wrapper .name`).onclick = e => {
                    this.Modal.render("todoInfo", e.target.closest(".todo"));
                };
            }); $(this._table).find('[data-src]').Lazy({
                effect: 'fadeIn',
                effectTime: 200,
                threshold: this._table.scrollHeight,
                visibleOnly: false,
                onError: function (element) {
                    console.log('error loading ' + element.data('src'));
                },
                autoDestroy: true
            });
            this.Data.get("User").then(user => {
                if (user.role == "MANAGER" || user.role == "G_MANAGER" || user.role == "ADMIN") this.Dropdown.init(this._table.closest(".body"), { single: true });
            });
            $(this._table).find('[data-toggle="tooltip"]').tooltip();
            this.Loader.hide();
        }

    }

    Factory.setSingletone("BoardsHandler", BoardsHandler);
})(window.Factory);