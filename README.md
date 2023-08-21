# Unisa2022-23.APS

# Generazione parametri
openssl ecparam -name prime256v1 -out prime256v1.pem

# Generazione Chiavi Private
openssl genpkey -paramfile prime256v1.pem -out ms_key.pem
openssl genpkey -paramfile prime256v1.pem -out andy_key.pem
openssl genpkey -paramfile prime256v1.pem -out me_key.pem
openssl genpkey -paramfile prime256v1.pem -out joker_key.pem
openssl genpkey -paramfile prime256v1.pem -out adm_key.pem

# Generazione certificati self-signed
openssl req -new -x509 -days 365 -key ms_key.pem -out ms_cert.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"
openssl req -new -x509 -days 365 -key me_key.pem -out me_cert.pem -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"

# Generazioni Cartelle
mkdir MS MS/private MS/newcerts
touch MS/index.txt MS/serial
echo "00" >> MS/serial
cp ms_cert.pem MS
cp ms_key.pem MS/private

mkdir ME ME/private ME/newcerts
touch ME/index.txt ME/serial
echo "00" >> ME/serial
cp me_key.pem ME/private
cp me_cert.pem ME

# Generazione Richiesta GP2.0
openssl req -new -key andy_key.pem -out andy_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"

openssl ca -in andy_request.pem -out andy_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslGP.cnf"

# Generazione Richiesta Certificato Mr.Joker's Casino e Nodo ADM
openssl req -new -key joker_key.pem -out joker_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"
openssl ca -in joker_request.pem -out joker_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"

openssl req -new -key adm_key.pem -out adm_request.pem -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"
openssl ca -in adm_request.pem -out adm_cert.pem -policy policy_anything -config "C:\msys64\mingw64\etc\ssl\opensslME.cnf"


# Generazione dei Keystore
openssl pkcs12 -export -out ms.p12 -inkey ms_key.pem -in ms_cert.pem -name ms -passout pass:aps2023
keytool -importkeystore -destkeystore ms_keystore.jks -srckeystore ms.p12 -srcstoretype PKCS12 -alias ms -srcstorepass aps2023 -deststorepass aps2023

openssl pkcs12 -export -out andy.p12 -inkey andy_key.pem -in andy_cert.pem -name andy -passout pass:aps2023
keytool -importkeystore -destkeystore andy_keystore.jks -srckeystore andy.p12 -srcstoretype PKCS12 -alias andy -srcstorepass aps2023 -deststorepass aps2023

openssl pkcs12 -export -out me.p12 -inkey me_key.pem -in me_cert.pem -name me -passout pass:aps2023
keytool -importkeystore -destkeystore me_keystore.jks -srckeystore me.p12 -srcstoretype PKCS12 -alias me -srcstorepass aps2023 -deststorepass aps2023

openssl pkcs12 -export -out joker.p12 -inkey joker_key.pem -in joker_cert.pem -name joker -passout pass:aps2023
keytool -importkeystore -destkeystore joker_keystore.jks -srckeystore joker.p12 -srcstoretype PKCS12 -alias joker -srcstorepass aps2023 -deststorepass aps2023

openssl pkcs12 -export -out adm.p12 -inkey adm_key.pem -in adm_cert.pem -name adm -passout pass:aps2023
keytool -importkeystore -destkeystore adm_keystore.jks -srckeystore adm.p12 -srcstoretype PKCS12 -alias adm -srcstorepass aps2023 -deststorepass aps2023

# Generazione del Truststore
keytool -genkeypair -keyalg RSA -alias dummy -keystore truststore.jks -storepass aps2023 -dname "CN=dummy, OU=dummy, O=dummy, L=dummy, S=dummy, C=dummy" -validity 1
keytool -delete -alias dummy -keystore truststore.jks -storepass aps2023

keytool -importcert -file ms_cert.der -alias ms -keystore truststore.jks -storepass aps2023
keytool -importcert -file me_cert.der -alias me -keystore truststore.jks -storepass aps2023

<!-- Stampa del truststore.jks -->
keytool -list -keystore truststore.jks -storepass aps2023


<!--         JokerChain jokerChain = new JokerChain(false);

        Transaction t1 = new Transaction("Data1sd", 1);
        Transaction t2 = new Transaction("Data2sdasd", 1);
        Transaction t3 = new Transaction("Data3", 5);
        Transaction t4 = new Transaction("Data4", 1);
        Transaction t5 = new Transaction("Data5", 1);
        Transaction t6 = new Transaction("Data5", 5);
        Transaction t7 = new Transaction("Data6", 1);

        jokerChain.addTransaction(t1);
        jokerChain.addTransaction(t2);
        jokerChain.addTransaction(t3);
        jokerChain.addTransaction(t4);
        jokerChain.addTransaction(t5);
        jokerChain.addTransaction(t6);
        jokerChain.addTransaction(t7);

        for(Transaction t : jokerChain.getTransactions())
            System.out.println(t.toString());

        String merkleRootForType1 = jokerChain.calculateMerkleRootForType(1);
        System.out.println(merkleRootForType1);
        BigInteger bigIntValue = new BigInteger(merkleRootForType1, 16);
        BigInteger value = bigIntValue.mod(new BigInteger("37"));
        System.out.println("Merkle Root for Type 1: " + String.valueOf(value)); -->