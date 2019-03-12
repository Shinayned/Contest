$(document).ready(function () {
    $(document).on({
        ajaxStart: function () {
            $body.addClass("loading");
        },
        ajaxStop: function () {
            $body.removeClass("loading");
        }
    });
    $(".infoForConcurs").hide();
    var contestId;
    console.log("tetstststs");
    $(".closed").click(function (e) {
        var contestId = $(this).closest(".element").attr("id");
        var button = this;
        $.ajax({
            type: "POST",
            url: "/admin/contest/changeStatus",
            data: "contestId=" + contestId,
            success: function (data) {
                $(button).hasClass("btn-danger")?
                    ($(button).addClass("btn-success").removeClass("btn-danger").text("Запустити"),($(button).hasClass("active")?
                        $(".closed2").addClass("btn-success").removeClass("btn-danger").text("Запустити конкурс")) :
                    ($(button).removeClass("btn-success").addClass("btn-danger").text("Зупинити"),
                        ($(button).hasClass("active")?$(".closed2").removeClass("btn-success").addClass("btn-danger").text("Зупинити конкурс"));
            },
            error: function () {
                location.href = "/error";
            }
        })
        return false;
    });

    $(".getInfo").click(function (e) {
        contestId = $(this).parent().attr("id");
        var button = $(this).parent().find("button");
        //var obj = $.p$(this).parent().find("");arseJSON( '{ "name": "Avionica", "id" : 1 , "description" : "something" , "amountOfApplication" : 0 , "expirationTime" : "2019-12-12" }' );
        $.ajax({
            type: "POST",
            url: "/admin/contest/getInfo",
            data: "contestId=" + contestId,
            success: function (data) {
                var obj = data;
                contestId = obj.id;
                $("#name").text("Назва конкурсу: " + obj.name);
                $("#description").text("Короткий опис: " + obj.description);
                $("#amouth").text("К-ть заявок: " + obj.amountOfApplications);
                button.addClass("active");
                if (obj.expirationTime != null) {
                    obj.expirationTime = obj.expirationTime.split(" ");
                    $("#expirationTime").val(obj.expirationTime[0]);
                    $("#time").val(obj.expirationTime[1]);
                }
                if (button.hasClass("btn-success"))
                    $(".closed2").addClass("btn-success").removeClass("btn-danger").text("Запустити конкурс");
                else
                    $(".closed2").removeClass("btn-success").addClass("btn-danger").text("Зупинити конкурс");
                $(".infoForConcurs").show();
            },
            error: function () {
                location.href = "/error";
            }
        });
        return false;
    });

    function validateEmail(email) {
        var pattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        return pattern.test(email);
    }

    $("#excelOnEmail").click(function (e) {
        var email = $("#inputAdress").serializeArray();
        if (validateEmail(email[0].value)) {
            var data = "contestId=" + contestId + "&email=" + email[0].value;
            console.log(data);
            $.ajax({
                type: "POST",
                url: "/admin/contest/sendApplicationsList",
                data: data,
                success: function () {
                    alert("Таблиця заявок відправленна на пошту");
                }
            });

        } else {
            $(".with-errors").text("Неправильний email");
        }
        return false;
    });
    $("#data-end").click(function (e) {
        var date = $("#expirationTime").val();
        var time = $("#time").val();
        var datatime = date.split("-");
        var endDate = new Date(datatime[0], datatime[1], datatime[2]);
        var todayDate = new Date();
        console.log(date);
        console.log(time);
        console.log(endDate);
        console.log(todayDate);
        if (endDate != "" && endDate >= todayDate) {
            var data = "contestId=" + contestId + "&" + "expirationTime=" + date + " " + time;
            console.log(data);
            $.ajax({
                type: "POST",
                url: "/admin/contest/setExpirationTime",
                data: data,
                success: function () {
                    alert("Термін дії зміненно");
                }
            });
        }
        else
            alert("Ви ввели дату у минулому!");

        return false;
    })
});

