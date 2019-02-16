
$("#save").click(function(e) {
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$.get("drive/download?id=" + id);
	e.preventDefault();
	return false;
    });

$("#delete").click(function(e){
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$('#'+id).remove();
	$.get("drive/remove?id=" + id);
	e.preventDefault();
	return false;
    });
