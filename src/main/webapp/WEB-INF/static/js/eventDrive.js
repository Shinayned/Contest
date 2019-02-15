$("#save").click(function() {
	console.log(this.id);
	$.get("drive/download?id=" + this.id)
	return false;
    });

$("#delete").click(function(){
	console.log(this.id);
	$.get("drive/remove?id=" + this.id)
	return false;
    });
