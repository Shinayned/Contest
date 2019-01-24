<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>Home</title>

    <!-- Bootstrap core CSS -->
    <link href="../static/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="../static/css/home.css" rel="stylesheet">

</head>

<body>
     <nav class="navbar navbar-expand fixed-top nav-hor">
 <div class="conteiner-fluid">
    <div class="row">
        <div class="col-12">
           
              <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <a href="#menu-toggle" class="d-md-none" id="menu-toggle">
                    <img src="../../static/img/hamburger.png" id="hamb" width="35" height="28">
                </a>
                <button class="btn btn-quit d-none d-md-block" type="submit" id="exit">Вийти</button>
              </div>
       
        </div>
    </div>
    </div>
         </nav>
<div id="wrapper">

        <!-- Sidebar -->
        <nav  class="fixed-top">
           <ul class="sidebar-nav" id="sidebar-wrapper">
                <a href="#">
                     <img src="../../static/img/iconphoto.png" alt="avatar" width="90" height="90" style="margin: 15px; background-color: #555555; border-radius: 50px;" id="avatar">
                     <span class="head-menu" id="name">Ім'я Прізвище</span>
                 </a>
                <div class="info" id="date">
                        Дата народження: 00.00.00
                </div>
                <div class="info" id="phone"> 
                        Телефон: 0978974820
                </div>
                <div class="info" id="email">
                        Email: oleh.melnechyk@gmail.com
                </div>
                <li>
                    <a href="#" class="text-center" style="margin-left: -25px;" id="edit-data">
                        Редагувати
                    </a>
                </li>
                <hr size="5" width="250" align="center" color="#1f1f1f">
                <li>
                    <a href="#">Головна</a>
                </li>
                <li>
          <a href="#">Мій диск</a>
        </li>
                 <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">Конкурси<span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="#">Дослідницький відбір</a></li>
            <li><a href="#">Науковий відбір</a></li>
            <li><a href="#">Державне замовлення</a></li>
          </ul>
            
        </li>
        <li>
          <a href="#">Про сайт</a>
        </li>
        <div class="conteiner nav-icon" >
                    <a href="#" class="bottom-panel mx-2"><img src="../../static/img/YSA.png" alt="" width="30" height="30"></a>
                    <a href="#" class="bottom-panel"><img src="../../static/img/facebook.png" alt="" width="30" height="30"></a>
                    <a href="#" class="bottom-panel" style="margin-right: 10px; position: absolute; right: 0px;"><img src="../../static/img/exit.png" alt="" width="30" height="30"></a>
            </div>
            </ul>
        </nav>
        <!-- /#sidebar-wrapper -->

        <!-- Page Content -->
        <div id="page-content-wrapper" class="py-0 px-0">
           
        <div class="row">
        <div class="col-md-3" >
            <!-- Sidebar -->
                
        </div>
        <div class="d-none d-md-block col-md-9 px-0" >

            <div id="carousel"  data-ride="carousel" class="carousel slide carousel-fade">
  <!-- Индикаторы -->
                 <ol class="carousel-indicators">
                    <li data-target="#carousel" data-slide-to="0" class="active"></li>
                    <li data-target="#carousel" data-slide-to="1"></li>
                    <li data-target="#carousel" data-slide-to="2"></li>
                </ol>
                <div class="carousel-inner">
                <div class="carousel-item active">
                    <img class="img-fluid" src="../../static/img/1.jpeg" alt="...">
                    <div class="carousel-caption">
                        <h1>Перший конкурс</h1>
                        <button type="submit" class="btn btn-light btn-active">Подати заявку</button>
                    </div>
                </div>
                <div class="carousel-item">
                    <img class="img-fluid" src="../../static/img/2.jpg" alt="...">
                     <div class="carousel-caption">
                        <h1>Другий конкурс</h1>
                         <button type="submit" class="btn btn-light btn-pause" disabled style="background-color: #ff8207;">Заявка розглядається</button>
                    </div>
                </div>
                <div class="carousel-item">
                    <img class="img-fluid" src="../../static/img/3.jpg" alt="...">
                     <div class="carousel-caption">
                        <h1>Третій конкурс</h1>
                         <button type="submit" class="btn btn-light btn-done" disabled style="background-color: #2ebb4e;">Заявка прийнята</button>
                    </div>
                </div>
                </div>
  <!-- Элементы управления -->
                <a class="carousel-control-prev" href="#carousel" role="button" data-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="sr-only">Предыдущий</span>
                </a>
                <a class="carousel-control-next" href="#carousel" role="button" data-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="sr-only">Следующий</span>
                </a>    
            </div>
            <div class="conteiner-fluid" style="background-color: #555555; height: 60px;">Contact</div>
        </div>
        <div class="col d-md-none text-center">
            <img class="img-fluid" src="../../static/img/2.jpg" alt="...">
                <div class="content">
                    <h1>Перший конкурс</h1>
                   <button type="submit" class="btn btn-light btn-active" style="background-color: #555555; color: #fff;">Подати заявку</button>
                </div> 
                <img class="img-fluid" src="../../static/img/3.jpg" alt="...">
                <div class="content">
                     <h1>Другий конкурс</h1>
                     <button type="submit" class="btn btn-light btn-pause" disabled style="background-color: #ff8207;">Заявка розглядається</button>
                </div>
                <img class="img-fluid" src="../../static/img/3.jpg" alt="...">
                 <div class="content">
                    <h1>Третій конкурс</h1>
                         <button type="submit" class="btn btn-light btn-done" disabled style="background-color: #2ebb4e;">Заявка прийнята</button>
                </div>
            </div>
            
           
            
        </div>
    </div>
</div>
</div>
        <!-- /#page-content-wrapper -->
    <!-- Bootstrap core JavaScript -->
    <script src="../static/vendor/jquery/jquery.min.js"></script>
    <script src="../static/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Menu Toggle Script -->
<script>
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
    </script>
</body>

</html>
