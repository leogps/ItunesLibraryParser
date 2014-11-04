package com.gps.itunes.lib.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gps.itunes.lib.exceptions.NoChildrenException;

/**
 * Object representation of any kind of element described in the Itunes Library
 * XML file. These elements are specified in the xml's dtd file.
 * 
 * @author leogps
 * 
 */
public class LibraryObject {

	private final Type type;
	private final LibraryObject parent;
	private LibraryObject[] children;
	
	/**
	 *  Array containing all the LibraryObject types.
	 */
	public static final Type[] TYPE_ARRAY = { Type.PLIST, Type.DICT, Type.KEY,
		Type.STRING, Type.INTEGER, Type.DATE, Type.TRUE, Type.FALSE,
		Type.ARRAY, Type.DATA, Type.REAL, Type.TEXTVALUE, };

	/**
	 * Creates a LibraryObject with the specified Type and it's parent
	 * LibraryObject.
	 * 
	 * @param type
	 * @param parent
	 */
	protected LibraryObject(final Type type, final LibraryObject parent) {
		this.type = type;
		this.parent = parent;
	}

	/**
	 * Sets the Children for this LibraryObject.
	 * 
	 * @param children
	 */
	public void setChildren(final LibraryObject... children) {
		this.children = children;
	}

	/**
	 * Adds a child to the LibraryObject.
	 * 
	 * @param child
	 */
	public void addChild(final LibraryObject... child) {
		final List<LibraryObject> childList = new ArrayList<LibraryObject>();
		if (children != null) {
			for (LibraryObject c : children) {
				childList.add(c);
			}
		}

		final List<LibraryObject> appendableList = Arrays.asList(child);

		childList.addAll(appendableList);

		children = childList.toArray(new LibraryObject[childList.size()]);

	}

	/**
	 * Returns the Type of the LibraryObject
	 * 
	 * @return {@link Type}
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Returns the parent of this LibraryObject 
	 * 
	 * @return {@link LibraryObject}
	 */
	public LibraryObject getParent() {
		return parent;
	}

	/**
	 * Returns all the children of this LibraryObject
	 * 
	 * @return array of {@link LibraryObject}s
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public LibraryObject[] getChildren() throws NoChildrenException {
		return children;
	}

	/**
	 * Returns true if the LibraryObject has any children.
	 * 
	 * @return boolean
	 */
	public boolean hasChildren() {
		return (children != null && children.length > 0);
	}

	/**
	 * Checks if this LibraryObject is the 'Key' element in the XML i.e.,
	 * Type.KEY
	 * 
	 * @return boolean
	 */
	public boolean isKey() {
		return false;
	}
	
	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		if (children != null) {
			for (LibraryObject child : children) {
				buffer.append((child == null) ? "(null)" : child.getType()
						+ ", ");
			}
		}

		return type + ": [" + "Parent: "
				+ ((parent == null) ? "Root" : parent.getType())
				+ "; Children: " + buffer + "]";
	}

}
