// Copyright 2023 David Terhune
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.github.ragudmezegiz.space.command

import groovy.transform.PackageScope
import java.util.prefs.Preferences

/* groovylint-disable DuplicateNumberLiteral */

/** Preferences command. */
class PrefsCommand extends AbstractCommand {

    private final Map values = [:]
    private boolean doClear = false
    private boolean doList = false

    PrefsCommand() {
        super()
    }

    @PackageScope
    PrefsCommand(Preferences prefs) {
        super(prefs)
    }

    void arguments(List args) {
        if (args.size() == 1) {
            if (args.get(0) == 'clear') {
                doClear = true
                return
            } else if (args.get(0) == 'list') {
                doList = true
                return
            }
        }

        args.each { str ->
            def kv = str.split('=')
            assert kv.size() == 2
            values[kv[0]] = kv[1]
        }
    }

    boolean execute() {
        userPrefs.sync()
        if (doClear) {
            userPrefs.clear()
        } else if (doList) {
            userPrefs.keys().each { k ->
                def v = userPrefs.get(k, 'UNDEF')
                System.console().println("$k = $v")
            }
        } else {
            values.each { e ->
                userPrefs.put(e.key, e.value)
            }
        }
        userPrefs.flush()
        return true
    }

    String help() {
        return '''\
            |prefs clear
            |  clear all preferences
            |prefs list
            |  list all preferences
            |prefs { <property>=<value> }
            |  set properties to specified values
            |  preferences are used by other commands
            |'''.stripMargin()
    }

}
