
const append_Docs = (data) => {
    let table = $(".archive-table tbody");
    data.forEach(obj => {
        let row = createElement("tr")
        row.className = "archive-row"
        row.innerHTML = `
            <td>${obj.document.date}</td>
            <td document-id="${obj.document.id}"><a href="#">${obj.document.name.substr(0, 36)}... .${obj.document.extName}</a></td>
            <td department-id="${obj.department.id}"><a href="#">${obj.department.name}</a></td>
            <td performer-id="${obj.performer.id}"><a href="#">${obj.performer.name}</a></td>
            <td><i class="fas fa-share-alt" doc-id="${obj.document.id}" id="shareDoc"></i> <i class="fa fa-download" doc-id="${obj.document.id}" id="downloadDoc"></i></td>
        `

        row.querySelector("#shareDoc").onclick = (e) => {
            shareDoc(e)
        }

        row.querySelector("#downloadDoc").onclick = (e) => {
            downloadDoc(e)
        }

        table.append(row)


    });

}


// upload files

function ajax_sendFiles(url, data, beforeResponse, successFunction) {

    var progressBar = $(barSelector);

    $.ajax({
        url: url,
        type: "POST",
        data: data,
        cache: false,
        dataType: "json",
        processData: false,
        contentType: false,
        statusCode: beforeResponse() ,
        success: function (data) {
            successFunction(data)
        },

    })

}

(function () {
    let files = []

    $(".uploadFiles").change(function () {

        if (files.length > 5) return

        let file = this.files["0"]
        let lastIndexOfDot = file.name.lastIndexOf('.')
        let longName = file.name.substr(0, lastIndexOfDot).substr(0, 32);

        files.push
            ({
                file: file,
                longName: longName,
                shortName: longName.substr(0, 15),
                fileType: file.name.substr(lastIndexOfDot + 1),

            });

        $("#uploadFilesLable").html(`${longName}...`)

        reloadFiles()


    })


    function reloadFiles() {

        let filesContainer = $(".upload-files")

        filesContainer.html('')

        files.forEach(function (element, index) {
            let file = createElement("div")
            file.className = "upload-file"
            file.setAttribute("file-id", index)

            file.innerHTML = `
            <i class="fal fa-times"></i>
            <img src="img/docs-img/${element.fileType}.png" alt="" class="fileImage">
            <a href="#">${element.shortName}...</a>
        `;

            $(file).find("i").on("click", (e) => {
                let element = $(e.target.parentElement)
                let id = element.attr("file-id");
                element.fadeOut(100)
                files.splice(id, 1)

                setTimeout(() => {
                    reloadFiles();
                }, 200);
            })

            filesContainer.append(file)

        })


    }

    function beforeResponse() {
        
    }

    $("#uploadFiles").on("click", () => {

        if (files.length == 0) return;

        let formData = new FormData()

        files.forEach(element => {
            formData.append("file", element.file)
        })

        ajax_sendFiles("/archive/doc/upload", formData,  , (data) => {
            if (data.success == true) {
                   window.location = "/archive"
            } else {
                alert(data.msg)
            }
        })

    })

})()

