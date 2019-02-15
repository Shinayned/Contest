$(document).ready(function(){
	var View = $('.showFilesList').clone(true);
	$("#start").remove();

	View.attr("id", "2sadasfas12e3");
	View.find('#text').text('txt.pdf');
	$('#FilesList').append(View);



//--------Це затестити треба----------
	var FileListsArray = [];
	FileListsArray = JSON.parse($.post("/drive/upload"));
	FileListsArray = $.post("/drive/upload");
	console.log(FileList);

	for (var i = FileListsArray.length - 1; i >= 0; i--) {
		View.attr("id", FileListsArray[i].id);
		View.find('#text').text(FileListsArray[i].name);
		$('#FilesList').append(View);
	}

});

$("#save").click(function() {
	$.ajax()
	{
			url: "/drive/download?id=" + this.id,
			type: 'get',
			success: function() {
				alert ('Скачивание началось!');
			}
	}

    });


$("#delete").click(function() {
	$.ajax()
	{
			url: "/drive/remove?id=" + this.id,
			type: 'get',
			success: function(data) {
				alert ('Удаленно');
			}
	}

    });