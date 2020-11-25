(function (Factory) {

    class Signup {
        constructor() {
            this.Alert = Factory.getClass("Alert");
            this.Regexp = Factory.getClass("Regexp");
            this.Loader = Factory.getClass("Loader");
            this.Http = Factory.getClass("Http");

            this.emailToConfirm = $("#emailToConfirm");
            this.confirmcode = $("#confirmcode");

            const ssEfield = sessionStorage.getItem("email");
            if (ssEfield) this.emailToConfirm.html(ssEfield);

            $("#confirm").on("click", e => this.confirm(e));
        }

        confirm(e) {
            e.preventDefault();

            if (this.confirmcode.val().length == 0) return this.Alert.render("warning", "Введіть код");
            this.Loader.show("infinity");

            this.Http.get(`/main/auth/verify?verificationCode=${this.confirmcode.val()}`, res => {
                this.Loader.hide(() => {
                    if (res.success) {
                        sessionStorage.setItem("confirm", true);
                        if (sessionStorage.getItem("signin")) {
                            window.location.href = "/com";
                        } else window.location.href = "/main/signup";
                    } else this.Alert.render("danger", `Сталася помилка ${res.msg.substr(0, 32)}...`);
                });
            });
        }
    }

    Factory.setSingletone("Signup", Signup);
})(window.Factory);