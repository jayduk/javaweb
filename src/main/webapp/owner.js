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

    $('#host_id').val(localStorage.getItem('id'))

    $('#new_good').on('click', function () {
        popValue('新物品', '', '', '')
    })

    axios({
        method: 'post',
        url: 'jav/query-goods-of-user',
        data: {
            id: localStorage.getItem('id'),
        },
    }).then(function (response) {
        var goods = response.data.data
        console.log(goods)

        var container = $('#goods-list')

        if (goods.length === 0) {
            container.append('<p>您还没有上架任何物品</p>')
        } else {
            for (let i = 0; i < goods.length; i++) {
                var tr = $('<tr></tr>')
                tr.append('<td>' + goods[i].title + '</td>')
                tr.append('<td>' + goods[i].description + '</td>')
                tr.append('<td>' + goods[i].price + '</td>')

                var td = $('<td></td>')

                var look = $('<button class="btn btn-xs btn-success">去查看</button>')
                look.on('click', function () {
                    window.location.href = 'good.html?id=' + goods[i].id
                })

                var edit = $(
                    '<button class="btn btn-success btn-xs" data-toggle="modal" data-target="#myModal">编辑</button>'
                )
                edit.on('click', function () {
                    $('#good_id').val(goods[i].id)
                    popValue('编辑', goods[i].title, goods[i].description, goods[i].price)
                })

                var remove = $('<button class="btn btn-xs btn-success">下架</button>')
                remove.on('click', function () {
                    axios({
                        method: 'post',
                        url: 'jav/remove_good',
                        data: {
                            id: goods[i].id,
                        },
                    }).then(function (response) {
                        alert(response.data.data)
                        window.location.reload()
                    })
                })

                td.append(look)
                td.append(edit)
                td.append(remove)

                tr.append(td)
                container.append(tr)
            }
        }
    })
}

function popValue(header, title, description, price) {
    if (header === '编辑') {
        $('#add_good_type').val('edit')
        $('#good-submit').text('确认修改')
    } else {
        $('#add_good_type').val('new')
        $('#good-submit').text('确认添加')
    }
    $('#myModalLabel').text(header)
    $('#title').val(title)
    $('#description').val(description)
    $('#price').val(price)
}

function removeCollect(userId, goodId) {
    axios({
        method: 'post',
        url: 'jav/collect',
        data: {
            userId: userId,
            goodId: goodId,
        },
    })
        .then(function (response) {
            alert(response.data.data)
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
