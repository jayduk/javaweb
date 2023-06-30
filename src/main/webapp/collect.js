window.onload = () => {
    if (localStorage.getItem('id') === null || localStorage.getItem('id') === undefined) {
        $('#user-dropdown').removeAttr('data-toggle')
        alert('请先登录')
        window.location.href = 'index.html'
        return
    } else {
        $('#user-dropdown').attr('data-toggle', 'dropdown')
        success(localStorage.getItem('id'))
    }

    axios({
        method: 'post',
        url: 'jav/query_collect_of_user',
        data: {
            userId: localStorage.getItem('id'),
        },
    }).then(function (response) {
        var collects = response.data.data

        console.log(collects)

        var container = $('#collect-list')

        if (collects.length === 0) {
            container.append('<p>您还没有收藏任何物品</p>')
        } else {
            for (let i = 0; i < collects.length; i++) {
                var tr = $('<tr></tr>')
                tr.append('<td>' + collects[i].good.title + '</td>')
                tr.append('<td>' + collects[i].good.price + '</td>')
                tr.append('<td>' + collects[i].createTime + '</td>')
                tr.append('<td>' + collects[i].user.nickname + '</td>')

                var td = $('<td></td>')

                var look = $('<button class="btn btn-xs btn-success">去查看</button>')
                look.on('click', function () {
                    window.location.href = 'good.html?id=' + collects[i].good.owner_id
                })

                var remove = $('<button class="btn btn-xs btn-success">移除</button>')
                remove.on('click', function () {
                    removeCollect(collects[i].id)
                })

                td.append(look)
                td.append(remove)

                tr.append(td)
                container.append(tr)
            }
        }
    })
}

function removeCollect(id) {
    axios({
        method: 'post',
        url: 'jav/remove_collect',
        data: {
            id: id,
        },
    })
        .then(function (response) {
            window.location.reload()
        })
        .catch(function (error) {
            alert(error.response.data.msg)
        })
}

function success(id) {
    $('#sign-out').on('click', function () {
        localStorage.removeItem('id')
    })

    queryUserById(id)
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
