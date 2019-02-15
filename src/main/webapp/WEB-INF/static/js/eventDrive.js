
$("#save").click(function(e) {
	var id = $(this).parent().parent().parent().parent().attr("id");
	console.log(id);
	$.get("drive/download?id=" + id);
	e.preventDefault();
	return false;
    });

$("#delete").click(function(e){
	var id = $(this).parent().parent().parent().parent().attr("id");
	console.log(id);
	$.get("drive/remove?id=" + id);
	e.preventDefault();
	return false;
    });
