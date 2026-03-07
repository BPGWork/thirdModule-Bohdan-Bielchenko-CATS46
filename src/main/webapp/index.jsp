<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Selection</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto+Slab:wght@100..900&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="css/MainStyle.css">
    <link rel="stylesheet" href="css/IndexStyle.css">
</head>
<body>

    <header class="block__header">
        <div class="container">
            <nav class="block__nav">
                <div class="block__logo">
                    <h1 class="logo__title">Виборка</h1>
                </div>
                <div class="block__profile">
                    <form action="${pageContext.request.contextPath}/index" method="post">
                        <button class="profile"
                            type="submit" name="profile" value="profile"></button>
                    </form>
                </div>
            </nav>
        </div>
    </header>
    <div class="block__content">
        <div class="container">
            <div class="grid__container">
                <c:forEach var="history" items="${historyList}">
                    <div class="block__history"> 
                        <form action="${pageContext.request.contextPath}/index" method="post">
                            <button style="background: url('image/historyBg/${history.name}.png') center/ 100% 100% no-repeat;" class="history" 
                                type="submit" name="choiceHistory" value="${history.name}">${history.name}</button>
                        </form>
                    </div>       
                </c:forEach>
            </div>
        </div>    
    </div>

    <div id="authModal" class="block__login" style="display: none;">
        <form action="${pageContext.request.contextPath}/login" method="POST" class="login__form">
            <h2>Авторизація</h2>
            <input type="text" name="login" placeholder="Логін" required /><br><br>
            <input type="password" name="password" placeholder="Пароль" required /><br><br>
            <button type="submit" name="btm__authentication" value="btm__login">Увійти</button>
            <button type="button" onclick="goToRegistration()">Реєстрація</button>
        </form>
    </div>

    <div id="registerModal" class="block__login" style="display:none;">

        <form action="${pageContext.request.contextPath}/registration"
            method="post"
            class="login__form">

            <h2>Реєстрація</h2> <br>
            <input type="text" name="login" placeholder="Логін" required> <br><br>
            <input type="password" name="password" placeholder="Пароль" required> <br><br>
            <input type="password" name="confirmPassword" placeholder="Повторіть пароль" required><br><br>
            <button type="submit">
                Зареєструватися
            </button>

            <button type="button" onclick="backToLogin()">
                Назад
            </button>

        </form>
    </div>

    <c:if test="${needAuth}">
        <script>
            window.addEventListener("DOMContentLoaded", function () {
                document.getElementById("authModal").style.display = "block";
            });
        </script>
    </c:if>

    <script>
        function goToRegistration() {
            document.getElementById("authModal").style.display = "none";
            document.getElementById("registerModal").style.display = "block";
        }
        function backToLogin() {
            document.getElementById("registerModal").style.display = "none";
            document.getElementById("authModal").style.display = "block";
        }
    </script>
</body>
</html>