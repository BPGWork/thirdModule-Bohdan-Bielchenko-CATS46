<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reality Breakdowm</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto+Slab:wght@100..900&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="css/MainStyle.css">
    <link rel="stylesheet" href="css/HistoryStyle.css">
</head>
<body style="background: url('${bgHistory}') center/ 100% 100% no-repeat;">
    <div class="container">
        <h1>${nameHistory}</h1>
        <p>${info}</p>
        <form action="${pageContext.request.contextPath}/main" method="post">
            <button type="submit" name="start" value="startHistory">
                Почати
            </button>
        </form>   
    </div>
</body>
</html>