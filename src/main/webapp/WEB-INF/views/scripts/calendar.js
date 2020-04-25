switch (window.location.search) {
  case "?lang=ru":
    months = [
      "Январь",
      "Февраль",
      "Март",
      "Апрель",
      "Май",
      "Июнь",
      "Июль",
      "Август",
      "Сентябрь",
      "Октяюрь",
      "Ноябрь",
      "Декабрь",
    ];
    break;

  case "?lang=ua":
    months = [
      "Січень",
      "Лютий",
      "Березень",
      "Квітень",
      "Травень",
      "Червень",
      "Липень",
      "Серпень",
      "Вересень",
      "Жовтень",
      "Листопад",
      "Грудень",
    ];
    break;

  case "?lang=ua":
    months = [
      "Jan",
      "Feb",
      "Mar",
      "Apr",
      "May",
      "Jun",
      "Jul",
      "Aug",
      "Sep",
      "Oct",
      "Nov",
      "Dec",
    ];
    break;
}



class setDate {

  constructor(year, month) {
    this.year = year;
    this.month = month;
  }

  FirstDay() {
    return new Date(this.year, this.month).getDay();
  }

  daysInMonth() {
    return 32 - new Date(this.year, this.month, 32).getDate();
	}
	
}



window.onload = () => {
	const ddl_calendar = selector(".ddl-calendar")
	const cl_calendar = selector(".cl-calendar")
	loadCalendar(ddl_calendar, cl_calendar);
}



function loadCalendar () {

	let currentYear = new Date().getFullYear();
	let currentMonth = new Date().getMonth();

	for (let i = 0; i < arguments.length; i++) {
		let calendar = arguments[i];

		relSelector( calendar, "#cal-year" ).innerHTML = currentYear;
		relSelector( calendar, "#cal-month" ).innerHTML = months[currentMonth];

		let date = new setDate(currentYear, currentMonth);
		let firstDay = date.FirstDay();
		let daysInMonth = date.daysInMonth();

		console.log(daysInMonth)

		let day = 1;

		for (let j = 0; j < 6; j++) {
			let row = createElem("tr");

			for (let e = 0; e < 7; e++) {
				
				if ( j == 0 && e + 1 < firstDay ) {
					let cell = createElem("td");
					cell.appendChild( createText("") );
					row.appendChild(cell)
				}
				else if ( day > daysInMonth ) {
					break
				}
				else {
					let cell = createElem("td");
					cell.appendChild( createText(day) );
					cell.className = "td-date";
					row.appendChild(cell);

					day++
				}
				
			}

			relSelector( calendar, "#cal-body" ).appendChild(row);
			
		}
		
	}

}

