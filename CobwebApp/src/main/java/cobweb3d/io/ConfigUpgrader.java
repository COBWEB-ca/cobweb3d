package cobweb3d.io;

import cobwebutil.Versionator;
import org.w3c.dom.Element;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Fixes for dealing with difference between older versions of configuration files.
 */
class ConfigUpgrader {

    private static final String[] VERSIONS = {
            "2011",
            "2015-01-14",
            "2017-03-15"
    };

    private static final String VERSION_LATEST = VERSIONS[VERSIONS.length - 1];

    public static void upgradeConfigFile(File filename) throws FileNotFoundException {
        FileInputStream file = new FileInputStream(filename);

        Element root = CobwebXmlHelper.openDocument(file);
        try {
            file.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        String version;

        if (root.getNodeName().equals("COBWEB3Config"))
            // new root element includes version number
            version = root.getAttribute("config-version");

        else if (
                root.getNodeName().equals("inputData") ||
                        // broken in 2.2009-09-29-3-g23f2baf, fixed in 2.0-34-g4db7bf2
                        root.getNodeName().equals("inputeData"))
            version = VERSIONS[0];

        else
            throw new IllegalArgumentException("This config file is very old and can't be read");

        if (version.compareTo(VERSION_LATEST) >= 0)
            return;

        if (version.equals(VERSIONS[0])) {
            // Upgrade from 2011
            upgradeUsingXSLT(filename, "upgrade-2011-2015-01-14.xslt");
            version = VERSIONS[1];
        }

        if (version.equals(VERSIONS[1])) {
            upgradeUsingXSLT(filename, "upgrade-2015-01-14-2017-03-15.xslt");
            version = VERSIONS[2];
        }

    }

    private static void upgradeUsingXSLT(File filename, String xsltName) throws TransformerFactoryConfigurationError {
        // backup file
        File bakFile = new File(filename + ".bak");
        for (int attempt = 1; bakFile.exists(); attempt++) {
            bakFile = new File(filename + ".bak" + attempt);
        }
        filename.renameTo(bakFile);

        InputStream inStream = null;
        try {
            inStream = new FileInputStream(bakFile);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(filename);
        } catch (FileNotFoundException ex) {
            try {
                inStream.close();
            } catch (IOException ex2) {
            }
            throw new RuntimeException(ex);
        }

        InputStream xsltStream = null;
        try {
            xsltStream = ClassLoader.getSystemResourceAsStream("compatibility/" + xsltName);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));
            transformer.setParameter("cobweb-version", Versionator.getVersion());

            transformer.transform(new StreamSource(inStream), new StreamResult(outStream));
        } catch (TransformerConfigurationException ex) {
            throw new RuntimeException(ex);
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                inStream.close();
            } catch (IOException ex2) {
            }
            try {
                outStream.close();
            } catch (IOException ex2) {
            }
            try {
                if (xsltStream != null) xsltStream.close();
            } catch (IOException ex2) {
            }
        }
    }

}
