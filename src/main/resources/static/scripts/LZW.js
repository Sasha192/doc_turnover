"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var LZW = function () {
        function LZW() {
            _classCallCheck(this, LZW);
        }

        _createClass(LZW, [{
            key: "encode",
            value: function encode(s) {
                var dict = {};
                var data = (s + "").split("");
                var out = [];
                var currChar = void 0;
                var phrase = data[0];
                var code = 256;
                for (var i = 1; i < data.length; i++) {
                    currChar = data[i];
                    if (dict[phrase + currChar] != null) {
                        phrase += currChar;
                    } else {
                        out.push(phrase.length > 1 ? dict[phrase] : phrase.charCodeAt(0));
                        dict[phrase + currChar] = code;
                        code++;
                        phrase = currChar;
                    }
                }
                out.push(phrase.length > 1 ? dict[phrase] : phrase.charCodeAt(0));
                for (var _i = 0; _i < out.length; _i++) {
                    out[_i] = String.fromCharCode(out[_i]);
                }
                return out.join("");
            }
        }, {
            key: "decode",
            value: function decode(s) {
                var dict = {};
                var data = (s + "").split("");
                var currChar = data[0];
                var oldPhrase = currChar;
                var out = [currChar];
                var code = 256;
                var phrase = void 0;
                for (var i = 1; i < data.length; i++) {
                    var currCode = data[i].charCodeAt(0);
                    if (currCode < 256) {
                        phrase = data[i];
                    } else {
                        phrase = dict[currCode] ? dict[currCode] : oldPhrase + currChar;
                    }
                    out.push(phrase);
                    currChar = phrase.charAt(0);
                    dict[code] = oldPhrase + currChar;
                    code++;
                    oldPhrase = phrase;
                }
                return out.join("");
            }
        }]);

        return LZW;
    }();

    Factory.setSingletone("LZW", LZW);
})(window.Factory);