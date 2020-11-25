"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Observer = function () {
        function Observer() {
            _classCallCheck(this, Observer);

            this.observers = [];
        }

        _createClass(Observer, [{
            key: "subscribe",
            value: function subscribe(f) {
                this.observers.push(f);
            }
        }, {
            key: "unsubscribe",
            value: function unsubscribe(f) {
                this.observers = this.observers.filter(function (cf) {
                    return cf !== f1;
                });
            }
        }, {
            key: "broadcast",
            value: function broadcast(data) {
                this.observers.forEach(function (f) {
                    return f(data);
                });
            }
        }]);

        return Observer;
    }();

    Factory.setPrototype("Observer", Observer);
})(window.Factory);