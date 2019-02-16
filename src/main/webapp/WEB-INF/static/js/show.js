$(document).ready(function(){
	var View = $('.showFilesList').clone();
	$("#start").remove();

	$.fn.multiply = function( id , name) {
    var newElements = this.clone();
    newElements.addClass(name);
    newElements.attr("id", id);
    newElements.find('#text').text(name);
    return newElements;
	};

/*$(View).multiply(1,"asdasd","asdsdas").insertAfter('#FilesList');

	 JSON.parse($.post("drive/fileList", function(data) {
		console.log(drive);
        for (var i = data.length - 1; i >= 0; i--) {
        	$(View).multiply(1,data[i].id,data[i].name).insertAfter('#FilesList');
        }
	}));*/

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




});

