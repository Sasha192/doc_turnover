"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function (Factory) {
    var Pagination = function () {
        function Pagination() {
            _classCallCheck(this, Pagination);

            this.Http = Factory.getClass("Http");
            this.Loader = Factory.getClass("Loader");

            if (!String.prototype.splice) {
                /**
                 * {JSDoc}
                 *
                 * The splice() method changes the content of a string by removing a range of
                 * characters and/or adding new characters.
                 *
                 * @this {String}
                 * @param {number} start Index at which to start changing the string.
                 * @param {number} delCount An integer indicating the number of old chars to remove.
                 * @param {string} newSubStr The String that is spliced in.
                 * @return {string} A new string with the spliced substring.
                 */
                String.prototype.splice = function (start, delCount, newSubStr) {
                    return this.slice(0, start) + newSubStr + this.slice(start + Math.abs(delCount));
                };
            }
        }

        _createClass(Pagination, [{
            key: "render",
            value: function render(selector, path, num, maxpages, handler) {
                var _this = this;

                var container = document.querySelector(selector),
                    cpage = container.querySelector(".current-page"),
                    nextPage = container.querySelector(".next-page"),
                    previousPage = container.querySelector(".previous-page"),
                    firstPage = container.querySelector(".first-page"),
                    lastPage = container.querySelector(".last-page"),
                    Handler = Factory.getClass(handler);

                cpage.innerHTML = num;

                var changePage = function changePage(n) {
                    if (n > maxpages || n < 1) return;
                    if (parseInt(cpage.innerText) == n) return;
                    num = n;cpage.innerHTML = n;
                    _this.Loader.show("infinity");
                    var url = formatUrl(path, n);
                    _this.Http.get(url, function (data) {
                        Handler.render(data);
                    });
                };

                nextPage.onclick = function () {
                    return changePage(num + 1);
                };
                previousPage.onclick = function () {
                    return changePage(num - 1);
                };
                lastPage.onclick = function () {
                    return changePage(maxpages);
                };
                firstPage.onclick = function () {
                    return changePage(1);
                };
            }
        }, {
            key: "formatUrl",
            value: function formatUrl(url, insertPath) {
                var startIdx = url.indexOf('?');
                if (startIdx < 0) {
                    url = url + "/" + insertPath;
                } else {
                    url = url.splice(startIdx, 0, "/" + insertPath);
                }
                return url;
            }
        }, {
            key: "init",
            value: function init(selector, path, handler) {
                var _this2 = this;

                var url = this.formatUrl(path, "maxpages");
                this.Http.get(url, function (n) {
                    var num = 1,
                        n = parseInt(n),
                        maxpages = n;
                    _this2.render(selector, path, num, maxpages, handler);
                });
            }
        }]);

        return Pagination;
    }();

    Factory.setSingletone("Pagination", Pagination);
})(window.Factory);