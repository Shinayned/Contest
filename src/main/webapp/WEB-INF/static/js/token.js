$(document).ready(function(){
var url = "/participant/changePassword?token=123lkjgfdsasb234987asdfhk";
var token = url.split('token=',2);
token = token[1];
console.log(token);

$('#change').click(function(e) {
 
    var data = $('#password').serialize();
    if(token) { data += "&token="+token}
    console.log(data);

 $.ajax({
        type: "POST",
        url: "/participant/changePassword",
        data: data
    });
    
})
});
