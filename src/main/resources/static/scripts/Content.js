"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Content = function () {
        function Content() {
            _classCallCheck(this, Content);

            this.templates = {};
            this.content = document.getElementById("root");
            this.Loader = Factory.getClass("Loader");
        }

        _createClass(Content, [{
            key: "render",
            value: function render(url) {
                var _this = this;

                this.content.style.opacity = 0;
                this.Loader.show("infinity");
                var template = this.templates[url].content.cloneNode(true);
                setTimeout(function () {
                    _this.content.innerHTML = "";
                    _this.content.append(template);
                    _this.content.style.opacity = 1;
                }, 100);
            }
        }, {
            key: "init",
            value: function init(obj) {
                for (var url in obj) {
                    this.templates[url] = obj[url];
                }
            }
        }]);

        return Content;
    }();

    Factory.setSingletone("Content", Content);
})(window.Factory);