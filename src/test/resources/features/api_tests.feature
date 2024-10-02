# language: de
Funktionalität: API Tests für JSONPlaceholder

  Grundlage:
    Angenommen die API ist verfügbar

  Szenario: Erfolgreicher GET-Request für einen Beitrag
    Wenn ich einen GET-Request an "/posts/1" sende
    Dann der Statuscode sollte 200 sein
    Und die Antwort sollte den Titel "sunt aut facere repellat provident occaecati excepturi optio reprehenderit" enthalten

  Szenario: Erfolgreicher POST-Request für einen neuen Beitrag
    Wenn ich einen POST-Request an "/posts" mit dem Titel "Testbeitrag" und Body "Dies ist ein Testbeitrag" sende
    Dann der Statuscode sollte 201 sein
    Und die Antwort sollte eine ID enthalten
    Und die Antwort sollte den Titel "Testbeitrag" enthalten

  Szenario: GET-Request für nicht vorhandenen Beitrag
    Wenn ich einen GET-Request an "/posts/999" sende
    Dann der Statuscode sollte 404 sein
    Und die Antwort sollte leer sein

  Szenario: GET-Request für alle Beiträge
    Wenn ich einen GET-Request an "/posts" sende
    Dann der Statuscode sollte 200 sein

  Szenario: POST-Request mit ungültigen Daten
    Wenn ich einen POST-Request an "/posts" mit dem Titel "" und Body "" sende
    Dann der Statuscode sollte 201 sein
    Und die Antwort sollte eine ID enthalten
