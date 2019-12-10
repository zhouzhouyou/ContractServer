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

function addTd(object, tr) {
    let keys = Object.keys(object);
    for (let i = 0; i < keys.length; i++) {
        let value = object[keys[i]];
        let td = $("<td></td>");
        td.text(value);
        tr.append(td);
    }
}

function addTr(collection, tableBody, additionalTr) {
    for (let i = 0; i < collection.length; i++) {
        let tr = $("<tr></tr>");
        addTd(collection[i], tr);
        additionalTr(collection[i], tr);
        tableBody.append(tr);
    }
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