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

import spock.lang.Specification

class AppTest extends Specification {
    def "no args returns non-zero"() {
        setup:
        def app = new App((String[])[])

        when:
        def result = app.execute()

        then:
        result != 0
    }
}
