$(".save").click(function(e) {
	var id = $(this).closest("div[title]").attr("id");
	var name = $(this).closest("div[title]").attr("name");
	this.setAttribute('download',name);
	this.href = "drive/download?id=" + id;
    });

$(".remove").click(function(e){
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$('#'+id).remove();
	$.get("drive/remove?id=" + id);
	return false;
});

function validateEmail(email) {
  var pattern  = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
  return pattern.test(email);
}

$("#post-btn").click(function (e) {
	var data = $("#inputEmail").serializeArray();
	if (validateEmail(data[0].value)) {
		var result = $.ajax({
            type: "POST",
            url: "/participant/resetPassword",
            data: data
        });
			  
	result.done(function() {
		$("#post-btn").remove();
		$("#errortext").addClass("good").text("Повідомлення було вислано на пошту");
	});
 
	result.fail(function() {
		$("#errortext").text("Такого email не існує");
	return false;})    
  } else {
    $("#errortext").text("Неправильний email");
  }
	return false;
})