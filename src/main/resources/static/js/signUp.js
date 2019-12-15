function failSignUp() {
    $("#failToSignUp").modal();
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

    let passwordInput2 = $("#password2");
    passwordInput2.blur(function () {
        if (passwordInput2.val() === passwordInput.val()) passwordInput2.removeClass("is-invalid").addClass("is-valid");
        else passwordInput2.removeClass("is-valid").addClass("is-invalid");
    });

    let signUpBtn = $("#signUp");
    signUpBtn.click(function () {
        if (!isValid(nameInput) || !isValid(passwordInput)) {
            $("#missingDataModal").modal();
            return;
        }

        if (!isValid(passwordInput2)) {
            $("#passwordNotMatchModal").modal();
            return;
        }

        $.ajax({
            type: "POST",
            url: "api/signUp",
            data: JSON.stringify({
                "name": nameInput.val(),
                "password": getMd5(passwordInput.val())
            }),
            headers: {'Content-Type': 'application/json;charset=utf8'},

            statusCode: {
                200: function (result) {
                    successLogin(result);
                }
            },

            error: function () {
                failSignUp();
            }
        })
    });

    let signInBtn = $("#signIn");
    signInBtn.click(function () {
        window.location.replace("login.html");
    });

});