package com.gps.itunes.lib.parser;

import com.gps.itunes.lib.exceptions.LibraryParseException;
import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.items.tracks.Track;
import com.gps.itunes.lib.parser.utils.LocationDecoder;
import com.gps.itunes.lib.parser.utils.LogInitializer;
import com.gps.itunes.lib.parser.utils.PropertyManager;
import com.gps.itunes.lib.tasks.LibraryParser;
import org.apache.commons.cli.*;

/**
 * Created by leogps on 11/15/15.
 */
public class Main {

    public static void main(String args[]) throws ParseException, LibraryParseException, NoChildrenException {
        Options options = buildOptions();
        CommandLine commandLine = parseArgs(options, args);
        processCommandLine(commandLine, options);

    }

    private static void processCommandLine(CommandLine commandLine, Options options) throws LibraryParseException, NoChildrenException {
        processAppInquiryOptions(options, commandLine);
        if(checkIfLibraryParseRequested(commandLine)) {
            processLibraryParseOptions(options, commandLine);
        }
    }

    private static boolean checkIfLibraryParseRequested(CommandLine commandLine) {
        for(Option option : commandLine.getOptions()) {
            return option.getOpt().equals(CommandLineOptions.LIBRARY.shortOptionName) ||
                    option.getOpt().equals(CommandLineOptions.PRINT_PLAY_LISTS.shortOptionName) ||
                    option.getOpt().equals(CommandLineOptions.PRINT_TRACKS.shortOptionName);
        }
        return false;
    }

    private static void processLibraryParseOptions(Options options, CommandLine commandLine) throws LibraryParseException, NoChildrenException {
        ItunesLibraryParser itunesLibraryParser = new LibraryParser();
        ItunesLibraryParsedData itunesLibraryParsedData = parseLibrary(itunesLibraryParser, commandLine);

        if(commandLine.hasOption(CommandLineOptions.PRINT_PLAY_LISTS.shortOptionName)) {
            Playlist[] playlists = itunesLibraryParsedData.getAllPlaylists();
            printPlaylists(playlists);
        }

        if(commandLine.hasOption(CommandLineOptions.PRINT_TRACKS.shortOptionName)) {
            Track[] tracks = itunesLibraryParsedData.getAllTracks();
            printTracks(tracks);
        }
    }

    private static void processAppInquiryOptions(Options options, CommandLine commandLine) {
        if(commandLine.getOptions().length < 1
                || commandLine.hasOption(CommandLineOptions.HELP.shortOptionName)) {
            printHelp(options);
        }

        if (commandLine.hasOption(CommandLineOptions.VERSION.shortOptionName)) {
            printVersion();
        }
    }

    private static void printTracks(Track[] tracks) {
        System.out.println("Printing Tracks:");
        for(Track track : tracks) {
            String decodedLocation = LocationDecoder.decodeLocation(track.getLocation());
            System.out.println(String.format("Id: %s; Name: %s, Location: %s",
                    track.getTrackId(), track.getTrackName(), decodedLocation));
        }
        System.out.println("End of tracks.");
    }

    private static void printPlaylists(Playlist[] playlists) {
        System.out.println("Printing playlists:");
        for(Playlist playlist : playlists) {
            System.out.println(String.format("Playlist Name: %s; Number of Tracks: %s", playlist.getName(), playlist.getPlaylistItems().length));
        }
        System.out.println("End of playlists.");
    }

    private static ItunesLibraryParsedData parseLibrary(ItunesLibraryParser itunesLibraryParser,
                                                    CommandLine commandLine) throws LibraryParseException, NoChildrenException {

        if(!commandLine.hasOption(CommandLineOptions.LIBRARY.shortOptionName)) {
            System.out.println(String.format("Please specify the library option by either %s or %s followed by the full path to the library file in quotes.",
                    CommandLineOptions.LIBRARY.shortOptionName, CommandLineOptions.LIBRARY.longOptionName));
            System.exit(0);
        }

        String libraryFilePath = commandLine.getOptionValue(CommandLineOptions.LIBRARY.shortOptionName);
        itunesLibraryParser.addParseConfiguration(PropertyManager.Property.LIBRARY_FILE_LOCATION_PROPERTY.getKey(),
                libraryFilePath);
        return itunesLibraryParser.parse();
    }

    private static void printVersion() {
        System.out.println("iTunesLibraryParser version: " + LogInitializer.VERSION);
    }

    private static CommandLine parseArgs(Options options, String[] args) throws ParseException {
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            return commandLineParser.parse(options, args);
        } catch (ParseException pe) {
            printHelp(options);
            throw pe;
        }
    }

    private static void printHelp(Options options) {
        String header = "Parses the iTunes Library XML file and lets perform operations on playlists and tracks\n\n";
        String footer = "\nPlease report issues at http://sourceforge.net/users/leogps";//TODO: change this.

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("iTunesLibraryParser", header, options, footer, true);
    }

    private static Options buildOptions() {
        Options options = new Options();

        OptionGroup requiredOptionGroup = new OptionGroup();
        requiredOptionGroup.addOption(buildOption(CommandLineOptions.LIBRARY));
        options.addOptionGroup(requiredOptionGroup);

        options.addOption(buildOption(CommandLineOptions.PRINT_PLAY_LISTS));
        options.addOption(buildOption(CommandLineOptions.PRINT_TRACKS));
        options.addOption(buildOption(CommandLineOptions.HELP));
        options.addOption(buildOption(CommandLineOptions.VERSION));

        return options;
    }

    public enum CommandLineOptions {
        LIBRARY("lib", "library-file", true, "iTunes Music Library.xml file", true),
        PRINT_PLAY_LISTS("plists", "list-playlists", false, "Lists Playlists in the library", true),
        PRINT_TRACKS("trks", "list-tracks", false, "Lists Tracks in the library", true),
        HELP("h", "help", false, "Prints this help message", false),
        VERSION("v", "version", false, "Prints the version", false);

        private String shortOptionName;
        private String longOptionName;
        private boolean hasArgument;
        private String description;
        private boolean isLibraryFileParsingRequired;

        CommandLineOptions(String shortOptionName, String longOptionName, boolean hasArgument, String description,
                           boolean isLibraryFileParsingRequired) {
            this.shortOptionName = shortOptionName;
            this.longOptionName = longOptionName;
            this.hasArgument = hasArgument;
            this.description = description;
            this.isLibraryFileParsingRequired = isLibraryFileParsingRequired;
        }
    }

    private static Option buildOption(CommandLineOptions commandLineOptions) {
        return new Option(commandLineOptions.shortOptionName, commandLineOptions.longOptionName,
                commandLineOptions.hasArgument, commandLineOptions.description);
    }

}
