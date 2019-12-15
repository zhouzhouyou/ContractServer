$(function () {
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

    let resetBtn = $("#reset");
    resetBtn.click(function () {
        if (!isValid(passwordInput)) {
            $("#missingDataModal").modal();
            return;
        }

        if (!isValid(passwordInput2)) {
            $("#passwordNotMatchModal").modal();
            return;
        }

        $.ajax({
            type: "POST",
            url: "api/user/resetPassword",
            data: JSON.stringify({
                "password": `${passwordInput.val()}`
            }),
            headers: {'Content-Type': 'application/json;charset=utf8','token': getToken()},

            statusCode: {
                200: function () {
                    window.location.replace("login.html");
                }
            },

            error: function () {
                alert("修改密码失败");
            }
        })
    });

    let cancelBtn = $("#cancel");
    cancelBtn.click(function () {
        window.location.replace("welcome.html");
    });
});