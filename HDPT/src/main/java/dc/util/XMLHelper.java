package dc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

@SuppressWarnings("unchecked")
public class XMLHelper {

	private static String DEFAULT_ENCODING = "utf-8";

	public static Document createDocument() {
		return DocumentHelper.createDocument();
	}

	public static Document getDocument(String fileName)
			throws FileNotFoundException, DocumentException {
		return getDocument(new File(fileName));
	}

	public static Document getDocument(File file) throws FileNotFoundException, DocumentException {
		String encoding = EncodingDetect.getJavaEncode(file.getAbsolutePath());
		return getDocument(new FileInputStream(file), encoding);
	}

	public static Document getDocument(InputStream ins) throws DocumentException {
		SAXReader oReader = new SAXReader();
		return oReader.read(ins);
	}
	
	public static Document getDocument(InputStream ins, String encoding) throws DocumentException {
		SAXReader oReader = new SAXReader();
		oReader.setEncoding(encoding);
		return oReader.read(ins);
	}

	public static void putDocument(OutputStream outs, Document doc) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = null;
		try {
			format.setEncoding(DEFAULT_ENCODING);
			writer = new XMLWriter(outs, format);
			writer.setEscapeText(false);
			writer.write(doc);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void putDocument(File file, Document doc) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = null;
		try {
			format.setEncoding("UTF-8");
			writer = new XMLWriter(new FileOutputStream(file), format);
			writer.setEscapeText(true);
			writer.write(doc);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void ConvertAttrs(Element el, HashMap<String, String> props,
			String prefix) {
		if (el == null) {
			return;
		}
		List<Attribute> attrs = el.attributes();
		for (Iterator<Attribute> it = attrs.iterator(); it.hasNext();) {
			Attribute attr = it.next();
			String name = attr.getName();
			if (prefix != null && prefix.length() > 0) {
				char c = name.charAt(0);
				if (c > 96 && c < 123) {
					c -= 32;
				}
				name = prefix + c + name.substring(1);
			}
			String value = attr.getText();
			props.put(name, value);
		}
	}

}
