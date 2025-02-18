@echo off
set "nom=test"
set "chemin=D:\logiciel\prog\web_dynamique\framework\sprint16"

set "deployement=D:\logiciel\prog\web_dynamique\test\sprint16"
set "lib=%chemin%\lib"

rem Crée le dossier "dossier" si nécessaire
mkdir "dossier"

rem Compiler les fichiers Java avec les bibliothèques du dossier lib
javac -d .\dossier -cp "%lib%\*" *.java

rem Aller dans le dossier "dossier"
cd "dossier"

rem Créer un fichier JAR
jar cf "../%nom%.jar" *

rem Revenir au dossier parent
cd ..

rem Déplacer le fichier JAR dans le dossier src
move "%nom%.jar" "%deployement%\lib"

rem Supprimer le dossier temporaire
rd /s /q "dossier"
