window.onload = () => {
    var href = window.location.href.toString()

    if (localStorage.getItem('id') === null || localStorage.getItem('id') === undefined) {
        $('#user-dropdown').removeAttr('data-toggle')
    } else {
        $('#user-dropdown').attr('data-toggle', 'dropdown')
        success(localStorage.getItem('id'))
    }

    axios({
        method: 'post',
        url: 'jav/search',
        data: getUrlParams(href),
    }).then(function (response) {
        let goods = response.data.data

        var container = $('#search-result')
        // container.clear()
        if (goods.length === 0) {
            container.append('<p>未找到符合要求的物品</p>')
        } else {
            for (var i = 0; i < goods.length; i++) {
                var template = $('#search-good-template').children('div').clone()
                var good = goods[i]
                template.find('a.recommendation').attr('href', 'good.html?id=' + good.id)
                template.find('.img-rounded').attr('src', good.title_img)
                template.find('.title').text(good.title)
                template.find('.price').text(good.price)
                template.find('.description').text(good.description)

                container.append(template)
            }
        }
    })
}

function success(id) {
    queryUserById(id)

    $('#sign-out').on('click', function () {
        localStorage.removeItem('id')
    })
}

function queryUserById(id) {
    axios({
        method: 'post',
        url: 'jav/query_user_by_id',
        data: {
            id: id,
        },
    }).then(function (response) {
        let data = response.data.data
        $('#user-nickname').text(data.nickname)

        $('#logout').on('click', function () {
            localStorage.removeItem('id')

            axios({
                method: 'post',
                url: 'jav/logout',
                data: {
                    id: id,
                },
            }).catch(function (error) {
                alert(error.data)
            })
        })
    })
}

function getUrlParams(queryString) {
    var params = {}
    queryString = queryString.substring(queryString.indexOf('?') + 1)
    var paramPairs = queryString.split('&')

    for (var i = 0; i < paramPairs.length; i++) {
        var pair = paramPairs[i].split('=')
        var key = decodeURIComponent(pair[0])
        var value = decodeURIComponent(pair[1])

        if (key) {
            if (params[key]) {
                if (Array.isArray(params[key])) {
                    params[key].push(value)
                } else {
                    params[key] = [params[key], value]
                }
            } else {
                params[key] = value
            }
        }
    }

    return params
}
