(function (Factory) {

    class ChartsHandler {
        constructor() {
            Factory.getClass("Notifications").render(document.querySelector(".tool-bar .notifications"));
            this.Data = Factory.getClass("Data");
            this._table = document.querySelector(".body");
            this.Loader = Factory.getClass("Loader");

            google.charts.load('current', { packages: ['corechart', 'bar'] });

            this.Data.get("Stats").then(list => {
                google.charts.setOnLoadCallback(() => this.render(list));
            });
        }

        render(list = []) {
            this._table.innerHTML = "";
            list.sort((a, b) => {
                let nameA = a.department ? a.department.name : a.department,
                    nameB = b.department ? b.department.name : b.department;
                if (nameA || nameB) {
                    if (nameA > nameB) return 1;
                    if (nameB > nameA) return -1;
                };return 0;
            });

            let lastDep,
                lastData = [];

            list.forEach((p, i) => {
                if (p.department && lastDep !== p.department.name) {
                    lastDep = p.department.name;
                    lastData = [['Виконавець', 'Нові', 'В прогреці', 'Просрочені', 'Завершені', 'Відкладені']];

                    lastData.push([p.name, p.newStatus, inprogress, overdue, completed, onhold]);
                    $(this._table).append(`<div class="chart"></div>`);
                } else if (p.department && lastDep == p.department.name) {
                    lastData.push([p.name, p.newStatus, inprogress, overdue, completed, onhold]);
                } else if (!p.department) {
                    if (lastDep !== "Не розподілені") {
                        lastData = [['Виконавець', 'Нові', 'В прогреці', 'Просрочені', 'Завершені', 'Відкладені']];
                        lastDep = "Не розподілені";
                        $(this._table).append(`<div class="chart"></div>`);
                    }

                    lastData.push([p.name, p.newStatus, inprogress, overdue, completed, onhold]);
                }

                if (!list[i + 1] || list[i + 1].department && list[i + 1].department.name !== lastDep) {
                    let container = this._table.querySelector(".chart:last-child"),
                        options = {
                        title: lastDep,
                        chartArea: { width: '50%' },
                        isStacked: true,
                        colors: ['#2196f3', '#ff9800', '#f44336', '#1bc943', '#b7bbca']
                    },
                        chart = new google.visualization.BarChart(container);

                    chart.draw(google.visualization.arrayToDataTable(lastData), options);
                } else if (!list[i + 1].department) {
                    let container = this._table.querySelector(".chart:last-child"),
                        options = {
                        title: lastDep,
                        chartArea: { width: '50%' },
                        isStacked: true,
                        colors: ['#2196f3', '#ff9800', '#f44336', '#1bc943', '#b7bbca']
                    },
                        chart = new google.visualization.BarChart(container);

                    chart.draw(google.visualization.arrayToDataTable(lastData), options);
                }
            });

            this.Loader.hide();
        }
    }

    Factory.setSingletone("ChartsHandler", ChartsHandler);
})(window.Factory);