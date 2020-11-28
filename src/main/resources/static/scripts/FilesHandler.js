"use strict";

function _instanceof(left, right) { if (right != null && typeof Symbol !== "undefined" && right[Symbol.hasInstance]) { return !!right[Symbol.hasInstance](left); } else { return left instanceof right; } }

function _classCallCheck(instance, Constructor) { if (!_instanceof(instance, Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

(function (Factory) {
    var Loader = Factory.getClass("Loader");

    var FilesHandler = /*#__PURE__*/function () {
        function FilesHandler() {
            var _this = this;

            _classCallCheck(this, FilesHandler);

            this.Modal = Factory.getClass("Modal");
            this.Selector = Factory.getClass("Selector", {
                on: function on(target) {
                    return _this._select(target);
                },
                off: function off(target) {
                    return _this._unselect(target);
                }
            });
            this.FilesManager = Factory.getClass("FilesManager");
            this._table = document.querySelector(".body tbody");
            this.Http = Factory.getClass("Http");
            this.init("#newTodo", function () {
                return _this.Modal.render("newTodo", _this.Selector.selected);
            });
            this.init("#existTodo", function () {
                return _this.Modal.render("existTodo", _this.Selector.selected);
            });
            this.init("#shareFiles", function () {
                return _this.Modal.render("shareFiles", _this.Selector.selected);
            });
            this.init("#downloadFiles", function () {
                return _this.FilesManager.download(_this.Selector.selected.map(function (f) {
                    return f.dataset.fileId;
                }));
            });
            this.init("#uploadFiles", function () {
                return _this.Modal.render("uploadFiles");
            });
            this.init("#filter", function () {
                return _this.Modal.render("filesFilter");
            });
            this.Data = Factory.getClass("Data");
            this.Data.get("Files").then(function (data) {
                return _this.render(data);
            });
            Factory.getClass("Pagination").init(".pagination", "/com/archive/doc/list", "FilesHandler");
            Factory.getClass("Notifications").render(document.querySelector(".tool-bar .notifications"));

            this._table.closest(".body").querySelector("thead #sortByData i").onclick = function (e) {
                var btn = e.target;

                if (btn.classList.contains("fa-caret-down")) {
                    _this.Http.get("/com/archive/doc/list/1?reverse=true", function (data) {
                        btn.classList.remove("fa-caret-down");
                        btn.classList.add("fa-caret-up");

                        _this.render(data);

                        Factory.getClass("Pagination").init(".pagination", "/com/archive/doc/list?reverse=true", "FilesHandler");
                    });
                } else {
                    _this.Data.get("Files").then(function (data) {
                        btn.classList.remove("fa-caret-up");
                        btn.classList.add("fa-caret-down");

                        _this.render(data);

                        Factory.getClass("Pagination").init(".pagination", "/com/archive/doc/list", "FilesHandler");
                    });
                }
            };
        }

        _createClass(FilesHandler, [{
            key: "_select",
            value: function _select(target) {
                target.closest('.table-row').querySelector('input[type="checkbox"]').checked = true;
            }
        }, {
            key: "_unselect",
            value: function _unselect(target) {
                target.closest('.table-row').querySelector('input[type="checkbox"]').checked = false;
            }
        }, {
            key: "init",
            value: function init(selector, callback) {
                document.querySelector("".concat(selector)).addEventListener("click", function () {
                    return callback();
                });
            }
        }, {
            key: "render",
            value: function render() {
                var _this2 = this;

                var data = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];
                this._table.innerHTML = "";
                data.forEach(function (f) {
                    return $(_this2._table).append("\n                <tr class=\"table-row\" data-file-id=\"".concat(f.id, "\">\n                    <td>\n                        <div class=\"td-wrapper\">\n                            <img data-src=\"/img/docs-img/").concat(f.extName.substr(1), ".png\" alt=\"\" />\n                            <span class=\"name\">\n                               ").concat(f.name, "\n                            </span>\n                            <span class=\"extension\" style=\"opacity:0\">\n                                ").concat(f.extName, "\n                            </span>\n                        </div>\n                    </td>\n                    <td data-name=\"date\">").concat(f.date, "</td>\n                    <td data-name=\"performer\">").concat(Factory.getClass("User").get(f.performerId).name, "</td>\n                    <td data-name=\"tools\">\n                        <div class=\"td-wrapper\">\n                            <div class=\"form-check\">\n                                <input class=\"form-check-input position-static\" type=\"checkbox\" />\n                            </div>\n                        </div>\n                    </td>\n                </tr>"));
                });
                Loader.hide();
                $(this._table).find('[data-src]').Lazy({
                    effect: 'fadeIn',
                    effectTime: 200,
                    threshold: this._table.scrollHeight,
                    visibleOnly: false,
                    onError: function onError(element) {
                        console.log('error loading ' + element.data('src'));
                    },
                    autoDestroy: true
                });
                this.Selector.init(document.querySelectorAll('.body .table-row'));
            }
        }]);

        return FilesHandler;
    }();

    Factory.setSingletone("FilesHandler", FilesHandler);
})(window.Factory);