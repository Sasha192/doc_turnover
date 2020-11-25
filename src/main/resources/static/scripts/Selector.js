"use strict";

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Selector = function () {
        function Selector(_ref) {
            var _this = this;

            var _ref2 = _slicedToArray(_ref, 1),
                obj = _ref2[0];

            _classCallCheck(this, Selector);

            this.selected = [];

            // Obj: on, off, selector, length
            this.obj = obj;
            if (this.obj.selector) {
                this.obj.selector.forEach(function (node) {
                    node.addEventListener("click", function (e) {
                        var target = $(e.target).parent(_this.obj.selector);!target.hasClass("active") ? _this.on(target[0]) : _this.off(target[0]);
                    });
                });
            }
        }

        _createClass(Selector, [{
            key: "init",
            value: function init(selector) {
                var _this2 = this;

                selector.forEach(function (node) {
                    node.addEventListener("click", function () {
                        !node.classList.contains("active") ? _this2.on(node) : _this2.off(node);
                    });
                });
            }
        }, {
            key: "clear",
            value: function clear(callback) {
                this.selected = [];
                this.obj.selector.forEach(function (node) {
                    if (node.classList.contains("active")) node.classList.remove("active");
                    callback(node);
                });
            }
        }, {
            key: "off",
            value: function off(target) {
                this.selected = this.selected.filter(function (node) {
                    return node !== target;
                });
                target.classList.remove("active");
                if (this.obj.off) this.obj.off(target);
            }
        }, {
            key: "on",
            value: function on(target) {
                if (this.obj.length && this.selected.length > this.obj.length - 1) this.off(this.selected[this.selected.length - 1]);
                this.selected.push(target);
                target.classList.add("active");
                if (this.obj.on) this.obj.on(target);
            }
        }]);

        return Selector;
    }();

    Factory.setPrototype("Selector", Selector);
})(window.Factory);