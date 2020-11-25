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
                const renderNotifications = (data = []) => {
                    data.forEach(n => {
                        let author = Factory.getClass("User").get(n.authorId);
                        $(list).append(`
                        <div class="notification"}>
                            <img data-src="${author.imgPath}" alt="" />
                            <div class="body">
                                <div class="name">
                                ${author.name}
                                </div>
                                <div class="msg">
                                    ${n.task ? `<a data-todo-id="${n.task.id}" role="button" class="link">${n.task.name.substr(0, 32)}... -</a>` : ""}
                                    "${n.message.substr(0, 256)}..."
                                </div>
                                <div class="meta">${n.date}</div>
                            </div>
                        </div>`);

                        if (n.todo) {
                            list.querySelector(".notification:last-child .link").onclick = e => {
                                Factory.getClass("Modal").render("todoInfo", e.target);
                            };
                        }
                    });

                    $(list).find('[data-src]').Lazy({
                        effect: 'fadeIn',
                        effectTime: 200,
                        threshold: list.scrollHeight,
                        visibleOnly: false,
                        onError: function (element) {
                            console.log('error loading ' + element.data('src'));
                        },
                        autoDestroy: true,
                        onFinishedAll: () => {
                            Loader.hide();
                        }
                    });
                };renderNotifications(data);

                let counter = 1;
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