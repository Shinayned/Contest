$("#save").click(function() {
	var id = $(this).parent().parent().parent().parent().attr("id");
	console.log(id);
	$.get("drive/download?id=" + id)
	return false;
    });

$("#delete").click(function(){
	var id = $(this).parent().parent().parent().parent().attr("id");
	console.log(id);
	$.get("drive/remove?id=" + id)
	return false;
    });
