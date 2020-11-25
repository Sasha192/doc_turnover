"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Dropdown = function () {
        function Dropdown() {
            var _this = this;

            _classCallCheck(this, Dropdown);

            document.querySelectorAll(".drop-down").forEach(function (n) {
                var btn = n.querySelector('[data-event="toggle"]');
                if (btn) {
                    btn.onclick = function (e) {
                        return !n.classList.contains("active") ? _this.open(n) : _this.close(n);
                    };
                }
                window.addEventListener("click", function (e) {
                    if (e.target.closest(".drop-down")) {
                        return;
                    } else _this.close(n);
                });
            });
        }

        _createClass(Dropdown, [{
            key: "open",
            value: function open(n) {
                var m = n.querySelector(".drop-down_menu");
                if (m) {
                    m.style.display = "block";m.style.opacity = 1;
                }
                n.classList.add("active");
            }
        }, {
            key: "close",
            value: function close(n) {
                var m = n.querySelector(".drop-down_menu");
                if (m) {
                    m.style.opacity = 0;m.style.display = "none";
                }
                n.classList.remove("active");
            }
        }, {
            key: "init",
            value: function init(selector) {
                var _this2 = this;

                var options = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

                selector.querySelectorAll(".drop-down").forEach(function (n) {
                    var btn = n.querySelector('[data-event="toggle"]');

                    if (btn) {
                        btn.onclick = function (e) {
                            if (!n.classList.contains("active")) {
                                if (options.single) {
                                    var active = selector.querySelector(".active");if (active) _this2.close(active);
                                };_this2.open(n);
                            } else _this2.close(n);
                        };
                    }

                    window.addEventListener("click", function (e) {
                        if (e.target.closest(".drop-down")) {
                            return;
                        } else _this2.close(n);
                    });
                });
            }
        }]);

        return Dropdown;
    }();

    Factory.setSingletone("Dropdown", Dropdown);
})(window.Factory);