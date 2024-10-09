
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h2>Somme </h2>
    <form action="login" method="post">
        <input name="nom" type="text" placeholder="Nom">
        <input name="adress" type="text" placeholder="Adress">
        <input name="mdp" type="mdp"  placeholder="password">
        <input type="submit" value="valider">
    </form>
    <h4><%=(request.getAttribute("login") != null) ? request.getAttribute("login"):"" %></h4>
</body>
</html>