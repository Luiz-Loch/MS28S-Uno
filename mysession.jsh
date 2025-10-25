PS C:\Users\joaoguilherm.pereira\Documents\Code\MS28S-Uno> $files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }
>> javac -d out $files
>> 
PS C:\Users\joaoguilherm.pereira\Documents\Code\MS28S-Uno> Get-ChildItem -Recurse -Path out -Filter *.class | Select-Object -First 10
>> 


    Diretório: C:\Users\joaoguilherm.pereira\Documents\Code\MS28S-Uno\out


Mode                 LastWriteTime         Length Name
----                 -------------         ------ ----
-a----        24/10/2025     20:13            412 Main$1.class
-a----        24/10/2025     20:13            513 Main$2.class
-a----        24/10/2025     20:13            909 Main.class


    Diretório: C:\Users\joaoguilherm.pereira\Documents\Code\MS28S-Uno\out\CardModel


Mode                 LastWriteTime         Length Name
----                 -------------         ------ ----
-a----        24/10/2025     20:13            352 ActionCard.class
-a----        24/10/2025     20:13           2350 CardDeck.class
-a----        24/10/2025     20:13            728 CardSpec.class
-a----        24/10/2025     20:13            288 NumberCard.class
-a----        24/10/2025     20:13            795 WildCard.class


----                 -------------         ------ ----
-a----        24/10/2025     20:13           1639 Dealer.class
-a----        24/10/2025     20:13            933 Game$1$1.class


PS C:\Users\joaoguilherm.pereira\Documents\Code\MS28S-Uno> Select-String -Path src\**\*.java -Pattern "public static void main" -SimpleMatch
>>
PS C:\Users\joaoguilherm.pereira\Documents\Code\MS28S-Uno>



                                                           jshell --class-path out
>>
|  Welcome to JShell -- Version 11.0.13
|  For an introduction type: /help intro

jshell>

jshell> /history

/history

jshell>

jshell> /save mysession.jsh