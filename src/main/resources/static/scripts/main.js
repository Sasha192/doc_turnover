$.ajax({
  url: "/departments",
  type: "GET",
  contentType: "application/json",
  success: function (data) {
    log(data);
    loadData(data);
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
  depCell.onchange = function () {
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

// archive

$.ajax({
  url: "/doc/list",
  type: "GET",
  contentType: "application/json",
  success: function (data) {
    loadArchive(data);
  },
  error: function (req, text, error) {
    console.log("Ошибка: " + text + " | " + error);
  },
});

function loadArchive(data) {
  console.log(data);
  let archive = selector(".arсhive-table tbody");
  for (let i = 0; i < data.length; i++) {
    let row = createElem("tr");
    row.className = "archive-row";
    let obj = data[i];
      row.innerHTML = `
        <td><a>${obj.name}</a></td>
        <td>${obj.creationDate}</td>
        <td><a>${obj.performer.department.departmentName}</a></td>
        <td><a>${obj.performer.mame}</a></td>      
        <td></td>
    `;

    log(row, archive);
    archive.appendChild(row);
  }
}

// '<td><span class="' +
// obj.priority +
// '">' +
// obj.name +
// "</span></td> <td>" +
// obj.date +
// '</td> <td><i class="fas fa-check success"></i></td> <td><a class="arhive-row__item" href="">' +
// obj.department +
// '</a></td> <td><a class="arhive-row__item" href="">' +
// obj.performer +
// '</a></td> <td><a class="arhive-row__item" data-id="' +
// obj.id +
// '" data-view="view" href="">Просмотр</a></td> <td><a class="arhive-row__item" data-id="${obj.id}" data-view="download" href=""><i class="fa fa-download"></i></a></td>';

/*{
  "id": 3,
    "creationDate": "Apr 12, 2020",
    "modificationDate": "Apr 12, 2020",
    "name": "filename_1",
    "path": "/home/kolmogorov/Java_Practice/bcrew/doc_turnover/src/main/webapp/archive/department__performer__15_04_2020.docx",
    "performer": {
  "id": 1,
      "mame": ""
      "department": {
    "id": 3,
        "departmentName": "sample3",
        "parentDepartment": "sample_parent3"
  }
},
  "deadline": false,
    "significance": 0
},*/


$("body").on("click", function (e) {
  let elem = e.target;
  log(elem.classList.contains("profile_bar-btn"));
  if (
      !elem.classList.contains("profile_bar-btn") &&
      !elem.parentElement.classList.contains("profile_bar-btn")
  ) {
    $(".profile_bar-btn").removeClass("active");
    $(".settings").fadeOut(100);
  }
});

function profileDDmenu() {
  let btn = $(".profile_bar-btn");
  let content = $(".settings");

  setTimeout(function () {
    if (!btn.hasClass("active")) {
      content.fadeIn(100);
      btn.addClass("active");
    } else {
      content.fadeOut(100);
      btn.removeClass("active");
    }
  }, 0);
}

// todo list

let todos = $(".todo_item");
let todos_noActive = selector("#todos-no-active")
let todos_active = selector("#todos-active");
let todo;

function todoDragStart() {
  $(this).fadeOut(300);
  todo = this;
}

function todoDragEnd() {
  if(!$(document.body).hasClass("modal-open")){
    $(this).fadeIn();
  };
  log("end");
}

function todoDrop(e) {
  $('#modalAddTodo').modal(open);

  selector("#addTodoBtn").onclick = () => {
    this.appendChild(todo);
    $(todo).fadeIn();
  }
  $('#modalAddTodo').on('hide.bs.modal', function (e) {
    if(todo.parentElement.getAttribute("id", "todos-no-active") ) {
      $(todo).fadeIn();
    };
  })
}

todos.each((i, todo) => {
  todo.addEventListener("dragstart", todoDragStart);

  todo.addEventListener("dragend", todoDragEnd);
});

todos_active.addEventListener("dragover", (e) => {
  e.preventDefault();
});

todos_active.addEventListener("dragenter", (e) => {
  e.preventDefault();
});

todos_active.addEventListener("drop", todoDrop);