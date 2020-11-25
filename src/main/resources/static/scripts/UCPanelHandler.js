(function (Factory) {

    class UCPanelHandler {
        constructor() {
            this.Data = Factory.getClass("Data");
            this.Loader = Factory.getClass("Loader");
            this.Lang = Factory.getClass("Lang");
            this.Http = Factory.getClass("Http");
            this.Alert = Factory.getClass("Alert");

            let file;
            this.Data.get("User").then(user => this.render(user));
            document.querySelector("#uploadFilesForm").onchange = function () {
                let name = document.querySelector('#uploadFilesFormName');
                name.innerText = this.files[0].name.substr(0, 32) + "...";
                file = this.files[0];
            };
            document.querySelector("#uploadFilesFormSubmit").onclick = () => {
                if (file) {
                    let formdata = new FormData();
                    formdata.append("file", file);
                    console.log(formdata.getAll("file"))
                    this.Loader.show("infinity");
                    this.Http.postFile("/com/settings/change/img", formdata, res => {
                        if (res.success) {
                            this.Data.update("User");
                            window.location = window.location;
                        } else {
                            this.Alert.render("danger", `Сталася помилка: ${res.msg.substr(0, 32)}`);
                        }
                    })
                }
            };

            document.querySelector("#changePass").onclick = () => {
                let pass1 = document.querySelector("#pass1"),
                    pass2 = document.querySelector("#pass2");

                if (pass1.value.trim() == "") return this.Alert.render("warning", "Напишіть старий пароль");
                if (pass2.value.trim() == "") return this.Alert.render("warning", "Напишіть новий пароль");
                this.Loader.show("infinity");

                this.Http.post("/com/settings/change/password", { oldPasswd: pass1.value, newPasswd: pass2.value }, res => {
                    this.Loader.hide(() => {
                        if (res.success) {
                            this.Alert.render("success", "Пароль змінено");
                            pass1.value = "";
                            pass2.value = "";
                        } else {
                            this.Alert.render("danger", "Сталася помилка: " + res.msg.substr(0, 32) + "...");
                        }
                    });
                });
            };
        }

        render(user) {
            document.querySelector("#userName").innerHTML = user.name;
            document.querySelector("#userEmail").innerHTML = user.performer.email;
            document.querySelector("#userDepartment").innerHTML =   user.department ?? "Не розподілено"
            document.querySelector("#userRole").innerHTML = this.Lang.get(user.performer.role);
            document.querySelector("#userImg").setAttribute("data-src", user.imgPath);
            $(document).find('[data-src]').Lazy({
                effect: 'fadeIn',
                effectTime: 200,
                threshold: document.body.clientHeight,
                visibleOnly: false,
                onError: function (element) {
                    console.log('error loading ' + element.data('src'));
                },
                autoDestroy: true
            });
            this.Loader.hide();
        }
    }

    Factory.setSingletone("UCPanelHandler", UCPanelHandler);
})(window.Factory);