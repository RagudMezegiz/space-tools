// Copyright 2022 David Terhune
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.github.ragudmezegiz.space

import com.github.ragudmezegiz.space.catalog.Catalog
import com.github.ragudmezegiz.space.command.Command
import com.github.ragudmezegiz.space.command.CommandFactory

import groovy.cli.Option
import groovy.cli.Unparsed
import groovy.cli.commons.CliBuilder

import java.util.prefs.Preferences

/* groovylint-disable DuplicateNumberLiteral */

/** Main application entry point. */
class App {

    private static final String FILE_SEP = System.getProperty('file.separator')

    /** Error message. */
    static String errorMsg

    /**
     * Program entry point.
     *
     * @param args command-line arguments
     */
    static void main(String[] args) {
        def app = new App(args)
        try {
            if (!app.execute()) {
                System.console().println(errorMsg)
            }
        } catch (IllegalArgumentException ex) {
            System.console().println(ex.message)
        }
    }

    /**
     * Return a new catalog instance.
     *
     * @return catalog
     */
    static Catalog catalog() {
        new File(folder()).mkdir()
        return new Catalog(database())
    }

    /**
     * Return the application preferences.
     *
     * @return preferences
     */
    static Preferences preferences() {
        return Preferences.userNodeForPackage(App)
    }

    /**
     * Execute as specified on the command line.
     *
     * @return true on success, false on failure
     */
    boolean execute() {
        if (options.help()) {
            // Help has been displayed already - exit
            return true
        }

        if (options.remaining().empty) {
            // Missing command
            App.setErrorMsg('Command required')
            return false
        }

        boolean doHelp = false
        if (options.remaining().get(0) == 'help') {
            doHelp = true
        }

        def cmd = CommandFactory.makeCommand(options.remaining().remove(0))
        cmd.arguments(options.remaining())

        if (doHelp) {
            System.console().println(cmd.help())
            return true
        }

        return cmd.execute()
    }

    /**
     * Return the database file name.
     *
     * @return database file name
     */
    private static String database() {
        return folder() + FILE_SEP + 'catalog.db'
    }

    /**
     * Return the application data folder.
     *
     * @return data folder
     */
    private static String folder() {
        return System.getProperty('user.home') + FILE_SEP + '.space-tools'
    }

    /** Command-line option specification. */
    interface Options {

        @Option(shortName='h', longName='help', description='print this text')
        boolean help()

        @Unparsed
        List remaining()

    }

    /** Command-line options. */
    def options

    /**
     * Constructor.
     *
     * @param args command-line arguments
     */
    App(String[] args) {
        def cli = new CliBuilder(usage:'space-tools [options] <command> {args}')
        options = cli.parseFromSpec(Options, args)
        if (options.help()) {
            cli.usage()
            System.console().println('''\
                |help <command>
                |  print help text for command
                |'''.stripMargin())
            CommandFactory.commandList().each { c ->
                System.console().println(c.help())
            }
        }
    }

}
