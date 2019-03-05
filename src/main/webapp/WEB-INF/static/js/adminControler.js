var contestId;
$(".closed").click(function (e) {
	if($(this).hasClass("unknowID"))
	{
		var contestId = $(this).closest(".element").attr("id");
	}
	console.log(contestId);
	$.post("/admin/contest/changeStatus", ("contestId="+contestId));
	return false;
});

$(".getInfo").click(function (e) {
	contestId = $(this).parent().attr("id");
	console.log(contestId);
		$.ajax({
            type: "POST",
            url: "/admin/contest/getInfo",
            data: "contestId=" + contestId,
            success: function () {
                alert("В цей час будуть оброблятись дані)");
            }
        });	
	return false;
});

function validateEmail(email) {
    var pattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    return pattern.test(email);
}

$("#excelOnEmail").click(function (e) {
	var email = $("#inputAdress").serializeArray();
	if (validateEmail(email[0].value)) {
		var data = "contestId="+contestId+"&email="+email[0].value;
		console.log(data);
        $.ajax({
            type: "POST",
            url: "/admin/contest/sendApplicationsList",
            data: data,
            success: function () {
                alert("Таблиця заявок відправленна на пошту");
            }
        });

    } else {
        $(".with-errors").text("Неправильний email");
    }	
    return false;
});