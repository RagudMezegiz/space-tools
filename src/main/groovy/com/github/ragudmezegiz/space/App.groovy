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

import groovy.cli.Option
import groovy.cli.Unparsed
import groovy.cli.commons.CliBuilder

class App {

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
        def cli = new CliBuilder(usage:'space-tools [options] <command> {args}');
        options = cli.parseFromSpec(Options, args)
        if (options.help()) {
            cli.usage()
            // TODO Add help text for commands
        }
    }

    /**
     * Execute as specified on the command line.
     *
     * @return exit code; 0 on success, non-zero on failure
     */
    int execute() {
        if (options.help()) {
            // Help has been displayed already - exit
            return 0
        }

        if (options.remaining().isEmpty()) {
            // Missing command
            return 1
        }

        // TODO implement
        return 0
    }

    /**
     * Program entry point.
     *
     * @param args command-line arguments
     */
    static void main(String[] args) {
        def app = new App(args)
        def result = app.execute()
        if (result) {
            // TODO Print error message to console or log
        }
    }
}
