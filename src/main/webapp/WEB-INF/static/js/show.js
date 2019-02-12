$(document).ready(function(){
	var View = $('.showFilesList').clone(true);
	$("#start").remove();

	View.attr("id", "2sadasfas12e3");
	View.find('#text').text('txt.pdf');
	$('#FilesList').append(View);



//--------Це затестити треба----------
	var FileListsArray = [];
	FileListsArray = $.post("/drive/upload");
	console.log(FileList);

	//$('#FilesList').append(View);
	//document.querySelector("body").appendChild(ViewFiles);
});