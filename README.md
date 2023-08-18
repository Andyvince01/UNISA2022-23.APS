# Unisa2022-23.APS



andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl ecparam -name prime256v1 -out prime256v1.pem

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl genpkey -paramfile prime256v1.pem -out ms_key.pem

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl genpkey -paramfile prime256v1.pem -out andy_key.pem

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl req -new -x509 -days 365 -key ms_key.pem -out ms_cert.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:IT
State or Province Name (full name) [Some-State]:Italia
Locality Name (eg, city) []:Roma
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Ministero della Salute
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:localhost
Email Address []:
Nome del richiedente [Mario]:.
Cognome del richiedente [Rossi]:.
Sesso del richiedente (M o F) [M]:.
Data di Nascita del richiedente nel formato dd-mm-yyyy [01-01-1980]:.
Luogo di Nascita del richiedente [Roma]:.
Provincia di Nascita del richiedente [RM]:.
Codice Fiscale del richiedente [RSSMRA80A01H501U]:.
Tipologia di tampone effettuato dal richiedente (Molecolare, Rapido, NULL) [NULL]:.
Tipologia di vaccino effettuato dal richiedente (BioNTech-Pfizer, Moderna, ..., NULL) [BioNTech-Pfizer]:.
Ente che rilascia il certificato [Ministero della Salute]:.

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# mkdir MS MS/private MS/newcerts

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# touch MS/index.txt MS/serial

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# echo "00" >> MS/serial

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# cp ms_cert.pem MS

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# cp ms_key.pem MS/private

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl req -new -key andy_key.pem -out andy_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:IT
State or Province Name (full name) [Some-State]:Italia
Locality Name (eg, city) []:
Organization Name (eg, company) [Internet Widgits Pty Ltd]:.
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:localhost
Email Address []:
Nome del richiedente [Mario]:Andrea Vincenzo
Cognome del richiedente [Rossi]:Ricciardi
Sesso del richiedente (M o F) [M]:
Data di Nascita del richiedente nel formato dd-mm-yyyy [01-01-1980]:20-03-2001
Luogo di Nascita del richiedente [Roma]:Salerno
Provincia di Nascita del richiedente [RM]:SA
Codice Fiscale del richiedente [RSSMRA80A01H501U]:RCCNRV01C20H703U
Tipologia di tampone effettuato dal richiedente (Molecolare, Rapido, NULL) [NULL]:
Tipologia di vaccino effettuato dal richiedente (BioNTech-Pfizer, Moderna, ..., NULL) [BioNTech-Pfizer]:
Ente che rilascia il certificato [Ministero della Salute]:

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl ca -in andy_request.pem -out andy_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"
Using configuration from C:\msys64\mingw64\etc\ssl\opensslGP.cnf
Check that the request matches the signature
Signature ok
Certificate Details:
        Serial Number: 0 (0x0)
        Validity
            Not Before: Aug 18 15:19:47 2023 GMT
            Not After : Feb 14 15:19:47 2024 GMT
        Subject:
            countryName               = IT
            stateOrProvinceName       = Italia
            commonName                = localhost
            nome                      = Andrea Vincenzo
            cognome                   = Ricciardi
            sesso                     = M
            data_di_nascita           = 20-03-2001
            luogo_di_nascita          = Salerno
            provincia_di_nascita      = SA
            codice_fiscale            = RCCNRV01C20H703U
            tampone                   = NULL
            vaccino                   = BioNTech-Pfizer
            organizzazione            = Ministero della Salute
        X509v3 extensions:
            X509v3 Basic Constraints:
                CA:TRUE
            X509v3 Subject Key Identifier:
                EC:76:AA:41:08:93:C8:6D:96:9A:B7:A6:9F:40:72:57:33:91:AE:9A
            X509v3 Authority Key Identifier:
                6A:32:1F:64:67:F9:0B:E1:9E:72:DB:2C:62:4F:29:FC:C0:07:AF:69
Certificate is to be certified until Feb 14 15:19:47 2024 GMT (180 days)
Sign the certificate? [y/n]:y


1 out of 1 certificate requests certified, commit? [y/n]y
Write out database with 1 new entries
Data Base Updated

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl pkcs12 -export -out ms.p12 -inkey ms_key.pem -in ms_cert.pem -name ms
Enter Export Password:
Verifying - Enter Export Password:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# keytool -importkeystore -destkeystore ms_keystore.jks -srckeystore ms.p12 -srcstoretype PKCS12 -alias ms
Importing keystore ms.p12 to ms_keystore.jks...
Enter destination keystore password:
Re-enter new password:
Enter source keystore password:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
#  keytool -import -alias ms -file andy_cert.pem -keystore ms_truststore.jks
Enter keystore password:
Re-enter new password:
Owner: OID.1.2.3.3=Ministero della Salute, OID.1.2.3.2.2=BioNTech-Pfizer, OID.1.2.3.2.1=NULL, OID.1.2.3.1.7=RCCNRV01C20H703U, OID.1.2.3.1.6=SA, OID.1.2.3.1.5=Salerno, OID.1.2.3.1.4=20-03-2001, OID.1.2.3.1.3=M, OID.1.2.3.1.2=Ricciardi, OID.1.2.3.1.1=Andrea Vincenzo, CN=localhost, ST=Italia, C=IT
Issuer: CN=localhost, O=Ministero della Salute, L=Roma, ST=Italia, C=IT
Serial number: 0
Valid from: Fri Aug 18 17:19:47 CEST 2023 until: Wed Feb 14 16:19:47 CET 2024
Certificate fingerprints:
         SHA1: 34:FC:19:D5:43:F7:56:51:F2:74:9A:F8:94:CE:C5:9D:8F:20:CC:2A
         SHA256: 5D:2A:89:89:CB:B9:CC:07:DF:00:98:37:CF:E6:BE:04:6A:C0:F0:32:9F:80:9B:EA:33:78:E0:40:97:C5:BF:B3
Signature algorithm name: SHA256withECDSA
Subject Public Key Algorithm: 256-bit EC (secp256r1) key
Version: 3

Extensions:

#1: ObjectId: 2.5.29.35 Criticality=false
AuthorityKeyIdentifier [
KeyIdentifier [
0000: 6A 32 1F 64 67 F9 0B E1   9E 72 DB 2C 62 4F 29 FC  j2.dg....r.,bO).
0010: C0 07 AF 69                                        ...i
]
]

#2: ObjectId: 2.5.29.19 Criticality=false
BasicConstraints:[
  CA:true
  PathLen: no limit
]

#3: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: EC 76 AA 41 08 93 C8 6D   96 9A B7 A6 9F 40 72 57  .v.A...m.....@rW
0010: 33 91 AE 9A                                        3...
]
]

Trust this certificate? [no]:  yes
Certificate was added to keystore

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl pkcs12 -export -out andy.p12 -inkey andy_key.pem -in andy_cert.pem -name andy
Enter Export Password:
Verifying - Enter Export Password:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# keytool -importkeystore -destkeystore andy_keystore.jks -srckeystore andy.p12 -srcstoretype PKCS12 -alias andy
Importing keystore andy.p12 to andy_keystore.jks...
Enter destination keystore password:
Re-enter new password:
Enter source keystore password:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
#  keytool -import -alias andy -file ms_cert.pem -keystore andy_truststore.jks
Enter keystore password:
Re-enter new password:
Owner: CN=localhost, O=Ministero della Salute, L=Roma, ST=Italia, C=IT
Issuer: CN=localhost, O=Ministero della Salute, L=Roma, ST=Italia, C=IT
Serial number: 329d3ff97ad391e245ab6809c0e7cec1339bada3
Valid from: Fri Aug 18 17:17:15 CEST 2023 until: Sat Aug 17 17:17:15 CEST 2024
Certificate fingerprints:
         SHA1: EF:D4:D5:4F:D1:4B:66:22:E8:09:80:2E:6E:CB:C5:CB:F7:54:85:13
         SHA256: E9:D4:9B:B2:2C:6D:69:A8:2D:48:B2:9C:FD:E5:0E:74:BD:49:8A:66:3D:F2:6B:90:A4:FC:FE:32:5D:A3:09:45
Signature algorithm name: SHA256withECDSA
Subject Public Key Algorithm: 256-bit EC (secp256r1) key
Version: 3

Extensions:

#1: ObjectId: 2.5.29.35 Criticality=false
AuthorityKeyIdentifier [
KeyIdentifier [
0000: 6A 32 1F 64 67 F9 0B E1   9E 72 DB 2C 62 4F 29 FC  j2.dg....r.,bO).
0010: C0 07 AF 69                                        ...i
]
]

#2: ObjectId: 2.5.29.19 Criticality=true
BasicConstraints:[
  CA:true
  PathLen: no limit
]

#3: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 6A 32 1F 64 67 F9 0B E1   9E 72 DB 2C 62 4F 29 FC  j2.dg....r.,bO).
0010: C0 07 AF 69                                        ...i
]
]

Trust this certificate? [no]:  yes
Certificate was added to keystore

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl genpkey -paramfile prime256v1.pem -out me_key.pem

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl req -new -x509 -days 365 -key me_key.pem -out me_cert.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:IT
State or Province Name (full name) [Some-State]:Italia
Locality Name (eg, city) []:Roma
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Ministero dell'Economia e delle Finanze
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:localhost
Email Address []:
Nome del richiedente [Mario]:.
Cognome del richiedente [Rossi]:.
Sesso del richiedente (M o F) [M]:.
Data di Nascita del richiedente nel formato dd-mm-yyyy [01-01-1980]:.
Luogo di Nascita del richiedente [Roma]:.
Provincia di Nascita del richiedente [RM]:.
Codice Fiscale del richiedente [RSSMRA80A01H501U]:.
Tipologia di tampone effettuato dal richiedente (Molecolare, Rapido, NULL) [NULL]:.
Tipologia di vaccino effettuato dal richiedente (BioNTech-Pfizer, Moderna, ..., NULL) [BioNTech-Pfizer]:.
Ente che rilascia il certificato [Ministero della Salute]:.

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# mkdir ME ME/private ME/newcerts

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# touch ME/index.txt ME/serial

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# echo "00" >> ME/serial

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# cp me_key.pem ME/private

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# cp me_cert.pem ME

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl genpkey -paramfile prime256v1.pem -out joker_key.pem

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl req -new -key joker_key.pem -out joker_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:IT
State or Province Name (full name) [Some-State]:Italia
Locality Name (eg, city) []:
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Mr. Joker's Casino
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:localhost
Email Address []:
Nome del richiedente [Mario]:.
Cognome del richiedente [Rossi]:.
Sesso del richiedente (M o F) [M]:.
Data di Nascita del richiedente nel formato dd-mm-yyyy [01-01-1980]:.
Luogo di Nascita del richiedente [Roma]:.
Provincia di Nascita del richiedente [RM]:.
Codice Fiscale del richiedente [RSSMRA80A01H501U]:.
Tipologia di tampone effettuato dal richiedente (Molecolare, Rapido, NULL) [NULL]:.
Tipologia di vaccino effettuato dal richiedente (BioNTech-Pfizer, Moderna, ..., NULL) [BioNTech-Pfizer]:.
Ente che rilascia il certificato [Ministero della Salute]:.

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl ca -in joker_request.pem -out joker_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"
Using configuration from C:\msys64\mingw64\etc\ssl\opensslGP.cnf
Check that the request matches the signature
Signature ok
The nome field needed to be supplied and was missing

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl ca -in joker_request.pem -out joker_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"
Using configuration from C:\msys64\mingw64\etc\ssl\opensslGP.cnf
Check that the request matches the signature
Signature ok
Certificate Details:
        Serial Number: 1 (0x1)
        Validity
            Not Before: Aug 18 17:48:16 2023 GMT
            Not After : Feb 14 17:48:16 2024 GMT
        Subject:
            countryName               = IT
            stateOrProvinceName       = Italia
            organizationName          = Mr. Joker's Casino
            commonName                = localhost
        X509v3 extensions:
            X509v3 Basic Constraints:
                CA:TRUE
            X509v3 Subject Key Identifier:
                17:2D:FF:23:B0:AE:38:1B:D6:7E:4B:6E:AE:17:9B:14:11:3D:C0:E5
            X509v3 Authority Key Identifier:
                6A:32:1F:64:67:F9:0B:E1:9E:72:DB:2C:62:4F:29:FC:C0:07:AF:69
Certificate is to be certified until Feb 14 17:48:16 2024 GMT (180 days)
Sign the certificate? [y/n]:y


1 out of 1 certificate requests certified, commit? [y/n]y
Write out database with 1 new entries
Data Base Updated

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl pkcs12 -export -out me.p12 -inkey me_key.pem -in me_cert.pem -name me
Enter Export Password:
Verifying - Enter Export Password:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# keytool -importkeystore -destkeystore me_keystore.jks -srckeystore me.p12 -srcstoretype PKCS12 -alias me
Importing keystore me.p12 to me_keystore.jks...
Enter destination keystore password:
Re-enter new password:
Enter source keystore password:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl pkcs12 -export -out joker.p12 -inkey joker_key.pem -in joker_cert.pem -name joker
Enter Export Password:
Verifying - Enter Export Password:
Verify failure
Can't read Password
0C220000:error:1400006B:UI routines:UI_process:processing error:../openssl-3.1.0/crypto/ui/ui_lib.c:544:while reading strings

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl pkcs12 -export -out joker.p12 -inkey joker_key.pem -in joker_cert.pem -name joker
Enter Export Password:
Verifying - Enter Export Password:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
#  keytool -import -alias ms -file ms_cert.pem -keystore truststore.jks
Enter keystore password:
Re-enter new password:
Owner: CN=localhost, O=Ministero della Salute, L=Roma, ST=Italia, C=IT
Issuer: CN=localhost, O=Ministero della Salute, L=Roma, ST=Italia, C=IT
Serial number: 329d3ff97ad391e245ab6809c0e7cec1339bada3
Valid from: Fri Aug 18 17:17:15 CEST 2023 until: Sat Aug 17 17:17:15 CEST 2024
Certificate fingerprints:
         SHA1: EF:D4:D5:4F:D1:4B:66:22:E8:09:80:2E:6E:CB:C5:CB:F7:54:85:13
         SHA256: E9:D4:9B:B2:2C:6D:69:A8:2D:48:B2:9C:FD:E5:0E:74:BD:49:8A:66:3D:F2:6B:90:A4:FC:FE:32:5D:A3:09:45
Signature algorithm name: SHA256withECDSA
Subject Public Key Algorithm: 256-bit EC (secp256r1) key
Version: 3

Extensions:

#1: ObjectId: 2.5.29.35 Criticality=false
AuthorityKeyIdentifier [
KeyIdentifier [
0000: 6A 32 1F 64 67 F9 0B E1   9E 72 DB 2C 62 4F 29 FC  j2.dg....r.,bO).
0010: C0 07 AF 69                                        ...i
]
]

#2: ObjectId: 2.5.29.19 Criticality=true
BasicConstraints:[
  CA:true
  PathLen: no limit
]

#3: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 6A 32 1F 64 67 F9 0B E1   9E 72 DB 2C 62 4F 29 FC  j2.dg....r.,bO).
0010: C0 07 AF 69                                        ...i
]
]

Trust this certificate? [no]:  yes
Certificate was added to keystore

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
#  keytool -import -alias andy -file andy_cert.pem -keystore truststore.jks
Enter keystore password:
Certificate was added to keystore

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
#  keytool -import -alias me -file me_cert.pem -keystore truststore.jks
Enter keystore password:
Owner: CN=localhost, O=Ministero dell'Economia e delle Finanze, L=Roma, ST=Italia, C=IT
Issuer: CN=localhost, O=Ministero dell'Economia e delle Finanze, L=Roma, ST=Italia, C=IT
Serial number: 2e5a2fedd6a664f1b5fa9ebb3d941ac749617d53
Valid from: Fri Aug 18 19:44:25 CEST 2023 until: Sat Aug 17 19:44:25 CEST 2024
Certificate fingerprints:
         SHA1: 50:AC:D0:29:DA:EB:DD:0D:9A:81:84:90:87:26:A2:F7:FB:2E:DF:2A
         SHA256: 71:B8:E5:82:CC:52:3A:5F:6E:82:DB:77:E0:70:0B:87:E8:F9:0E:DA:FA:7B:12:DB:6F:88:62:B6:5B:9A:A8:B4
Signature algorithm name: SHA256withECDSA
Subject Public Key Algorithm: 256-bit EC (secp256r1) key
Version: 3

Extensions:

#1: ObjectId: 2.5.29.35 Criticality=false
AuthorityKeyIdentifier [
KeyIdentifier [
0000: 5C 82 A2 C7 14 35 C0 79   12 F8 CF FC CA F7 4A D5  \....5.y......J.
0010: 64 A4 CA 24                                        d..$
]
]

#2: ObjectId: 2.5.29.19 Criticality=true
BasicConstraints:[
  CA:true
  PathLen: no limit
]

#3: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 5C 82 A2 C7 14 35 C0 79   12 F8 CF FC CA F7 4A D5  \....5.y......J.
0010: 64 A4 CA 24                                        d..$
]
]

Trust this certificate? [no]:  yes
Certificate was added to keystore

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
#  keytool -import -alias joker -file joker_cert.pem -keystore truststore.jks
Enter keystore password:
Certificate was added to keystore

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# openssl pkcs12 -export -out joker.p12 -inkey joker_key.pem -in joker_cert.pem -name joker
Enter Export Password:
Verifying - Enter Export Password:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
# keytool -importkeystore -destkeystore joker_keystore.jks -srckeystore joker.p12 -srcstoretype PKCS12 -alias joker
Importing keystore joker.p12 to joker_keystore.jks...
Enter destination keystore password:
Re-enter new password:
Enter source keystore password:

andyv@MSI MINGW64 /g/Il mio Drive/Università degli Studi di Salerno/Magistrale - AI and IR/I°Anno/Secondo Semestre 2022-23/APS/Project - JokerCasinò/Certs/GP2.0
#
