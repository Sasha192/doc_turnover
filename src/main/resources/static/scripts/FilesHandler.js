(function (Factory) {
    const Loader = Factory.getClass("Loader");

    class FilesHandler {

        constructor() {
            this.Modal = Factory.getClass("Modal");
            this.Selector = Factory.getClass("Selector", { on: target => this._select(target), off: target => this._unselect(target) });
            this.FilesManager = Factory.getClass("FilesManager");
            this._table = document.querySelector(".body tbody");
            this.Http = Factory.getClass("Http");

            this.init("#newTodo", () => this.Modal.render("newTodo", this.Selector.selected));
            this.init("#existTodo", () => this.Modal.render("existTodo", this.Selector.selected));
            this.init("#shareFiles", () => this.Modal.render("shareFiles", this.Selector.selected));
            this.init("#downloadFiles", () => this.FilesManager.download(this.Selector.selected.map(f => f.dataset.fileId)));
            this.init("#uploadFiles", () => this.Modal.render("uploadFiles"));
            this.init("#filter", () => this.Modal.render("filesFilter"));

            this.Data = Factory.getClass("Data");
            this.Data.get("Files").then(data => this.render(data));
            Factory.getClass("Pagination").init(".pagination", "/com/archive/doc/list", "FilesHandler");

            Factory.getClass("Notifications").render(document.querySelector(".tool-bar .notifications"));
            this._table.closest(".body").querySelector("thead #sortByData i").onclick = e => {
                let btn = e.target;
                if (btn.classList.contains("fa-caret-down")) {
                    this.Http.get("/com/archive/doc/list/1?reverse=true", data => {
                        btn.classList.remove("fa-caret-down");
                        btn.classList.add("fa-caret-up");
                        this.render(data);
                        Factory.getClass("Pagination").init(".pagination", "/com/archive/doc/list?reverse=true", "FilesHandler");
                    });
                } else {
                    this.Data.get("Files").then(data => {
                        btn.classList.remove("fa-caret-up");
                        btn.classList.add("fa-caret-down");
                        this.render(data);
                        Factory.getClass("Pagination").init(".pagination", "/com/archive/doc/list", "FilesHandler");
                    });
                }
            };
        }

        _select(target) {
            target.closest('.table-row').querySelector('input[type="checkbox"]').checked = true;
        }

        _unselect(target) {
            target.closest('.table-row').querySelector('input[type="checkbox"]').checked = false;
        }

        init(selector, callback) {
            document.querySelector(`${selector}`).addEventListener("click", () => callback());
        }

        render(data = []) {
            this._table.innerHTML = "";
            data.forEach(f => $(this._table).append(`
                <tr class="table-row" data-file-id="${f.id}">
                    <td>
                        <div class="td-wrapper">
                            <img data-src="./img/docs-img/${f.extName.substr(1)}.png" alt="" />
                            <span class="name">
                               ${f.name}
                            </span>
                            <span class="extension">
                                ${f.extName}
                            </span>
                        </div>
                    </td>
                    <td data-name="date">${f.date}</td>
                    <td data-name="performer">${Factory.getClass("User").get(f.performerId).name}</td>
                    <td data-name="tools">
                        <div class="td-wrapper">
                            <div class="form-check">
                                <input class="form-check-input position-static" type="checkbox" />
                            </div>
                        </div>
                    </td>
                </tr>`));

            Loader.hide()

            $(this._table).find('[data-src]').Lazy({
                effect: 'fadeIn',
                effectTime: 200,
                threshold: this._table.scrollHeight,
                visibleOnly: false,
                onError: function (element) {
                    console.log('error loading ' + element.data('src'));
                },
                autoDestroy: true
            });
            this.Selector.init(document.querySelectorAll('.body .table-row'));
        }

    }

    Factory.setSingletone("FilesHandler", FilesHandler);
})(window.Factory);