package JokerCasino;

import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PemReader {

    public static List<String> read(String cert_path) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new FileInputStream(cert_path));

        // Lista risultati
        List<String> info_player = new ArrayList<String>();

        // Estraiamo i valori del campo Subject e del campo notAfter
        String subjectDN = cert.getSubjectDN().getName();
        Date notAfter = cert.getNotAfter();

        // Inseriamo la data avente il formato dd-mm-yyyy nella lista risultati
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        info_player.add(dateFormat.format(notAfter));
        
        // Inseriamo nella lista solo i valori relativi al campo "Codice Fiscale" e "Data di Nascita"
        List<String> oidList = Arrays.asList("OID.1.2.3.1.4", "OID.1.2.3.1.7");

        String[] fields = subjectDN.split(",");
        for(String field : fields){
            String[] fieldSplit = field.split("=");
            if(oidList.contains(fieldSplit[0].trim()))
                info_player.add(fieldSplit[1]);
        }

        return info_player;
    }
}
