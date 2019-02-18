$(".save").click(function(e) {
	//var id = $(this).closest("div[title]").attr("id");
	var name = $(this).closest("div[title]").attr("name");
	//$.get("drive/download?id=" + id);
	this.href = name;
	//return false;
    });

$(".remove").click(function(e){
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$('#'+id).remove();
	$.get("drive/remove?id=" + id);
	return false;
});