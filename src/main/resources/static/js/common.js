function successLogin(result) {
    Cookies.set("token", result);
    window.location.replace("welcome");
}

function getToken() {
    return Cookies.get('token');
}

function isValid(control) {
    return control.hasClass("is-valid");
}

function setValid(object, valid) {
    if (valid) object.removeClass("is-invalid").addClass("is-valid");
    else object.removeClass("is-valid").addClass("is-invalid");
}

function addRows(collection, tableBody, alternative, additional) {
    collection.forEach(function (element) {
        let tr = $(`<tr></tr>`);
        if (alternative === null) Object.keys(element).forEach(value => tr.append($(`<td>${element[value]}</td>`)));
        else alternative(element, tr);
        if (additional != null) additional(element, tr);
        tableBody.append(tr);
    });
}

function refreshTable(tableBody, collection, alternative, additional ) {
    tableBody.empty();
    addRows(collection, tableBody, alternative, additional);
}

function sendRequest(method, uri, data, successFunc, failFunc) {
    $.ajax({
        type: method,
        uri: uri,
        data: JSON.stringify(data),
        headers: {'Content-Type': 'application/json;charset=utf8', 'token': Cookies.get("token")},
        success: function (data) {
            successFunc(data);
            console.log(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            failFunc(XMLHttpRequest, textStatus, errorThrown);
        }
    });
}