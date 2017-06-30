# Konfigurieren des Mazenet-Servers

Falls benötigt, kann man beim Aufruf des Mazenet-Servers mit -c eine Configdatei angeben, um die Standardconfig zu überschreiben.

```
usage: java -jar maze-server.jar [options]
Available Options:
 -c <arg>   path to property file for configuration
 -h         displays this help message
```

Eine Beispielconfigdatei ist folgende:
```
# Startwert fuer die Spieleranzahl kann aber noch veraendert werden
NUMBER_OF_PLAYERS = 1
LOCALE = de

# Die Zeit in Milisekunden, nach der ein Logintimeout eintritt LOGINTIMEOUT = 60000 entspricht einer Minute
LOGINTIMEOUT = 120000
LOGINTRIES = 3
SENDTIMEOUT = 30000
# Die maximale Anzahl der Versuche einen gueltigen Zug zu uebermitteln
MOVETRIES = 3

PORT = 5123

# SSL Settings
SSL_PORT = 5124
SSL_CERT_STORE=./keystore.jks
SSL_CERT_STORE_PASSWD=transformers

# Wenn TESTBOARD = true ist, dann ist das Spielbrett bei jedem Start identisch (zum Debugging)
TESTBOARD = true
# Hiermit lassen sich die Testfaelle anpassen (Pseudozufallszahlen)
TESTBOARD_SEED = 0

# Die Zeit in Milisekunden, die die Animation eines Zug (die Bewegung des Pins) benoetigen soll
MOVEDELAY = 400
# Die Zeit in Milisekunden, die das Einschieben der Shiftcard dauern soll
# SHIFTDELAY = 500
SHIFTDELAY = 700
# USERINTERFACE definiert die zu verwendende GUI Gueltige Werte: BetterUI, MazeFX, CLIUI
USERINTERFACE = BetterUI

# Maximale Laenge des Spielernamens
MAX_NAME_LENGTH = 30
```
Hier wird StandardUI auf die ältere UI BetterUI gewechselt.
