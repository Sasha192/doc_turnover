"use strict";

(function () {

    document.querySelectorAll("[title]").forEach(function (element) {
        element.setAttribute("data-toggle", "tooltip");
        element.setAttribute("data-placement", "auto");
    });
    $('[data-toggle="tooltip"]').tooltip();
})();