$(document).ready(function(){
var url = location.href;
var token = url.split('token=',2);
token = token[1];

$('#change').click(function(e) {
    var valid = $(".form-horizontal").serializeArray();
    if(valid[0].value == valid[1].value && valid[0].value.length >= 6){
        var data = $("#password").serialize();
        if(token) { data += "&token="+token}
            
        $.ajax({
            type: "POST",
            url: "/participant/changePassword",
            data: data,
            success: function () {
                location.href="../../pages/login.html";
            },
            error: function () {
                location.href="../../pages/error.html";
            }            

        });
    }    
    else if(valid[0].value.length >= 6)
        $("#errortext").text("Паролі не збігаються");
    else
        $("#errortext").text("Пароль короткий");

    
 return false;
    
})
});
