Nach dem starten sollten einige default Werte automatisch erstellt werden.

User:
	Test01:123
	[SYS_ADMIN]:admin

Das Admin hat die Möglichkeit die Rechte von anderen Users anzupsassen.
Die beiden Accounts selber verfügen nicht über die Möglichkeit das Spiel zu spiel,
dazu muss ein neues Nutzer registriert werden.

Dazu auf 
	-> localhost:8080/eternal/login.xhtml
	-> "Need an account?" anklicken
	-> Information ausfüllen
	-> registrieren

Zurzeit ist es in der Anwendung möglich Gebäude zu erreichten und Resources zu sammeln.
Ebenfalls kann man einige Account-Daten ändern. Über die 'Galaxie' kann man die Position von anderen Nutzern
sehen (Am besten welche Anlegen). Ab 5 Nutzern wird ein neuer Sector eröffnet und kann über die Navigation am unteren Rand
erkundet werden.
Der Trade Befehl brint einem zum Trade window mit der User-ID des ausgewählten Nutzers (Diese kann auch von Hand eingeben werder)
Der Handel selbst funktioniert leider noch nicht.

Passwörter sind überall im klartext zu sehen, da während der Entwicklung der Schwerpunkt auf anderen Dingen lagen
Im Produktiv-System würden siese durch h:inputSecret usw. ersetzt werden.

Über den UserRole Editor können einzelne Aktionen im Spiel eingeschränkt werden.
Die Editor kann am Anfang nur vom [SYS_ADMIN] benutzt werden, aber durch die Vergabe von Rechten
auch von normlen Usern benutzt werden.