# Unisa2022-23.APS

Durante la pandemia, il proprietario di una Sala Bingo, Mister Joker, ha deciso 
di creare delle stanze virtuali online in cui le persone possono partecipare a 
vari giochi di fortuna, ossia giochi dove sulla base di valori casuali c'è un 
vincitore. Per realizzare questa idea, contatta un gruppo di studenti del corso 
di laurea magistrale in ingegneria informatica dell'università di Salerno per 
creare una funzionalità che generi in modo casuale stringhe continue. 
L'obiettivo è creare un sistema trasparente che eviti imbrogli. 
Il governo ha imposto il divieto di partecipare ad eventi sociali online a chi non 
possiede il Green Pass. Tuttavia, il garante per la protezione dei dati personali 
ha vietato l’invio del Green Pass attraverso canali telematici. Il governo ha 
quindi pubblicato una call aperta a tutti per proposte di formato del Green 
Pass 2.0 che preveda l'unico uso delle informazioni strettamente necessarie 
per accedere a un servizio. Mister Joker ha chiesto agli studenti di creare una 
funzionalità per l'accesso alle sale virtuali della sua Sala Bingo, anche se non 
sa ancora quali informazioni saranno necessarie nel Green Pass 2.0. Tuttavia, 
questo non è un problema, perché gli studenti intendo progettare un sistema 
dinamico che permette al proprietario del Green Pass 2.0 di usarlo in sicurezza 
al variare delle politiche nel tempo circa quali dati bisogna possedere per 
accedere al servizio. Il Green Pass 2.0 progettato dagli studenti viene emesso 
dal Ministero della Salute e permette agli utenti di identificarsi con la sala 
Bingo per accedere al proprio profilo e, successivamente, alla funzionalità di 
generazione continua di stringhe casuali.
Ci sono oppositori dell'innovazione chiamati tecnocrati che puntano sui rischi 
delle nuove tecnologie e sono considerati allarmisti e complottisti dai 
sostenitori dell'innovazione. Per evitare la strumentalizzazione, è necessario 
che il sistema sia trasparente e aperto alla verifica da parte di tutti. Mister 
Joker ha sottolineato l'importanza di questa trasparenza, citando una canzone 
di Franco Califano che diceva: "Non mi fido di nessuno".


# Esecuzione dell'applicazione
Per l'esecuzione dell'applicazione è sufficiente scaricare e unzippare la cartella ed eseguire sul terminale la seguente istruzione:

`java -jar joker.jar`

A questo punto verrà chiesto di inserire il path al GP2.0 per poter accedere. Il progetto fornisce tre certificati differenti:

1. "Certs/andy_keystore.jks": Certificato valido dalla durata di 180 giorni (Valido da Sabato 19/08/2023 18:04:33 fino a Giovedì 15/02/2024 17:04:33). Attraverso questo certificato è possibile simulare l'esperienza di gioco.

1. "Certs/mario_keystore.jks": Certificato scaduto dalla durata di 2 giorni (Valido da Lunedì 20/08/2023 14:00:00 fino a Mercoledì 22/08/2023 14:00:00). In questo caso, non viene permesso l'accesso al gioco.

1. "Certs/infante_keystore.jks": Certificato valido dalla durata di 180 giorni  (Valido da Mercoledi 23/08/2023 23:37:40 fino a Lunedì 19/02/2024 22:37:40). In questo caso, non viene permesso l'accesso al gioco, essendo il giocatore minorenne (Data di Nascita: ).

# Script Comandi Openssl e Keytool

# Generazione parametri
`openssl ecparam -name prime256v1 -out prime256v1.pem`

# Generazione Chiavi Private
`openssl genpkey -paramfile prime256v1.pem -out ms_key.pem`
`openssl genpkey -paramfile prime256v1.pem -out andy_key.pem`
`openssl genpkey -paramfile prime256v1.pem -out me_key.pem`
`openssl genpkey -paramfile prime256v1.pem -out joker_key.pem`
`openssl genpkey -paramfile prime256v1.pem -out adm_key.pem`

<!-- Altri Player -->
`openssl genpkey -paramfile prime256v1.pem -out mario_key.pem`
`openssl genpkey -paramfile prime256v1.pem -out infante_key.pem`

# Generazione certificati self-signed
`openssl req -new -x509 -days 365 -key ms_key.pem -out ms_cert.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"`
`openssl req -new -x509 -days 365 -key me_key.pem -out me_cert.pem -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"`

# Generazioni Cartelle
`mkdir MS MS/private MS/newcerts`
`touch MS/index.txt MS/serial`
`echo "00" >> MS/serial`
`cp ms_cert.pem MS`
`cp ms_key.pem MS/private`

`mkdir ME ME/private ME/newcerts`
`touch ME/index.txt ME/serial`
`echo "00" >> ME/serial`
`cp me_key.pem ME/private`
`cp me_cert.pem ME`

# Generazione Richiesta GP2.0
`openssl req -new -key andy_key.pem -out andy_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"`
`openssl ca -in andy_request.pem -out andy_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"`

<!-- Altri Player -->
`openssl req -new -key mario_key.pem -out mario_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"`
`openssl ca -in mario_request.pem -out mario_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf" -startdate 230820120000Z -enddate 230822120000Z`

`openssl req -new -key infante_key.pem -out infante_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"`
`openssl ca -in infante_request.pem -out infante_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"`

# Generazione Richiesta Certificato Mr.Joker's Casino e Nodo ADM
`openssl req -new -key joker_key.pem -out joker_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"`
`openssl ca -in joker_request.pem -out joker_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"`

`openssl req -new -key adm_key.pem -out adm_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"`
`openssl ca -in adm_request.pem -out adm_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"`


# Generazione dei Keystore
`openssl pkcs12 -export -out ms.p12 -inkey ms_key.pem -in ms_cert.pem -name ms -passout pass:aps2023`
`keytool -importkeystore -destkeystore ms_keystore.jks -srckeystore ms.p12 -srcstoretype PKCS12 -alias ms -srcstorepass aps2023 -deststorepass aps2023`

`openssl pkcs12 -export -out andy.p12 -inkey andy_key.pem -in andy_cert.pem -name andy -passout pass:aps2023`
`keytool -importkeystore -destkeystore andy_keystore.jks -srckeystore andy.p12 -srcstoretype PKCS12 -alias andy -srcstorepass aps2023 -deststorepass aps2023`

`openssl pkcs12 -export -out me.p12 -inkey me_key.pem -in me_cert.pem -name me -passout pass:aps2023`
`keytool -importkeystore -destkeystore me_keystore.jks -srckeystore me.p12 -srcstoretype PKCS12 -alias me -srcstorepass aps2023 -deststorepass aps2023`

`openssl pkcs12 -export -out joker.p12 -inkey joker_key.pem -in joker_cert.pem -name joker -passout pass:aps2023`
`keytool -importkeystore -destkeystore joker_keystore.jks -srckeystore joker.p12 -srcstoretype PKCS12 -alias joker -srcstorepass aps2023 -deststorepass aps2023`

`openssl pkcs12 -export -out adm.p12 -inkey adm_key.pem -in adm_cert.pem -name adm -passout pass:aps2023`
`keytool -importkeystore -destkeystore adm_keystore.jks -srckeystore adm.p12 -srcstoretype PKCS12 -alias adm -srcstorepass aps2023 -deststorepass aps2023`

<!-- Altri Player -->
`openssl pkcs12 -export -out mario.p12 -inkey mario_key.pem -in mario_cert.pem -name mario -passout pass:aps2023`
`keytool -importkeystore -destkeystore mario_keystore.jks -srckeystore mario.p12 -srcstoretype PKCS12 -alias mario -srcstorepass aps2023 -deststorepass aps2023`

`openssl pkcs12 -export -out infante.p12 -inkey infante_key.pem -in infante_cert.pem -name infante -passout pass:aps2023`
`keytool -importkeystore -destkeystore infante_keystore.jks -srckeystore infante.p12 -srcstoretype PKCS12 -alias infante -srcstorepass aps2023 -deststorepass aps2023`

# Generazione del Truststore
`openssl x509 -in ms_cert.pem -out ms_cert.der -outform der`
`openssl x509 -in me_cert.pem -out me_cert.der -outform der`

`keytool -genkeypair -keyalg RSA -alias dummy -keystore truststore.jks -storepass aps2023 -dname "CN=dummy, OU=dummy, O=dummy, L=dummy, S=dummy, C=dummy" -validity 1`
`keytool -delete -alias dummy -keystore truststore.jks -storepass aps2023`

`keytool -importcert -file ms_cert.pem -alias ms -keystore truststore.jks -storepass aps2023`
`keytool -importcert -file me_cert.pem -alias me -keystore truststore.jks -storepass aps2023`

<!-- Stampa del truststore.jks -->
`keytool -list -keystore truststore.jks -storepass aps2023`