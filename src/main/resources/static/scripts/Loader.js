"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Loader = function () {
        function Loader() {
            _classCallCheck(this, Loader);

            this.loaders = {};
            this._init("infinity", document.querySelector("progress-bars .indeterminate"));
        }

        _createClass(Loader, [{
            key: "show",
            value: function show(name) {
                var loader = this.loaders[name];this.hide();
                loader.classList.add("active");
                $(loader).fadeIn(100);
            }
        }, {
            key: "hide",
            value: function hide(callback) {
                var loader = document.querySelector("progress-bars .active");
                if (loader) {
                    loader.classList.remove("active");$(loader).fadeOut(100, function () {
                        if (callback) callback();
                    });
                }
            }
        }, {
            key: "_init",
            value: function _init(name, selector) {
                this.loaders[name] = selector;
            }
        }]);

        return Loader;
    }();

    Factory.setSingletone("Loader", Loader);
})(window.Factory);