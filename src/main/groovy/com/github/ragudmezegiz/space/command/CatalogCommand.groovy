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

import com.github.ragudmezegiz.space.App
import groovy.json.JsonLexer
import groovy.json.JsonToken
import groovy.json.JsonTokenType
import groovy.transform.PackageScope
import java.util.prefs.Preferences
import wslite.rest.RESTClient
import wslite.rest.Response

/** Catalog operation command. */
class CatalogCommand extends AbstractCommand {

    // Unexpected token error message
    private static final String UNEXPECTED = 'Unexpected token in catalog JSON'

    // Space-Track.org site
    private static final String SPACETRACK_URL = 'https://www.space-track.org'

    // Space-Track.org auth path
    @PackageScope
    static final String AUTH_PATH = '/ajaxauth/login'

    // Space-Track.org catalog query
    // All non-decayed objects with updates in the last 30 days,
    // in JSON format
    @PackageScope
    static final String CATALOG_QUERY = '''\
https://www.space-track.org/basicspacedata/query/class/gp\
/decay_date/null-val/epoch/%3Enow-30/orderby/norad_cat_id\
/format/json'''

    @PackageScope
    static final String MISSING = 'UNKNOWN'

    private final RESTClient client

    CatalogCommand() {
        super()
        client = new RESTClient(SPACETRACK_URL)
    }

    CatalogCommand(Preferences prefs) {
        this(prefs, new RESTClient(SPACETRACK_URL))
    }

    @PackageScope
    CatalogCommand(Preferences prefs, RESTClient rc) {
        super(prefs)
        client = rc
    }

    void arguments(List args) {
        assert args.size() == 1
        assert args.remove(0) == 'update'
    }

    boolean execute() {
        return doUpdate()
    }

    String help() {
        return '''\
            |catalog update
            |  download catalog from Space-Track.org and update local database
            |  uses preferences "identity" and "password" to authenticate
            |'''.stripMargin()
    }

    private boolean doUpdate() {
        // Get the identity and password from the preferences
        String id = userPrefs.get('identity', MISSING)
        String pw = userPrefs.get('password', MISSING)
        if (id == MISSING || pw == MISSING) {
            App.errorMsg = 'Missing identity or password'
            return false
        }

        // Authenticate and execute the catalog query
        Response response = client.post(path: AUTH_PATH) {
            urlenc identity: id, password: pw, query: CATALOG_QUERY
        }
        if (response.statusCode != 200) {
            App.errorMsg = response.statusMessage
            return false
        }

        // Put elements into catalog
        def bis = new ByteArrayInputStream(response.data)
        def reader = new InputStreamReader(bis)
        def jlex = new JsonLexer(reader)
        return readElementsArray(jlex)
    }

    private boolean readElementsArray(JsonLexer jlex) {
        expect(jlex.nextToken(), JsonTokenType.OPEN_BRACKET)

        boolean success = true
        while (jlex.hasNext()) {
            def tok = jlex.next()
            switch (tok.type) {
                case JsonTokenType.OPEN_CURLY:
                    success = success && readElement(jlex)
                    break
                case JsonTokenType.COMMA:
                    // Ignore the comma between elements
                    break
                case JsonTokenType.CLOSE_BRACKET:
                    // Array now finished
                    return success
                default:
                    App.errorMsg = UNEXPECTED
                    return false
            }
        }
    }

    private boolean readElement(JsonLexer jlex) {
        // The open curly bracket has already been pulled.
        def element = [:]
        while (jlex.hasNext()) {
            def tok = jlex.next()
            switch (tok.type) {
                case JsonTokenType.COMMA:
                    // Ignore comma between fields
                    break
                case JsonTokenType.CLOSE_CURLY:
                    // Finished with this element
                    // TODO Insert element into catalog
                    System.console().println(element.TLE_LINE0)
                    return true
                case JsonTokenType.STRING:
                    // Field name
                    expect(jlex.nextToken(), JsonTokenType.COLON)
                    element[tok.value] = readValue(jlex)
                    break
                default:
                    App.errorMsg = UNEXPECTED
                    return false
            }
        }
    }

    private Object readValue(JsonLexer jlex) {
        return jlex.nextToken().value
    }

    private void expect(JsonToken tok, JsonTokenType expType) {
        assert tok.type == expType
    }

}

