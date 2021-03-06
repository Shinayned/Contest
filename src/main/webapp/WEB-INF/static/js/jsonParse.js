function valideForm(input) {
    var email = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    var error = false;
    for (var i = input.length - 1; i >= 0; i--) {
        if (input[i].required) {
            switch (input[i].name) {

                case "email":
                    if (!(email.test(input[i].value))) {
                        $("." + input[i].name).text("Некоректний email");
                        $("#" + input[i].id).focus();
                        error = true;
                    }
                    break;
                case "phone":
                    if (!(input[i].value.length == 17 && (input[i].value.indexOf("_")))) {
                        $("." + input[i].name).text("Неправильний телефон");
                        $("#" + input[i].id).focus();
                        error = true;
                    }
                    break;
                case "password":
                    if (input[i].value.length < 6) {
                        $("." + input[i].name).text("Короткий пароль!");
                        $("#" + input[i].id).focus();
                        error = true;
                    }
                    if (input[i].value != $("#inputPasswordConfirm").val()) {
                        console.log(input[i].value + " === " + $("#inputPasswordConfirm").value);
                        $("#" + input[i].id).focus();
                        $("." + input[i].name).text("Паролі не збігаються!");
                        error = true;
                    }
                    break;
                default:
                    if (input[i].value == "") {
                        $("." + input[i].name).text("Поле пусте!");
                        error = true;
                    }
            }
        }
    }
    return error;
}
$("#submit").on('click',function(){
    var data = $("#my-form").serializeObject();
    var input = $("input");
    var url = this.name;
    if ((valideForm(input))) {
        return false;
    }
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function () {
            if (url == "registration") {
                alert("На ваш email відправлений лист");
                location.href = "../../pages/login.html";
            }
            else if (url == "edit") {
                location.reload();
            }
        },
        error: function (request, status, error) {
            if (status == 500)
            {
                $(".email").text("Цей email вже зареєстрований");
            }
            else
                location.href = "../../pages/error.html";
        }
    });


    return false;
});


$('#multiForm').on('submit', function (e) {
    $("body").addClass("loading");

    e.preventDefault();

    var url = $("form").attr("action");
    var formData = new FormData(document.querySelector('#multiForm'));

    $.ajax({
        url: url,
        method: 'POST',
        contentType: false, // важно - убираем форматирование данных по умолчанию
        processData: false, // важно - убираем преобразование строк по умолчанию
        data: formData,
        success: function () {
            $("body").removeClass("loading");
            alert("Заявка відправлена на обробку!");
            location.href = "/home";
        },
        error: function (request, status, error) {
            $("body").removeClass("loading");
            // do something!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
    });
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
