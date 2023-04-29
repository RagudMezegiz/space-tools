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
package com.github.ragudmezegiz.space.catalog

import groovy.sql.Sql

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant

import spock.lang.Specification

/* groovylint-disable MethodName, MethodReturnTypeRequired */

/** Test of class Catalog. */
class CatalogTest extends Specification {

    def "update inserts if not present"() {
        given:
        Sql sql = Sql.newInstance('jdbc:sqlite::memory:')
        Catalog cat = new Catalog(sql)

        when:
        def inserted = cat.updateElement(VANGUARD_1)
        def count = cat.elementCount()

        then:
        inserted
        count == 1
    }

    def "update updates if present"() {
        given:
        Sql sql = Sql.newInstance('jdbc:sqlite::memory:')
        Catalog cat = new Catalog(sql)
        cat.updateElement(VANGUARD_1)

        when:
        def inserted = cat.updateElement(VANGUARD_1_X)
        def count = cat.elementCount()

        then:
        !inserted
        count == 1
    }

    /* groovylint-disable-next-line StaticDateFormatField, StaticSimpleDateFormatField */
    private static final DateFormat FMT = new SimpleDateFormat('yyyy-MM-dd', Locale.US)

    private static final Map VANGUARD_1 = [
        CCSDS_OMM_VERS: 2.0,
        COMMENT: 'GENERATED VIA SPACE-TRACK.ORG API',
        CREATION_DATE: Date.from(Instant.parse('2023-04-16T04:30:29Z')),
        ORIGINATOR: '18 SPCS',
        OBJECT_NAME: 'VANGUARD 1',
        OBJECT_ID: '1958-002B',
        CENTER_NAME: 'EARTH',
        REF_FRAME: 'TEME',
        TIME_SYSTEM: 'UTC',
        MEAN_ELEMENT_THEORY: 'SGP4',
        EPOCH: Date.from(Instant.parse('2023-04-15T19:02:29.925024Z')),
        MEAN_MOTION: 10.85079051,
        ECCENTRICITY: 0.18468760,
        INCLINATION: 34.2555,
        RA_OF_ASC_NODE: 287.6260,
        ARG_OF_PERICENTER: 318.6071,
        MEAN_ANOMALY: 28.8080,
        EPHEMERIS_TYPE: 0,
        CLASSIFICATION_TYPE: 'U',
        NORAD_CAT_ID: 5,
        ELEMENT_SET_NO: 999,
        REV_AT_EPOCH: 31738,
        BSTAR: 0.00012282000000,
        MEAN_MOTION_DOT: 0.00000104,
        MEAN_MOTION_DDOT: 0.0000000000000,
        SEMIMAJOR_AXIS: 8618.419,
        PERIOD: 132.709,
        APOAPSIS: 3831.999,
        PERIAPSIS: 648.569,
        OBJECT_TYPE: 'PAYLOAD',
        RCS_SIZE: 'SMALL',
        COUNTRY_CODE: 'US',
        LAUNCH_DATE: FMT.parse('1958-03-17'),
        SITE: 'AFETR',
        DECAY_DATE: null,
        FILE: 3888171,
        GP_ID: 230108008,
        TLE_LINE0: '0 VANGUARD 1',
        TLE_LINE1: '1 00005U 58002B   23105.79340191  .00000104  00000-0  12282-3 0  9998',
        TLE_LINE2: '2 00005  34.2555 287.6260 1846876 318.6071  28.8080 10.85079051317382'
    ]

    private static final Map VANGUARD_1_X = [
        CCSDS_OMM_VERS: 2.0,
        COMMENT: 'GENERATED VIA SPACE-TRACK.ORG API',
        CREATION_DATE: Date.from(Instant.parse('2023-04-16T04:30:29Z')),
        ORIGINATOR: '18 SPCS',
        OBJECT_NAME: 'VANGUARD 1X',
        OBJECT_ID: '1958-002B',
        CENTER_NAME: 'EARTH',
        REF_FRAME: 'TEME',
        TIME_SYSTEM: 'UTC',
        MEAN_ELEMENT_THEORY: 'SGP4',
        EPOCH: Date.from(Instant.parse('2023-04-15T19:02:29.925024Z')),
        MEAN_MOTION: 10.85079051,
        ECCENTRICITY: 0.18468760,
        INCLINATION: 34.2555,
        RA_OF_ASC_NODE: 287.6260,
        ARG_OF_PERICENTER: 318.6071,
        MEAN_ANOMALY: 28.8080,
        EPHEMERIS_TYPE: 0,
        CLASSIFICATION_TYPE: 'U',
        NORAD_CAT_ID: 5,
        ELEMENT_SET_NO: 999,
        REV_AT_EPOCH: 31738,
        BSTAR: 0.00012282000000,
        MEAN_MOTION_DOT: 0.00000104,
        MEAN_MOTION_DDOT: 0.0000000000000,
        SEMIMAJOR_AXIS: 8618.419,
        PERIOD: 132.709,
        APOAPSIS: 3831.999,
        PERIAPSIS: 648.569,
        OBJECT_TYPE: 'PAYLOAD',
        RCS_SIZE: 'SMALL',
        COUNTRY_CODE: 'US',
        LAUNCH_DATE: FMT.parse('1958-03-17'),
        SITE: 'AFETR',
        DECAY_DATE: null,
        FILE: 3888171,
        GP_ID: 230108008,
        TLE_LINE0: '0 VANGUARD 1X',
        TLE_LINE1: '1 00005U 58002B   23105.79340191  .00000104  00000-0  12282-3 0  9998',
        TLE_LINE2: '2 00005  34.2555 287.6260 1846876 318.6071  28.8080 10.85079051317382'
    ]

}
