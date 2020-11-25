"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Lang = function () {
        function Lang() {
            _classCallCheck(this, Lang);

            this.words = {
                "admin": "Адміністратор",
                "g_manager": "Головний менеджер",
                "manager": "Менеджер",
                "secretary": "Секретар",
                "performer": "Виконавець",
                "free": "Безкоштовний",
                "standart": "Стандартний",
                "unlimited": "Необмежений",
                "new": "Нове",
                "inprogress": "В процесі",
                "onhold": "Відкладено",
                "completed": "Завершено",
                "overdue": "Прострочено",
                "guest": "Гість"
            };
        }

        _createClass(Lang, [{
            key: "get",
            value: function get(word) {
                if (this.words[word.toLowerCase()]) {
                    return this.words[word.toLowerCase()];
                } else return word;
            }
        }]);

        return Lang;
    }();

    Factory.setSingletone("Lang", Lang);
})(window.Factory);