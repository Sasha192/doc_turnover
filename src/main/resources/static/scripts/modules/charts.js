
import { Http } from "./services.js"

Chart.defaults.global.defaultFontFamily = "Roboto regular";
Chart.defaults.global.defaultFontSize = 16;

window.onload = () => {

    let currentLink = document.querySelector('[data-key="date"]')

    let newData = [
        {
            "type": "MONTHLY",
            "expirationTime": 2592000000,
            "creationDate": "Sep 6, 2020, 6:31:26 PM",
            "amount": 0,
            "newS": 1,
            "overdue": 1,
            "completed": 1,
            "inprogress": 1,
            "onhold": 1,
            "expiredDeadline": 0,
            "performerId": 40,
            "briefPerformer": {
                "name": "Андрій Бунін 1",
                "department": "Начальник",
                "imgPath": "/img/default.jpg",
                "email": "Bunindom@gmail.com",
                "roles": [
                    "G_MANAGER"
                ],
                "id": 25
            },
            "id": 1
        },
        {
            "type": "MONTHLY",
            "expirationTime": 2592000000,
            "creationDate": "Sep 6, 2020, 6:31:26 PM",
            "amount": 0,
            "newS": 2,
            "overdue": 2,
            "completed": 2,
            "inprogress": 2,
            "onhold": 2,
            "expiredDeadline": 0,
            "performerId": 40,
            "briefPerformer": {
                "name": "Андрій Бунін 2",
                "department": "Начальник2",
                "imgPath": "/img/default.jpg",
                "email": "Bunindom@gmail.com",
                "roles": [
                    "G_MANAGER"
                ],
                "id": 12
            },
            "id": 1
        },
        {
            "type": "MONTHLY",
            "expirationTime": 2592000000,
            "creationDate": "Sep 6, 2020, 6:31:26 PM",
            "amount": 3,
            "newS": 3,
            "overdue": 3,
            "completed": 3,
            "inprogress": 3,
            "onhold": 3,
            "expiredDeadline": 0,
            "performerId": 15,
            "briefPerformer": {
                "name": "Андрій Бунін 3",
                "department": "Perforator",
                "imgPath": "/img/default.jpg",
                "email": "Bunindom@gmail.com",
                "roles": [
                    "G_MANAGER"
                ],
                "id": 40
            },
            "id": 1
        },
        {
            "type": "MONTHLY",
            "expirationTime": 2592000000,
            "creationDate": "Sep 6, 2020, 6:31:26 PM",
            "amount": 0,
            "newS": 4,
            "overdue": 4,
            "completed": 4,
            "inprogress": 4,
            "onhold": 4,
            "expiredDeadline": 0,
            "performerId": 15,
            "briefPerformer": {
                "name": "Андрій Бунін 4",
                "department": "Perforator",
                "imgPath": "/img/default.jpg",
                "email": "Bunindom@gmail.com",
                "roles": [
                    "G_MANAGER"
                ],
                "id": 40
            },
            "id": 1
        },
        {
            "type": "MONTHLY",
            "expirationTime": 2592000000,
            "creationDate": "Sep 6, 2020, 6:31:26 PM",
            "amount": 0,
            "newS": 5,
            "overdue": 5,
            "completed": 5,
            "inprogress": 5,
            "onhold": 5,
            "expiredDeadline": 0,
            "performerId": 32,
            "briefPerformer": {
                "name": "Андрій Бунін 5",
                "department": "Начальник",
                "imgPath": "/img/default.jpg",
                "email": "Bunindom@gmail.com",
                "roles": [
                    "G_MANAGER"
                ],
                "id": 40
            },
            "id": 1
        },
        {
            "type": "MONTHLY",
            "expirationTime": 2592000000,
            "creationDate": "Sep 6, 2020, 6:31:26 PM",
            "amount": 0,
            "newS": 6,
            "overdue": 6,
            "completed": 6,
            "inprogress": 6,
            "onhold": 6,
            "expiredDeadline": 0,
            "performerId": 32,
            "briefPerformer": {
                "name": "Андрій Бунін 6",
                "department": "Начальник",
                "imgPath": "/img/default.jpg",
                "email": "Bunindom@gmail.com",
                "roles": [
                    "G_MANAGER"
                ],
                "id": 40
            },
            "id": 1
        },
    ]

    class dateToggle {
        static week() {
            Http.get("/test", data => {
                updateData(newData)
            })
            currentLink.innerHTML = "За останній тиждень"
        }
        static month() {
            Http.get("/test", data => {
                updateData(newData)
            })
            currentLink.innerHTML = "За останній місяць"
        }
        static year() {
            Http.get("/test", data => {
                updateData(newData)
            })
            currentLink.innerHTML = "За останній рік"
        }
        static allTime() {
            Http.get("/test", data => {
                updateData(newData)
            })
            currentLink.innerHTML = "За весь час"
        }
    }

    {
        document.querySelector("#weekChart").onclick = () => dateToggle.week()
        document.querySelector("#monthChart").onclick = () => dateToggle.month()
        document.querySelector("#yearChart").onclick = () => dateToggle.year()
        document.querySelector("#allTimeChart").onclick = () => dateToggle.allTime()
    }

    Http.get("/test", data => {
        updateData(newData)
        currentLink.innerHTML = "За останній місяць"
    })

    function updateData(data) {

        let nextData = data.sort(
            (obj1, obj2) => {
                let depo1 = obj1.briefPerformer.department;
                let depo2 = obj2.briefPerformer.department;
                return depo1.localeCompare(depo2);
            }
        )

        updatePer(nextData)
    }

    function updatePer(pData) {
        $(".charts").html("")
        let perData = { performers: [], tasks: [] }

        pData.forEach(user => {
            perData.performers.push(user.briefPerformer.name)
            perData.tasks.push([user.newS, user.inprogress, user.completed, user.overdue, user.onhold])
        })

        let currentDep = ''
        let currentChart
        pData.forEach((per, i) => {
            if (currentDep !== per.briefPerformer.department) {
                currentDep = per.briefPerformer.department

                let chart = document.createElement("canvas")
                let block = document.createElement("div")

                block.className = "chart-container perchart-container"
                chart.className = `perchart`
                chart.dataset.dep = currentDep

                $(block).append(`<div class="dep-title">${currentDep}</div>`)
                block.append(chart)

                $(".charts").append(block)

                let thisChart = new Chart(chart, {
                    type: "horizontalBar",
                    data: {
                        labels: [perData.performers[i]],
                        datasets: [
                            {
                                label: "Нові",
                                data: [

                                ],
                                backgroundColor: 'rgba(54, 162, 235, 1)',
                                borderColor: 'rgba(54, 162, 235, 1)',
                                pointBackgroundColor: "#777",
                                borderWidth: 1
                            },
                            {
                                label: "В прогресі",
                                data: [

                                ],
                                backgroundColor: 'rgba(255, 206, 86, 1)',
                                borderColor: 'rgba(255, 206, 86, 1)',
                                pointBackgroundColor: "#777",
                                borderWidth: 1
                            },
                            {
                                label: "Завершені",
                                data: [

                                ],
                                backgroundColor: '#26de81',
                                borderColor: 'rgba(75, 192, 192, 1)',
                                pointBackgroundColor: "#777",
                                borderWidth: 1
                            },
                            {
                                label: "Просрочені",
                                data: [

                                ],
                                backgroundColor: 'rgba(255, 99, 132, 1)',
                                borderColor: 'rgba(255,99,132,1)',
                                pointBackgroundColor: "#777",
                                borderWidth: 1
                            },
                            {
                                label: "Відкладені",
                                data: [

                                ],
                                backgroundColor: '#bababa',
                                borderColor: 'rgba(0, 0, 0, 0.4)',
                                pointBackgroundColor: "#777",
                                borderWidth: 1
                            },

                        ]
                    },
                    options: {
                        scales: {
                            yAxes: [{
                                categoryPercentage: 0.2,
                                barPercentage: 0.5,
                                gridLines: {
                                    zeroLineColor: "black",
                                    zeroLineWidth: 1
                                },
                                stacked: false,
                            },
                            ],
                            xAxes: [{
                                ticks: {
                                    suggestedMin: 0,
                                    min: 0,
                                    beginAtZero: true
                                }
                            }]

                        },
                        legend: {
                            position: "right"
                        }


                    }
                });

                thisChart.data.datasets.forEach((dataset, x) => {
                    dataset.data.push(perData.tasks[i][x])
                })

                currentChart = thisChart
            } else {
                currentChart.data.labels.push(perData.performers[i])
                currentChart.data.datasets.forEach((dataset, x) => {
                    dataset.data.push(perData.tasks[i][x])
                })

            }

            currentChart.update()
        })
    }
}
