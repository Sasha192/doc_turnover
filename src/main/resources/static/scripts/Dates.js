"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Dates = function () {
        function Dates() {
            _classCallCheck(this, Dates);

            this.Date = new Date();
            this.months = ["Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень", "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"];
        }

        _createClass(Dates, [{
            key: "mLength",
            value: function mLength() {
                var y = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : this.year();
                var m = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : this.mIndex();

                return 32 - new Date(y, m, 32).getDate();
            }
        }, {
            key: "date",
            value: function date() {
                return this.Date.getDate();
            }
        }, {
            key: "month",
            value: function month() {
                return this.months[this.Date.getMonth()];
            }
        }, {
            key: "year",
            value: function year() {
                return this.Date.getFullYear();
            }
        }, {
            key: "firstDay",
            value: function firstDay() {
                var y = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : this.year();
                var m = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : this.month();

                return new Date(y, m).getDay();
            }
        }, {
            key: "mIndex",
            value: function mIndex() {
                var m = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : this.month();

                var i = this.months.indexOf(m);if (i !== -1) {
                    return i;
                } else return undefined;
            }
        }, {
            key: "DMY",
            value: function DMY() {
                var d = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : this.date();
                var m = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : this.mIndex();
                var y = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : this.year();

                d = parseInt(d);
                m = parseInt(m) + 1;
                y = parseInt(y);

                if (d < 10) d = '0' + d;if (m < 10) m = '0' + m;return d + '.' + m + '.' + y;
            }
        }, {
            key: "parseDMY",
            value: function parseDMY(str) {
                var d = parseInt(str.substring(0, 2)),
                    m = parseInt(str.substring(3, 5)) - 1,
                    y = parseInt(str.substring(6, 10));return [d, m, y];
            }
        }]);

        return Dates;
    }();

    Factory.setSingletone("Dates", Dates);
})(window.Factory);