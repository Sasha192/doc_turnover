"use strict";

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

(function (Factory) {
    var Finder = function (_Factory$getClass) {
        _inherits(Finder, _Factory$getClass);

        function Finder(_ref) {
            var _ref2 = _slicedToArray(_ref, 3),
                selector = _ref2[0],
                data = _ref2[1],
                defData = _ref2[2];

            _classCallCheck(this, Finder);

            var _this = _possibleConstructorReturn(this, (Finder.__proto__ || Object.getPrototypeOf(Finder)).call(this));

            _this.fieldFind = selector.querySelector('[data-name="field"]');
            _this.lastVal = "";

            selector.onsubmit = function (e) {
                e.preventDefault();
                var val = _this.fieldFind.value.trim().toLowerCase(),
                    response = [];

                if (_this.lastVal == val) {
                    return;
                } else if (val == "" && defData) {
                    _this.lastVal = val;return _this.broadcast(defData);
                }

                _this.lastVal = val;
                data.forEach(function (k) {
                    if (k.name.trim().toLowerCase().indexOf(val) !== -1) response.push(k);
                });_this.broadcast(response);

                _this.fieldFind.value = "";
            };
            return _this;
        }

        return Finder;
    }(Factory.getClass("Observer -g"));

    Factory.setPrototype("Finder", Finder);
})(window.Factory);