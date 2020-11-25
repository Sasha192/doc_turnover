"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function () {
    var Factory = function () {
        function Factory() {
            _classCallCheck(this, Factory);

            this.classes = new Map([]);
        }

        _createClass(Factory, [{
            key: "setPrototype",
            value: function setPrototype(className, classConstructor) {
                this.classes.set(className, function (args) {
                    return new classConstructor(args);
                });
                this.classes.set(className + " -g", function () {
                    return classConstructor;
                });
            }
        }, {
            key: "setSingletone",
            value: function setSingletone(className, classConstructor) {
                this.classes.set(className, new classConstructor());
            }
        }, {
            key: "getClass",
            value: function getClass(className) {
                var constructor = this.classes.get(className);
                if (typeof constructor == "function") {
                    for (var _len = arguments.length, args = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
                        args[_key - 1] = arguments[_key];
                    }

                    return constructor(args);
                } else return constructor || console.error("Oops! not possible to read class: " + className);
            }
        }]);

        return Factory;
    }();

    window.Factory = new Factory();
})();