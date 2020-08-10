import { User } from "./modules/services.js"
import { dropDown } from "./modules/animation.js"
import { closeAllDropDowns } from "./modules/animation.js"
import { Insert_Files } from "./modules/form-handler.js"
import { Insert_Users } from "./modules/form-handler.js"
import { Insert_Todos } from "./modules/form-handler.js"
import { Insert_Tasks } from "./modules/form-handler.js"
import { insert_toArchive } from "./modules/form-handler.js"
import * as animation from "./modules/animation.js"
import * as service from "./modules/services.js"
import { Http } from "./modules/services.js"


$(document).ready(function () {
    if (User.role() == "performer" || User.role() == "secretary") {

        {  // Todo
            document.querySelectorAll("[contenteditable]").forEach(editor => {
                editor.removeAttribute("contenteditable")
                editor.setAttribute("readonly", "true")
            })

            $("#task-info #todo-complete-control").css("display", "none").html("")
        }

        {   // archive
            $(".nav-bar_item#linkToFiles").css("display", "none")
        }

    } else {

        {   // Todo
            $("#task-info #todo-send-report-control").css("display", "none")
            $("#task-info .report-msg").css("display", "none")
            $("#task-info #send-report-files").css("display", "none")
        }

    }

    dropDown()
})

// -------------------
//    Access Module
// -------------------

import { validation } from "./modules/form-handler.js"

(function () {

    if (window.location.pathname == "/auth") {

        // sign in

        document.querySelector("#login-button").onclick = () => {
            let data = validation($("#login-form"))
            if (data) {
                $(".access-block #confirm.signup").fadeOut(400, () => {
                    $(".access-block #signup-form").fadeIn(400)
                    $(".access-block #signup-form .status").html("")
                })

                let confirmBlock = $(".access-block #confirm.login")
                confirmBlock.find(".email").html(data.email)

                confirmBlock.find("#confirm-button").on("click", () => {
                    let confirmCode = confirmBlock.find(".confirm").val()

                    $.post(`/auth/verify?confirmCode=${confirmCode}`, (data) => {
                        if (data.success) {
                            window.location = "/"
                        } else {
                            alert(data.msg)
                        }
                    })

                })

                $.ajax({
                    type: "POST",
                    url: "/auth/login",
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    dataType: "json",
                    success: function (data) {
                        if (!data.success) {
                            $("#login-form .status").html(data.msg)
                            $("#login-form .status").css("opacity", "1")
                        } else {
                            $(".access-block #login-form").fadeOut(400, () => {
                                confirmBlock.fadeIn(400).css("display", "flex")
                            })
                        }
                    }
                });
            }
        }

        // sign up

        document.querySelector("#signup-button").onclick = () => {
            let data = validation($("#signup-form"))
            if (data) {
                $(".access-block #confirm.login").fadeOut(400, () => {
                    $(".access-block #login-form").fadeIn(400)
                    $(".access-block #login-form .status").html("")
                })

                let confirmBlock = $(".access-block #confirm.signup")
                confirmBlock.find(".email").html(data.email)

                confirmBlock.find("#confirm-button").on("click", () => {
                    let confirmCode = confirmBlock.find(".confirm").val()

                    $.post(`/auth/verify?confirmCode=${confirmCode}`, (data) => {
                        if (data.success) {
                            window.location = "/"
                        } else {
                            alert(data.msg)
                        }
                    })

                })

                $.ajax({
                    type: "POST",
                    url: "/auth/reg",
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    dataType: "json",
                    success: function (data) {
                        if (!data.success) {
                            $("#signup-form .status").css("opacity", "1")
                            $("#signup-form .status").html(data.msg)
                        } else {

                            $(".access-block #signup-form").fadeOut(400, () => {
                                confirmBlock.fadeIn(400).css("display", "flex")
                            })

                        }


                    }
                });

            }

        }
    }

})();

// -------------------
//   Archive Options
// -------------------

(function () {
    // archive options

    let page_id = 1

    if (window.location.pathname == "/archive") {

        Http.get(`/archive/doc/list?page_id=${page_id}`, data => {

            let waitForData = new Promise((resolve, reject) => {
                setTimeout(() => {
                    $(".load-box").find(".sk-chase").addClass("d-none")
                    $(".load-box").css({
                        height: "0",
                        width: "0"
                    })

                    resolve()

                }, 100)
            })

            waitForData.then(
                () => {
                    $(".archive-table").css("display", "table")
                }
            )
            insert_toArchive($(".archive-table"), data)
            dropDown()
            if ($(".archive-table .archive-table_row").length > 15) {
                $("#showMore").css("display", "flex")
            }

        })

        // pagination
        {
            document.querySelector("#showMore").onclick = () => {
                Http.get(`/archive/doc/list?page_id=${page_id + 1}`, data => {

                    let waitForData = new Promise((resolve, reject) => {
                        setTimeout(() => {
                            $(".load-box").find(".sk-chase").addClass("d-none")
                            $(".load-box").css({
                                height: "0",
                                width: "0"
                            })

                            resolve()

                        }, 100)
                    })

                    waitForData.then(
                        () => {
                            $(".archive-table").css("display", "table")
                        }
                    )
                    insert_toArchive($(".archive-table"), data)
                    dropDown()
                    if ($(".archive-table .archive-table_row").length > 15) {
                        $("#showMore").css("display", "flex")
                    }

                    page_id += 1
                    console.log(page_id)

                })
            }
        }

        // add new todo
        {
            let filesHandle = new Insert_Files($("#add-NewTodo .files-container")),
                usersHandle = new Insert_Users($("#add-NewTodo .drop-down_list#user-list"), $("#add-NewTodo  .performer-list"))

            {
                let status = $("#add-NewTodo").find(".status")

                $("#call-add-NewTodo").on("click", () => {
                    filesHandle.append(window.archive_Selected_Files)

                    $.get("/performers/list", (data) => {
                        usersHandle.append(data)
                    })

                    document.querySelector("#add-NewTodo .performer-list").innerHTML = ""
                    $("#add-NewTodo").modal("show")
                    status.find(".status-text").html(""); status.css("opacity", "0"); status.find(".status-spinner").removeClass("d-flex").toggle("d-none")
                })

                $("#add-NewTodo_btn").on("click", () => {

                    let name = $("#add-NewTodo #name").val().trim(),
                        description = $("#add-NewTodo #description").val().trim(),
                        performerList = usersHandle.getData(),
                        docList = filesHandle.getData()

                    if (name.trim().length == 0 || name.length > 256 || name.length < 8) {

                        status.css("opacity", "1")
                        status.find(".status-text").html("Name must be at least 8 characters and no more 256")

                    } else if (performerList.length == 0) {

                        status.css("opacity", "1")
                        status.find(".status-text").html("Please select performers")

                    } else {

                        let dateControl_words = document.querySelector("#add-NewTodo .calendar#dateControl .date").textContent.replace(",", "").split(" "),
                            dateControl = `${dateControl_words[1]}.${window.months.findIndex(element => element == dateControl_words[0]) + 1}.${dateControl_words[2]}`

                        let dateDeadline_words = document.querySelector("#add-NewTodo .calendar#dateDeadline .date").textContent.replace(",", "").split(" "),
                            deadline = `${dateDeadline_words[1]}.${window.months.findIndex(element => element == dateDeadline_words[0]) + 1}.${dateDeadline_words[2]}`

                        let data = { name, dateControl, deadline, description, performerList, docList, keyWords: [], status: "New" }

                        status.find(".status-text").html(""); status.css("opacity", "1"); status.find(".status-spinner").removeClass("d-none").addClass("d-flex")

                        Http.post("/task/create", data, () => {
                            status.find(".status-text").html(""); status.css("opacity", "0"); status.find(".status-spinner").removeClass("d-flex").addClass("d-none")
                            location.reload()
                        })

                    }

                })

            }

        }
        // add to existing todo
        {

            let filesHandle = new Insert_Files($("#add-ExistingTodo .files-container")),
                todosHandle = new Insert_Todos($("#add-ExistingTodo .drop-down_list"), $("#add-ExistingTodo .selected_Todo"))

            $("#call-add-ExistingTodo").on("click", () => {
                $("#add-ExistingTodo").modal("show")

                filesHandle.append(window.archive_Selected_Files)
                $.get("/archive/todo/list?status=New", (data) => {
                    todosHandle.append(data)
                })
            })

            let status = $("#add-ExistingTodo").find(".status")

            $("#add-ExistingTodo #add-ExistingTodo_btn").on("click", () => {

                status.find(".status-text").html(""); status.css("opacity", "0"); status.find(".status-spinner").removeClass("d-flex").addClass("d-none")

                if (!$("#add-ExistingTodo").find(".selected_Todo .board-item").length == 0) {
                    let data = []
                    filesHandle.getData().forEach(doc => {
                        data.push(doc.id)
                    })

                    status.find(".status-text").html(""); status.css("opacity", "1"); status.find(".status-spinner").removeClass("d-none").addClass("d-flex")



                    Http.post("/task/modify/doc_list", { docList: data, todoId: todosHandle.getData(), comment: $("#add-ExistingTodo").find("#comment").val().trim() }, data => {
                        status.find(".status-text").html(""); status.css("opacity", "0"); status.find(".status-spinner").removeClass("d-flex").addClass("d-none");
                        location.reload()
                    })

                } else {
                    status.css("opacity", "1")
                    status.find(".status-text").html("Please select task")
                }

            })

        }
        // download handler
        {
            $("#call-DownloadDoc").on("click", () => {
                let data = [];

                window.archive_Selected_Files.forEach(obj => {
                    data.push(obj.id)
                })

                window.downloadFile(`/archive/doc/download?id=${data}`)
            })
        }
        // upload handler
        {
            let data = [],
                handle = new Insert_Files($("#upload-docs .files-container")),
                id = 0,
                formData = new FormData()

            $("#call-UploadDoc").on("click", () => {
                $("#upload-docs").modal("show")

            })

            $("#upload-docs").find(".uploadFiles").on("change", function () {
                data = []
                data = [...handle.getData()]

                if (data.length > 5) return
                let file = this.files[0];

                data.push({
                    name: file.name,
                    id: id,
                    file: file
                })

                id++
                handle.append(data)
            })

            $("#upload-docs_btn").on("click", () => {

                let data = handle.getData()

                if (data.length < 1) { return } else {

                    $("#upload-docs").find(".status").css("opacity", "1")
                    $("#upload-docs").find(".status-spinner").removeClass("d-none").addClass("d-flex")

                    data.forEach(obj => {
                        formData.append("file", obj.file)
                    })

                    $.ajax({
                        type: "post",
                        url: "/archive/doc/upload",
                        cache: false,
                        dataType: "json",
                        processData: false,
                        contentType: false,
                        data: formData,
                        success: (data) => {
                            if (data.success == true) {
                                $("#upload-docs").find(".status").css("opacity", "0")
                                $("#upload-docs").find(".status-spinner").removeClass("d-flex").addClass("d-none")
                            } else {
                                // some code...
                            }
                        },
                        complete: () => {
                            $("#upload-docs").find(".status").css("opacity", "0")
                            $("#upload-docs").find(".status-spinner").removeClass("d-flex").addClass("d-none")
                            formData = null;
                            formData = new FormData()
                        }
                    })

                }

            })
        }
        // share handler
        {
            let handle = new Insert_Files($("#share-docs .files-container")),
                status = $("#share-docs .status")

            $("#call-ShareDoc").on("click", () => {
                handle.append(window.archive_Selected_Files)
                $('#share-docs').modal("show")
                status.find(".status-text").html(""); status.css("opacity", "0"); status.find(".status-spinner").removeClass("d-flex").addClass("d-none")
            })

            $("#share-docs_btn").on("click", () => {

                const error = (error) => {
                    status.css("opacity", "1")
                    status.find(".status-text").html(error)
                }

                let docs = [],
                    email = $("#share-docs").find("#recipient").val(),
                    message = $("#share-docs").find("#message").val()

                if (email == "" || email == " ") {
                    error("Please enter e-mail")
                } else if (!window.emailValid.test(email)) {
                    error("Email format invalid")
                } else if (message.length > 1024) {
                    error("Name must be no more 1024 characters")
                } else {

                    status.find(".status-text").html(""); status.css("opacity", "1"); status.find(".status-spinner").removeClass("d-none").addClass("d-flex")
                    handle.getData().forEach(obj => {
                        docs.push(obj.id)
                    })

                    let data = { email: $("#share-docs").find("#recipient").val(), message: $("#share-docs").find("#message").val(), docList: docs}
                    Http.post("/archive/doc/send", data, data => {
                        status.find(".status-text").html(""); status.css("opacity", "0"); status.find(".status-spinner").removeClass("d-flex").addClass("d-none")
                        location.reload()
                    })

                }

            })
        }
    }

})();

// -------------------
//     Team Board
// -------------------

(function () {
    if (window.location.pathname == "/teamboard") {

        let handlerNew = new Insert_Tasks($(".board.new")),
            hendlerProgress = new Insert_Tasks($(".board.InProgress")),
            hendlerCompleted = new Insert_Tasks($(".board.completed")),
            hendlerOverdue = new Insert_Tasks($(".board.overdue")),
            hendlerOnHold = new Insert_Tasks($(".board.onhold"));

        (async () => {

            await $.get("/task/mb/list", (data) => {
                handlerNew.append(data)
                $(".board.new").find(".todo-count").html(`(${data.length})`)
            })

            await $.get("/task/mb/list", (data) => {
                hendlerProgress.append(data)
                $(".board.InProgress").find(".todo-count").html(`(${data.length})`)
            })

            await $.get("/task/mb/list", (data) => {
                hendlerCompleted.append(data)
                $(".board.completed").find(".todo-count").html(`(${data.length})`)

            })

            await $.get("/task/mb/list", (data) => {
                hendlerOverdue.append(data)
                $(".board.overdue").find(".todo-count").html(`(${data.length})`)

            })


            await $.get("/task/mb/list", (data) => {
                hendlerOnHold.append(data)
                $(".board.onhold").find(".todo-count").html(`(${data.length})`)
            })

            if (User.role() !== "performer") {
                changeTaskState()
            }
        })();
    }
})();

// -------------------
//      My Board
// -------------------

(function () {
    if (window.location.pathname == "/myboard") {

        let handlerNew = new Insert_Tasks($(".board.new")),
            hendlerProgress = new Insert_Tasks($(".board.InProgress")),
            hendlerCompleted = new Insert_Tasks($(".board.completed")),
            hendlerOverdue = new Insert_Tasks($(".board.overdue")),
            hendlerOnHold = new Insert_Tasks($(".board.onhold"));

        (async () => {

            await $.get("/task/mb/list", (data) => {
                handlerNew.append(data)
                $(".board.new").find(".todo-count").html(`(${data.length})`)
            })

            await $.get("/task/mb/list", (data) => {
                hendlerProgress.append(data)
                $(".board.InProgress").find(".todo-count").html(`(${data.length})`)
            })

            await $.get("/task/mb/list", (data) => {
                hendlerCompleted.append(data)
                $(".board.completed").find(".todo-count").html(`(${data.length})`)

            })

            await $.get("/task/mb/list", (data) => {
                hendlerOverdue.append(data)
                $(".board.overdue").find(".todo-count").html(`(${data.length})`)

            })


            await $.get("/task/mb/list", (data) => {
                hendlerOnHold.append(data)
                $(".board.onhold").find(".todo-count").html(`(${data.length})`)
            })

            changeTaskState()

        })();

    }
})();

function changeTaskState() {
    let dropItem,
        inprogress = $(".board.InProgress .board-body"),
        onhold = $(".board.onhold .board-body"),
        newBoard = $(".board.new .board-body"),
        overdue = $(".board.overdue .board-body")

    if (window.location.pathname == "/myboard") {

        newBoard.find(".board-item").each((i, todo) => {
            todo.setAttribute("draggable", "true")

            todo.addEventListener("dragstart", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "0"
                }, 200)
            })

            todo.addEventListener("dragend", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "1"
                }, 200)
            })
        })

        overdue.find(".board-item").each((i, todo) => {
            todo.setAttribute("draggable", "true")

            todo.addEventListener("dragstart", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "0"
                }, 200)
            })

            todo.addEventListener("dragend", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "1"
                }, 200)
            })
        })


        inprogress.on("dragover", (e) => {
            e.preventDefault()
        })

        inprogress.on("drop", (e) => {
            if (e.target.closest(".board-item")) {
                e.target.closest(".board-item").before(dropItem)
            } else {
                inprogress.append(dropItem)
            }

            dropItem.querySelector(".board-item_status").innerHTML = "progress"
            dropItem.removeAttribute("draggable")

            Http.get(`/task/modify/status?status=inprogress&task_id=${dropItem.getAttribute("todo-id")}`)
        })

    }

    if (window.location.pathname == "/teamboard") {

        onhold.find(".board-item").each((i, todo) => {
            todo.setAttribute("draggable", "true")

            todo.addEventListener("dragstart", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "0"
                }, 200)
            })

            todo.addEventListener("dragend", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "1"
                }, 200)
            })
        })

        inprogress.find(".board-item").each((i, todo) => {
            todo.setAttribute("draggable", "true")

            todo.addEventListener("dragstart", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "0"
                }, 200)
            })

            todo.addEventListener("dragend", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "1"
                }, 200)
            })
        })


        newBoard.find(".board-item").each((i, todo) => {
            todo.setAttribute("draggable", "true")

            todo.addEventListener("dragstart", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "0"
                }, 200)
            })

            todo.addEventListener("dragend", (e) => {
                dropItem = e.target
                $(dropItem).animate({
                    opacity: "1"
                }, 200)
            })
        })


        // drop in onhold

        onhold.on("dragover", (e) => {
            e.preventDefault()
        })

        onhold.on("drop", (e) => {
            if (e.target.closest(".board-item")) {
                e.target.closest(".board-item").before(dropItem)
            } else {
                onhold.append(dropItem)
            }

            dropItem.querySelector(".board-item_status").innerHTML = "onhold"

            Http.get(`/task/modify/status?status=onhold&task_id=${dropItem.getAttribute("todo-id")}`)
        })

        // drop in new

        newBoard.on("dragover", (e) => {
            e.preventDefault()
        })

        newBoard.on("drop", (e) => {
            if (e.target.closest(".board-item")) {
                e.target.closest(".board-item").before(dropItem)
            } else {
                newBoard.append(dropItem)
            }

            dropItem.querySelector(".board-item_status").innerHTML = "new"

            Http.get(`/task/modify/status?status=new&task_id=${dropItem.getAttribute("todo-id")}`)
        })

        // drop in progress

    }

};

// -------------------
//    Notification
// -------------------

(async () => {

    await new Promise((resolve, reject) => {
        Http.get('/notifications/list', data => {
            data.forEach(notification => {
                $("#notifications-list").append(
                    `<div class="notification" data-todo-id="${notification.id}">
                        <img src="${notification.authorImgPath}" alt="">
                        <div class="notification-content">
                            <div class="title">${notification.authorName}</div>
                            <div class="desc">${notification.description}</div>
                            <div class="meta">${notification.date}</div>
                        </div>
                    </div>`
                )
            })

            let notiLength

            Http.get("/notifications/new/count", data => {
                notiLength = data.msg
                if (notiLength > 9) {
                    notiLength = "9+"
                } else {
                    notiLength = notiLength
                }

                document.querySelector("#notifications").dataset.count = notiLength
            })

            resolve()
        })
    })

    document.querySelectorAll("#notifications-list .notification").forEach(notification => {
        notification.onclick = (e) => {
            $("#task-info").modal("show")
            $.get(`/task?todoId=${e.target.closest(".notification").dataset.todoId}`, todos => {
                $("#task-info").modal("show")
                closeAllDropDowns()
                $("#task-info").find("#add-modify").fadeOut(200)
                $("#task-info").find("#add-report").fadeOut(200)
                $("#todo-slider").carousel(0)
                $("#todo-slider").carousel('pause')
                Insert_Tasks.insert_TodoInfo(todos, e.target.closest(".notification").dataset.todoId)
            })
        }
    })

    document.querySelector("#notifications .drop-down_btn").addEventListener("click", () => {
        Http.post("/notifications/see/all", { success: true }, data => {
            $("#notifications").removeAttr("data-count")
        })
    })

})()


// -------------------
//   Content Editor
// -------------------

document.querySelectorAll("[contenteditable]").forEach(editor => {
    editor.addEventListener("paste", (e) => {
        e.preventDefault();
        const text = (e.originalEvent || e).clipboardData.getData('text/plain');
        window.document.execCommand('insertText', false, text);
    })
});

