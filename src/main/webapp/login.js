window.onload = () => {
    $('#username').on('change', function () {
        username_check($(this), right_info, wrong_info)
    })

    $('#password').on('change', function () {
        password_check($(this), right_info, wrong_info)
    })

    $('#login-form').ajaxForm(function (data) {
        let response = JSON.parse(data)
        if (response.code === 200) {
            localStorage.setItem('id', response.data)
            window.location.href = 'index.html'
        } else {
            alert(response.msg)
        }
    })
}

function login_check() {
    if (!username_check($('#username'), right_info, wrong_info)) {
        return false
    }

    // if (!password_check($("password"), right_info, wrong_info)) {
    //   return false;
    // }

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
                    right_info(jq)
                } else {
                    wrong_info(jq, 'Username does not exists.')
                }
            })
            .catch(function (error) {
                console.log(error.data + 'NETWORK ERROR')
            })

        return true
    }
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
