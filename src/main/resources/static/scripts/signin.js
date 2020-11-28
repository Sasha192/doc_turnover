(function (Factory) {

    class Signin {
        constructor() {
            this.Alert = Factory.getClass("Alert");
            this.Regexp = Factory.getClass("Regexp");
            this.Loader = Factory.getClass("Loader");
            this.Http = Factory.getClass("Http");

            this.email = $("#email");
            this.passwd = $("#passwd");
            this.rmme = document.querySelector("#rmme");

            $("#signup").on("click", e => this.signin(e));
            $("#suwf").on("click", e => this.signinWF(e));
            $("#suwg").on("click", e => this.signinWG(e));
        }

        signin(e) {
            e.preventDefault();

            if (this.email.val().length == 0) return this.Alert.render("warning", "Введіть електронну пошту");
            if (this.passwd.val().length == 0) return this.Alert.render("warning", "Введіть пароль");
            this.Loader.show("infinity");

            this.Http.post("/main/auth/login/", {
                email: this.email.val().trim(),
                passwd: this.passwd.val(),
                rememberMe: this.rmme.checked
            }, res => {
                this.Loader.hide(() => {
                    if (res.success) {
                        sessionStorage.setItem("email", this.email.val());
                        sessionStorage.setItem("signin", this.email.val());
                        window.location.href = "/main/confirm";
                    } else this.Alert.render("danger", `Сталася помилка ${res.msg.substr(0, 32)}...`);
                });
            });
        }

        signinWF(e) {}

        signinWG(e) {}
    }

    Factory.setSingletone("Signin", Signin);
})(window.Factory);