import { Http } from "./services.js"
import { User } from "./services.js"
import * as services from "./services.js"

class Insert_Files {

    constructor(container) {
        this.container = container
        this.data = []
    }

    append(files) {
        this.container.html("")

        this.data = [...files]
        this.data.forEach(element => {
            this.container.append(`
            <div class="file" file-id="${element.id}">
                <div class="file-content">
                    <img src="img/docs-img/${element.name.substr(element.name.lastIndexOf('.') + 1).replace(/\s+/g, ' ').trim()}.png" alt="">
                    <div class="file-name">
                        ${element.name.substr(0, 22)}..
                    </div>
                </div>
                <button class="dismiss" id="${element.id}">
                    <i class="fas fa-trash"></i>
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

    clean() {
        this.data = []
    }

}

export { Insert_Files }

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
                    <div class="user" user-id="${element.id}">
                        <div class="d-flex align-items-center">
                            <img src="img/services/man.png" alt="">
                            <div class="user-meta">
                                <a href="#" class="user-name">${element.name}</a>
                                <div class="user-department">
                                ${element.department.substring(0, 24)}
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
                        <i class="fas fa-trash"></i>
                    </button>
                `)

                $(user.parentElement).remove()
                this.performerList.append(user)

                user.querySelector(".dismiss").onclick = (e) => {
                    let drop_Item = document.createElement("div"); drop_Item.classList.add("drop-down_item"); drop_Item.append(e.target.closest(".user"))
                    $(e.target.closest(".dismiss")).remove()
                    this.userList.append(drop_Item); this.reload()
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

export { Insert_Users }

class Insert_Todos {

    constructor(todo_list, insert_list) {
        this.todo_list = todo_list
        this.insert_list = insert_list
    }

    append(data) {
        data.forEach(todo => {
            this.todo_list.append(`
            <div class="drop-down_item">
                <div class="board-item ${todo.status}" todo-id="${todo.id}">
                    <div class="board-item_content">
                        <img src="" alt="">
                        <div>
                            <div class="board-item_title">
                                ${todo.name}
                                <span>
                                    ${todo.dateDeadline}
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
                        <i class="fas fa-trash"></i>
                    </button>
                `)

                $(todo.parentElement).remove()


                {
                    if (this.insert_list.find(".board-item").length > 0) {
                        let drop_Item = document.createElement("div"); drop_Item.classList.add("drop-down_item"); $(drop_Item).append(this.insert_list.find(".board-item"))
                        $(drop_Item).find(".dismiss").remove()
                        this.todo_list.append(drop_Item); this.reload()
                    }
                }

                this.insert_list.html("")
                this.insert_list.append(todo)

                todo.querySelector(".dismiss").onclick = (e) => {
                    let drop_Item = document.createElement("div"); drop_Item.classList.add("drop-down_item"); drop_Item.append(e.target.closest(".board-item"))
                    $(e.target.closest(".dismiss")).remove()
                    this.todo_list.append(drop_Item); this.reload()
                }

            }
        })
    }

    getData() {

        return this.insert_list.find(".board-item").attr("todo-id")

    }

}

export { Insert_Todos }


let reportUpload = new Insert_Files($("#task-info .report-upload-files"))

class Insert_Tasks {

    constructor(inserList) {
        this.insertList = inserList
    }

    append(data) {

        this.insertList.find(".todo-count").html(`(${data.length})`)

        if(window.location.pathname == "/myboard") {
            data.forEach(todo => {
                this.insertList.find(".board-body").append(
                    `<div class="board-item" todo-id="${todo.id}">
                <img src=${todo.managerImgPath} alt="">
                <div class="w-100">
                    <div class="board-item_title">${todo.name.trim().substring(0, 18)}..<span>${todo.deadlineDate}</span></div>
                    <div class="board-item_status">${todo.status}</div>
                </div>
                </div>`
                )
            })
        }


        if(window.location.pathname == "/teamboard") {
            data.forEach(todo => {
                this.insertList.find(".board-body").append(
                    `<div class="board-item" todo-id="${todo.id}">
                <img src=${todo.managerImgPath} alt="">
                <div class="w-100">
                    <div class="board-item_title">${todo.name.trim().substring(0, 18)}..<span>${todo.deadlineDate}</span></div>
                    <div class="board-item_status">${todo.status}</div>
                </div>
                </div>`
                )
            })
        }


        this.insertList.find(".board-item_title").each((i, task) => {
            task.onclick = (e) => {
                $("#task-info").modal("show")
                $("#task-info").find("#add-modify").fadeOut(200)
                $("#task-info").find("#add-report").fadeOut(200)
                $("#todo-slider").carousel(0)
                $("#todo-slider").carousel('pause')
                $.get(`/task/details?todoId=${e.target.closest(".board-item").getAttribute("todo-id")}`, (data) => {
                    Insert_Tasks.insert_TodoInfo(data, e.target.closest(".board-item").getAttribute("todo-id"))
                })
            }
        })


    }

    static insert_TodoInfo(info, todoId) {

        // main settings

        if (User.role() == "PERFORMER" || User.role() == "SECRETARY") {} else {

            // modify
            document.querySelector("#add-modify").onclick = () => {
                let name = document.querySelector("#task-info .todo-title").value.trim(),
                    description = document.querySelector("#task-info .description-text").textContent.trim()

                Http.post("/task/modify/name_description", { name, description ,todo_id: todoId }, data => {
                    location.reload()
                })

            }

            // success
            document.querySelector("#task-complete").onclick = () => {
                Http.get(`/task/modify/status?status=completed&task_id=${todoId}`, data => {
                    location.reload()
                })
            }

            // overdue
            document.querySelector("#task-overdue").onclick = () => {
                Http.get(`/task/modify/status?status=overdue&task_id=${todoId}`, data => {
                    location.reload()
                })
            }

        }

        {
            $("#task-info").find(".todo-title").val(info.name)
            $("#task-info").find(".todo-title").on("change", (e) => {
                if (e.target.value.length == 0) {
                    e.target.value = info.name
                }
            })
            document.querySelector("#task-info .todo-title").oninput = (e) => {
                if (e.target.value.length !== 0 && e.target.value.trim() !== info.name) {
                    $("#task-info").find("#add-modify").fadeIn(200)
                } else if (document.querySelector("#task-info .description-text").textContent.trim() == info.description.trim()) {
                    $("#task-info").find("#add-modify").fadeOut(200)
                } else {
                    $("#task-info").find("#add-modify").fadeIn(200)
                }
            }
        }

        {
            if (info.report) {
                $("#task-info #todo-complete-control").css("display", "flex")
            } else {
                $("#task-info #todo-complete-control").css("display", "none")
            }
        }

        // download detail files

        {
            document.querySelector("#download-details-files").onclick = (e) => {
                let data = []
                $("#task-info #details-files .file").each((i, file) => {
                    data.push(file.getAttribute("file-id"))
                })

                window.downloadFile(`/archive/doc/download?id=${data}`)
            }
        }
        // download report files

        {
            document.querySelector("#download-report-files").onclick = (e) => {
                let data = []
                $("#task-info .files-container-report .file").each((i, file) => {
                    data.push(file.getAttribute("file-id"))
                })

                window.downloadFile(`/archive/doc/download?id=${data}`)
            }
        }

        // Append files

        {
            let files = $("#task-info").find("#details-files")
            files.html("")
            $("#task-info .details-files #count").html(`(${info.docList.length})`)

            info.docList.forEach(doc => {
                files.append(
                    `<div class="file" file-id="${doc.id}">
                        <div class="file-content">
                            <img src="img/docs-img/${doc.extName.replace(".", "")}.png" alt="">
                            <div class="file-name">
                                ${doc.name.substring(0, 26)}...
                            </div>
                        </div>
                    </div>`
                )
            })

        }

        // Append performers

        {
            let performers = $("#task-info").find(".performers")
            performers.html("")
            $("#task-info .performers-container #count").html(`(${info.performers.length})`)

            info.performers.forEach(user => {
                performers.append(
                    `<div class="user">
                    <div class="d-flex align-items-center">
                        <img src="${user.imgPath}" alt="">
                        <div class="user-meta">
                            <a href="#" class="user-name">${user.name.substring(0, 28)}...</a>
                            <div class="user-department">
                                ${user.department.name.substring(0, 28)}
                            </div>
                        </div>
                    </div>
                    </div>`
                )
            })
        }

        // Append comments

        {
            let comments = $("#task-info").find(".comments")
            $("#task-info .comments #count").html(`(${info.comments.length})`)

            let comments_container = comments.find(".comments-container")
            comments_container.html("")

            info.comments.forEach(comment => {
                comments_container.append(
                    `<div class="comment">
                        <img class="author-image" src="${comment.performer.imgPath}" alt="">
                        <div class="comment-body">
                            <div class="author-name">${comment.performer.name}</div>
                            <div class="comment-text">
                                ${comment.comment.substring(0, 64)}..
                            </div>
                            <div class="comment-date">${comment.date}</div>
                        </div>
                    </div>`
                )
            })

            // add comments

            document.querySelector("#task-info .add-comment button").onclick = (e) => {
                let comment = e.target.closest(".add-comment").querySelector("input").value
                if (comment.length == 0) return

                let data = {
                    todoId,
                    comment
                }


                console.log(data)
                Http.post("/task/comment/add", data, (data) => {
                    $("#task-info .comments .comments-container").append(
                        `<div class="comment">
                        <img class="author-image" src="${User.imgPath()}" alt="">
                            <div class="comment-body">
                            <div class="author-name">${User.name()}</div>
                            <div class="comment-text">
                                ${comment.trim().substring(0, 64)}..
                            </div>
                            <div class="comment-date">${window.current_DMY}</div>
                        </div>
                    </div>`
                    )

                    $("#task-info .comments #count").html(`(${parseInt(document.querySelector("#task-info .comments #count").textContent.replace(/\D+/g, "")) + 1})`)

                })

                e.target.closest(".add-comment").querySelector("input").value = ""

            }
        }

        // Append description

        {
            let description = $("#task-info").find(".description-text")
            description.html(info.description)

            description.on("blur", (e) => {
                if (e.target.textContent.length == 0) {
                    e.target.innerHTML = info.description
                    $("#task-info").find("#add-modify").fadeOut(200)
                } else if (e.target.textContent !== info.description && e.target.textContent.length !== 0) {
                    $("#task-info").find("#add-modify").fadeIn(200)
                }
            })

            description.on("input", (e) => {
                if (e.target.innerHTML.length > 1024) {
                    e.target.innerHTML = e.target.innerHTML.substring(0, 1024)
                }
            })
        }

        // Upload report-files + Send report

        {

            {
                $("#task-info").find(".report-upload-files").html("")
                $("#task-info").find("#report-file-lable").html("Choose file")

                let data = [],
                    id = 0

                reportUpload.clean()

                document.querySelector("#task-info").querySelector("#report-upload-file").onchange = function () {
                    data = []
                    data = [...reportUpload.getData()]

                    if (data.length > 5) return
                    let file = this.files[0]

                    data.push({ name: file.name, id: id, file: file })
                    id++

                    reportUpload.append(data)
                    if (reportUpload.getData().length !== 0) {
                        $("#task-info").find("#add-report").fadeIn(200)
                    } else {
                        $("#task-info").find("#add-report").fadeOut(200)
                    }
                }
            }

            {
                document.querySelector("#task-info .report-msg textarea").oninput = (e) => {
                    if (e.target.value.length !== 0) {
                        $("#task-info").find("#add-report").fadeIn(200)
                    } else {
                        if ($("#task-info .report-upload-files .file").length == 0) {
                            $("#task-info").find("#add-report").fadeOut(200)
                        }
                    }
                }
            }

            document.querySelector("#task-info #add-report").onclick = () => {

                let formData = new FormData()
                formData.append("comment", $("#task-info .report-msg textarea").val())

                reportUpload.getData().forEach(obj => {
                    formData.append("file", obj.file)
                })

                $("#task-info").find(".status").css("opacity", "1")
                $("#task-info").find(".status-spinner").removeClass("d-none").addClass("d-flex")

                Http.files(`/report/upload?todoId=${todoId}`,
                    formData,
                        data => {
                        if (data.success == true) {
                            $("#task-info").find(".status").css("opacity", "0")
                            $("#task-info").find(".status-spinner").removeClass("d-flex").addClass("d-none")
                        } else {
                            alert(data.msg)
                        }
                    },
                    data => {
                        $("#task-info").find(".status").css("opacity", "0")
                        $("#task-info").find(".status-spinner").removeClass("d-flex").addClass("d-none")
                        formData = null;
                        formData = new FormData()
                        location.reload()
                    },
                    error => {alert(error)}
                    )
            }


        }

        // Append report comments

        {
            $("#task-info .report-list").html("")
            if (info.report) {
                info.report.comments.forEach(comment => {
                    $("#task-info .report-list").append(
                        `<div class="comment">
                        <img class="author-image" src="${comment.performer.imgPath}" alt="">
                        <div class="comment-body">
                            <div class="author-name">${comment.performer.name}</div>
                            <div class="comment-text">
                                ${comment.comment.substring(0, 64)}..
                            </div>
                            <div class="comment-date">${comment.date}</div>
                        </div>
                    </div>`
                    )
                })
            }

        }

        // Append report files

        {
            $("#task-info .files-container-report").html("")
            if (info.report) {
                $("#task-info .files-report #count").html(`(${info.report.docList.length})`)

                info.report.docList.forEach(doc => {
                    $("#task-info .files-container-report").append(
                        `<div class="file" file-id="${doc.id}">
                    <div class="file-content">
                        <img src="img/docs-img/${doc.extName.replace(".", "")}.png" alt="">
                        <div class="file-name">
                            ${doc.name.substring(0, 26)}...
                        </div>
                    </div>
                    </div>`
                    )
                })
            }
        }
    }

}

export { Insert_Tasks }

// ------------------------------
//    Archive Insert + Handler
// ------------------------------

function insert_toArchive(table, data) {

    data.forEach(element => {
        table.append(
            `<tr class="archive-table_row" id="${element.id}">
        <td>
        <div class="archive-row_item">
        <div class="custom-control custom-checkbox info">
        <input type="checkbox" class="option-checkbox custom-control-input"
            id="checkbox" disabled>
        <label class="custom-control-label" for="checkbox"></label>
        </div>
            <div id="archive-file_name">
        ${element.name.substring(0, 24)}...${element.extName}
            </div>
        </div>
        </td>
        <td>
        <div class="archive-row_item" id="archive-file_tasks">
            Related Tasks :<span id="related-tasks_value">${element.taskCount}</span>
        </div>
        </td>
        <td>
        <div class="archive-row_item" id="archive-file_performer" performer-id="${element.performerId}">
        ${element.performerName}
        </div>
        </td>
        <td>
        <div class="archive-row_item" id="archive-file_department" department-id="${element.departmentId}">
        ${element.departmentName.substring(0,28)}
        </div>
        </td>
        <td>
        <div class="archive-row_item" id="archive-file_date">
        ${element.date}
        </div>
        </td>
        </tr>
    `)
    })

    selection_files()

}

let selected_files = []
function selection_files() {

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

export { insert_toArchive }

function validation(form) {

    let status = form.find(".status"),
        password = form.find(".password").val(),
        email = form.find(".e-mail").val(),
        data = {}

    const error = (text) => {
        status.html(text)
        status.css("opacity", "1")
    }

    if (email == "" || email.trim() == "") {
        error("Будь ласка, введіть електронну пошту")
        return false
    } else if (!window.emailValid.test(email)) {
        error("Невірний формат пошти")
        return false
    } else {
        data.email = email
    }

    if (form.find(".firstName").length == 1 && form.find(".firstName").val().trim() == "") {
        error("Будь ласка, введіть ім'я")
        return false
    } else {
        data.firstName = form.find(".firstName").val()
    }

    if (form.find(".lastName").length == 1 && form.find(".lastName").val().trim() == "") {
        error("Будь ласка, введіть прізвище")
        return false
    } else {
        data.lastName = form.find(".lastName").val()
    }

    if (password == "" || password.trim() == "") {
        error("Будь ласка, введіть пароль")
        return false
    } else {
        data.password = password
    }

    if (password.length < 8) {
        error("Пароль повинен бути довжиною не менше 8 символів")
        return false
    }

    if (form.find(".repeat-password").length == 1 && password !== form.find(".repeat-password").val()) {
        error("Паролі не свіпвадають")
        return false
    }

    if (form.find(".remember").checked == true) {
        data.remember = true;
    } else {
        data.remember = false
    }

    return data;

}

export { validation }

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



























