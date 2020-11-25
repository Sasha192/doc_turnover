"use strict";

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Datepicker = function () {
        function Datepicker(_ref) {
            var _this = this;

            var _ref2 = _slicedToArray(_ref, 4),
                selector = _ref2[0],
                dmy = _ref2[1],
                on = _ref2[2],
                off = _ref2[3];

            _classCallCheck(this, Datepicker);

            this.Dates = Factory.getClass("Dates");
            this.selector = selector;
            this.dmy = dmy;
            this.on = on;
            this.off = off;

            this.control = this.selector.querySelector(".control");
            this.control.querySelector('[data-event="previous"]').onclick = function () {
                return _this.toggleControl("previous");
            };
            this.control.querySelector('[data-event="next"]').onclick = function () {
                return _this.toggleControl("next");
            };

            var _ref3 = dmy ? this.Dates.parseDMY(dmy) : [undefined, undefined, undefined],
                _ref4 = _slicedToArray(_ref3, 3),
                d = _ref4[0],
                m = _ref4[1],
                y = _ref4[2];

            this.cMonth = this.control.querySelector('[data-name="pickerMonth"]');this.cMonth.innerHTML = this.Dates.month();
            this.cYear = this.control.querySelector('[data-name="pickerYear"]');this.cYear.innerHTML = this.Dates.year();
            this.sDate = this.selector.querySelector('[data-name="selectedDate"]');this.render(d, m, y);
        }

        _createClass(Datepicker, [{
            key: "toggleControl",
            value: function toggleControl(arg) {
                switch (arg) {
                    case "previous":
                        var preM = this.Dates.mIndex(this.cMonth.innerHTML) == 0 ? 11 : this.Dates.mIndex(this.cMonth.innerHTML) - 1,
                            preY = preM == 11 ? parseInt(this.cYear.innerHTML) - 1 : parseInt(this.cYear.innerHTML);

                        this.render(1, preM, preY);
                        break;

                    case "next":
                        var nexM = this.Dates.mIndex(this.cMonth.innerHTML) == 11 ? 0 : this.Dates.mIndex(this.cMonth.innerHTML) + 1,
                            nexY = nexM == 0 ? parseInt(this.cYear.innerHTML) + 1 : parseInt(this.cYear.innerHTML);

                        this.render(1, nexM, nexY);
                        break;
                }
            }
        }, {
            key: "toggleDate",
            value: function toggleDate(target) {
                var d = target.querySelector(".date-item").innerHTML,
                    m = this.Dates.mIndex(this.cMonth.innerHTML),
                    y = this.cYear.innerHTML;

                this.sDate.innerHTML = this.Dates.DMY(d, m, y);
                if (this.on) this.on(target);
            }
        }, {
            key: "render",
            value: function render() {
                var d = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : this.Dates.date();
                var m = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : this.Dates.mIndex();
                var y = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : this.Dates.year();

                this.cYear.innerHTML = y;this.cMonth.innerHTML = this.Dates.months[m];
                this.sDate.innerHTML = this.dmy ? this.dmy : this.Dates.DMY(d, m, y);

                var mLength = this.Dates.mLength(y, m),
                    firstDay = this.Dates.firstDay(y, m) || 7;
                var body = this.selector.querySelector("tbody");body.innerHTML = "";

                for (var _d = 0; _d < mLength + (firstDay - 1); _d++) {
                    var cell = document.createElement("td");if (_d + 1 > firstDay - 1) $(cell).append("<div class=\"date-item\">" + (_d + 1 - firstDay + 1) + "</div>");
                    if (_d % 7 == 0) body.append(document.createElement("tr"));var rows = body.querySelectorAll("tr");$(rows[rows.length - 1]).append(cell);
                }

                Factory.getClass("Selector", {
                    length: 1,
                    on: this.toggleDate.bind(this),
                    selector: body.querySelectorAll(".date-item")
                });
            }
        }, {
            key: "getDate",
            value: function getDate() {
                return this.sDate.innerHTML;
            }
        }, {
            key: "set",
            value: function set(dmy) {
                this.sDate.innerHTML = dmy;
            }
        }]);

        return Datepicker;
    }();

    Factory.setPrototype("Datepicker", Datepicker);
})(window.Factory);