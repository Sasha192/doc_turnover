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
            console.log(list)
            this._table.innerHTML = "";
            list.sort((a, b) => {
                let depA = a.performer.department;
                let depB = b.performer.department;
                let nameA = depA ? depA.name : depA,
                    nameB = depB ? depB.name : depB;
                if (nameA || nameB) {
                    if (nameA > nameB) return 1;
                    if (nameB > nameA) return -1;
                };return 0;
            });

            let lastDep,
                lastData = [];

            list.forEach((p, i) => {
                if (p.performer.department && lastDep !== p.performer.department.name) {
                    lastDep = p.performer.department.name;
                    lastData = [['Виконавець', 'Нові', 'В прогреці', 'Просрочені', 'Завершені', 'Відкладені']];

                    lastData.push([p.performer.name, p.newStatus, p.inprogress, p.overdue, p.completed, p.onhold]);
                    $(this._table).append(`<div class="chart"></div>`);
                } else if (p.performer.department && lastDep == p.performer.department.name) {
                    lastData.push([p.performer.name, p.newStatus, p.inprogress, p.overdue, p.completed, p.onhold]);
                } else if (!p.performer.department) {
                    if (lastDep !== "Не розподілені") {
                        lastData = [['Виконавець', 'Нові', 'В прогреці', 'Просрочені', 'Завершені', 'Відкладені']];
                        lastDep = "Не розподілені";
                        $(this._table).append(`<div class="chart"></div>`);
                    }

                    lastData.push([p.performer.name, p.newStatus, p.inprogress, p.overdue, p.completed, p.onhold]);
                }

                if (!list[i + 1] || list[i + 1].performer.department && list[i + 1].performer.department.name !== lastDep) {
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