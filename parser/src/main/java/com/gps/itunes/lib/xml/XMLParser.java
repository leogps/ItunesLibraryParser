package com.gps.itunes.lib.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.gps.itunes.lib.exceptions.LibraryParseException;
import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.types.Array;
import com.gps.itunes.lib.types.True;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.gps.itunes.lib.parser.utils.ProcessesingTimeCheck;
import com.gps.itunes.lib.parser.utils.PropertyManager;
import com.gps.itunes.lib.types.Data;
import com.gps.itunes.lib.types.Dict;
import com.gps.itunes.lib.types.False;
import com.gps.itunes.lib.types.Helper;
import com.gps.itunes.lib.types.Key;
import com.gps.itunes.lib.types.LDate;
import com.gps.itunes.lib.types.LInteger;
import com.gps.itunes.lib.types.LString;
import com.gps.itunes.lib.types.LibraryObject;
import com.gps.itunes.lib.types.Plist;
import com.gps.itunes.lib.types.TextValue;
import com.gps.itunes.lib.types.Type;

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
			if(!PropertyManager.isWebContext()) {
				localDTDStream = getLocalDTDStream(filePath);
			} else {
				localDTDStream = getWebDTDStream(filePath);
			}

			final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();

			docBuilderFactory.setValidating(true); // and validating parser
													// features
			docBuilderFactory.setNamespaceAware(true); // Set namespace aware
			docBuilderFactory.setIgnoringElementContentWhitespace(true);

			final DocumentBuilder docBuilder = docBuilderFactory
					.newDocumentBuilder();
			final Document doc = docBuilder.parse(localDTDStream);

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

	private InputStream getWebDTDStream(final String filePath) throws MalformedURLException, IOException {
		InputStream is = null;

		final String xmlContents = IOUtils.toString(new FileReader(new File(
				filePath)));

		final String dtdModifiedString = xmlContents.replace(PropertyManager
				.readWebProperties().getProperty("dtdUrl"), System.getProperty("ILP_WEB_DTD"));

		is = IOUtils.toInputStream(dtdModifiedString, "UTF8");

		return is;
	}

	private void checkLibraryFileExistence(final String filePath)
			throws FileNotFoundException {
		FileReader fr = null;
		try {
			 fr = new FileReader(filePath);
		} finally {
			if(fr != null) {
				IOUtils.closeQuietly(fr);
			}
		}
	}

	private InputStream getLocalDTDStream(String filePath) throws IOException {

		InputStream is = null;

		final String xmlContents = IOUtils.toString(new FileReader(new File(
				filePath)));

		final String absolutePath = new File("").getAbsolutePath();

		final File dtdFile = new File(absolutePath
				+ PropertyManager.getProperties().getProperty("localDtdFile"));

		final String dtdModifiedString = xmlContents.replace(PropertyManager
				.getProperties().getProperty("dtdUrl"), dtdFile.getPath());

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
