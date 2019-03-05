$(document).ready(function(){
	$(".infoForConcurs").hide();
var contestId;
$(".closed").click(function (e) {
	if($(this).hasClass("unknowID"))
	{
		var contestId = $(this).closest(".element").attr("id");
	}
	console.log(contestId);
	$.ajax({
            type: "POST",
            url: "/admin/contest/changeStatus",
            data: "contestId="+contestId,
            success: function (data) {
                 $(this).hasClass("btn-danger") ?
                 $(this).addClass("btn-success").removeClass("btn-danger").text("Запустити"):
                 $(this).removeClass("btn-success").addClass("btn-danger").text("Зупинити");
            }
        });
	return false;
});

$(".getInfo").click(function (e) {
	contestId = $(this).parent().attr("id");
		//var obj = $.parseJSON( '{ "name": "Avionica", "id" : 1 , "description" : "something" , "amountOfApplication" : 0 , "expirationTime" : "2019-12-12" }' );							
		$.ajax({
            type: "POST",
            url: "/admin/contest/getInfo",
            data: "contestId="+contestId,
            success: function (data) {
            	var obj = $.parseJSON(data);
                $("#name").text("Назва конкурсу: " + obj.name);
    			$("#description").text("Короткий опис: " + obj.description);
    			$("#amouth").text("К-ть заявок: "+ obj.amountOfApplication);
    			$("#expirationTime").val(obj.expirationTime);	
				$(".infoForConcurs").show();
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
});