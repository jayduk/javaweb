window.onload = () => {
    if (localStorage.getItem('id') === null || localStorage.getItem('id') === undefined) {
        $('#user-dropdown').removeAttr('data-toggle')
    } else {
        $('#user-dropdown').attr('data-toggle', 'dropdown')
        success(localStorage.getItem('id'))
    }

    queryRecommendGoods()
    queryRecommendCarousel()
}

function queryRecommendCarousel() {
    axios({
        method: 'post',
        url: 'jav/recommend-goods-list',
        data: {
            count: 5,
        },
    }).then(function (response) {
        var carousels = response.data.data
        var div = $('#recommend-carousel')

        for (let i = 0; i < carousels.length; i++) {
            var template = $('#recommend-carousel-template').children('a').clone()

            if (i == 0) {
                template.addClass('active')
            }

            template.attr('href', 'good.html?id=' + carousels[i].id)
            template.find('img').attr('src', carousels[i].title_img)

            div.append(template)
        }
    })
}

function success(id) {
    queryUserById(id)

    $('#sign-out').on('click', function () {
        localStorage.removeItem('id')
    })
}

function queryRecommendGoods() {
    axios({
        method: 'post',
        url: 'jav/recommend-goods-list',
        data: {
            count: 20,
        },
    }).then(function (response) {
        var goods = response.data.data
        var div = $('#recommend-goods-list')
        var row
        for (var i = 0; i < goods.length; i++) {
            if (i % 3 == 0) {
                row = $("<div class='row'></div>")
                div.append(row)
            }
            row.append(create_recommend_good(goods[i]))
        }
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

function create_recommend_good(good) {
    var recommend_good = $('#recommend-good-template').children('div').clone()

    recommend_good.find('a.recommendation').attr('href', 'good.html?id=' + good.id)
    recommend_good.find('.img-rounded').attr('src', good.title_img)
    recommend_good.find('.title').text(good.title)
    recommend_good.find('.price').text(good.price)

    console.log(recommend_good.html())

    return recommend_good
}
