$(document).ready(function () {
    $body = $("body");

$(document).on({
    ajaxStart: function() { $body.addClass("loading");    },
     ajaxStop: function() { $body.removeClass("loading"); }    
});
    var dropZone = $('#upload-container');

    $('#file-input').focus(function () {
        $('label').addClass('focus');
    })
        .focusout(function () {
            $('label').removeClass('focus');
        });


    dropZone.on('drag dragstart dragend dragover dragenter dragleave drop', function () {
        return false;
    });

    dropZone.on('dragover dragenter', function () {
        dropZone.addClass('dragover');
    });

    dropZone.on('dragleave', function (e) {
        let dx = e.pageX - dropZone.offset().left;
        let dy = e.pageY - dropZone.offset().top;
        if ((dx < 0) || (dx > dropZone.width()) || (dy < 0) || (dy > dropZone.height())) {
            dropZone.removeClass('dragover');
        }
    });

    dropZone.on('drop', function (e) {
        dropZone.removeClass('dragover');
        let files = e.originalEvent.dataTransfer.files;
        sendFiles(files);
    });
    $('#file-input').change(function () {
        let files = this.files;
        sendFiles(files);
    });

    function sendFiles(files) {
        let maxFileSize = 15242880;
        let Data = new FormData();
        $(files).each(function (index, file) {
            if ((file.size <= maxFileSize)) {
                Data.append('uploadingFiles', file);
            }
        });
        Data.append("testVar", "IT'S WORKING!!!!!!");
        $.ajax({
            url: "/drive/upload/",
            type: "POST",
            data: Data,
            contentType: false,
            processData: false,
            success: function (data) {
                alert('Файлы были успешно загружены!');
                location.reload();
            }
        });

        return false;
    }
});