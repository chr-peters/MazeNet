# Konfiguration des Mazenet-Servers

# Startwert fuer die Spieleranzahl, kann aber noch veraendert werden
NUMBER_OF_PLAYERS = 1
LOCALE = de

# Die Zeit in Millisekunden, nach der ein Logintimeout eintritt; LOGINTIMEOUT = 60000 entspricht einer Minute
LOGINTIMEOUT = 120000
LOGINTRIES = 3
SENDTIMEOUT = 30000
# Die maximale Anzahl der Versuche, einen gueltigen Zug zu uebermitteln
MOVETRIES = 3

PORT = 5123

# SSL Settings
SSL_PORT = 5124
SSL_CERT_STORE=./our_keystore.jks
SSL_CERT_STORE_PASSWD=transformers

# Wenn TESTBOARD = true ist, dann ist das Spielbrett bei jedem Start identisch (zum Debugging)
TESTBOARD = false
# Hiermit lassen sich die Testfaelle anpassen (Pseudozufallszahlen)
TESTBOARD_SEED = 0

# Die Zeit in Millisekunden, welche die Animation eines Zug (die Bewegung des Pins) benoetigen soll
MOVEDELAY = 400
# Die Zeit in Millisekunden, die das Einschieben der Shiftcard dauern soll
# SHIFTDELAY = 500
SHIFTDELAY = 500
# USERINTERFACE definiert die zu verwendende GUI Gueltige Werte: BetterUI, MazeFX, CLIUI
USERINTERFACE = MazeFX

# Maximale Laenge des Spielernamens
MAX_NAME_LENGTH = 30
