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

import spock.lang.Specification

/* groovylint-disable MethodName, MethodReturnTypeRequired */

/** Test of class CommandFactory. */
class CommandFactoryTest extends Specification {

    def "nonexistent command throws exception"() {
        when:
        CommandFactory.makeCommand('nonexistentcommand')

        then:
        IllegalArgumentException e = thrown()
        e.message == 'nonexistentcommand is not a valid command'
    }

    def "prefs command available"() {
        when:
        def result = CommandFactory.makeCommand('prefs')

        then:
        result instanceof PrefsCommand
    }

    def "catalog command available"() {
        when:
        def result = CommandFactory.makeCommand('catalog')

        then:
        result instanceof CatalogCommand
    }

}
