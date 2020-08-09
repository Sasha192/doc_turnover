
// -------------------
//     Scroll bar
// -------------------


(function () {
    $(document).scroll(() => {
        if ($(document).scrollTop() > $(".header-menu").height() + 200) {
            $(".archive-filter").css("position", "fixed").css("top", "0").css("box-shadow", "0px 0px 3px 0px #adadad")
        } else {
            $(".archive-filter").attr("style", "")
        }
    })
})();

// -------------------
//      Drop Down
// -------------------

function dropDown() {
    $(".drop-down_btn").each((index, element) => {
        element.onclick = (e) => {
            let button = e.target.closest(".drop-down_btn");
            (!button.closest(".drop-down").classList.contains("droped")) ? dropDown(button) : dropUp(button)
        }
    })

    $(".drop-down").each((index, dropBlock) => {
        dropBlock.querySelectorAll(".drop-down_item").forEach(dropItem => {
            dropItem.onclick = (e) => {
                if (dropBlock.querySelector(".drop-down_selected") !== null) {
                    dropBlock.querySelector(".drop-down_selected").innerHTML = e.target.closest(".drop-down_item").textContent
                } else {
                    return
                }
            }
        });
    })

    function dropDown(button) {
        closeAllDropDowns()
        $(button.closest(".drop-down").querySelector(".drop-down_list")).fadeIn(150)
        button.closest(".drop-down").classList.add("droped")

    }

    function dropUp(button) {
        $(button.closest(".drop-down").querySelector(".drop-down_list")).fadeOut(150)
        button.closest(".drop-down").classList.remove("droped")
    }

}

export { dropDown }

// ------------------------------------------
//        Change State Access Module
// ------------------------------------------

(function () {

    if (window.location.pathname == "/auth") {
        let state = "signUp",
            stateBlock = $(".state-change")

        $(".shift-button").on("click", () => {
            if (state == "signUp") {
                stateBlock.animate({
                    "right": "+50%",
                }, 200, () => {
                    stateBlock.find("button").html("Аутентифікація")
                    stateBlock.find("h6").html("Вхід до системи!")
                })
                state = "signIn"
            } else if (state == "signIn") {
                stateBlock.animate({
                    "right": '0%',
                }, 200, () => {
                    stateBlock.find("button").html("Реєстрація")
                    stateBlock.find("h6").html("Почати!")
                })
                state = "signUp"
            }
        })
    }

})();


function closeAllDropDowns() {

    $(".calendar").each((index, calendar) => {
        calendar.classList.remove("droped")
        $(calendar).find(".body").fadeOut(150)
    })

    $(".drop-down_list").each((index, dropList) => {
        dropList.closest(".drop-down").classList.remove("droped")
        $(dropList).fadeOut(150)
    })
}

export { closeAllDropDowns }


// -------------------
//    Calendar anim
// -------------------

window.onclick = (e) => {
    if (e.target.closest(".calendar")) return
    if (e.target.closest(".drop-down")) return
    closeAllDropDowns()
}

(function () {

    $(".calendar").each((index, calendar) => {
        calendar.querySelector(".head-block").onclick = (e) => {
            let calendar = e.target.closest(".calendar")
            if (!calendar.classList.contains("droped")) {
                closeAllDropDowns()
                $(calendar).find(".body").fadeIn(150)
                calendar.classList.add("droped")
            } else {
                closeAllDropDowns()
            }
        }
    })

})();
