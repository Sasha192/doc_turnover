(function (Factory) {

    class ECPanel {
        constructor() {
            this.Dropdown = Factory.getClass("Dropdown");
            this.Data = Factory.getClass("Data");
            this.Loader = Factory.getClass("Loader");
            this.Lang = Factory.getClass("Lang");
            this.Alert = Factory.getClass("Alert");
            this.Http = Factory.getClass("Http");
            this.Modal = Factory.getClass("Modal");

            this._table = document.querySelector(".enterprises");
            this.Data.get("Enterprises").then(list => this.render(list));

            let input = document.querySelector(".create-department__input"),
                btn = document.querySelector(".create-department__button");

            btn.onclick = () => {
                if (input.value.trim().length == 0) return;
                if (input.value.trim().length > 64) return this.Alert.render("warning", "Назва не більше 64 символів");
                this.Loader.show("infinity");
                this.Http.post("/main/tenants/create", { companyName: input.value.trim(), tariff: "FREE"}, res => {
                    input.value = "";
                    this.Loader.hide(() => {
                        if (res.success) {
                            this.Alert.render("success", " створено.");
                            this.Data.update("Enterprises").then(data => this.render(data));
                            this.Modal.render("tariffs", res.msg);
                        } else this.Alert.render("danger", "Сталася помилка." + res.msg.substr(0, 32) + "...");
                    });
                });
            };
        }

        render(list = []) {
            this._table.innerHTML = "";
            list.forEach(e => {
                var tenantIdId = e._id;
                $(this._table).append(`
                <tr class="table-row" data-enterprise-id="${tenantIdId}">
                    <td>
                        <div class="td-wrapper">
                            <span class="name" ${e.active ? 'style="border-left: 3px solid #2196f3; padding: 0 20px"' : ``}>
                            ${e.companyName}
                            </span>
                        </div>
                    </td>
                    <td data-name="date">${e.number}</td>
                    <td data-name="space">${e.creationDate}</td>
                    <td data-name="space">${e.space} / ${e.maxSpace}Gb</td>
                    <td data-name="space">${this.Lang.get(e.tariff)}</td>
                    <td data-name="tools">
                       <div class="drop-down">
                       <button data-event="toggle">
                            <i style="font-size:16px" class="far fa-ellipsis-h"></i>
                       </button>
                       <div style="width:250px; right:25px;" class="drop-down_menu">
                           <div class="drop-down__list">
                                ${!e.active ? '<button data-event="toActive" class="item">Обрати як активне <i class="fal fa-toggle-on"></i></button>' : ``}
                                <button data-event="addNewEmployes" class="item">Додати співробітників<i class="far fa-users"></i></button>
                                <button data-event="changeTariff" class="item">Змінити тариф <i class="far fa-shopping-cart"></i></button>
                                <button data-event="delete" class="item">Видалити <i class="far fa-trash"></i></button>
                           </div>
                       </div>
                   </div>
                    </td>
                </tr> `);

                let row = this._table.querySelector(".table-row:last-child");
                let toActive = row.querySelector('[data-event="toActive"]');if (toActive) toActive.onclick = event => {
                    let btn = event.target.closest("button");
                    btn.setAttribute("disabled", "true");
                    this.Loader.show("infinity");
                    this.Http.get(`/main/tenants/connect?tenantId=${tenantIdId}`, res => {
                        btn.removeAttribute("disabled");
                        if (res.success) {
                            this.Data.update("Enterprises").then(data => this.render(data));
                        } else this.Loader.hide(() => {
                            this.Alert.render("danger", `Сталася помилка: ${res.msg.substr(0, 32)}...`);
                        });
                    });
                };

                row.querySelector('[data-event="delete"]').onclick = event => {
                    let btn = event.target.closest("button");
                    btn.setAttribute("disabled", "true");
                    this.Loader.show("infinity");
                    this.Http.get(`/main/tenants/remove?tenantId=${tenantIdId}`, res => {
                        btn.removeAttribute("disabled");
                        if (res.success) {
                            this.Data.update("Enterprises").then(data => this.render(data));
                        } else this.Loader.hide(() => {
                            this.Alert.render("danger", `Сталася помилка: ${res.msg.substr(0, 32)}...`);
                        });
                    });
                };
                row.querySelector('[data-event="changeTariff"]').onclick = () => {
                    this.Modal.render("tariffs", tenantIdId);
                };
                row.querySelector('[data-event="addNewEmployes"]').onclick = () => {
                    this.Modal.render("addNewEmployes", tenantIdId);
                };
            });

            this.Dropdown.init(document.querySelector(".enterprises"), { single: true });
            this.Loader.hide();
        }
    }

    Factory.setSingletone("ECPanel", ECPanel);
})(window.Factory);