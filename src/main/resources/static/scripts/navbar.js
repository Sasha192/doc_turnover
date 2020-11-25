"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Navbar = function () {
        function Navbar() {
            _classCallCheck(this, Navbar);

            this.elements = {};

            this.init({
                "charts": document.querySelectorAll('[data-event="charts"]'),
                "files": document.querySelectorAll('[data-event="files"]'),
                "settigns": document.querySelectorAll('[data-event="settings"]'),
                'clPanel': document.querySelectorAll('[data-event="clPanel"]'),
                "boards": document.querySelectorAll('[data-event="boards"]'),
                "econfig": document.querySelectorAll('[data-event="enterpriseCongfig"]'),
                "uconfig": document.querySelectorAll('[data-event="userConfig"]')
            });

            Factory.getClass("Data").get("User").then(function (user) {
                document.querySelectorAll('[data-name="profileImg"] img').forEach(function (img) {
                    return img.src = user.imgPath;
                });
            });
        }

        _createClass(Navbar, [{
            key: "init",
            value: function init(obj) {
                var _this = this;

                var _loop = function _loop(key) {
                    _this.elements[key] = { observers: [] };

                    var sub = function () {
                        for (var i = 0; i < arguments.length; i++) {
                            var f = arguments[i];
                            this.elements[key].observers.push(f);
                        }
                    }.bind(_this);

                    _this.elements[key].sub = sub;

                    var unsub = function () {
                        var _arguments = arguments,
                            _this2 = this;

                        var _loop2 = function _loop2(i) {
                            var f = _arguments[i];
                            _this2.elements[key].observers = constructor.elements[key].observers.filter(function (m) {
                                return m !== f;
                            });
                        };

                        for (var i = 0; i < arguments.length; i++) {
                            _loop2(i);
                        }
                    }.bind(_this);

                    _this.elements[key].unsub = unsub;

                    _this.elements[key].style = function (css) {
                        this[key].forEach(function (node) {
                            for (var _key in css) {
                                node.style[_key] = css[_key];
                            }
                        });
                    }.bind(obj);

                    _this.elements[key].active = function () {
                        return obj[key].forEach(function (node) {
                            var activeNode = node.closest(".nav-rail").querySelector(".active");
                            if (activeNode) {
                                activeNode.classList.remove("active");
                                node.classList.add("active");
                            } else {
                                node.classList.add("active");
                            }
                        });
                    };

                    obj[key].forEach(function (node) {
                        node.addEventListener("click", function () {
                            return _this.elements[key].observers.forEach(function (f) {
                                return f();
                            });
                        });
                    });
                };

                for (var key in obj) {
                    _loop(key);
                }
            }
        }]);

        return Navbar;
    }();

    Factory.setSingletone("Navbar", Navbar);
})(window.Factory);