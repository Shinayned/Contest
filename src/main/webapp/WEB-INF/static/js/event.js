
$(".save").click(function(e) {
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$.get("drive/download?id=" + id);
	return false;
    });

$(".remove").click(function(e){
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$('#'+id).remove();
	$.get("drive/remove?id=" + id);
	return false;
});