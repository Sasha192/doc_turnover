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
  let archive = selector(".arсhive-table tbody");
  for (let i = 0; i < data.length; i++) {
    let row = createElem("tr");
    row.className = "archive-row";
    let obj = data[i];
    log(obj);
      row.innerHTML = `
        <td><a href="" data-document-id="${obj.document.id}">${obj.document.name}</a></td>
        <td>${obj.document.creationDate}</td>
        <td><a href="" data-department-id="${obj.department.id}">${obj.department.departmentName}</a></td>
        <td><a href="" data-performer-id="${obj.performer.id}">${obj.performer.name}</a></td>
        <td><a href=""></a></td>
    `;
    archive.appendChild(row);
  }
}

$("body").on("click", function (e) {
  let elem = e.target;
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


let files;
let upload_input = relSelector("#upload_documents-body", "input");
c
let upload_lable = relSelector("#upload_documents-body", "label");

let file;
let upload_input = selector("#file");

upload_input.onchange = () => {

    file = upload_input.files[0];
    log(file);
}

selector("#upload_documents-submit").onclick = () => {

    let form_data = new FormData(selector(".sendform"));
    form_data.append('file', file);
    log(form_data);


    $.ajax({
        url: "/doc/upload",
        type: "POST",
        data: form_data,
        cache: false,
        dataType: "json",
        processData: false,
        contentType: false,
        success: () => {
            alert("success");
        }
    })
}