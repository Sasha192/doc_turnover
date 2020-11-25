"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Alert = function () {
        function Alert() {
            _classCallCheck(this, Alert);

            this.alerts = {};

            this._init("warning", "\n            <div style=\"display: none\" class=\"alert alert-warning alert-dismissible\" role=\"alert\">\n                <strong></strong>\n                <msg></msg>\n                <button type=\"button\" class=\"close\" aria-label=\"Close\">\n                    <span aria-hidden=\"true\">&times;</span>\n                </button>\n            </div> ");
            this._init("success", "\n            <div style=\"display: none\" class=\"alert alert-success alert-dismissible\" role=\"alert\">\n                <strong>\u0423\u0441\u043F\u0456\u0448\u043D\u043E! </strong>\n                <msg></msg>\n                <button type=\"button\" class=\"close\" aria-label=\"Close\">\n                    <span aria-hidden=\"true\">&times;</span>\n                </button>\n            </div>\n            ");
            this._init("danger", "\n            <div class=\"alert alert-danger alert-dismissible\" role=\"alert\">\n                <strong></strong>\n                <msg></msg>\n                <button type=\"button\" class=\"close\" aria-label=\"Close\">\n                    <span aria-hidden=\"true\">&times;</span>\n                </button>\n            </div>\n            ");
            this._init("confirm", "\n            <div class=\"alert alert-primary alert-dismissible\" role=\"alert\">\n                <strong></strong>\n                <msg></msg>\n                <button class=\"text-primary ml-3\">\n                    \u041F\u0456\u0434\u0442\u0432\u0435\u0440\u0434\u0438\u0442\u0438\n                </button>\n                <button type=\"button\" class=\"close\" aria-label=\"Close\">\n                    <span aria-hidden=\"true\">&times;</span>\n                </button>\n            </div>\n            ");
        }

        _createClass(Alert, [{
            key: "render",
            value: function render(alertClass, msg) {
                var _this = this;

                var obj = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};

                var alert = this.alerts[alertClass];
                $("alerts").prepend(alert); alert = document.querySelector("alerts .alert");
                var interval = setTimeout(function () {
                    return _this._close(alert, obj);
                }, 7000);
                alert.querySelector(".close").onclick = function () {
                    _this._close(alert, obj); clearTimeout(interval);
                };
                alert.querySelector("msg").innerHTML = msg; $(alert).slideDown(100);
                if (obj.confirm) {
                    var button = alert.querySelector("button");
                    if (button) button.onclick = function () {
                        obj.confirm(); alert.classList.add("confirm"); _this._close(alert, obj);
                    };
                }
            }
        }, {
            key: "_init",
            value: function _init(alertClass, template) {
                this.alerts[alertClass] = template;
            }
        }, {
            key: "_close",
            value: function _close(alert, obj) {
                if (obj.unConfirm && !alert.classList.contains("confirm")) obj.unConfirm();
                $(alert).slideUp(100, function () {
                    return $(alert).remove();
                });
            }
        }]);

        return Alert;
    }();

    Factory.setSingletone("Alert", Alert);
})(window.Factory);