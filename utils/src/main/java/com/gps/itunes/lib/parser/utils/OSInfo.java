package com.gps.itunes.lib.parser.utils;

/**
 * Utility class to retrieve Operating system info.
 *
 * @author leogps
 *
 */
public class OSInfo {

	private static String OS = System.getProperty("os.name").toLowerCase();

	private final static String OS_NAME = "os.name";
        private final static String OS_ARCH = "os.arch";

    /**
     * Checks if the current machine is a Mac
     *
     * @return boolean
     */
	public static boolean isOSMac() {
		return OS.contains("mac");
	}

	/**
	 * Checks if the current machine is a Windows machine.
	 *
	 * @return
	 */
	public static boolean isOSWin() {
		return OS.contains("win");
	}

	public static boolean isOSLinux() {
		return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
	}

	/**
	 * Checks if the System is a 64 bit machine.
	 *
	 * @return boolean
	 */
	public static boolean isArch64() {
		return containsIgnoreCase(System.getProperty(OS_ARCH), "x86_64" ) ||
				containsIgnoreCase(System.getProperty(OS_ARCH), "amd64" );
	}

	/**
	 * Checks if the System is a 32 bit machine.
	 *
	 * @return boolean
	 */
	public static boolean isArch32() {
		return !isArch64();
	}

	public static String getOsPrefix() {
		if (isOSMac()) {
			return "mac";
		}
		if (isOSWin()) {
			return "win";
		}
		return "nix";
	}

	public static boolean containsIgnoreCase(String string, String searchString) {
		return string != null && string.toLowerCase().contains(searchString.toLowerCase());
	}

	public static void main(String[] args) {

		System.out.println(OS);

		if (isOSWin()) {
			System.out.println("This is Windows");
		} else if (isOSMac()) {
			System.out.println("This is MacOS");
		} else if (isOSLinux()) {
			System.out.println("This is Unix or Linux");
		} else {
			System.out.println("Your OS is not supported!!");
		}
	}
}
