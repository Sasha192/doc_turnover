import * as service from "./modules/services.js"
import * as animation from "./modules/animation.js"

// -------------------
//    Access Module
// -------------------

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
                $.post("/auth/login", data, (data) => {
                    if (!data.success) {
                        $("#login-form .status").html(data.message)
                    } else {
                        $(".access-block #login-form").fadeOut(400, () => {
                            $(".access-block #confirm.login").fadeIn(400).css("display", "flex")
                        })
                    }
                })
            }
        }
        document.querySelector("#signup-button").onclick = () => {
            let data = validation($("#signup-form"))
            if (data) {
                $(".access-block #confirm.login").fadeOut(400, () => {
                    $(".access-block #login-form").fadeIn(400)
                    $(".access-block #login-form .status").html("")
                })
                console.log(data)

                let newData = JSON.stringify(data)

                $.ajax({
                    type: "POST",
                    url: "/auth/reg",
                    data:newData ,
                    contentType: "application/json",
                    dataType: "json",
                    success: function(data){
                        alert(data)
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

(function () {
    // archive options

    if (window.location.pathname == "/") {

        // add new todo
        {
            let filesHandle = new Insert_Files($("#add-NewTodo .files-container")),
                usersHandle = new Insert_Users($("#add-NewTodo .drop-down_list#user-list"), $("#add-NewTodo  .performer-list"))

            {
                let status = $("#add-NewTodo").find(".status")

                $("#call-add-NewTodo").on("click", () => {
                    filesHandle.append(window.archive_Selected_Files)

                    $.get("/user-list", (data) => {
                        usersHandle.append(data)
                    })

                    document.querySelector("#add-NewTodo .performer-list").innerHTML = ""
                    $("#add-NewTodo").modal("show")
                    status.find(".status-text").html(""); status.css("opacity", "0"); status.find(".status-spinner").removeClass("d-flex").toggle("d-none")
                })

                $("#add-NewTodo_btn").on("click", () => {

                    let name = $("#add-NewTodo #name").val(),
                        description = $("#add-NewTodo #description").val(),
                        performerList = usersHandle.getData(),
                        docList = filesHandle.getData(),
                        managerId = "1-23980123ialskdlasdas"

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
                            dateDeadline = `${dateDeadline_words[1]}.${window.months.findIndex(element => element == dateDeadline_words[0]) + 1}.${dateDeadline_words[2]}`

                        let data = { name, dateControl, dateDeadline, description, managerId, performerList, docList, keyWords: [], status: "New" }

                        status.find(".status-text").html(""); status.css("opacity", "1"); status.find(".status-spinner").removeClass("d-none").addClass("d-flex")

                        $.post("/archive/doc/upload", data, () => {
                            setTimeout(() => {
                                window.location = window.location
                            }, 2000)
                        })

                        console.log(data)

                    }

                })

            }

            $("#addExistingTodo").on("click", () => {
                // ... some code
            })
        }
        // add to existing todo
        {

            let filesHandle = new Insert_Files($("#add-ExistingTodo .files-container")),
                todosHandle = new Insert_Todos($("#add-ExistingTodo .drop-down_list"), $("#add-ExistingTodo .selected_Todo"))

            $("#call-add-ExistingTodo").on("click", () => {
                $("#add-ExistingTodo").modal("show")

                filesHandle.append(window.archive_Selected_Files)
                $.get("/archive/todo/list-new", (data) => {
                    todosHandle.append(data)
                })
            })

        }
        // download handler
        {
            $("#call-DownloadDoc").on("click", () => {
                let data = [];

                window.archive_Selected_Files.forEach(obj => {
                    data.push(obj.id)
                })
                console.log(data)
                $.post("/archive/doc/download", data, (data) => {
                    alert(data)
                })
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
                                // $("#upload-docs").find(".status").css("opacity", "0")
                                // $("#upload-docs").find(".spinner-status").removeClass("d-flex").addClass("d-none")
                            } else {
                                // some code...
                            }
                        },
                        complete: () => {
                            // $("#upload-docs").find(".status").css("opacity", "0")
                            // $("#upload-docs").find(".spinner-status").removeClass("d-flex").addClass("d-none")
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
                status.find(".status-text").html(""); status.css("opacity", "0"); status.find(".status-spinner").removeClass("d-flex").toggle("d-none")
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
                    $.post("/archive/doc/share", docs, (data) => {

                    })

                }

            })
        }
    }

})()




























