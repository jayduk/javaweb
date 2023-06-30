window.onload = () => {
    $('#username').on('change', function () {
        username_check($(this), right_info, wrong_info)
    })

    $('#password').on('change', function () {
        password_check($(this), right_info, wrong_info)
        password_confirm_check($('#password2'), $(this), right_info, wrong_info)
    })

    $('#password2').on('change', function () {
        password_confirm_check($(this), $('#password'), right_info, wrong_info)
    })

    $('nickname').on('change', function () {
        if ($(this).val().length > 64) {
            wrong_info($(this), 'Nickname must be at most 64 characters long.')
        } else {
            right_info($(this))
        }
    })
}

function registry_check() {
    if (!username_check($('#username'), right_info, wrong_info)) {
        return false
    }

    if (!password_check($('#password'), right_info, wrong_info)) {
        return false
    }

    if (!password_confirm_check($('#password2'), $('#password'), right_info, wrong_info)) {
        return false
    }

    if ($('#nickname').val().length > 64) {
        return false
    }
    return true
}

function username_check(jq, right_info, wrong_info) {
    let username = jq.val()

    if (username.length < 6) {
        wrong_info(jq, 'Username must be at least 6 characters long.')
        return false
    } else if (username.length > 10) {
        wrong_info(jq, 'Username must be at most 10 characters long.')
        return false
    } else if (!username.match(/^[a-zA-Z0-9]+$/)) {
        wrong_info(jq, 'Username must contain only letters and numbers.')
        return false
    } else {
        right_info(jq)
        axios({
            method: 'post',
            url: 'http://localhost:8080/jav/is_exists_user',
            data: {
                username: username,
            },
        })
            .then(function (response) {
                if (response.data.data === true) {
                    wrong_info(jq, 'Username already exists.')
                } else {
                    right_info(jq)
                }
            })
            .catch(function (error) {
                console.log(error.data + 'NETWORK ERROR')
            })
    }
    return true
}

function password_check(jq, right_info, wrong_info) {
    let password = jq.val()

    if (password.length < 8) {
        wrong_info(jq, 'Password must be at least 8 characters long.')
        return false
    } else if (password.length > 16) {
        wrong_info(jq, 'Password must be at most 16 characters long.')
        return false
    } else if (!password.match(/^[a-zA-Z0-9]+$/)) {
        wrong_info(jq, 'Password must contain only letters and numbers.')
        return false
    } else {
        right_info(jq)
        return true
    }
}

function password_confirm_check(jq, jq1, right_info, wrong_info) {
    if (!password_check(jq, right_info, wrong_info)) return false

    if (jq1.val() !== jq.val()) {
        wrong_info(jq, 'Password does not match.')
        return false
    }
    return true
}

function right_info(jq) {
    jq.parent().parent().addClass('has-success')
    jq.parent().parent().removeClass('has-error')

    jq.parent().next().css('visibility', 'hidden')
}

function wrong_info(jq, err_msg) {
    jq.parent().parent().addClass('has-error')
    jq.parent().parent().removeClass('has-success')

    jq.parent().next().text(err_msg)
    jq.parent().next().css('visibility', 'visible')
}
