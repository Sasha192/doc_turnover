// FILTER //

$.ajax({
  url: "/departments",
  type: "GET",
  contentType: "application/json",
  success: function (data) {
    let departments = JSON.parse(data);
    console.log(data);
    loadData(departments);
  },
});

function loadData(data) {
  let depCell = selector("#filter-depart");

  for (let i = 0; i < data.length; i++) {
    let option = createElem("option");
    option.setAttribute("value", i);
    option.innerHTML = data[i].name;
    depCell.appendChild(option);
  }
  let selected;

  depCell.onchange = () => {
    selected = depCell.options[depCell.selectedIndex];
    let empCell = selector("#filter-employee");
    if (selected.getAttribute("value")) {
      let index = selected.getAttribute("value");
      let employees = data[index].employees;

      empCell.innerHTML = "";

      for (let i = 0; i < employees.length; i++) {
        let option = createElem("option");
        option.setAttribute("value", i);
        option.innerHTML = employees[i];
        empCell.appendChild(option);
      }

      empCell.removeAttribute("disabled");
    } else {
      empCell.setAttribute("disabled", "");
      let option = createElem("option");
      option.innerHTML = "Выберите работника";
      empCell.innerHTML = "";
      empCell.appendChild(option);
    }
  };
}

// ARHIVE //

$.ajax({
  url: "/main-arhive/data",
  type: "GET",
  contentType: "application/json",
  success: function (data) {
    let arhive = JSON.parse(data);
    loadArhive(arhive);
  },
});

function loadArhive(data) {
  for (let i = 0; i < data.length; i++) {
    let row = createElem("tr");
		row.className = "arhive-row";

		let name = createElem("td").innerHTML = '<span class="'data[i].priority'">'data[i].name'</span>';
		let date = createElem("td").innerHTML = data[i].date;
		let department = createElem("td").innerHTML = data[i].department;
		let performer = createElem("td").innerHTML = data[i].performer;
		let view = createElem("td").innerHTML = '<a class="arhive-row__item" data-view="view" href="">Просмотр</a>'



		row.appendChild("")
		
  }
}

{
  /* <tr class="arhive-row">
			<td><span class="primary">Пойти пожарить шашлиндос с Михалычем</span></td>
			<td>29.11.2021</td>
			<td><i class="fas fa-check success"></i></td>
			<td><a class="arhive-row__item" href="">Рукожопы</a></td>
			<td><a class="arhive-row__item" href="">Lorem ipsum dolor sit.</a></td>
			<td><a class="arhive-row__item" data-view="view" href="">Просмотр</i></i></a></td>
			<td><a class="arhive-row__item" data-view="download" href=""><i class="fa fa-download"></i></a></td>
</tr> */
}
