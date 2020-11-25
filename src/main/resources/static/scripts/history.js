(function (Factory) {

    class History {

        constructor() {
            this.Navbar = Factory.getClass("Navbar");
            this.Content = Factory.getClass("Content");

            this.Content.init({
                "/com/charts": document.querySelector('templates [data-content="charts"]'),
                "/com/files": document.querySelector('templates [data-content="files"]'),
                "/com/boards": document.querySelector('templates [data-content="boards"]'),
                "/com/clPanel": document.querySelector('templates [data-content="clPanel"]'),
                "/main/econfig": document.querySelector('templates [data-content="econfig"]'),
                "/com/uconfig": document.querySelector('templates [data-content="uconfig"]'),
            });

            this.Navbar.elements["files"].sub(() => this.push({ title: "Файли", url: "/com/files" }));

            this.Navbar.elements["boards"].sub(() => this.push({ title: "Дошки", url: "/com/boards" }));

            this.Navbar.elements["clPanel"].sub(() => this.push({ title: "Панель управління", url: "/com/clPanel" }));

            this.Navbar.elements["econfig"].sub(() => this.push({ title: "Кабінет підприємства", url: "/main/econfig" }));

            this.Navbar.elements["uconfig"].sub(() => this.push({ title: "Кабінет користувача", url: "/com/uconfig" }));

            this.Navbar.elements["charts"].sub(() => this.push({ title: "Статистика", url: "/com/charts" }));

            if (history.state == null) {
                this.set({ title: "Дошки", url: "/com/boards" });
            } else this.set(history.state);

            window.onpopstate = e => {
                if (e.state) {
                    this.set(e.state);
                } else return;
            };
        }

        render(data) {
            this.Content.render(data.url);
            this.Navbar.elements[data.url.substring(5)].active();
            document.title = data.title;
        }

        push(data) {
            history.pushState(data, data.title, data.url);
            this.render(data);
        }

        set(data) {
            history.replaceState(data, data.title, data.url);
            this.render(data);
        }

    }

    Factory.setSingletone("History", History);
})(window.Factory);