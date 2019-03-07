$(document).ready(function(){
	var View = $('.showFilesList').clone(true,true);
	$("#start").remove();

	$.fn.multiply = function( id , name) {
    var newElements = this.clone(true,true);
    //newElements.addClass(name);
    newElements.attr("name", name);
    newElements.attr("id", id);
    newElements.find('#text').text(name);
    return newElements;
	};

	/*var i = 0;
	$("#add").click(function(e){
		$(View).multiply("id"+i,"test.docx").insertAfter('#FilesList');
		i++;
		return false;
	})*/
		$.post("drive/fileList", function(data) {
        for (var i = data.length - 1; i >= 0; i--) {
        	$(View).multiply(data[i].id,data[i].name).insertAfter('#FilesList');
        }
	});

});

