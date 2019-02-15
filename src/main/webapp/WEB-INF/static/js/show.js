$(document).ready(function(){
	var View = $('.showFilesList').clone(true);
	$("#start").remove();

	View.attr("id", "2sadasfas12e3");
	View.find('#text').text('teeext.pdf');
	$('#FilesList').append(View);



//--------Це затестити треба----------
	var FileListsArray = [];
	FileListsArray = JSON.parse($.post("drive/fileList"));
	console.log(FileListsArray);

	for (var i = FileListsArray.length - 1; i >= 0; i--) {
		View.attr("id", FileListsArray[i].id);
		View.find('#text').text(FileListsArray[i].name);
		$('#FilesList').append(View);
	}

});

