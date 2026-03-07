<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto+Slab:wght@100..900&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="css/MainStyle.css">
    <link rel="stylesheet" href="css/ProfileStyle.css">
</head>
<body>
    <header class="block__header">
        <div class="container">
            <nav class="block__nav">
                <div class="block__logo">
                    <h1 class="logo__title">Виборка</h1>
                </div>
                <div class="block__mainPage">
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <button class="mainPage"
                            type="submit" name="btm" value="mainPage">Головна</button>
                    </form>
                </div>
            </nav>
        </div>
    </header>

    <div class="profile__statistics">
        <div class="container">
            <div class="flex__container">
                <div class="block__profile">
                    <p class="nickname">${nickname}</p>
                </div>
                <div class="block__statistics">
                    <p class="positive">Зіграно: ${result}</p>
                </div>
            </div>
        </div>
    </div>

    <div class="block__content">
        <div class="container">
            <div class="grid__container">
                <c:forEach var="choice" items="${listOfStoryEndings}">
                    <div class="block__history"> 
                        <p class="result__text">${choice}</p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>