$(document).ready(function(){
	var View = $('.showFilesList').clone(true,true);
	$("#start").remove();

	$.fn.multiply = function( id , name) {
    var newElements = this.clone(true,true);
    newElements.addClass(name);
    newElements.attr("id", id);
    newElements.find('#text').text(name);
    return newElements;
	};


$(".save").click(function(e) {
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$.get("drive/download?id=" + id);
	e.preventDefault();
	return false;
    });

$(".remove").click(function(e){
	var id = $(this).closest("div[title]").attr("id");
	console.log(id);
	$('#'+id).remove();
	$.get("drive/remove?id=" + id);
	e.preventDefault();
	return false;
});

	JSON.parse($.post("drive/fileList", function(data) {
		console.log(drive);
        for (var i = data.length - 1; i >= 0; i--) {
        	$(View).multiply(data[i].id,data[i].name).insertAfter('#FilesList');
        }
	}));

});

