window.onload = () => {
    if (localStorage.getItem('id') === null || localStorage.getItem('id') === undefined) {
        $('#user-dropdown').removeAttr('data-toggle')
    } else {
        $('#user-dropdown').attr('data-toggle', 'dropdown')
        success(localStorage.getItem('id'))
    }

    loadGoodDetail()
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

function loadGoodDetail() {
    var href = window.location.href.toString()
    axios({
        method: 'post',
        url: 'jav/query/good',
        data: getUrlParams(href),
    }).then(function (response) {
        var good = response.data.data
        $('#good-title').text(good.title)
        $('#good-price').text(good.price)
        $('#good-description').text(good.description)
        $('#good-title-img').attr('src', good.title_img)

        axios({
            method: 'post',
            url: 'jav/query_collect_state',
            data: {
                userId: localStorage.getItem('id'),
                goodId: good.id,
            },
        }).then(function (response) {
            if (response.data.data === true) {
                $('#collect-btn').text('取消收藏')
            } else {
                $('#collect-btn').text('收藏')
            }
        })

        $('#collect-btn').on('click', function () {
            if (localStorage.getItem('id') == null) {
                alert('请先登录')
                return
            }
            axios({
                method: 'post',
                url: 'jav/collect',
                data: {
                    userId: localStorage.getItem('id'),
                    goodId: good.id,
                },
            })
                .then(function (response) {
                    alert(response.data.data)
                    window.location.reload()
                })
                .catch(function (error) {
                    alert(error.response.msg)
                })
        })

        $('#buy-btn').on('click', function () {
            alert('暂不支持购买')
        })

        axios({
            method: 'post',
            url: 'jav/query-images-of-good',
            data: {
                id: good.id,
            },
        }).then(function (response) {
            var images = response.data.data
            var div = $('#good-show-container')
            if (images.length === 0) {
                div.append($('<p>暂无图片</p>'))
            }
            for (var i = 0; i < images.length; i++) {
                div.append($("<img  class='jumbotron' src='" + images[i] + "'></img>"))
            }
        })

        loadOwnerOfGood(good.owner_id)
    })
}

function loadOwnerOfGood(id) {
    axios({
        method: 'post',
        url: 'jav/query_user_by_id',
        data: {
            id: id,
        },
    }).then(function (response) {
        var user = response.data.data
        $('#owner-nickname').text(user.nickname)
        $('#owner-img').attr('src', user.img)
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
