"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Split = function () {
        function Split() {
            _classCallCheck(this, Split);
        }

        _createClass(Split, [{
            key: "split",
            value: function split(data, a) {
                var result = [];

                for (var d = 0; d < data.length; d++) {
                    var element = data[d];
                    if (d % a == 0) result.push([]);
                    result[result.length - 1].push(element);
                }

                return result;
            }
        }]);

        return Split;
    }();

    Factory.setSingletone("Split", Split);
})(window.Factory);