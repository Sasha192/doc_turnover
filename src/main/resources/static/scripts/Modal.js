(function (Factory) {

    class Modal {
        constructor() {
            this.modals = {};
            this.Http = Factory.getClass("Http");
            this.Alert = Factory.getClass("Alert");
            this.Data = Factory.getClass("Data");
            this.Split = Factory.getClass("Split");
            this.Loader = Factory.getClass("Loader");
            this.Regexp = Factory.getClass("Regexp");
            this.Dropdown = Factory.getClass("Dropdown");
            this.FilesManager = Factory.getClass("FilesManager");

            this.init("newTodo", document.querySelector('[data-modal="newTodo"]'), d => {
                if (d.length == 0) return this.Alert.render("warning", "Оберіть файли...");
                return "done";
            }, d => {
                // Variables
                let modal = this.modals["newTodo"].node,
                    name = modal.querySelector('[data-role="name"]'),
                    description = modal.querySelector('[data-role="description"]'),
                    files = modal.querySelector('[data-role="files"]'),
                    performers = modal.querySelector('[data-role="performers"]'),
                    deadline = Factory.getClass("Datepicker", modal.querySelector('[data-role="calendar-deadline"]')),
                    control = Factory.getClass("Datepicker", modal.querySelector('[data-role="calendar-control"]')),
                    submit = modal.querySelector('[data-event="submit"]'),
                    pList = new Map(),
                    pageNum = 0,
                    pagBtn = modal.querySelector('[data-event="pagination"]');

                description.value = "";

                // Performers
                pagBtn.style.display = "flex";
                const renderPerformers = (data, clear = true) => {
                    if (clear) performers.innerHTML = "";
                    data.forEach(p => {
                        $(performers).append(`
                            <div class="performer" data-performer-id="${p.id}">
                                <div class="user-block">
                                    <img class="img" data-src="${p.imgPath}" alt="" />
                                    <span class="name">${p.name}</span>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input position-static" type="checkbox" ${p.checked == true ? "checked" : ``}/>
                                </div>
                            </div>`);
                        performers.querySelector(`[data-performer-id="${p.id}"] .form-check-input`).onclick = e => {
                            let pId = e.target.closest(".performer").dataset.performerId;
                            if (e.target.checked == true) {
                                pList.set(pId, p);pList.get(pId).checked = true;
                            } else {
                                pList.get(pId).checked = false;pList.delete(pId);
                            }
                        };
                    });$(performers).find('[data-src]').Lazy({
                        effect: 'fadeIn',
                        autoDestroy: true,
                        effectTime: 200,
                        threshold: performers.scrollHeight,
                        visibleOnly: false,
                        onError: function (element) {
                            console.log('error loading ' + element.data('src'));
                        }
                    });
                };this.Data.get("Performers").then(result => {
                    let data = this.Split.split(result, 10);renderPerformers(data[0]);
                    let findForm = Factory.getClass("Finder", modal.querySelector(".findPerformer"), result, data[0]);
                    findForm.subscribe(result => {
                        $(performers).animate({
                            scrollTop: 0
                        }, 100, () => renderPerformers(result));
                        pageNum = 0;if (result == data[0]) {
                            pagBtn.style.display = "flex";
                        } else pagBtn.style.display = "none";
                    });
                    pagBtn.onclick = () => {
                        if (data.length - 1 !== pageNum) {
                            pageNum += 1;renderPerformers(data[pageNum], false);
                            if (data.length - 1 == pageNum) pagBtn.style.display = "none";
                        } else return;
                        $(performers).animate({
                            scrollTop: performers.scrollHeight
                        }, 100);
                    };
                });

                // Files    
                files.innerHTML = "";
                if (d) d.forEach(f => {
                    let file = f.closest("tr"),
                        fileName = file.querySelector(".name").innerHTML.trim(),
                        fileExt = file.querySelector(".extension").innerHTML.trim(),
                        fileId = file.dataset.fileId;

                    $(files).append(`
                        <div class="file" data-file-id="${fileId}">
                            <img class="img" data-src="./img/docs-img/${fileExt.substr(1)}.png" alt="" />
                            <span class="name">
                                ${fileName}
                            </span>
                            <span class="extension"> ${fileExt} </span>
                        </div>`);
                });$(files).find('[data-src]').Lazy({
                    effect: 'fadeIn',
                    autoDestroy: true,
                    effectTime: 200,
                    threshold: files.scrollHeight,
                    visibleOnly: false,
                    onError: function (element) {
                        console.log('error loading ' + element.data('src'));
                    }
                });

                // Confirm
                submit.onclick = () => {
                    if (description.value.length > 2024) return this.Alert.render("warning", "Опис не більше 2024 символів");
                    if (name.value.length == 0 || name.length > 232) return this.Alert.render("warning", "Укажіть назву");
                    if (pList.size == 0) return this.Alert.render("warning", "Оберіть виконвців...");

                    let performers = [];for (let key of pList.keys()) {
                        performers.push(key);pList.get(key).checked = false;pList.delete(key);
                    }

                    this.Loader.show("infinity");

                    this.Http.post("/com/task/create", {
                        name: name.value.trim(),
                        description: description.value.trim(),
                        performerList: performers,
                        docList: Array.from(files.querySelectorAll(".file"), f => f.dataset.fileId),
                        dateControl: control.getDate(), dateDeadline: deadline.getDate()
                    }, res => {
                        this.Loader.hide();setTimeout(() => {
                            if (res.success) {
                                this.Alert.render("success", "Задачу створено.");
                            } else {
                                this.Alert.render("danger", "Сталася помилка. " + res.msg.substr(0, 32) + "...");
                            }
                        }, 400);
                        console.log({
                            name: name.value.trim(),
                            description: description.value.trim(),
                            performerList: performers,
                            docList: Array.from(files.querySelectorAll(".file"), f => f.dataset.fileId),
                            dateControl: control.getDate(), deadline: dateDeadline.getDate()
                        });
                        $(modal).modal("hide");name.value = "";description.innerHTML = "";
                    });
                };
            });

            this.init("existTodo", document.querySelector('[data-modal="existTodo"]'), d => {
                if (d.length == 0) return this.Alert.render("warning", "Оберіть файли...");
                return "done";
            }, d => {
                let modal = this.modals["existTodo"].node,
                    files = modal.querySelector('[data-role="files"]'),
                    submit = modal.querySelector('[data-event="submit"]'),
                    todos = modal.querySelector('[data-role="todos"]'),
                    tList = new Map(),
                    pageNum = 0,
                    pagBtn = modal.querySelector('[data-event="pagination"]');

                // Todos
                pagBtn.style.display = "flex";
                const renderTodos = (data, clear = true) => {
                    if (clear) todos.innerHTML = "";

                    data.forEach(t => {
                        let status = Factory.getClass("Lang").get(status);

                        $(todos).append(`
                        <div class="todo" data-todo-id="${t.id}">
                            <img data-placement="top" data-toggle="tooltip" title="${t.manager.firstName} ${t.manager.lastName}" data-src="${t.manager.imgPath}" alt="">
                            <div class="body">
                                <div class="name">
                                   ${t.name}
                                </div>
                                <span data-placement="top" data-toggle="tooltip" title="${status}" class="status ${t.status}"></span>
                            </div>
                            <div class="form-check">
                                <input ${t.checked ? `checked` : ``} class="form-check-input position-static" type="checkbox" />
                            </div>
                        </div>`);
                        todos.querySelector(`[data-todo-id="${t.id}"] .form-check-input`).onclick = e => {
                            let tId = e.target.closest(".todo").dataset.todoId;
                            if (e.target.checked == true) {
                                tList.set(tId, t);tList.get(tId).checked = true;
                            } else {
                                tList.get(tId).checked = false;tList.delete(tId);
                            }
                        };
                    });

                    $(todos).find('[data-src]').Lazy({
                        effect: 'fadeIn',
                        autoDestroy: true,
                        effectTime: 200,
                        threshold: todos.scrollHeight,
                        visibleOnly: false,
                        onError: function (element) {
                            console.log('error loading ' + element.data('src'));
                        }
                    });

                    $(todos).find('[data-toggle="tooltip"]').tooltip();
                };

                // this.Data.get("Todos").then(result => {
                //     let data = this.Split.split(result, 10); renderTodos(data[0])
                //     let findForm = Factory.getClass("Finder", modal.querySelector(".findTodo"), data, data[0]);
                //     findForm.subscribe((result) => {
                //         $(todos).animate({
                //             scrollTop: 0
                //         }, 100, () => renderTodos(result))
                //         pageNum = 0; if (result == data[0]) {
                //             pagBtn.style.display = "flex"
                //         } else pagBtn.style.display = "none"
                //     });

                //     pagBtn.onclick = () => {
                //         if (data.length - 1 !== pageNum) {
                //             pageNum += 1; renderTodos(data[pageNum], false);
                //             if (data.length - 1 == pageNum) pagBtn.style.display = "none"
                //         } else return
                //         $(todos).animate({
                //             scrollTop: todos.scrollHeight
                //         }, 100)
                //     }
                // })

                this.Data.get("Todos").then(data => {
                    let splitData = this.Split.split(data, 10);renderTodos(splitData[0]);
                    let findForm = Factory.getClass("Finder", modal.querySelector(".findTodo"), data, splitData[0]);

                    findForm.subscribe(result => {
                        $(todos).animate({
                            scrollTop: 0
                        }, 100, () => renderTodos(result));
                        pageNum = 0;if (result == data[0]) {
                            pagBtn.style.display = "flex";
                        } else pagBtn.style.display = "none";
                    });

                    pagBtn.onclick = () => {
                        if (data.length - 1 !== pageNum) {
                            pageNum += 1;renderTodos(data[pageNum], false);
                            if (data.length - 1 == pageNum) pagBtn.style.display = "none";
                        } else return;
                        $(todos).animate({
                            scrollTop: todos.scrollHeight
                        }, 100);
                    };
                });

                // Files    
                files.innerHTML = "";
                d.forEach(f => {
                    let file = f.closest("tr"),
                        fileName = file.querySelector(".name").innerHTML.trim(),
                        fileExt = file.querySelector(".extension").innerHTML.trim(),
                        fileId = file.dataset.fileId;

                    $(files).append(`
                            <div class="file" data-file-id="${fileId}">
                                <img class="img" data-src="./img/docs-img/${fileExt.substr(1)}.png" alt="" />
                                <span class="name">
                                    ${fileName}
                                </span>
                                <span class="extension"> ${fileExt} </span>
                            </div>`);
                });$(files).find('[data-src]').Lazy({
                    effect: 'fadeIn',
                    autoDestroy: true,
                    effectTime: 200,
                    threshold: files.scrollHeight,
                    visibleOnly: false,
                    onError: function (element) {
                        console.log('error loading ' + element.data('src'));
                    }
                });

                // Confirm
                submit.onclick = () => {
                    if (tList.size == 0) return this.Alert.render("warning", "Оберіть завдання.");

                    let todos = [];for (let key of tList.keys()) {
                        todos.push(key);tList.get(key).checked = false;tList.delete(key);
                    };$(modal).modal("hide");this.Loader.show("infinity");

                    this.Http.post("/com/task/modify/docs", {
                        todoIds: todos,
                        docIds: Array.from(files.querySelectorAll(".file"), f => f.dataset.fileId)
                    }, res => {
                        this.Loader.hide();setTimeout(() => {
                            if (res.success) {
                                this.Alert.render("success", "Файли додано.");
                            } else {
                                this.Alert.render("danger", "Сталася помилка." + res.msg.substr(0, 32) + "...");
                            }
                        }, 400);
                        console.log({
                            todos: todos,
                            documents: Array.from(files.querySelectorAll(".file"), f => f.dataset.fileId)
                        });
                    });
                };
            });

            this.init("uploadFiles", document.querySelector('[data-modal="uploadFiles"]'), () => {
                return "done";
            }, () => {
                let modal = this.modals["uploadFiles"].node,
                    files = modal.querySelector('[data-role="files"]'),
                    form = modal.querySelector('[data-event="uploadFiles"]'),
                    submit = modal.querySelector('[data-event="submit"]'),
                    fList = () => files.querySelectorAll(".file");let data = new Map();form.value = "";

                files.innerHTML = "";
                const Alert = this.Alert;
                form.onchange = function () {
                    if (this.files.length > 6 || this.files.length > 6 - fList().length) return Alert.render("danger", `${6 - fList().length ? `не більше ніж: ${6 - fList().length} файлів` : `Максимальна кількість файлів!`}`);

                    for (let i = 0; i < this.files.length; i++) {
                        const f = this.files[i];
                        let name = f.name.substr(0, f.name.lastIndexOf("."));
                        if (data.has(encodeURIComponent(name))) {
                            Alert.render("warning", `файл з таким ім'ям: ${name.substr(0, 32)}... - вже завантажено`);
                        } else {
                            let ext = f.name.substr(f.name.lastIndexOf("."));

                            $(files).prepend(`
                            <div class="file" data-file-id="${encodeURIComponent(name)}">
                                <img class="img" src="./img/docs-img/${ext.substr(1)}.png" alt="" />
                                <span class="name">
                                    ${decodeURIComponent(name)}
                                </span>
                                <span class="extension"> ${ext} </span>
                                <i data-event="dismiss" class="fas fa-trash"></i>
                            </div>`);

                            files.querySelector('.file [data-event="dismiss"]').onclick = e => {
                                let file = $(files).find(e.target.closest(".file"));

                                file.slideUp(100, () => file.remove());
                                data.delete(encodeURIComponent(name));
                            };

                            data.set(encodeURIComponent(name), f);
                        }
                    }
                    form.value = "";
                };

                // Confirm
                submit.onclick = () => {
                    if (data.size == 0 || data.size > 6) return this.Alert.render("warning", "Оберіть файли");
                    let formData = new FormData();
                    data.forEach(f => formData.append("files", f));
                    $(modal).modal("hide");this.Loader.show("infinity");

                    this.Http.postFIle("/com/archive/doc/upload", formData, res => {
                        this.Loader.hide();setTimeout(() => {
                            if (res.success) {
                                this.Alert.render("success", "Файли завантажено.");
                            } else {
                                this.Alert.render("danger", "Сталася помилка." + res.msg.substr(0, 32) + "...");
                            }
                        }, 400);
                    });
                };
            });

            this.init("tariffs", document.querySelector('[data-modal="tariffs"]'), () => {
                return "done";
            }, id => {
                let modal = this.modals["tariffs"].node,
                    standart = modal.querySelector("#standartTariff"),
                    unlimited = modal.querySelector("#unlimitedTariff");

                function changeTariff(tariff) {
                    console.log("work");
                }

                standart.onclick = () => changeTariff("standart");
                unlimited.onclick = () => changeTariff("unlimited");
            });

            this.init("shareFiles", document.querySelector('[data-modal="shareFiles"]'), d => {
                if (d.length == 0) return this.Alert.render("warning", "Оберіть файли...");
                if (d.length > 6) return this.Alert.render("warning", "Не більше 6 файлів");
                return "done";
            }, d => {
                let modal = this.modals["shareFiles"].node,
                    files = modal.querySelector('[data-role="files"]'),
                    submit = modal.querySelector('[data-event="submit"]'),
                    msg = modal.querySelector('[data-name="message"]'),
                    email = modal.querySelector('[data-name="email"]');

                email.value = "";msg.value = "";

                files.innerHTML = "";
                d.forEach(f => {
                    let file = f.closest("tr"),
                        fileName = file.querySelector(".name").innerHTML.trim(),
                        fileExt = file.querySelector(".extension").innerHTML.trim(),
                        fileId = file.dataset.fileId;

                    $(files).append(`
                            <div class="file" data-file-id="${fileId}">
                                <img class="img" data-src="./img/docs-img/${fileExt.substr(1)}.png" alt="" />
                                <span class="name">
                                    ${fileName}
                                </span>
                                <span class="extension"> ${fileExt} </span>
                            </div>`);
                });$(files).find('[data-src]').Lazy({
                    effect: 'fadeIn',
                    autoDestroy: true,
                    effectTime: 200,
                    threshold: files.scrollHeight,
                    visibleOnly: false,
                    onError: function (element) {
                        console.log('error loading ' + element.data('src'));
                    }
                });

                // Confirm
                submit.onclick = () => {
                    if (email.value.trim().length == 0) return this.Alert.render("warning", "Введіть електронну адресу");
                    if (!this.Regexp.email.test(email.value.trim())) return this.Alert.render("warning", "Невірний формат електронної адреси");

                    $(modal).modal("hide");this.Loader.show("infinity");

                    this.Http.post("/com/archive/doc/send", {
                        docList: Array.from(files.querySelectorAll(".file"), f => f.dataset.fileId), message: msg.value.trim(), email: email.value.trim()
                    }, res => {
                        this.Loader.hide();setTimeout(() => {
                            if (res.success) {
                                this.Alert.render("success", "Файли відправлено.");
                                console.log({ documents: Array.from(files.querySelectorAll(".file"), f => f.dataset.fileId), message: msg.value.trim(), email: email.value.trim() });
                            } else {
                                this.Alert.render("danger", "Сталася помилка." + res.msg.substr(0, 32) + "...");
                            }
                        }, 400);
                    }, {
                        headers: {
                            "Content-Type": false,
                            "cache": false,
                            "processData": false
                        }
                    });
                };
            });

            this.init("todoInfo", document.querySelector('[data-modal="todoInfo"]'), () => {
                return "done";
            }, d => {
                this.Loader.show("infinity");
                let modal = this.modals["todoInfo"].node,
                    name = modal.querySelector('[data-role="name"]'),
                    description = modal.querySelector('[data-role="description"]'),
                    files = modal.querySelector('[data-role="files"]'),
                    rFiles = modal.querySelector('[data-role="report-files"]'),
                    comments = modal.querySelector('[data-role="comments"]'),
                    toggleBtns = modal.querySelector(".toggle-buttons"),
                    detailsBtn = toggleBtns.querySelector('[ data-slide-to="0"]'),
                    reportBtn = toggleBtns.querySelector('[ data-slide-to="1"]'),
                    controlBtns = modal.querySelector('.control-buttons'),
                    completeBtn = controlBtns.querySelector(".event-complete"),
                    overdueBtn = controlBtns.querySelector(".event-overdue"),
                    onholdBtn = controlBtns.querySelector(".event-onhold"),
                    tonewBtn = controlBtns.querySelector(".event-new"),
                    inProgressBtn = modal.querySelector(".event-inProgress"),
                    changeBtn = modal.querySelector(".apply-changes"),
                    uploadBtn = modal.querySelector(".upload-files"),
                    downloadFiles = modal.querySelector('[data-event="downloadFiles"]'),
                    downloadReportFiles = modal.querySelector('[data-event="downloadReportFiles"]'),
                    uploadFiles = modal.querySelector('[data-role="upload-files"]'),
                    reportForm = modal.querySelector('[data-event="upload-report"]'),
                    pageNum = 0,
                    pagBtn = modal.querySelector('[data-event="pagination"]'),
                    performers = modal.querySelector('[data-role="performers"]'),
                    pList = new Map();

                const User = Factory.getClass("User");

                comments.innerHTML = "";
                description.value = "";
                name.value = "";
                reportForm.value = "";
                uploadFiles.innerHTML = "";
                rFiles.innerHTML = "";
                files.innerHTML = "";
                performers.innerHTML = "";
                pagBtn.style.display = "flex";
                changeBtn.style.display = "none";
                controlBtns.style.display = "none";
                inProgressBtn.style.display = "none";
                description.setAttribute("disabled", "true");
                name.setAttribute("readonly", "true");

                this.Http.get(`/com/task/details?todoId=${d.dataset.todoId}`, t => {

                    {
                        // Name + Description
                        name.value = t.name;description.value = t.description;
                    }

                    {
                        // Files - Details
                        if (t.documents) t.documents.forEach(f => {
                            let fileName = f.name,
                                fileExt = f.extName,
                                fileId = f.id;

                            $(files).prepend(`
                                <div class="file" data-file-id="${fileId}">
                                    <img class="img" src="./img/docs-img/${fileExt.substr(1)}.png" alt="" />
                                    <span class="name">
                                        ${fileName}
                                    </span>
                                    <span class="extension"> ${fileExt} </span>
                                    <button style="display:none"  data-event="dismiss">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>`);

                            files.querySelector('.file [data-event="dismiss"]').onclick = e => {
                                let file = $(files).find(e.target.closest(".file")),
                                    fileId = e.target.closest(".file").dataset.dataId,
                                    btn = e.target.closest('[data-event="dismiss"]');

                                btn.setAttribute("disabled", "true");
                                this.Alert.render("confirm", "Файл буде видално. Ви впевнені?", {
                                    confirm: () => {
                                        this.Loader.show("infinity");
                                        this.Http.post("/com/task/doc/remove", { taskId: d.dataset.todoId, docId: fileId }, res => {
                                            this.Loader.hide(() => {
                                                if (res.success) {
                                                    file.slideUp(100, () => file.remove());
                                                    this.Alert.render("success", "Файл прибрано.");
                                                } else {
                                                    this.Alert.render("danger", "Сталася помилка.");
                                                    btn.removeAttribute("disabled");
                                                }
                                            });
                                        });
                                    },
                                    unConfirm: () => {
                                        btn.removeAttribute("disabled");
                                    }
                                });
                            };
                        });$(files).find('[data-src]').Lazy({
                            effect: 'fadeIn',
                            autoDestroy: true,
                            effectTime: 200,
                            threshold: files.scrollHeight,
                            visibleOnly: false,
                            onError: function (element) {
                                console.log('error loading ' + element.data('src'));
                            }
                        });
                    }

                    {
                        // Comments
                        if (t.comments) t.comments.forEach(c => {
                            let author = Factory.getClass("User").get(c.authorId);
                            $(comments).prepend(`
                                <div class="comment" data-comment-id="${c.id}">
                                    <img data-src="${author.imgPath}" alt="" />
                                    <div class="body">
                                        <div class="name">
                                        ${author.name}
                                        <button style="display:none"  data-event="dismiss" class="dismiss">
                                        <i class="fa fa-trash"></i>
                                        </button>
                                        </div>
                                        <div class="text">
                                            ${c.comment}
                                        </div>
                                        <div class="meta"> 
                                            ${c.date} 
                                        </div>
                                    </div>
                                </div>`);

                            comments.querySelector(`[data-comment-id="${c.id}"] [data-event="dismiss"]`).onclick = e => {
                                let commentId = e.target.closest(".comment").dataset.commentId,
                                    comment = e.target.closest(".comment"),
                                    btn = e.target.closest('[data-event="dismiss"]');

                                btn.setAttribute("disabled", "true");
                                this.Alert.render("confirm", "Коментар буде видално. Ви впевнені?", {
                                    confirm: () => {
                                        this.Loader.show("infinity");
                                        this.Http.post("/com/task/comment/remove", { taskId: d.dataset.todoId, commentId }, res => {
                                            this.Loader.hide();setTimeout(() => {
                                                if (res.success) {
                                                    $(comment).slideUp(100, () => comment.remove());
                                                    this.Alert.render("success", "Коментар видалено.");
                                                } else {
                                                    this.Alert.render("danger", "Сталася помилка.");
                                                    btn.removeAttribute("disabled");
                                                }
                                            }, 400);
                                        });
                                    },
                                    unConfirm: () => {
                                        btn.removeAttribute("disabled");
                                    }
                                });
                            };
                        });$(comments).find('[data-src]').Lazy({
                            effect: 'fadeIn',
                            autoDestroy: true,
                            effectTime: 200,
                            threshold: files.scrollHeight,
                            visibleOnly: false,
                            onError: function (element) {
                                console.log('error loading ' + element.data('src'));
                            }
                        });

                        let commentsBlock = comments.closest('[data-name="comments"]'),
                            commentInput = commentsBlock.querySelector('.add-comment [data-name="comment"'),
                            commentBtn = commentsBlock.querySelector('.add-comment [data-event="sendComment"]');

                        commentBtn.onclick = () => {
                            if (commentInput.value.trim().length == 0) return this.Alert.render("warning", "Введіnm коментар");
                            this.Loader.show("infinity");
                            let comment = commentInput.value;
                            commentInput.value = "";
                            this.Http.post("/com/task/comment/add", { taskId: d.dataset.todoId, comment }, res => {
                                this.Loader.hide(() => {
                                    if (res.success) {
                                        this.Data.get("User").then(u => {
                                            $(comments).prepend(`
                                            <div class="comment" data-comment-id="${res.msg}">
                                                <img data-src="${u.imgPath}" alt="" />
                                                <div class="body">
                                                    <div class="name">
                                                        ${u.name}
                                                    <button style="display:none"  data-event="dismiss" class="dismiss">
                                                    <i class="fa fa-trash"></i>
                                                    </button>
                                                    </div>
                                                    <div class="text">
                                                        ${comment}
                                                    </div>
                                                    <div class="meta"> 
                                                        ${Factory.getClass("Dates").DMY()} 
                                                    </div>
                                                </div>
                                            </div>`);

                                            $(comments).find('[data-src]').Lazy({
                                                effect: 'fadeIn',
                                                autoDestroy: true,
                                                effectTime: 200,
                                                threshold: files.scrollHeight,
                                                visibleOnly: false,
                                                onError: function (element) {
                                                    console.log('error loading ' + element.data('src'));
                                                }
                                            });
                                        });
                                    } else {
                                        this.Alert.render("danger", `Сталася помилка: ${res.msg.substr(0, 32)}...`);
                                    }
                                });
                            });
                        };
                    }

                    {
                        // Files - Report
                        if (t.report.documents) t.report.documents.forEach(f => {
                            let fileName = f.name,
                                fileExt = f.extName,
                                fileId = f.id;

                            $(rFiles).prepend(`
                                <div class="file" data-report-id="${t.report.id}" data-file-id="${fileId}">
                                    <img class="img" src="./img/docs-img/${fileExt.substr(1)}.png" alt="" />
                                    <span class="name">
                                        ${fileName}
                                    </span>
                                    <span class="extension"> ${fileExt} </span>
                                    <button data-event="dismiss">
                                        <i style="display:none"  class="fas fa-trash"></i>
                                    </button>
                                </div>`);

                            rFiles.querySelector('.file [data-event="dismiss"]').onclick = e => {
                                let file = $(rFiles).find(e.target.closest(".file")),
                                    fileId = e.target.closest(".file").dataset.fileId,
                                    btn = e.target.closest('[data-event="dismiss"]');

                                btn.setAttribute("disabled", "true");
                                this.Alert.render("confirm", "Файл буде видално. Ви впевнені?", {
                                    confirm: () => {
                                        this.Loader.show("infinity");
                                        this.Http.post("/com/report/doc/remove", { reportId: e.target.closest(".file").dataset.reportId, docId: fileId }, res => {
                                            this.Loader.hide(() => {
                                                console.log({ reportId: e.target.closest(".file").dataset.reportId, fileId });
                                                if (res.success) {
                                                    file.slideUp(100, () => file.remove());
                                                    this.Alert.render("success", "Файл прибрано.");
                                                } else {
                                                    this.Alert.render("danger", "Сталася помилка.");
                                                    btn.removeAttribute("disabled");
                                                }
                                            });
                                        });
                                    },
                                    unConfirm: () => {
                                        btn.removeAttribute("disabled");
                                    }
                                });
                            };
                        });$(rFiles).find('[data-src]').Lazy({
                            effect: 'fadeIn',
                            autoDestroy: true,
                            effectTime: 200,
                            threshold: rFiles.scrollHeight,
                            visibleOnly: false,
                            onError: function (element) {
                                console.log('error loading ' + element.data('src'));
                            }
                        });
                    }

                    {
                        // User rights
                        this.Data.get("User").then(user => {
                            if (user.role == "MANAGER" || user.role == "G_MANAGER" || user.role == "ADMIN") {
                                controlBtns.style.display = "flex";
                                modal.querySelectorAll('[data-event="dismiss"]').forEach(b => b.style.display = "block");
                                description.removeAttribute("disabled");
                                name.removeAttribute("readonly");

                                {
                                    // Performers
                                    const pList = new Map();
                                    const renderPerformers = (data, clear = true) => {
                                        if (clear) performers.innerHTML = "";
                                        data.forEach(p => {
                                            $(performers).append(`
                                                <div class="performer" data-performer-id="${p.id}">
                                                    <div class="user-block">
                                                        <img class="img" data-src="${p.imgPath}" alt="" />
                                                        <span class="name">${p.name}</span>
                                                    </div>
                                                    <div class="form-check">
                                                        <input class="form-check-input position-static" type="checkbox" ${p.checked == true ? "checked" : ``}/>
                                                    </div>
                                                </div>`);
                                            performers.querySelector(`[data-performer-id="${p.id}"] .form-check-input`).onclick = e => {
                                                let pId = e.target.closest(".performer").dataset.performerId;
                                                if (e.target.checked == true) {
                                                    e.target.checked = false;e.target.setAttribute("disabled", "true");
                                                    this.Alert.render("confirm", "Виконавця буде додано до задачі. Ви впевнені?", {
                                                        confirm: () => {
                                                            this.Loader.show("infinity");
                                                            this.Http.post("/com/task/performer/add", { taskId: d.dataset.todoId, performerId: pId }, res => {
                                                                if (res.success) {
                                                                    pList.set(pId, p);pList.get(pId).checked = true;e.target.checked = true;
                                                                    this.Alert.render("success", "Виконавця додано.");
                                                                } else this.Alert.render("danger", `Сталася помилка: ${res.msg.substr(0, 32)}...`);
                                                                this.Loader.hide();e.target.removeAttribute("disabled");
                                                            });
                                                        },
                                                        unConfirm: () => e.target.removeAttribute("disabled")
                                                    });
                                                } else {
                                                    e.target.checked = true;e.target.setAttribute("disabled", "true");
                                                    this.Alert.render("confirm", "Виконавця буде вилучено з задачі. Ви впевнені?", {
                                                        confirm: () => {
                                                            this.Loader.show("infinity");
                                                            this.Http.post("/com/task/preformer/remove", { taskId: d.dataset.todoId, performerId: pId }, res => {
                                                                if (res.success) {
                                                                    pList.get(pId).checked = false;pList.delete(pId);e.target.checked = false;
                                                                    this.Alert.render("success", "Виконавця вилучено.");
                                                                } else this.Alert.render("danger", `Сталася помилка: ${res.msg.substr(0, 32)}...`);
                                                                this.Loader.hide();e.target.removeAttribute("disabled");
                                                            });
                                                        },
                                                        unConfirm: () => e.target.removeAttribute("disabled")
                                                    });
                                                }
                                            };
                                        });$(performers).find('[data-src]').Lazy({
                                            effect: 'fadeIn',
                                            autoDestroy: true,
                                            effectTime: 200,
                                            threshold: performers.scrollHeight,
                                            visibleOnly: false,
                                            onError: function (element) {
                                                console.log('error loading ' + element.data('src'));
                                            }
                                        });
                                    };

                                    let pageNum = 0;
                                    this.Data.get("Performers").then(data => {

                                        data.forEach(p => pList.set(p.id, p));
                                        t.performerList.forEach(id => pList.get(id).checked = true);

                                        data.sort((perf1, perf2) => {
                                            let p1 = perf1.checked;
                                            let p2 = perf2.checked;
                                            if (p1 !== p2) {
                                                if (p1) {
                                                    return -1;
                                                } else {
                                                    return 1;
                                                }
                                            } else {
                                                return 0;
                                            }
                                        });

                                        let splitData = this.Split.split(data, 10);
                                        renderPerformers(splitData[0]);
                                        let findForm = Factory.getClass("Finder", modal.querySelector(".findPerformer"), data, splitData[0]);

                                        findForm.subscribe(result => {
                                            $(performers).animate({
                                                scrollTop: 0
                                            }, 100, () => renderPerformers(result));
                                            pageNum = 0;if (result == splitData[0]) {
                                                pagBtn.style.display = "flex";
                                            } else pagBtn.style.display = "none";
                                        });

                                        pagBtn.onclick = () => {
                                            if (splitData.size - 1 !== pageNum) {
                                                pageNum += 1;renderPerformers(splitData[pageNum], false);
                                                if (splitData.length - 1 == pageNum) pagBtn.style.display = "none";
                                            } else return;
                                            $(performers).animate({
                                                scrollTop: performers.scrollHeight
                                            }, 100);
                                        };
                                    });
                                }
                            } else {
                                t.performerList.forEach(id => {
                                    let user = User.get(id);

                                    $(performers).append(`
                                    <div class="performer" data-performer-id="${user.id}">
                                        <div class="user-block">
                                            <img class="img" data-src="${user.imgPath}" alt="" />
                                            <span class="name">${user.name}</span>
                                        </div>
                                    </div>`);
                                });

                                $(performers).find('[data-src]').Lazy({
                                    effect: 'fadeIn',
                                    autoDestroy: true,
                                    effectTime: 200,
                                    threshold: performers.scrollHeight,
                                    visibleOnly: false,
                                    onError: function (element) {
                                        console.log('error loading ' + element.data('src'));
                                    }
                                });
                            }

                            if (t.status !== "INPROGRESS") {
                                inProgressBtn.style.display = "block";
                            }
                        });
                    }

                    {
                        // Download files
                        downloadFiles.onclick = () => this.FilesManager.download(Array.from(files.querySelectorAll(".file")).map(f => f.dataset.fileId));

                        downloadReportFiles.onclick = () => this.FilesManager.download(Array.from(rFiles.querySelectorAll(".file")).map(f => f.dataset.fileId));
                    }

                    {
                        // Apply changes
                        name.oninput = () => {
                            if (name.value.length == 0) {
                                if (changeBtn.style.display = "block" && description.value.trim() == t.description) changeBtn.style.display = "none";
                            } else if (name.value.trim() == t.name) {
                                if (changeBtn.style.display = "block" && description.value.trim() == t.description) changeBtn.style.display = "none";
                            } else {
                                changeBtn.style.display = "block";
                            }
                        };
                        name.onchange = () => {
                            if (name.value.length == 0) {
                                name.value = t.name;
                            }
                        };

                        description.oninput = () => {
                            if (description.value.length == 0) {
                                if (changeBtn.style.display = "block" && name.value.trim() == t.name) changeBtn.style.display = "none";
                            } else if (description.value.trim() == t.description) {
                                if (changeBtn.style.display = "block" && name.value.trim() == t.name) changeBtn.style.display = "none";
                            } else {
                                changeBtn.style.display = "block";
                            }
                        };
                        description.onchange = () => {
                            if (description.value.length == 0) {
                                description.value = t.description;
                            }
                        };

                        changeBtn.onclick = () => {
                            this.Loader.show("infinity");
                            let data = { todoId: d.dataset.todoId };
                            if (description.value.trim() !== t.description) {
                                data.description = description.value;
                            }
                            if (name.value.trim() !== t.name) {
                                data.name = name.value;
                            }
                            $(modal).modal("hide");

                            this.Http.post("/com/task/modify/name", data, res => {
                                this.Loader.hide();setTimeout(() => {
                                    if (res.success) {
                                        this.Alert.render("success", "Зміни застосовано.");
                                    } else {
                                        this.Alert.render("danger", `Сталася помилка ${res.msg.substr(0, 32)}.`);
                                    }
                                }, 400);
                            });
                        };
                    }

                    this.Loader.hide();
                });

                {
                    // Toggle buttons
                    toggleBtns.querySelector(".active").classList.remove("active");detailsBtn.classList.add("active");
                    detailsBtn.onclick = e => {
                        if (!e.target.classList.contains("active")) {
                            toggleBtns.querySelector(".active").classList.remove("active");
                            detailsBtn.classList.add("active");
                        } else return;
                    };

                    reportBtn.onclick = e => {
                        if (!e.target.classList.contains("active")) {
                            toggleBtns.querySelector(".active").classList.remove("active");
                            reportBtn.classList.add("active");
                        } else return;
                    };
                }

                {
                    // Upload report files
                    uploadBtn.style.display = "none";
                    let fList = () => uploadFiles.querySelectorAll(".file");let data = new Map();
                    const Alert = this.Alert;
                    reportForm.onchange = function () {
                        if (this.files.length > 6 || this.files.length > 6 - fList().length) return Alert.render("danger", `${6 - fList().length ? `не більше ніж: ${6 - fList().length} файлів` : `Максимальна кількість файлів!`}`);

                        for (let i = 0; i < this.files.length; i++) {
                            const f = this.files[i];
                            let name = f.name.substr(0, f.name.lastIndexOf("."));
                            if (data.has(encodeURIComponent(name))) {
                                Alert.render("warning", `файл з таким ім'ям: ${name.substr(0, 32)}... - вже завантажено`);
                            } else {
                                let ext = f.name.substr(f.name.lastIndexOf("."));

                                $(uploadFiles).prepend(`
                                <div class="file" data-file-id="${encodeURIComponent(name)}">
                                    <img class="img" src="./img/docs-img/${ext.substr(1)}.png" alt="" />
                                    <span class="name">
                                        ${decodeURIComponent(name)}
                                    </span>
                                    <span class="extension"> ${ext} </span>
                                    <i data-event="dismiss" class="fas fa-trash"></i>
                                </div>`);

                                uploadFiles.querySelector('.file [data-event="dismiss"]').onclick = e => {
                                    let file = $(uploadFiles).find(e.target.closest(".file"));

                                    file.slideUp(100, () => {
                                        file.remove();if (fList().length == 0) uploadBtn.style.display = "none";
                                    });
                                    data.delete(encodeURIComponent(name));
                                };

                                data.set(encodeURIComponent(name), f);
                            }
                        }
                        reportForm.value = "";
                        if (fList().length > 0) uploadBtn.style.display = "block";
                    };uploadBtn.onclick = () => {
                        if (data.size > 0) {
                            let formData = new FormData();
                            data.forEach(f => formData.append("files", f));
                            $(modal).modal("hide");this.Loader.show("infinity");
                            console.log(formData.getAll("files"));
                            formData.append("taskId", d.dataset.todoId);

                            this.Http.postFIle("/com/report/upload", formData, res => {
                                this.Loader.hide();setTimeout(() => {
                                    if (res.success) {
                                        this.Alert.render("success", "Файли завантажено.");
                                    } else {
                                        this.Alert.render("danger", "Сталася помилка.");
                                    }
                                }, 400);
                            });
                        } else {
                            this.Alert.render("warning", "Сталася помилка.");
                        }
                    };
                }

                {
                    // Change status
                    const changeStatus = status => {
                        $(modal).modal("hide");this.Loader.show("infinity");
                        this.Http.post("/com/task/modify/status", { taskId: d.dataset.todoId, status }, res => {
                            this.Loader.hide(() => {
                                if (res.success) {
                                    this.Alert.render("success", "Статус змінено.");
                                    this.BoardsHandler = Factory.getClass("BoardsHandler");
                                    this.Data.update("Todos").then(data => this.BoardsHandler.render(data));
                                } else {
                                    this.Alert.render("danger", "Сталася помилка.");
                                }
                            });
                        });
                    };

                    completeBtn.onclick = () => changeStatus("COMPLETED");
                    overdueBtn.onclick = () => changeStatus("OVERDUE");
                    onholdBtn.onclick = () => changeStatus("ONHOLD");
                    tonewBtn.onclick = () => changeStatus("NEW");
                    inProgressBtn.onclick = () => changeStatus("INPROGRESS");
                }
            });$('[data-modal="todoInfo"]').on("hidden.bs.modal", () => {
                $(todoInfoCarousel).carousel(0);
                this.Data.get("Performers").then(data => {
                    data.forEach(p => {
                        setTimeout(() => {
                            if (p.checked) p.checked = false;
                        }, 0);
                    });
                });
            });

            this.init("filesFilter", document.querySelector('[data-modal="filesFilter"]'), () => {
                return "done";
            }, () => {
                let modal = this.modals["filesFilter"].node,
                    df = Factory.getClass("Datepicker", modal.querySelector('[data-role="calendar-from"]')),
                    dt = Factory.getClass("Datepicker", modal.querySelector('[data-role="calendar-to"]')),
                    name = modal.querySelector(".filter__name"),
                    submit = modal.querySelector('[data-event="submit"]');

                this.Dropdown.init(modal, { single: true });
                const FilesHandler = Factory.getClass("FilesHandler");

                // Confirm
                submit.onclick = () => {
                    this.Loader.show("infinity");
                    this.Http.get(`/com/archive/doc/list?${name.value.length > 0 ? `name=${name.value}&` : ``}startDate=${df.getDate()}&endDate=${dt.getDate()}`, data => {
                        FilesHandler.render(data);$(modal).modal("hide");
                        Factory.getClass("Pagination").init(".pagination", `/archive/doc/list?${name.value.length > 0 ? `name=${name.value}&` : ``}startDate=${df.getDate()}&endDate=${dt.getDate()}`, "FilesHandler");
                    });
                };
            });

            this.init("addNewEmployes", document.querySelector('[data-modal="addNewEmployes"]'), () => {
                return "done";
            }, d => {
                let modal = this.modals["addNewEmployes"].node,
                    submit = modal.querySelector('[data-event="submit"]'),
                    form = modal.querySelector(".add-employes__input-wrapper"),
                    input = modal.querySelector(".add-employes__input"),
                    list = modal.querySelector(".add-employes__list");

                list.innerHTML = "";

                form.onsubmit = e => {
                    e.preventDefault();
                    if (input.value.length == 0) return;
                    if (!this.Regexp.email.test(input.value)) {
                        input.value = "";
                        return this.Alert.render("warning", "Не вірний формат пошти");
                    }
                    $(list).prepend(`
                    <div class="add-employes__item">
						<div class="add-employes__item-name">${input.value}</div>
						<button class="add-employes__item-remove">
							<i class="fa fa-trash"></i>
						</button>
					</div>
                    `);
                    list.querySelector(".add-employes__item-remove").onclick = e => {
                        let employee = e.target.closest(".add-employes__item");
                        $(employee).slideUp(150, () => $(employee).remove());
                    };
                    input.value = "";
                };

                // Confirm
                submit.onclick = () => {
                    if (list.querySelectorAll(".add-employes__item").length == 0) return this.Alert.render("warning", "Додайте адреси.");
                    this.Loader.show("infinity");
                    this.Http.post("/com/tenants/invite", {
                        emails: Array.from(list.querySelectorAll(".add-employes__item")).map(e => e.querySelector(".add-employes__item-name").innerText.trim()),
                        tenantId: d.id
                    }, res => {
                        this.Loader.hide(() => {
                            console.log({ employes: Array.from(list.querySelectorAll(".add-employes__item")).map(e => e.querySelector(".add-employes__item-name").innerText.trim()) });
                            if (res.success) {
                                this.Alert.render("success", "Запрошення відправлено.");
                            } else {
                                this.Alert.render("danger", `Сталася помилка ${res.msg.substr(0, 32)}...`);
                            }
                            $(modal).modal("hide");
                        });
                    });
                };
            });
        }

        render(name, data) {
            this.modals[name].render(data);
        }

        init(name, node, conditions, render) {
            this.modals[name] = {
                render: data => {
                    if (!data) {
                        $(node).modal("show");setTimeout(() => render(), 200);
                    } else if (conditions(data) == "done") {
                        $(node).modal("show");setTimeout(() => render(data), 200);
                    }
                }, node
            };
        }
    }

    Factory.setSingletone("Modal", Modal);
})(window.Factory);