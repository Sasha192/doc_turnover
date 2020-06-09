let link = window.location.pathname

switch (link) {
    case "/archive":
        ajax_sendRequest("/archive/doc/list", append_Docs)
        log(link)
        break;
    case "/":
        ajax_sendRequest("/archive/doc/list", append_Docs)
        break;

    default:
        log("hueta")
        break;
}



function ajax_sendRequest(url, successFunction) {

    $.ajax({
        url: url,
        type: "GET",
        contentType: "application/json",
        success: function(data) {
            successFunction(data)
        },
    });

}

function ajax_sendResponse(url, data, successFunction) {

    $.ajax({
        url: url,
        type: "POST",
        data: data,
        success: function(data) {
            successFunction(data)
        },
    });

}