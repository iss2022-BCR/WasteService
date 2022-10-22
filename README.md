<div align="center">

[![MIT License][license-shield]][license-url]
[![Downloads][downloads-shield]][downloads-url]
[![Stars][stars-shield]][stars-url]\
[![Java][java-shield]][java-url]
[![IntelliJ IDEA][intellij-shield]][intellij-url]

<br/>
</div>

<div align="center">
  <img align="center" width="25%" src="commons/resources/imgs/walle.jpg"/>
  
  <h1>WasteService</h1>
  
  Sistema software distribuito per un ***servizio di smaltimento rifiuti differenziato***. Progetto per il corso di [Ingegneria dei Sistemi Software M](https://www.unibo.it/it/didattica/insegnamenti/insegnamento/2021/468003), dell'Università di Bologna.
  
  [Clicca qui](https://htmlpreview.github.io/?https://raw.githubusercontent.com/iss2022-BCR/WasteService/main/Sprint0/index.html)
per visualizzare i documenti di progetto.
  
</div>



### Descrizione
- c'è un pot di ingresso dove arrivano dei camion (indoor)
- il trolley (robot) sta normalmente alla home, quando arriva un camion manda un messaggio dicendo il numero di kg di spazzatura da scaricare
- controlla se nei due contenitori VETRO e PLASTICA c'è ancora spazio:
  - se non ci sta manda via il camion, a un altro servizio (non ci guarda);
  - altrimenti gestisce la richiesta e inizia a scarica.
- quando ha finito di scaricare:
  - se è arrivato un altro camion inizia subito a scaricarlo;
  - altrimenti torna alla home.

Ci serve raspberry con LED come sistema di allarme/monitoring:
- LED spento: quando il trolley è in HOME;
- LED acceso lampeggiante: quando il sistema si muove;
- LED acceso fisso: quando il sistema è stopped.

Il robot è stopped quando si mette una mano davanti al radar.

Meccanismo di STOP/RESUME: c'è un manager che supervisiona tutto con una GUI. La GUI mostra lo stato corrente del transport trolley e possibilmente la sua posizione nella stanza. NB: potrebbe anche essere vaga, tipo il robot si sta muovendo oppure no, è in HOME, ecc.

### Linee Guida
Fornire una comunicazione nel momento in cui iniziamo a lavorare al problema, spiegando che iniziamo da analisi requisiti e analisi del problema.
Integriamo col prof quando siamo arrivati alla fine di questa fase, perché andiamo avanti a step.
Sfruttare il ricevimento del giovedì per farlo (ore 16-18), gli mandiamo una mail, oppure prenotarsi per ricevimento ad hoc.

### Roadmap
- [ ] Ripassare gli HTML sulla teoria (TemaFinale2022, Conclusioni, Costruire Software, QActor, ...)


[java-shield]: https://img.shields.io/badge/Java-ED8B00?logo=java&logoColor=white
[java-url]: https://www.java.com
[intellij-shield]: https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?logo=intellij-idea&logoColor=blue
[intellij-url]: https://www.jetbrains.com/idea/ 
[downloads-shield]: https://img.shields.io/github/downloads/iss2022-BCR/WasteService/total
[downloads-url]: https://github.com/iss2022-BCR/WasteService/releases/latest
[license-shield]: https://img.shields.io/github/license/iss2022-BCR/WasteService
[license-url]: https://github.com/iss2022-BCR/WasteService/blob/main/LICENSE
[stars-shield]: https://custom-icon-badges.herokuapp.com/github/stars/iss2022-BCR/WasteService?logo=star&logoColor=yellow
[stars-url]: https://github.com/iss2022-BCR/WasteService/stargazers
