# WasteService
Sistema software distribuito per lo servizio di smaltimento rifiuti differenziato. Progetto per Ingegneria dei Sistemi Software M @Unibo

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
