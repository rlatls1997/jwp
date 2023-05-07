$(document).ready(function () {/* jQuery toggle layout */
    $('#btnToggle').click(function () {
        if ($(this).hasClass('on')) {
            $('#main .col-md-6').addClass('col-md-4').removeClass('col-md-6');
            $(this).removeClass('on');
        } else {
            $('#main .col-md-4').addClass('col-md-6').removeClass('col-md-4');
            $(this).addClass('on');
        }
    });
});

String.prototype.format = function () {
    var args = arguments;

    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};

$(".answerWrite input[type=submit]").click(addAnswer);

function addAnswer(e) {
    e.preventDefault();

    const queryString = $("form[name=answer]").serialize();

    $.ajax({
        type: 'post',
        url: '/api/qna/addAnswer',
        data: queryString,
        dataType: 'json',
        error: onError,
        success: onSuccessAddAnswer,
    })
}

function onSuccessAddAnswer(json, status) {
    let answerTemplate = $("#answerTemplate").html();
    const template = answerTemplate.format(json.writer, new Date(json.createdDate), json.contents, json.answerId, json.answerId);
    $(".qna-comment-slipp-articles").prepend(template);
}

function onError() {
}

$(".qna-comment").click(deleteAnswer);

function deleteAnswer(e) {
    e.preventDefault();

    const target = $(e.target);

    if (target.hasClass("link-delete-article")) {
        const queryString = target.closest(".form-delete").serialize();

        $.ajax({
            type: 'post',
            url: '/api/qna/deleteAnswer',
            data: queryString,
            dataType: 'json',
            error: onError,
            success: function () {
                target.closest(".article").remove();
            },
        })
    }
}
