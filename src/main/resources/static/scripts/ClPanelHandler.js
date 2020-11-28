(function (Factory) {
    class ClPanel {

        constructor() {
            this.Dropdown = Factory.getClass("Dropdown");
            this.Data = Factory.getClass("Data");
            this.Loader = Factory.getClass("Loader");
            this._table = $(".departments");
            this.Lang = Factory.getClass("Lang");
            this.Alert = Factory.getClass("Alert");
            this.Http = Factory.getClass("Http");

            Factory.getClass("Notifications").render(document.querySelector(".tool-bar .notifications"));
            this.Data.get("Performers").then(list => this.render(list));

            let input = document.querySelector(".create-department__input"),
                btn = document.querySelector(".create-department__button");
            btn.onclick = () => {
                if (input.value.trim().length == 0) return;
                if (input.value.trim().length > 64) return this.Alert.render("warning", "Назва не більше 64 символів");
                this.Loader.show("infinity");
                this.Http.get(`/com/departments/create?name=${input.value.trim()}`, res => {
                    input.value = "";
                    this.Loader.hide(() => {
                        if (res.success) {
                            this.Alert.render("success", "Відділ створено.");
                            this.Data.update("Performers").then(data => this.render(data));
                        } else this.Alert.render("danger", "Сталася помилка.");
                    });
                });
            };
        }

        render(list = []) {
            this._table.html("");

            list.sort((a, b) => {
                let nameA = a.department ? a.department.name : a.department,
                    nameB = b.department ? b.department.name : b.department;
                if (nameA || nameB) {
                    if (nameA > nameB) return 1;
                    if (nameB > nameA) return -1;
                };return 0;
            });

            const changeRole = e => {
                let btn = e.target;

                if (btn.closest('td').querySelector('[data-event="toggle"]').innerText.trim() == btn.innerText.trim()) return;

                let userId = btn.closest(".department__user").dataset.userId,
                    value = btn.innerText.trim(),
                    role = Object.keys(this.Lang.words).find(key => this.Lang.words[key] === value);

                role = role.toUpperCase()

                btn.style.pointerEvents = "none";
                this.Alert.render("confirm", "Роль буде змінено. Ви впевнені?", {
                    confirm: () => {
                        this.Loader.show("infinity");
                        this.Http.post("/com/performers/modify/role", { performerId: userId, role }, res => {
                            this.Loader.hide(() => {
                                btn.style.pointerEvents = "auto";
                                console.log({ userId, role });
                                if (res.success) {
                                    this.Alert.render("success", "Роль змінено.");
                                    btn.closest('td').querySelector('[data-event="toggle"]').innerText = value;
                                } else {
                                    this.Alert.render("danger", "Сталася помилка.");
                                }
                            });
                        });
                    },
                    unConfirm: () => {
                        btn.style.pointerEvents = "auto";
                    }
                });
            };

            const changeDep = e => {
                let btn = e.target;

                if (btn.closest('td').querySelector('[data-event="toggle"]').innerText.trim() == btn.innerText.trim()) return;

                let userId = btn.closest(".department__user").dataset.userId,
                    value = btn.dataset.departmentId;

                btn.style.pointerEvents = "none";
                this.Alert.render("confirm", "Відділ буде змінено. Ви впевнені?", {
                    confirm: () => {
                        this.Loader.show("infinity");
                        this.Http.post("/com/performers/modify/department", { performerId: userId, departmentId: value }, res => {
                            btn.style.pointerEvents = "auto";
                            this.Loader.hide(() => {
                                if (res.success) {
                                    this.Alert.render("success", "Відділ змінено.");
                                    this.Data.update("Performers").then(data => this.render(data));
                                } else {
                                    this.Alert.render("danger", "Сталася помилка.");
                                }
                            });
                        });
                    },
                    unConfirm: () => {
                        btn.style.pointerEvents = "auto";
                    }
                });
            };

            const removeUser = e => {
                let btn = e.target,
                    userId = btn.closest(".department__user").dataset.userId;
                btn.style.pointerEvents = "none";
                this.Alert.render("confirm", "Користувача буде видалено з системи. Ви впевнені?", {
                    confirm: () => {
                        this.Loader.show("infinity");
                        this.Http.post("/com/performers/remove", { performerId: userId }, res => {
                            this.Loader.hide(() => {
                                btn.style.pointerEvents = "auto";
                                console.log({ userId });
                                if (res.success) {
                                    this.Alert.render("success", "Користувача видалено.");
                                    let user = $(btn.closest('tr'));
                                    console.log(user.next(), user.prev());
                                    if (!user.next().hasClass("department__user") && !user.prev().hasClass("department__user")) {
                                        user.before(`   
                                        <tr class="department__no-assigned">
                                        <td>
                                             Немає учасників...
                                         </td>
                                        </tr>`);
                                    }
                                    user.slideDown(400, () => user.remove());
                                    this.Data.update("Performers");
                                } else {
                                    this.Alert.render("danger", "Сталася помилка.");
                                }
                            });
                        });
                    },
                    unConfirm: () => {
                        btn.style.pointerEvents = "auto";
                    }
                });
            };

            const removeDep = e => {
                let depId = e.target.closest('[data-department-id]').dataset.departmentId,
                    btn = e.target;
                btn.style.pointerEvents = "none";
                this.Alert.render("confirm", "Відділ буде видалено. Ви впевнені?", {
                    confirm: () => {
                        this.Loader.show("infinity");
                        this.Http.get(`/com/departments/remove?departmentId=${depId}`, res => {
                            btn.style.pointerEvents = "auto";
                            console.log({ depId });
                            this.Loader.hide(() => {
                                if (res.success) {
                                    this.Alert.render("success", "Відділ видалено.");
                                    this.Data.update("Performers").then(data => this.render(data));
                                } else this.Alert.render("danger", "Сталася помилка.");
                            });
                        });
                    },
                    unConfirm: () => {
                        btn.style.pointerEvents = "auto";
                    }
                });
            };

            let lastDep;list.forEach(u => {
                if (u.department) {
                    if (lastDep !== u.department.name) {
                        lastDep = u.department.name;
                        this._table.append(` 
                    <tr data-department-id="${u.department.id}" class="department__name">
                        <td>
                            ${u.department.name}
                            <div class="drop-down d-inline-block">
                                <button data-event="toggle">
                                    <i class="department__management fa fa-ellipsis-h"></i>
                                </button>
                                <div class="drop-down_menu">
                                    <div class="drop-down__list">
                                         <div value="delete" class="item">Видалити</div>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                    </tr>`);
                        this._table[0].querySelector(`[data-department-id="${u.department.id}"] [value="delete"]`).onclick = e => removeDep(e);
                    }
                } else if(lastDep !== "Нерозподілені"){
                    lastDep = "Нерозподілені"
                    this._table.append(` 
                    <tr class="department__name"><td>Нерозподілені</td><td>
                    </tr>`);
                }

                this._table.append(` 
                <tr class="department__user" data-user-id=${u.id}>
                <td>
                    <div class="td-wrapper">
                        <img data-src="${u.imgPath}" alt="">
                        ${u.name}
                    </div>
                </td>
                <td>${u.email ? u.email : ``}</td>
                <td>
                    <div class="drop-down">
                    <button data-event="toggle">
                        ${this.Lang.get(u.role)}
                    </button>
                    <div class="drop-down_menu">
                        <div class="drop-down__list roleList">
                            <div data-value="G_MANAGER" class="item">Головний менеджер</div>
                            <div data-value="MANAGER" class="item">Менеджер</div>
                            <div data-value="SECRETARY" class="item">Секретар</div>
                            <div data-value="PERFORMER" class="item">Виконавець</div>
                        </div>
                    </div>
                </div>
                </td>
                <td>
                    <div class="drop-down">
                        <button data-event="toggle">
                            ${u.department ? u.department.name : "Нерозподілено"}
                        </button>
                        <div class="drop-down_menu">
                            <div class="drop-down__list depList">

                            </div>
                        </div>
                    </div>
                </td>
                <td>
                    <i class="deaprtment__remove-user fa fa-trash"></i>
                </td>
                </tr>`);
                let user = this._table[0].querySelector(`[data-user-id="${u.id}"]`);

                user.querySelector('.deaprtment__remove-user').onclick = e => removeUser(e);
                user.querySelector('[data-value="G_MANAGER"]').onclick = e => changeRole(e);
                user.querySelector('[data-value="MANAGER"]').onclick = e => changeRole(e);
                user.querySelector('[data-value="SECRETARY"]').onclick = e => changeRole(e);
                user.querySelector('[data-value="PERFORMER"]').onclick = e => changeRole(e);
            });

            this.Data.update("Departments").then(data => {
                let depList = '';
                data.forEach(d => {
                    let dep = this._table[0].querySelector(`[data-department-id="${d.id}"]`);
                    if (!dep) {
                        this._table.append(` 
                        <tr data-department-id="${d.id}" class="department__name">
                            <td>
                                ${d.name}
                                <div class="drop-down d-inline-block">
                                    <button data-event="toggle">
                                        <i class="department__management fa fa-ellipsis-h"></i>
                                    </button>
                                    <div class="drop-down_menu">
                                        <div class="drop-down__list">
                                            <div value="delete" class="item">Видалити</div>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td>
                        </tr>`);

                        this._table[0].querySelector(`[data-department-id="${d.id}"] [value="delete"]`).onclick = e => removeDep(e);

                        this._table.append(`     
                        <tr class="department__no-assigned">
                            <td>
                                 Немає учасників...
                             </td>
                        </tr>`);

                        this.Dropdown.init(document.querySelector(`[data-department-id="${d.id}"]`), { single: true });
                    }
                    depList += `<div data-department-id="${d.id}" class="item">${d.name}</div>`;
                });
                this._table[0].querySelectorAll(".depList").forEach(list => {
                    $(list).append(depList);
                    list.querySelectorAll('[data-department-id]').forEach(d => d.onclick = e => changeDep(e));
                });
            });

            this.Dropdown.init(document.querySelector(".departments"), { single: true });
            this._table.find('[data-src]').Lazy({
                effect: 'fadeIn',
                effectTime: 200,
                threshold: this._table.scrollHeight,
                visibleOnly: false,
                onError: function (element) {
                    console.log('error loading ' + element.data('src'));
                },
                autoDestroy: true
            });
            this.Loader.hide();
        }

    }

    Factory.setSingletone("ClPanel", ClPanel);
})(window.Factory);