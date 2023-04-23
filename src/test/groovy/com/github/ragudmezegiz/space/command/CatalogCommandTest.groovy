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
import groovy.json.JsonSlurper
import java.util.prefs.Preferences
import spock.lang.Ignore
import spock.lang.Specification
import wslite.http.HTTPMethod
import wslite.http.HTTPRequest
import wslite.http.HTTPResponse
import wslite.rest.Response
import wslite.rest.RESTClient

/* groovylint-disable MethodName, MethodReturnTypeRequired */

/** Test of class CatalogCommand. */
class CatalogCommandTest extends Specification {

    @Ignore
    def "run catalog command for real"() {
        given:
        CatalogCommand cmd = new CatalogCommand()

        when:
        def result = cmd.execute()

        then:
        result
    }

    def "missing identity preference fails"() {
        given:
        Preferences prefs = Mock(Preferences)
        RESTClient client = Mock(RESTClient)
        CatalogCommand cmd = new CatalogCommand(prefs, client)
        cmd.arguments(['update'])

        when:
        def result = cmd.execute()

        then:
        !result
        App.errorMsg == 'Missing identity or password'
        with(prefs) {
            1 * get('identity', _) >> CatalogCommand.MISSING
            1 * get('password', _) >> 'P@55w0rd!'
        }
    }

    def "missing password preference fails"() {
        given:
        Preferences prefs = Mock(Preferences)
        RESTClient client = Mock(RESTClient)
        CatalogCommand cmd = new CatalogCommand(prefs, client)
        cmd.arguments(['update'])

        when:
        def result = cmd.execute()

        then:
        !result
        App.errorMsg == 'Missing identity or password'
        with(prefs) {
            1 * get('identity', _) >> 'user'
            1 * get('password', _) >> CatalogCommand.MISSING
        }
    }

    def "successful response updates catalog"() {
        given:
        HTTPRequest hreq = new HTTPRequest().with {
            url = new URL('https://www.space-track.org')
            method = HTTPMethod.POST
            /* groovylint-disable-next-line ImplicitClosureParameter */
            it
        }
        HTTPResponse hres = new HTTPResponse().with {
            statusCode = 200
            statusMessage = 'Ok'
            contentType = 'text/json'
            data = JSON
            /* groovylint-disable-next-line ImplicitClosureParameter */
            it
        }
        Response resp = new Response(hreq, hres).with {
            text = hres.contentAsString
            json = new JsonSlurper().parseText(text)
            /* groovylint-disable-next-line ImplicitClosureParameter */
            it
        }
        Preferences prefs = Mock(Preferences)
        RESTClient client = Mock(RESTClient)
        CatalogCommand cmd = new CatalogCommand(prefs, client)
        cmd.arguments(['update'])

        when:
        def result = cmd.execute()

        then:
        result
        with(prefs) {
            1 * get('identity', _) >> 'user'
            1 * get('password', _) >> 'P@55w0rd!'
        }
        with(client) {
            1 * post(_, _) >> resp
        }
    }

    def "failure response generates error"() {
        given:
        HTTPRequest hreq = new HTTPRequest().with {
            url = new URL('https://www.space-track.org')
            method = HTTPMethod.POST
            /* groovylint-disable-next-line ImplicitClosureParameter */
            it
        }
        HTTPResponse hres = new HTTPResponse().with {
            statusCode = 403
            statusMessage = 'User not authorized'
            /* groovylint-disable-next-line ImplicitClosureParameter */
            it
        }
        Response resp = new Response(hreq, hres)
        Preferences prefs = Mock(Preferences)
        RESTClient client = Mock(RESTClient)
        CatalogCommand cmd = new CatalogCommand(prefs, client)
        cmd.arguments(['update'])

        when:
        def result = cmd.execute()

        then:
        !result
        App.errorMsg == 'User not authorized'
        with(prefs) {
            1 * get('identity', _) >> 'user'
            1 * get('password', _) >> 'P@55w0rd!'
        }
        with(client) {
            1 * post(_, _) >> resp
        }
    }

    // JSON returned by successful query
    private static final byte[] JSON = '''[{
            "CCSDS_OMM_VERS": "2.0",
            "COMMENT": "GENERATED VIA SPACE-TRACK.ORG API",
            "CREATION_DATE": "2023-04-16T04:30:29",
            "ORIGINATOR": "18 SPCS",
            "OBJECT_NAME": "VANGUARD 1",
            "OBJECT_ID": "1958-002B",
            "CENTER_NAME": "EARTH",
            "REF_FRAME": "TEME",
            "TIME_SYSTEM": "UTC",
            "MEAN_ELEMENT_THEORY": "SGP4",
            "EPOCH": "2023-04-15T19:02:29.925024",
            "MEAN_MOTION": "10.85079051",
            "ECCENTRICITY": "0.18468760",
            "INCLINATION": "34.2555",
            "RA_OF_ASC_NODE": "287.6260",
            "ARG_OF_PERICENTER": "318.6071",
            "MEAN_ANOMALY": "28.8080",
            "EPHEMERIS_TYPE": "0",
            "CLASSIFICATION_TYPE": "U",
            "NORAD_CAT_ID": "5",
            "ELEMENT_SET_NO": "999",
            "REV_AT_EPOCH": "31738",
            "BSTAR": "0.00012282000000",
            "MEAN_MOTION_DOT": "0.00000104",
            "MEAN_MOTION_DDOT": "0.0000000000000",
            "SEMIMAJOR_AXIS": "8618.419",
            "PERIOD": "132.709",
            "APOAPSIS": "3831.999",
            "PERIAPSIS": "648.569",
            "OBJECT_TYPE": "PAYLOAD",
            "RCS_SIZE": "SMALL",
            "COUNTRY_CODE": "US",
            "LAUNCH_DATE": "1958-03-17",
            "SITE": "AFETR",
            "DECAY_DATE": null,
            "FILE": "3888171",
            "GP_ID": "230108008",
            "TLE_LINE0": "0 VANGUARD 1",
            "TLE_LINE1": "1 00005U 58002B   23105.79340191  .00000104  00000-0  12282-3 0  9998",
            "TLE_LINE2": "2 00005  34.2555 287.6260 1846876 318.6071  28.8080 10.85079051317382"
        },
        {
            "CCSDS_OMM_VERS": "2.0",
            "COMMENT": "GENERATED VIA SPACE-TRACK.ORG API",
            "CREATION_DATE": "2023-04-16T04:30:29",
            "ORIGINATOR": "18 SPCS",
            "OBJECT_NAME": "VANGUARD 2",
            "OBJECT_ID": "1959-001A",
            "CENTER_NAME": "EARTH",
            "REF_FRAME": "TEME",
            "TIME_SYSTEM": "UTC",
            "MEAN_ELEMENT_THEORY": "SGP4",
            "EPOCH": "2023-04-15T19:20:38.899392",
            "MEAN_MOTION": "11.86983097",
            "ECCENTRICITY": "0.14626120",
            "INCLINATION": "32.8717",
            "RA_OF_ASC_NODE": "137.5920",
            "ARG_OF_PERICENTER": "322.2928",
            "MEAN_ANOMALY": "28.3426",
            "EPHEMERIS_TYPE": "0",
            "CLASSIFICATION_TYPE": "U",
            "NORAD_CAT_ID": "11",
            "ELEMENT_SET_NO": "999",
            "REV_AT_EPOCH": "39757",
            "BSTAR": "0.00171280000000",
            "MEAN_MOTION_DOT": "0.00003253",
            "MEAN_MOTION_DDOT": "0.0000000000000",
            "SEMIMAJOR_AXIS": "8117.809",
            "PERIOD": "121.316",
            "APOAPSIS": "2926.994",
            "PERIAPSIS": "552.353",
            "OBJECT_TYPE": "PAYLOAD",
            "RCS_SIZE": "MEDIUM",
            "COUNTRY_CODE": "US",
            "LAUNCH_DATE": "1959-02-17",
            "SITE": "AFETR",
            "DECAY_DATE": null,
            "FILE": "3888171",
            "GP_ID": "230108003",
            "TLE_LINE0": "0 VANGUARD 2",
            "TLE_LINE1": "1 00011U 59001A   23105.80600578  .00003253  00000-0  17128-2 0  9996",
            "TLE_LINE2": "2 00011  32.8717 137.5920 1462612 322.2928  28.3426 11.86983097397577"
        }]'''.getBytes('ISO-8859-1')

}
