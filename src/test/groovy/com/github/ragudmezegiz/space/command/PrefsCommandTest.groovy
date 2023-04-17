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

import java.util.prefs.Preferences
import spock.lang.Specification

/* groovylint-disable MethodName, MethodReturnTypeRequired */

/** Test of class PrefsCommand. */
class PrefsCommandTest extends Specification {

    def "set preferences"() {
        given:
        def prefs = Mock(Preferences)
        def cmd = new PrefsCommand(prefs)
        cmd.arguments(['identity=test', 'password=P@55w0rd!'])

        when:
        def result = cmd.execute()

        then:
        result
        with(prefs) {
            1 * sync()
            1 * put('identity', 'test')
            1 * put('password', 'P@55w0rd!')
            1 * flush()
            0 * _
        }
    }

    def "clear preferences"() {
        given:
        def prefs = Mock(Preferences)
        def cmd = new PrefsCommand(prefs)
        cmd.arguments(['clear'])

        when:
        def result = cmd.execute()

        then:
        result
        with(prefs) {
            1 * sync()
            1 * clear()
            1 * flush()
            0 * _
        }
    }

    def "list preferences"() {
        given:
        def prefs = Mock(Preferences)
        def cmd = new PrefsCommand(prefs)
        cmd.arguments(['list'])

        when:
        def result = cmd.execute()

        then:
        result
        with(prefs) {
            1 * sync()
            1 * keys() >> new String[]{ 'user', 'password' }
            1 * get('user', _)
            1 * get('password', _)
            1 * flush()
            0 * _
        }
    }

}
