(function (Factory) {

    class Notifications {
        render(selector) {
            this.selector = selector;
            this.Http = Factory.getClass("Http");
            this.Data = Factory.getClass("Data");

            let wrapper = this.selector.querySelector(".notifications-wrapper"),
                list = this.selector.querySelector(".notifications-list"),
                btn = this.selector.querySelector('[data-event="toggle"]');

            this.Data.get("Notifications").then(data => {
                const renderOnAuthor = (n, hours, min) => {
                    Factory.getClass("User").get(n.authorId).then(author => {
                        $(list).append(`
                        <div class="notification"}>
                            <img data-src="${author.imgPath}" alt="" />
                            <div class="body">
                                <div class="name">
                                ${author.name}
                                </div>
                                <div class="msg">
                                    ${n.taskName ? `<a data-todo-id="${n.taskId}" role="button" class="link">${n.taskName.substr(0, 32)}... -</a>` : ""}
                                    <p>"${n.message.substr(0, 256)}..."</p>
                                </div>
                                <div class="meta">${n.date} <span>
                                ${hours} : ${min}</span></div>
                            </div>
                        </div>`)

                        $(list).find('.notification [data-src]').Lazy({
                            effect: 'fadeIn',
                            effectTime: 200,
                            threshold: list.scrollHeight,
                            visibleOnly: false,
                            onError: function (element) {
                                console.log('error loading ' + element.data('src'));
                            },
                            autoDestroy: true
                        });

                        if (n.taskName) {
                            list.querySelector(".notification:last-child .link").onclick = e => {
                                console.log(e.target)
                                Factory.getClass("Modal").render("todoInfo", e.target);
                            };
                        }
                    })

                }
                const renderWithoutAuthor = (n, hours, min) => {
                    $(list).append(`
                        <div class="notification"}>
                            <img style="opacity: 0%" src="/img/default.jpg" alt="" />
                            <div class="body">
                                <div class="name">
                                </div>
                                <div class="msg">
                                    ${n.taskName ? `<a data-todo-id="${n.taskId}" role="button" class="link">${n.taskName.substr(0, 32)}... -</a>` : ""}
                                    <p>"${n.message.substr(0, 256)}..."</p>
                                </div>
                                <div class="meta">${n.date} <span>
                                ${hours} : ${min}</span></div></div>
                            </div>
                        </div>`)

                    $(list).find('.notification [data-src]').Lazy({
                        effect: 'fadeIn',
                        effectTime: 200,
                        threshold: list.scrollHeight,
                        visibleOnly: false,
                        onError: function (element) {
                            console.log('error loading ' + element.data('src'));
                        },
                        autoDestroy: true
                    });

                    if (n.taskName) {
                        list.querySelector(".notification:last-child .link").onclick = e => {
                            console.log(e.target)
                            Factory.getClass("Modal").render("todoInfo", e.target);
                        };
                    }
                }
                const renderNotifications = (data = []) => {
                    data.sort((a, b) => {
                        return b.creationTime - a.creationTime;
                    })
                    data.forEach(n => {
                        let eventTime = Number(n.creationTime);
                        eventTime = new Date(eventTime);
                        let hours = eventTime.getHours();
                        let min = eventTime.getMinutes();
                        if (hours < 10) {
                            hours = '0' + hours;
                        }
                        if (min < 10) {
                            min = '0' + min;
                        }
                        if (n.authorId) {
                            renderOnAuthor(n, hours, min)
                        } else {
                            renderWithoutAuthor(n, hours, min);
                        }
                    });

                };
                renderNotifications(data);

                let counter = 2;
                wrapper.querySelector('[data-event="more"]').onclick = () => {
                    this.Http.get(`/com/notifications/list/${counter}`, data => {
                        renderNotifications(data);
                        counter++;
                    });
                };
                this.Http.get("/com/notifications/new/count", data => {
                    if (parseInt(data.msg) > 0) {
                        this.selector.dataset.amount = data.msg;
                    }
                })
            });

            btn.addEventListener("click", () => wrapper.classList.contains("active") ? this._close(wrapper) : this._open(wrapper));
            window.addEventListener("click", e => {
                if (e.target.closest(".notifications")) {
                    return;
                } else this._close(wrapper);
            });
        }

        _open(target) {
            $(target).fadeIn(100);
            target.classList.add("active");
            if (this.selector.dataset.amount) {
                this.selector.removeAttribute("data-amount");
                this.Http.get("/com/notifications/see/all");
            }
        }

        _close(target) {
            $(target).fadeOut(100);
            target.classList.remove("active");
        }
    }

    Factory.setPrototype("Notifications", Notifications);
})(window.Factory);