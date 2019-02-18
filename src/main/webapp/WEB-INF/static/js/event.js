$(".save").click(function(e) {
	var id = $(this).closest("div[title]").attr("id");
	this.href = "drive/download?id=" + id;
    });

$(".remove").click(function(e){
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$('#'+id).remove();
	$.get("drive/remove?id=" + id);
	return false;
});