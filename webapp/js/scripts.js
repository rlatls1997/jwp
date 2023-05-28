// $(".qna-comment").on("click", ".answerWrite input[type=submit]", addAnswer);
$(".answerWrite input[type=submit]").click(addAnswer);
$(document).on("click", ".qna-comment-slipp-articles button[type=submit]", function (event) {
    event.preventDefault();
    deleteAnswer($(this));
});


function addAnswer(e) {
    e.preventDefault();

    var queryString = $("form[name=answer]").serialize();

    $.ajax({
        type: 'post',
        url: '/api/qna/addAnswer',
        data: queryString,
        dataType: 'json',
        error: onError,
        success: onSuccess,
    });
}

function onSuccess(json, status) {
    var answer = json.answer;
    var answerTemplate = $("#answerTemplate").html();
    var createDate = new Date(answer.createdDate);
    var dateString = `${createDate.getFullYear()}-${createDate.getMonth()}-${createDate.getDate()} ${createDate.getHours()}:${createDate.getMinutes()}:${createDate.getSeconds()}`;
    var template = answerTemplate.format(answer.writer, dateString, answer.contents, answer.answerId, answer.answerId, answer.questionId);
    $(".qna-comment-slipp-articles").prepend(template);

    var countOfComment = json.countOfComment;
    $("#qna-count-of-comment").html(countOfComment);
}

function onError(xhr, status) {
    alert("error");
}

function deleteAnswer(target) {
    var queryString = target.closest("form[name=answerDelete]").serialize();

    $.ajax({
        type: 'post',
        url: '/api/qna/deleteAnswer',
        data: queryString,
        dataType: 'json',
        error: () => alert("error"),
        success: (json, status) => {
            var countOfComment = json.countOfComment;
            $("#qna-count-of-comment").html(countOfComment);

            target.closest(".article").remove();
        },
    });
}

String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};