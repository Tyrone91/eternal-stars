Nach dem starten sollten einige default Werte automatisch erstellt werden.

User:
	Test01:123
	[SYS_ADMIN]:admin

Das Admin hat die M�glichkeit die Rechte von anderen Users anzupsassen.
Die beiden Accounts selber verf�gen nicht �ber die M�glichkeit das Spiel zu spiel,
dazu muss ein neues Nutzer registriert werden.

Dazu auf 
	-> localhost:8080/eternal/login.xhtml
	-> "Need an account?" anklicken
	-> Information ausf�llen
	-> registrieren

Zurzeit ist es in der Anwendung m�glich Geb�ude zu erreichten und Resources zu sammeln.
Ebenfalls kann man einige Account-Daten �ndern. �ber die 'Galaxie' kann man die Position von anderen Nutzern
sehen (Am besten welche Anlegen). Ab 5 Nutzern wird ein neuer Sector er�ffnet und kann �ber die Navigation am unteren Rand
erkundet werden.
Der Trade Befehl brint einem zum Trade window mit der User-ID des ausgew�hlten Nutzers (Diese kann auch von Hand eingeben werder)
Der Handel selbst funktioniert leider noch nicht.

Passw�rter sind �berall im klartext zu sehen, da w�hrend der Entwicklung der Schwerpunkt auf anderen Dingen lagen
Im Produktiv-System w�rden siese durch h:inputSecret usw. ersetzt werden.

�ber den UserRole Editor k�nnen einzelne Aktionen im Spiel eingeschr�nkt werden.
Die Editor kann am Anfang nur vom [SYS_ADMIN] benutzt werden, aber durch die Vergabe von Rechten
auch von normlen Usern benutzt werden.