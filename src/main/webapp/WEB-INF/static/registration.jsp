

<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="http://bootstraptema.ru/plugins/2015/bootstrap3/bootstrap.min.css" />
    <link rel="stylesheet" href="style.css">
    <title>YSA Kvest</title>
    <script type="text/javascript">
  function mask(inputName, mask, evt) {
    try {
      var text = document.getElementById(inputName);
      var value = text.value;

      // If user pressed DEL or BACK SPACE, clean the value
      try {
        var e = (evt.which) ? evt.which : event.keyCode;
        if ( e == 46 || e == 8 ) {
          text.value = "";
          return;
        }
      } catch (e1) {}

      var literalPattern=/[0\*]/;
      var numberPattern=/[0-9]/;
      var newValue = "";

      for (var vId = 0, mId = 0 ; mId < mask.length ; ) {
        if (mId >= value.length)
          break;

        // Number expected but got a different value, store only the valid portion
        if (mask[mId] == '0' && value[vId].match(numberPattern) == null) {
          break;
        }

        // Found a literal
        while (mask[mId].match(literalPattern) == null) {
          if (value[vId] == mask[mId])
            break;

        newValue += mask[mId++];
      }

      newValue += value[vId++];
      mId++;
    }

    text.value = newValue;
  } catch(e) {}
}
</script>
  </head>
  <body>

  	<div class="container">
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				
 				<form data-toggle="validator" role="form" class="form-horizontal">
 					<span class="heading">Реєстрація</span>
 				<div class="form-group">
 					<label for="inputName" class="control-label">Введіть ПІБ:</label>
 					<input type="text" class="form-control" id="inputName" placeholder="Введите Ваше имя" required>
 				</div>
 				<div class="form-group">
 					<label for="inputDate" class="control-label">Дата народження:</label>
 					<input type="date" class="form-control" id="inputDate" required>
 					<div class="help-block with-errors"></div>
 				</div>
 				<div class="form-group">
 					<label for="inputTel" class="control-label">Ваш телефон:</label>
 					<input type="text" id="zipCode" class="form-control" onkeyup="javascript:mask('zipCode', '+38(000)000-00-00', event);" value="+38(___)___-____" >

 					<div class="help-block with-errors"></div>
 				</div>
 				<div class="form-group">
 					<label for="inputEmail" class="control-label">Ваш E-mail</label>
 					<input type="email" class="form-control" id="inputEmail" placeholder="Email" data-error="Вы не правильно ввели Ваш E-mail" required pattern="38[0-9]{3}[0-9]{3}[0-9]{2}[0-9]{2}">
 					<div class="help-block with-errors"></div>
 				</div>
 				<div class="form-group">
 					<label for="inputMessage" class="control-label text-center">Детальніша інформація(Місце роботи/навчання(курс,спеціальність,факультет))</label>
 					 <textarea name="message" id="message" class="form-control" rows="9" cols="25"  style="min-height: 150px; required="required" placeholder="Введіть текст"></textarea>
 					<div class="help-block with-errors"></div>
 				</div>
 				<div class="form-group">
 					<label for="inputPassword" class="control-label">Введіть пароль(не менше 6 символів)</label>
 						<div class="form-group">
 							<div class="col-xs-6">
 								<input type="password" data-toggle="validator" data-minlength="6" class="form-control" id="inputPassword" placeholder="Пароль"  required>
 							</div>
 							<div class="col-xs-6">
 								<input type="password" class="form-control" id="inputPasswordConfirm" data-match="#inputPassword" data-match-error="Ошибка! Пароли не совпадают!" placeholder="Повторіть" required>
 							</div>
 							<div class="help-block with-errors"></div>
 						</div>
 				</div>
 				<!--<div class="form-group">
 					<div class="checkbox">
						<div class="help-block with-errors"></div>
 					</div>
 				</div>-->
 				<div class="form-group">
 					<button type="submit" class="btn btn-primary">Отправить</button>
 				</div>
 				</form>
			</div></div></div>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js" integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>
  </body>
</html>