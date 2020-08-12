import { Http } from "./services.js"
import { Lang } from "./services.js"
import { dropDown } from "./animation.js"

// Append Users 

(async () => {

    await new Promise((resolve, reject) => {
        Http.get("/performers/list", users => {
            console.log(users)
            users.forEach(user => {
                let userRole,
                    userDep

                // Validate Role
                if (user.roles.length == 0) {
                    userRole = `
                    <div class="drop-down">
                        <button class="drop-down_btn">
                            <span class="drop-down_selected">Призначити</span> <i class="fas fa-sort-down"></i>
                        </button>
                        <div class="drop-down_list" id="set-role">
                            <div class="drop-down-list_inner">
                            </div>
                        </div>
                    </div> ` } else {
                    userRole = Lang.role(user.roles[0])
                }

                // Validate Department
                if (!user.department) {
                    userDep = `
                    <div class="drop-down">
                        <button class="drop-down_btn">
                            <span class="drop-down_selected">Призначити</span> <i class="fas fa-sort-down"></i>
                        </button>
                        <div class="drop-down_list" id="set-dep">
                            <div class="drop-down-list_inner">
                            </div>
                        </div>
                    </div> ` } else {
                    userDep = user.department
                }

                $("#CL-Panel").append(
                    `<tr class="user" data-user-id="${user.id}">
                        <td><img src="${user.imgPath}" alt=""></td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>${userDep}</td>
                        <td data-role="${user.role}">${userRole}</td>
                        <td>
                            <div class="drop-down" id="user-config">
                                <button class="drop-down_btn"><i class="user-config-btn fas fa-ellipsis-v"></i></button>
                                <div class="drop-down_list">
                                    <button class="drop-down_item remove-user">
                                        Видалити
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </td>
                    </tr>`)
            })

            resolve()
        })
    }).then(
        () => {
            (async () => {

                // Set Department
                await new Promise((resolve, reject) => {
                    Http.get("/departments/list", deps => {
                        deps.forEach(dep => $("#set-dep .drop-down-list_inner").append(`<div class="drop-down_item" data-dep-id="${dep.id}">${dep.name}</div>`));
                        document.querySelectorAll(".remove-user").forEach(btn => {
                            btn.addEventListener("click", (e) => {
                                Http.get("/performers/remove")
                            })
                        })
                        resolve()
                    })
                }).then(
                    () => {
                        $("#set-dep .drop-down_item").each((i, dep) => {
                            dep.addEventListener("click", (e) => {
                                let userId = e.target.closest("[data-user-id]").dataset.userId,
                                    depId = e.target.closest(".drop-down_item").dataset.depId

                                if (e.target.closest(".drop-down_item").textContent.trim() == e.target.closest(".drop-down").querySelector(".drop-down_selected").textContent.trim()) return
                                Http.post(`/performers/modify/department?performer_id=${userId}&department_id=${depId}`, null, data => {
                                    location.reload()
                                })
                            })
                        })

                        dropDown()
                    }
                )

                // Set Roles
                await new Promise((resolve, reject) => {
                    Http.get("/roles/list", data => {
                        data.forEach(
                            role => $("#set-role .drop-down-list_inner").append(`<div class="drop-down_item" data-role-id="${role}">${Lang.role(role)}</div>`));
                        resolve()
                    })
                }).then(
                    () => {
                        $("#set-role .drop-down_item").each((i, dep) => {
                            dep.addEventListener("click", (e) => {
                                let userId = e.target.closest("[data-user-id]").dataset.userId,
                                    roleId = e.target.closest(".drop-down_item").dataset.roleId

                                if (e.target.closest(".drop-down_item").textContent.trim() == e.target.closest(".drop-down").querySelector(".drop-down_selected").textContent.trim()) return
                                Http.post(`/performers/modify/role?role=${roleId}&performer_id=${userId}`, null, data => {
                                   location.reload()
                                })
                            })
                        })

                        dropDown()
                    }
                )
            })()
        }
    )

})();

// Create new department
(function () {
    document.querySelector("#create-dep").onsubmit = (e) => {
        e.preventDefault()

        if (e.target.querySelector("input").value.trim().length == 0) return
        Http.get(`/departments/create?name="${e.target.querySelector("input").value.trim()}"`, () => { location.reload() })

    }
})()