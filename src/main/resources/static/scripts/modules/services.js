// -------------------
//        Date
// -------------------

(function () {

    const months = [
        "Jenuary", "February", "March",
        "April", "May", "June",
        "July", "August", "September",
        "October", "November", "December"]

    let date = new Date(),
        month_id = date.getMonth(),
        month = months[date.getMonth()],
        year = date.getFullYear()

    {
        window.months = months
        window.month_id = month_id
        window.month = month
        window.year = year
        window.current_MDY = `${month} ${date.getDate()}, ${year}`
    }

})();

// -------------------s
//     Validation
// -------------------

(function () {
    const emailValid =
        /^(([^<>()\[\]\\.,;:\s@ "]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    window.emailValid = emailValid
})();

// -------------------
//     Insert Data
// -------------------

// import { insert_toTeamBoard} from "./form-handler.js"
// import { insert_toTMyBoard} from "./form-handler.js"
// import { insert_toTaskTable} from "./form-handler.js"

import {insert_toArchive} from "./form-handler.js"

(function () {

    $.get("/archive/doc/list", (data) => {

        let waitForData = new Promise((resolve, reject) => {
            setTimeout(() => {
                $(".load-box").find(".sk-chase").addClass("d-none")
                $(".load-box").css({
                    height: "0",
                    width: "0"
                })

                resolve("result")

            }, 100)
        })

        waitForData.then(
            result => {
                $(".archive-table").css("display", "table")
            }
        )

        insert_toArchive($(".archive-table"), data)
    })

})();

// $.get("/archive/doc/list", (data) => {
//     insert_toArchive($(".archive-table"), data)
//     selection_files()
// })

// $.get("/archive/doc/list", (data) => {
//     insert_toArchive($(".archive-table"), data)
//     selection_files()
// })

// $.get("/archive/doc/list", (data) => {
//     insert_toArchive($(".archive-table"), data)
//     selection_files()
// })

// $.get("/archive/doc/list", (data) => {
//     insert_toArchive($(".archive-table"), data)
//     selection_files()
// })

