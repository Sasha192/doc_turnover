"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var FilesManager = function () {
        function FilesManager() {
            _classCallCheck(this, FilesManager);
        }

        _createClass(FilesManager, [{
            key: "download",
            value: function download(data) {
                if (data.length == 0) return Factory.getClass("Alert").render("warning", "Обріть файли");
                var download = window.open("/archive/doc/download?docId=" + data.join("docId="), "Download");download.close();
            }
        }]);

        return FilesManager;
    }();

    Factory.setSingletone("FilesManager", FilesManager);
})(window.Factory);