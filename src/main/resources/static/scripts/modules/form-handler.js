// -------------------
//   Files Handler
// -------------------

class Insert_Files {

    constructor(container) {
        this.container = container
        this.data = []
    }

    append(files) {
        this.data = [...files]
        this.container.html("")
        this.data.forEach(element => {
            this.container.append(`
            <div class="file" file-id="${element.id}">
                <div class="file-content">
                    <img src="img/docs-img/${element.name.substr(element.name.lastIndexOf('.') + 1).replace(/\s+/g, ' ').trim()}.png" alt="">
                    <div class="file-name">
                        ${element.name.substr(0, 32)}..
                    </div>
                </div>
                <button class="dismiss" id="${element.id}">
                     <i class="fal fa-times"></i>
                </button>
            </div>
         `)
        })

        this.container.find(".dismiss").each((index, element) => {

            element.onclick = (e) => {
                let fileId = $(e.target.closest(".dismiss")).attr("id")
                this.data.forEach(element => {

                    if (element.id == fileId) {
                        this.container.find(`[file-id="${element.id}"]`).remove();
                        this.data.splice(this.data.findIndex(obj => obj == element), 1)
                        this.append(this.data)
                        if (this.data.length == 0) $('.modal').modal("hide")
                    } else {
                        return
                    }

                })
            }
        })
    }

    getData() {

        return this.data

    }

}

export {Insert_Files}

class Insert_Users {

    constructor(userList, performerList) {
        this.userList = userList
        this.performerList = performerList
    }

    append(data) {
        this.userList.html("")

        data.forEach(element => {
            this.userList.append(`
                <div class="drop-down_item">
                    <div class="user" user-id="${element.userId}">
                        <div class="d-flex align-items-center">
                            <img src="img/services/man.png" alt="">
                            <div class="user-meta">
                                <a href="#" class="user-name">${element.name}</a>
                                <div class="user-department">
                                ${element.department}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `)
        })

        this.reload()
    }

    reload() {
        this.userList.find(".drop-down_item").each((i, element) => {
            element.onclick = (e) => {
                let user = e.target.closest(".drop-down_item").querySelector(".user")

                $(user).append(`
                    <button class="dismiss">
                        <i class="fal fa-times"></i>
                    </button>
                `)

                $(user.parentElement).remove()
                this.performerList.append(user)

                user.querySelector(".dismiss").onclick = (e) => {
                    let drop_Item = document.createElement("div");
                    drop_Item.classList.add("drop-down_item");
                    drop_Item.append(e.target.closest(".user"))
                    $(e.target.closest(".dismiss")).remove()
                    this.userList.append(drop_Item);
                    this.reload()
                }


            }
        })
    }

    getData() {
        let data = []

        this.performerList.find(".user").each((i, performer) => {
            data.push(performer.getAttribute("user-id"))
        })

        return data
    }

}

export {Insert_Users}

class Insert_Todos {

    constructor(todo_list, insert_list) {
        this.todo_list = todo_list
        this.insert_list = insert_list
    }

    append(data) {
        this.todo_list.html("")

        data.forEach(todo => {
            this.todo_list.append(`
            <div class="drop-down_item">
                <div class="board-item ${todo.status}" todo-id=""${todo.todoId}>
                    <div class="board-item_content">
                        <img src="img/avatars/4.jpg" alt="">
                        <div>
                            <div class="board-item_title">
                                ${todo.name}
                                <span>
                                    ${todo.dateDedaline}
                                </span>
                            </div>
                            <div class="board-item_status">
                                ${todo.status}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            `)
        })

        this.reload()
    }

    reload() {
        this.todo_list.find(".drop-down_item").each((i, element) => {
            element.onclick = (e) => {
                let todo = element.querySelector(".board-item")

                $(todo).append(`
                    <button class="dismiss">
                        <i class="fal fa-times"></i>
                    </button>
                `)

                $(todo.parentElement).remove()


                {
                    if (this.insert_list.find(".board-item").length > 0) {
                        let drop_Item = document.createElement("div");
                        drop_Item.classList.add("drop-down_item");
                        $(drop_Item).append(this.insert_list.find(".board-item"))
                        $(drop_Item).find(".dismiss").remove()
                        this.todo_list.append(drop_Item);
                        this.reload()
                    }
                }

                this.insert_list.html("")
                this.insert_list.append(todo)

                todo.querySelector(".dismiss").onclick = (e) => {
                    let drop_Item = document.createElement("div");
                    drop_Item.classList.add("drop-down_item");
                    drop_Item.append(e.target.closest(".board-item"))
                    $(e.target.closest(".dismiss")).remove()
                    this.todo_list.append(drop_Item);
                    this.reload()
                }

            }
        })
    }

    getData() {
        let data = []

        this.insert_list.find(".board-item").each((i, todo) => {
            data.push(todo.getAttribute("todo-id"))
        })

        return data
    }

}

export {Insert_Todos}

// -------------------
//   Archive Handler
// -------------------

function insert_toArchive(table, data) {

    table.html("")
    data.forEach(element => {
        table.append(`
        <tr class="archive-table_row" id="${element.document.id}">
        <td>
        <div class="archive-row_item">
        <div class="custom-control custom-checkbox info">
        <input type="checkbox" class="option-checkbox custom-control-input"
            id="checkbox" disabled>
        <label class="custom-control-label" for="checkbox"></label>
        </div>
            <div id="archive-file_name">
${element.document.name}
            </div>
        </div>
        </td>
        <td>
        <div class="archive-row_item" id="archive-file_tasks">
            Related Tasks :<span id="related-tasks_value">${element.document.tasks}</span>
        </div>
        </td>
        <td>
        <div class="archive-row_item" id="archive-file_performer" performer-id="${element.performer.id}">
            ${element.performer.name}
        </div>
        </td>
        <td>
        <div class="archive-row_item" id="archive-file_department" department-id="${element.department.id}">
            ${element.department.name}
        </div>
        </td>
        <td>
        <div class="archive-row_item" id="archive-file_date">
            ${element.document.date}
        </div>
        </td>
        </tr>
    `)
    })

    selection_files()

}

function selection_files() {

    let selected_files = []

    $(".archive-table").each((i, table) => {
        $(table).find(".archive-table_row").each((index, element) => {

            element.onclick = (e) => {

                let row = e.target.closest("tr"),
                    checkbox = row.querySelector(".custom-checkbox")

                if (row.classList.contains("active")) {
                    checkbox.style = ""
                    checkbox.setAttribute("disabled", "true")
                    checkbox.querySelector('input[type="checkbox"]').checked = false
                    selected_files.splice(selected_files.findIndex(element => element.id == row.getAttribute("id")), 1)

                } else {

                    checkbox.style.opacity = "1"
                    checkbox.removeAttribute("disabled")
                    checkbox.querySelector('input[type="checkbox"]').checked = true
                    selected_files.push({
                        id: row.getAttribute("id"),
                        name: $(row.querySelector("#archive-file_name")).text()
                    })

                }

                row.classList.toggle("active")

                if (selected_files.length == 1) $("#archive-options").fadeIn(100)
                if (selected_files.length == 0) $("#archive-options").fadeOut(100)

                if (selected_files.length == 7) {
                    let prevousElement = selected_files[selected_files.length - 2]
                    let row = $(table).find(`.archive-table_row#${prevousElement.id}`)
                    let checkbox = row.find(".custom-checkbox")
                    checkbox.css("opacity", "")
                    checkbox.attr("disabled", "true")
                    checkbox.find('input[type="checkbox"]').prop('checked', false)
                    selected_files.splice(selected_files.findIndex(element => element == prevousElement), 1)
                    row.removeClass("active")
                }


                window.archive_Selected_Files = selected_files
            }

        })
    })
}

export {insert_toArchive}

// ------------------------------
//    Access module Validation 
// ------------------------------

function validation(form) {

    let status = form.find(".status"),
        password = form.find(".password").val(),
        email = form.find(".e-mail").val()

    const error = (text) => {
        status.html(text)
        status.css("opacity", "1")
        return false
    }

    if (email == "" || email == " ") {
        error("Please enter e-mail")
        return false
    } else {
        if (!window.emailValid.test(email)) error("Email format invalid")
    }

    if (password == "" || password == " ") error("Please enter password")
    if (password.length < 6) error("Password must be at least 8 characters")

    if (form.find(".repeat-password").length == 1) {
        if (password !== form.find(".repeat-password").val()) error("Password must be at least 8 characters")
    }

    return {email, password, remember: true}

}

export {validation}

// ------------------------------
//           Calendar
// ------------------------------


(function () {

    window.onload = function () {

        let calendars = $(".calendar")
        calendars.each((i, calendar) => {
            calendar.querySelector(".head-block .date").innerHTML = window.current_MDY
            calendar.querySelector(".body .current-month").innerHTML = window.month
            calendar.querySelector(".body .current-year").innerHTML = window.year

            calendar.querySelector(".body .control #nextMonth").onclick = () => calendarNext(calendar)

            calendar.querySelector(".body .control #previousMonth").onclick = () => calendarPrevious(calendar)
            insert_toCalendar(window.year, window.month_id, calendar)
        })

    }

    function insert_toCalendar(year, month, calendar) {

        let daysInMonth = 32 - new Date(year, month, 32).getDate(),
            firstDay = new Date(year, month).getDay(),
            lastDayIndex = 1

        calendar.querySelector("tbody").innerHTML = ""

        for (let i = 0; i < 6; i++) {
            let row = document.createElement("tr")
            for (let x = 0; x < 7; x++) {
                let td = document.createElement("td")
                if (lastDayIndex < daysInMonth + 1) {
                    if (i == 0 && x + 1 < firstDay) {
                        td.innerHTML = `<div class="disabled date-item">-</div>`
                    } else {
                        td.innerHTML = `<div class="date-item">${lastDayIndex}</div>`

                        if (x == 6) {
                            td.classList.add("week-end")
                            td.classList.add("week-day")
                        }

                        lastDayIndex++
                    }
                } else if (lastDayIndex > daysInMonth) {
                    td.innerHTML = `<div class="disabled date-item">-</div>`
                }

                row.append(td)

            }

            calendar.querySelector("tbody").append(row)
        }

        calendar.querySelectorAll("td div").forEach(element => {
            element.onclick = (e) => {
                let date_item = e.target.closest("td div")
                if (!date_item.classList.contains("active") && !date_item.closest("td").classList.contains("week-end")) {
                    $(date_item.closest(".calendar")).find(".active").removeClass("active")
                    date_item.classList.add("active")
                    calendar.querySelector(".head-block .date").innerHTML = `${calendar.querySelector(".body .current-month").textContent} ${date_item.textContent}, ${calendar.querySelector(".body .current-year").textContent}`
                } else {
                    date_item.classList.remove("active")
                }
            }
        })

    }

    function calendarPrevious(calendar) {
        let month = calendar.querySelector(".body .current-month").textContent,
            previousMonthIndex = window.months.findIndex(elm => elm == month) - 1,
            previousMonth = window.months[previousMonthIndex],
            previousYear = Number(calendar.querySelector(".body .current-year").textContent)

        if (previousMonth == undefined) {
            previousMonth = window.months[11]
            previousYear = previousYear - 1
        }

        if (previousYear) {
            calendar.querySelector(".body .current-year").innerHTML = previousYear
        }

        if (previousMonth == window.CurrentMonth && window.CurrentYear == calendar.querySelector(".body .current-year").textContent) {
            calendar.querySelector("#previousMonth").classList.add("d-none")
        }

        calendar.querySelector(".body .current-month").innerHTML = previousMonth
        insert_toCalendar(previousYear, previousMonthIndex, calendar)
    }

    function calendarNext(calendar) {
        let month = calendar.querySelector(".body .current-month").textContent,
            nextMonthIndex = window.months.findIndex(elm => elm == month) + 1,
            nextMonth = window.months[nextMonthIndex],
            nextYear = Number(calendar.querySelector(".current-year").textContent)

        if (nextMonth == undefined) {
            nextMonth = window.months[0]
            nextYear = Number(calendar.querySelector(".current-year").textContent) + 1
        }
        if (nextYear) {
            calendar.querySelector(".current-year").innerHTML = nextYear
        }
        if (month == window.CurrentMonth) {
            calendar.querySelector("#previousMonth").removeClass("d-none")
        }
        calendar.querySelector(".current-month").innerHTML = nextMonth
        insert_toCalendar(nextYear, nextMonthIndex, calendar)
    }

})();






























