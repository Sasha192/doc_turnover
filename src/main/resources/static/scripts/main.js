// -------------------
//    Access Module
// -------------------


import * as service from "./modules/services.js"
import * as animation from "./modules/animation.js"

import { validation } from "./modules/form-handler.js"

(function () {

    if (window.location.pathname == "/auth") {
        document.querySelector("#login-button").onclick = () => {
            let data = validation($("#login-form"))
            if (data) {
                $(".access-block #confirm.signup").fadeOut(400, () => {
                    $(".access-block #signup-form").fadeIn(400)
                    $(".access-block #signup-form .status").html("")
                })

                let confirmBlock = $(".access-block #confirm.login")

                confirmBlock.find("#confirm-button").on("click", () => {
                    let confirmCode = confirmBlock.find(".confirm").val()

                    $.post(`/auth/verify?verificationCode=${confirmCode}`, (data) => {
                        if(data.success) {
                            window.location = "/archive"
                        } else {
                            alert(data.msg);
                            console.log(data.msg)
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
        document.querySelector("#signup-button").onclick = () => {
            let data = validation($("#signup-form"))
            if (data) {
                $(".access-block #confirm.login").fadeOut(400, () => {
                    $(".access-block #login-form").fadeIn(400)
                    $(".access-block #login-form .status").html("")
                })

                let confirmBlock = $(".access-block #confirm.signup")
                confirmBlock.find("#confirm-button").on("click", () => {
                    let confirmCode = confirmBlock.find(".confirm").val()

                    $.post(`/auth/verify?verificationCode=${confirmCode}`, (data) => {
                        if(data.success) {
                            window.location = "/archive"
                        } else {
                            alert(data.msg);
                            console.log(data.msg)
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

import { Insert_Files } from "./modules/form-handler.js"
import { Insert_Users } from "./modules/form-handler.js"
import { Insert_Todos } from "./modules/form-handler.js"
import { Insert_Tasks } from "./modules/form-handler.js"

(function () {
    // archive options

    if (window.location.pathname == "/archive") {

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
                        docList = []

                    filesHandle.getData().forEach(doc => {
                        docList.push(doc.id)
                    })

                    if (name.length == 0 || name == "" || name == " " || name.length > 256 || name.length < 8) {

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

                        let data = { name, dateControl, deadline, description, performerList, docList, keyWords: [], status: "new" }

                        status.find(".status-text").html(""); status.css("opacity", "1"); status.find(".status-spinner").removeClass("d-none").addClass("d-flex")

                        console.log(data)

                        $.post("/task/create", JSON.stringify(data), (data) => {
                            if(data.success) {
                                window.location = window.location
                                console.log(data)
                            } else {
                                alert(data.msg)
                            }
                        }, "json")

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

                    console.log(JSON.stringify({ docList: data, todoId: todosHandle.getData() }))

                    $.post("/task/modify/doc", JSON.stringify({ docList: data, todoId: todosHandle.getData() }), (data) => {

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

                            window.location = window.location
                        },
                        complete: (error) => {
                            $("#upload-docs").find(".status").css("opacity", "0")
                            $("#upload-docs").find(".status-spinner").removeClass("d-flex").addClass("d-none")
                            formData = null;
                            formData = new FormData();

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

                    let data = { email: $("#share-docs").find("#recipient").val(), message: $("#share-docs").find("#message").val(), docs }
                    $.post("/archive/doc/share", JSON.stringify(docs), (data) => {

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
        // append new todos
        {
            let handlerNew = new Insert_Tasks($(".board.new")),
                hendlerProgress = new Insert_Tasks($(".board.InProgress")),
                hendlerCompleted = new Insert_Tasks($(".board.completed")),
                hendlerOverdue = new Insert_Tasks($(".board.overdue")),
                hendlerOnHold = new Insert_Tasks($(".board.onhold"))

            $.get("/task/tm/list", (data) => {
                handlerNew.append(data)
                $(".board.new").find(".todo-count").html(`(${data.length})`)
            })
            $.get("/task/tm/list", (data) => {
                hendlerProgress.append(data)
                $(".board.InProgress").find(".todo-count").html(`(${data.length})`)
            })
            $.get("/task/tm/list", (data) => {
                hendlerCompleted.append(data)
                $(".board.completed").find(".todo-count").html(`(${data.length})`)
            })
            $.get("/task/tm/list", (data) => {
                hendlerOverdue.append(data)
                $(".board.overdue").find(".todo-count").html(`(${data.length})`)
            })
            $.get("/task/tm/list", (data) => {
                hendlerOnHold.append(data)
                $(".board.onhold").find(".todo-count").html(`(${data.length})`)
            })
        }
    }
})();

// -------------------
//      My Board
// -------------------

(function () {
    if (window.location.pathname == "/myboard") {
        // append new todos

        let handlerNew = new Insert_Tasks($(".board.new")),
            hendlerProgress = new Insert_Tasks($(".board.InProgress")),
            hendlerCompleted = new Insert_Tasks($(".board.completed")),
            hendlerOverdue = new Insert_Tasks($(".board.overdue")),
            hendlerOnHold = new Insert_Tasks($(".board.onhold"));

        (async () => {

            await $.get("task/my/list/new", (data) => {
                handlerNew.append(data)
                $(".board.new").find(".todo-count").html(`(${data.length})`)
                console.log("new")
            })

            await $.get("task/my/list/inprogress", (data) => {
                hendlerProgress.append(data)
                $(".board.InProgress").find(".todo-count").html(`(${data.length})`)
                console.log("InProgress")
            })

            await $.get("task/my/list/completed", (data) => {
                hendlerCompleted.append(data)
                $(".board.completed").find(".todo-count").html(`(${data.length})`)
                console.log("completed")
            })

            await $.get("task/my/list/overdue", (data) => {
                hendlerOverdue.append(data)
                $(".board.overdue").find(".todo-count").html(`(${data.length})`)
                console.log("ovedue")
            })


            await $.get("task/my/list/onhold", (data) => {
                hendlerOnHold.append(data)
                $(".board.onhold").find(".todo-count").html(`(${data.length})`)
                console.log("onhold")
            })

            changeTaskState($(".board.new"), $(".board.InProgress"))

        })();

    }
})();

function changeTaskState(from, to) {
    let dropItem,
        toBoard = to.find(".board-body")

    from.find(".board-item").each((i, todo) => {
        todo.setAttribute("draggable", "true")

        todo.addEventListener("dragstart", (e) => {
            dropItem = e.target
            $(dropItem).animate({
                opacity:"0"
            }, 200)
        })

        todo.addEventListener("dragend", (e) => {
            dropItem = e.target
            $(dropItem).animate({
                opacity:"1"
            }, 200)
        })
    })

    toBoard.on("dragover", (e) => {
        e.preventDefault()
    })

    toBoard.on("drop", (e) => {
        console.log("drop")
        if(e.target.closest(".board-item")) {
            e.target.closest(".board-item").before(dropItem)
        } else {
            toBoard.append(dropItem)
        }

        dropItem.removeAttribute("draggable")
        dropItem.querySelector(".board-item_status").innerHTML = "inprogress"

        // $.get(`/task/modify/status?task_id=${dropItem.getAttribute("todo-id")}&status=inprogress`, (data) => {
        //     console.log(data)
        // }, "json")
        $.ajax({
            url: `/task/modify/status?task_id=${dropItem.getAttribute("todo-id")}&status=inprogress`,
            success: (data) => {

            },
            dataType: "json",
            method: "GET",
            contentType:"application/json"
        });
    })
}

// -------------------
//   Content Editor
// -------------------

document.querySelectorAll("[contenteditable]").forEach(editor => {
    editor.addEventListener("paste", (e) => {
        e.preventDefault();
        const text = (e.originalEvent || e).clipboardData.getData('text/plain');
        window.document.execCommand('insertText', false, text);
    })
})