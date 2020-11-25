"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var User = function () {
        function User() {
            var _this = this;

            _classCallCheck(this, User);

            this.Data = Factory.getClass("Data");
            this.userList = new Map();
            this.Data.get("Performers").then(function (data) {
                data.forEach(function (u) {
                    setTimeout(function () {
                        _this.userList.set(u.id, u);
                    }, 0);
                });
            });
        }

        _createClass(User, [{
            key: "get",
            value: function get(id) {
                return this.userList.get(id);
            }
        }]);

        return User;
    }();

    Factory.setSingletone("User", User);
})(window.Factory);