<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Result</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto+Slab:wght@100..900&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="css/MainStyle.css">
    <link rel="stylesheet" href="css/ResultStyle.css">
</head>
<body style="background: url('${bgHistory}') center/ 100% 100% no-repeat;">
    <div class="container">
        <div class="block__title">
            <h3 class="title">${resultText}</h3>
        </div>
        <form class="layer__choice" action="${pageContext.request.contextPath}/result" method="POST">
            <button class="return__home__page" type="submit" name="goHome" value="goHomePage">
                Головна
            </button>
        </form>
    </div>
</body>
</html>