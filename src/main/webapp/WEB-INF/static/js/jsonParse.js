function valideForm(input) {
var email  = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
var error = false;
for (var i = input.length - 1; i >= 0; i--) {
    if(input[i].name == "email")
    {
        if(!(email.test(input[i].value)))
        {
            $("."+input[i].name).text("Некоректний email");
            error = true;
        }        
    }
    else if(input[i].name == "phone")
    {
        if(!(input[i].value.length == 17 && (input[i].value.indexOf("_"))))
        {
            $("."+input[i].name).text("Неправильний телефон");
            error = true;               
        }
    }
    else if(input[i].name == "password")
    {
        if(input[i].value.length < 6)
        {
            $("."+input[i].name).text("Короткий пароль!");
            error = true;
        }        
    }
    else
    {
        if(input[i].value == "")
        {
            $("."+input[i].name).text("Поле пусте!");
            error = true;
        }
    }}
    return error;
}
$("#submit").click(function (e) {
    var data = $("#my-form").serializeObject();
    var input = $("#my-form").serializeArray();
    var url = this.name;
    if ( !(valideForm(input) )) {
        console.log("error");
    }
//parse in JSON
    //POST to server
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json"
    });

    if(this.name == "registration")
    {
       var c_alert = alert;

        window.alert = function (str) { //override default alert
            c_alert(str);
            location.href = "../../pages/login.html";
        }
        alert("На ваш email відправлений лист");
    }
    else if(this.name == "edit")
        location.reload();

        location.href="../../pages/error.html";

    return false;
});


//MAGIC
(function ($) {
    $.fn.serializeObject = function () {

        var self = this,
            json = {},
            push_counters = {},
            patterns = {
                "validate": /^[a-zA-Z][a-zA-Z0-9_]*(?:\[(?:\d*|[a-zA-Z0-9_]+)\])*$/,
                "key": /[a-zA-Z0-9_]+|(?=\[\])/g,
                "push": /^$/,
                "fixed": /^\d+$/,
                "named": /^[a-zA-Z0-9_]+$/
            };


        this.build = function (base, key, value) {
            base[key] = value;
            return base;
        };

        this.push_counter = function (key) {
            if (push_counters[key] === undefined) {
                push_counters[key] = 0;
            }
            return push_counters[key]++;
        };

        $.each($(this).serializeArray(), function () {

            // skip invalid keys
            if (!patterns.validate.test(this.name)) {
                return;
            }

            var k,
                keys = this.name.match(patterns.key),
                merge = this.value,
                reverse_key = this.name;

            while ((k = keys.pop()) !== undefined) {

                // adjust reverse_key
                reverse_key = reverse_key.replace(new RegExp("\\[" + k + "\\]$"), '');

                // push
                if (k.match(patterns.push)) {
                    merge = self.build([], self.push_counter(reverse_key), merge);
                }

                // fixed
                else if (k.match(patterns.fixed)) {
                    merge = self.build([], k, merge);
                }

                // named
                else if (k.match(patterns.named)) {
                    merge = self.build({}, k, merge);
                }
            }

            json = $.extend(true, json, merge);
        });

        return json;
    };
})(jQuery);




