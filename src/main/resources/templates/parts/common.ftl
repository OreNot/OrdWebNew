<#macro page>
<#include "security.ftl">
<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Менеджмент УЦ</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">


    <!-- Bootstrap CSS -->

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <!--
                    <link href="http://10.161.193.164:8080/UC/js/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
-->
        <link rel="shortcut icon" href="${urlprefixPath}/img/favicon.ico" type="image/x-icon">


</head>
<body>
    <#include "navbar.ftl">
<div class="container mt-5">
    <#nested>
</div>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->



<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>


<!--
<script src="http://10.161.193.164:8080/UC/js/jquery-3.3.1.slim.min.js" crossorigin="anonymous"></script>
<script src="http://10.161.193.164:8080/UC/js/popper.min.js" crossorigin="anonymous"></script>
<script src="http://10.161.193.164:8080/UC/js/bootstrap.min.js" crossorigin="anonymous"></script>
-->
</body>
</html>

</#macro>