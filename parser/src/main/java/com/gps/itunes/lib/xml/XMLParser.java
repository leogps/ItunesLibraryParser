package com.gps.itunes.lib.xml;

import com.gps.itunes.lib.exceptions.LibraryParseException;
import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.parser.utils.FileUtils;
import com.gps.itunes.lib.parser.utils.ProcessesingTimeCheck;
import com.gps.itunes.lib.parser.utils.PropertyManager;
import com.gps.itunes.lib.types.*;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Map;

/**
 * Itunes Library XML parser
 * 
 * @author leogps
 *
 */
public class XMLParser {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(XMLParser.class);

	/**
	 * Parses the XML at the specified path and returns the LibraryObject of the
	 * Root element. <br />
	 * The root element contains everything as it's children and their children.
	 * 
	 * @param filePath
	 * @return {@link LibraryObject}
	 * @throws com.gps.itunes.lib.exceptions.LibraryParseException
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public LibraryObject parseXML(final String filePath)
			throws LibraryParseException, NoChildrenException {
		LibraryObject root = null;

		try {
			final InputStream localDTDStream;
			
			checkLibraryFileExistence(filePath);
			localDTDStream = getLocalDTDStream(filePath, PropertyManager.getConfigurationMap());

			final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();

			docBuilderFactory.setValidating(true); // and validating parser
													// features
			docBuilderFactory.setNamespaceAware(true); // Set namespace aware
			docBuilderFactory.setIgnoringElementContentWhitespace(true);

			final DocumentBuilder docBuilder = docBuilderFactory
					.newDocumentBuilder();
			final Document doc = docBuilder.parse(localDTDStream, "UTF-8");

			// normalize text representation
			doc.getDocumentElement().normalize();
			log.debug("Root element of the doc is "
					+ doc.getDocumentElement().getNodeName());

			final NodeList listOfPlaylists = doc.getElementsByTagName("plist");
			final int playlistLength = listOfPlaylists.getLength();
			log.debug("Total no of playlist elements : " + playlistLength);

			final Element rootElement = doc.getDocumentElement();

			root = getPlaylistElement(
					Helper.getPlaylistType(rootElement.getNodeName()), null);

			final ProcessesingTimeCheck timeChecker = new ProcessesingTimeCheck();
			timeChecker.setANow();

			buildDOM(root, rootElement);

			normalize(root);

			timeChecker.setBNow();
			timeChecker.printTimeTaken();

		} catch (SAXParseException err) {
			final String msg = "** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId();
			throw new LibraryParseException(msg, err);

		} catch (ParserConfigurationException pce) {
			throw new LibraryParseException(pce);
		} catch(FileNotFoundException fnfe){
			throw new LibraryParseException(fnfe);
		} catch (IOException ioEx) {
			throw new LibraryParseException(ioEx);
		} catch (SAXException saxEx) {
			throw new LibraryParseException(saxEx);
		}
		return root;
	}

	private void checkLibraryFileExistence(final String filePath)
			throws FileNotFoundException {
		FileUtils.checkFileExistenceThrowable(filePath);
	}

	private InputStream getLocalDTDStream(String filePath, Map<String, String> configurationMap) throws IOException {

		InputStream is = null;

		final String xmlContents = IOUtils.toString(new FileReader(new File(
				filePath)));

		String dtdModifiedString = xmlContents;
        if(configurationMap.containsKey(PropertyManager.Property.ITUNES_LOCAL_DTD_FILE_PROPERTY.getKey())) {

			String path = configurationMap.get(PropertyManager.Property.ITUNES_LOCAL_DTD_FILE_PROPERTY.getKey());
			String prefix = "";
			if(!path.startsWith(File.separator)) {
				prefix = new File("").getAbsolutePath();
			}

            final File localDtdFile = new File(prefix + path);

            if(localDtdFile.exists()) {
                dtdModifiedString = dtdModifiedString.replace(configurationMap.get(
						PropertyManager.Property.ITUNES_DEFAULT_XML_DTD_URL.getKey()), localDtdFile.getPath());
            }
        }

		is = IOUtils.toInputStream(dtdModifiedString, "UTF8");

		return is;
	}

	private void normalize(final LibraryObject libraryObject)
			throws NoChildrenException {
		if (libraryObject.hasChildren()) {

			final LibraryObject[] children = libraryObject.getChildren();

			for (int index = 0; index < children.length; index++) {

				final LibraryObject child = children[index];

				if (child.getType() == Type.DICT) {
					final Dict dictionary = (Dict) child;

					if (dictionary.hasChildren()) {

						final LibraryObject[] dictionaryChildren = dictionary
								.getChildren();

						for (int i = 0; i < dictionaryChildren.length; i++) {
							final Key dictionaryKey = (Key) dictionaryChildren[i];
							final LibraryObject dictionaryValue = dictionaryChildren[++i];

							dictionaryKey.setKeyValue(dictionaryValue);
							dictionary.put(dictionaryKey, dictionaryValue);

						}
					}

				}

				if (child.hasChildren()) {
					normalize(child);
				}

			}
		}
	}

	private void buildDOM(final LibraryObject playlistElement,
			final Element element) {

		final NodeList nodeList = element.getChildNodes();

		for (int index = 0; index < nodeList.getLength(); index++) {
			final Node node = nodeList.item(index);

			if (node.getNodeType() == Node.TEXT_NODE) {
				playlistElement.addChild(getPlaylistElement(Type.TEXTVALUE,
						playlistElement, node.getTextContent()));
			} else {
				final Type type = Helper.getPlaylistType(node.getNodeName());
				final LibraryObject childPlaylistElement = getPlaylistElement(
						type, playlistElement);

				playlistElement.addChild(childPlaylistElement);

				if (node.hasChildNodes()) {
					buildDOM(childPlaylistElement, (Element) node);
				}
			}

		}
	}

	private LibraryObject getPlaylistElement(Type type,
			LibraryObject parentElement, String... textContent) {

		switch (type) {

		case PLIST:
			return new Plist();

		case DICT:
			return new Dict(parentElement);

		case KEY:
			return new Key(parentElement);

		case STRING:
			return new LString(parentElement);

		case INTEGER:
			return new LInteger(parentElement);

		case DATE:
			return new LDate(parentElement);

		case TRUE:
			return new True(parentElement);

		case FALSE:
			return new False(parentElement);

		case ARRAY:
			return new Array(parentElement);

		case DATA:
			return new Data(parentElement);

		case TEXTVALUE:
			if (textContent == null || textContent.length < 1) {
				return new TextValue(parentElement, null);
			} else {
				return new TextValue(parentElement, textContent[0]);
			}

		default:
			return null;
		}

	}

}
