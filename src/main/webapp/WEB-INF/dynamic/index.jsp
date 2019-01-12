<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>Project Contest</title>
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Maven+Pro|Roboto">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <style>
        #deadlineTime {
            position: absolute;
            top: 50%;
            left: 50%;
            margin-left: -318px;
            margin-top: -59px;
            font-size: 100px;
            font-family: 'Maven Pro';
        }
        h1, h2, h3 {
            font-size: 50px;
            font-family: 'Roboto';
        }
    </style>
</head>
<body>
    <h1 align="center">Project CONTEST :)</h1>
    <h2 align="center">Deadline:</h2>
    <h3 id="currentTime" align="center"></h3>
    <div id="deadlineTime"></div>
    <script>
        var deadline = new Date(2019, 1, 0);
        deadline.setHours(deadline.getHours() - 3);
        timer();
        function timer() {
            var currentTime = new Date();
            var difference = new Date(deadline - currentTime);

            $("#deadlineTime").empty();
            $("#deadlineTime").append(difference.getDate() + "d " + difference.getHours() + ":" + difference.getMinutes() +
                ":" + difference.getSeconds());
            setTimeout(timer, 1000)
        }

        setInterval(timer, 1000);
    </script>
</body>
</html>