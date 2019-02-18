$(".save").click(function(e) {
	var id = $(this).closest("div[title]").attr("id");
	var name = $(this).closest("div[title]").attr("name");
	console.log(name);
	//$.get("drive/download?id=" + id);
	this.href = name;
	console.log(this.href);
	return false;
    });

$(".remove").click(function(e){
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$('#'+id).remove();
	$.get("drive/remove?id=" + id);
	return false;
});