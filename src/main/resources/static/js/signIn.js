function failLogin() {
    $("#notMatchModal").modal();
}

$(function () {
    let nameInput = $("#name");
    nameInput.blur(function () {
        let nameValue = nameInput.val().length;
        if (nameValue >= 1 && nameValue <= 40) nameInput.removeClass("is-invalid").addClass("is-valid");
        else nameInput.removeClass("is-valid").addClass("is-invalid");
    });

    let passwordInput = $("#password");
    passwordInput.blur(function () {
        let passwordValue = passwordInput.val().length;
        if (passwordValue === 0) passwordInput.removeClass("is-valid").addClass("is-invalid");
        else passwordInput.removeClass("is-invalid").addClass("is-valid");
    });

    let signInBtn = $("#signIn");
    signInBtn.click(function () {
        if (!isValid(nameInput) || !(isValid(passwordInput))) {
            $("#missingDataModal").modal();
            return;
        }

        $.ajax({
            type: "POST",
            url: "api/signIn",
            data: JSON.stringify({
                "name": `${nameInput.val()}`,
                "password": `${passwordInput.val()}`
            }),
            headers: {'Content-Type': 'application/json;charset=utf8'},

            statusCode: {
                200: function (result) {
                    successLogin(result);
                }
            },

            error: function () {
                failLogin();
            }
        })
    });

    let signUpBtn = $("#signUp");
    signUpBtn.click(function () {
        window.location.replace("signUp.html");
    });

    let forgetPassword = $("#forgetPassword");
    forgetPassword.click(function () {
        $("#forgetPasswordModal").modal();
    })
});