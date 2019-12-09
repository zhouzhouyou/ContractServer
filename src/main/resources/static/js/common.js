function successLogin(result) {
    Cookies.set("token", result);
    window.location.replace("welcome");
}

function isValid(control) {
    return control.hasClass("is-valid");
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