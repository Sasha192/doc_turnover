"use strict";

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Regexp = function Regexp() {
        _classCallCheck(this, Regexp);

        this.email = /\S+@\S/;
    };

    Factory.setSingletone("Regexp", Regexp);
})(window.Factory);