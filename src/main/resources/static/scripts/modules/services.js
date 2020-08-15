import { dropDown } from "./animation.js"


class Http {

    static post(url, data, callback) {
        $.ajax({
            type: "post",
            url,
            dataType: "json",
            data: JSON.stringify(data),
            success: callback,
            contentType: "application/json; charset=UTF-8"
        })
    }

    static get(url, callback) {
        $.ajax({
            type: "get",
            url,
            dataType: "json",
            success: callback,
            contentType: "application/json; charset=UTF-8"
        })
    }

    static files(url, data, success, complete, error) {
        $.ajax({
            type: "post",
            url,
            cache: false,
            dataType: "json",
            processData: false,
            contentType: false,
            data,
            success,
            complete,
            error
        })
    }

};

export { Http }

// -------------------
//        Date
// -------------------

(function () {

    const months = [
        "Jenuary", "February", "March",
        "April", "May", "June",
        "July", "August", "September",
        "October", "November", "December"]

    let date = new Date(),
        month_id = date.getMonth(),
        month = months[date.getMonth()],
        year = date.getFullYear()

    function formatDate(date) {

        let dd = date.getDate();
        if (dd < 10) dd = '0' + dd;

        let mm = date.getMonth() + 1;
        if (mm < 10) mm = '0' + mm;

        return dd + '.' + mm + '.' + date.getFullYear();
    }

    window.months = months
    window.month_id = month_id
    window.month = month
    window.year = year
    window.current_MDY = `${month} ${date.getDate()}, ${year}`
    window.current_DMY = formatDate(date)

})();

// -------------------
//      User props
// -------------------

class User {
    static role () {}
    static name () {}
    static id () {}
    static imgPath () {}

}

Http.get("/performer/my/info", user => {
    User.role = () => {return user.roles[0]}
    User.name = () => {return user.name}
    User.imgPath = () => {return user.imgPath}
    User.id = () => {return user.id}
})

export { User }


// -------------------s
//     Validation
// -------------------

(function () {
    const emailValid =
        /^(([^<>()\[\]\\.,;:\s@ "]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    window.emailValid = emailValid
})();

// -------------------
//     Insert Data
// -------------------

// defaultAjax

class Lang {
    static role(role) {
        switch (role) {
            case "PERFORMER":
                return "Виконавець"
                break;
            case "MANAGER":
                return "Менеджер"
                break;
            case "SECRETARY":
                return "Секретар"
                break;
            case "G_MANAGER":
                return "Головний Менеджер"
                break;
            case "ADMIN":
                return "Адміністратор"
                break;
            default:
                return "NONE"
                break;
        }
    }
}

export { Lang }

window.downloadFile = function(sUrl) {
    window.open(sUrl, '_self');
};
