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
import groovy.transform.PackageScope

/**
 * Space element catalog.
 */
class Catalog implements AutoCloseable {

    private static final String EL_CREATE = '''
        CREATE TABLE IF NOT EXISTS elements (
            NORAD_CAT_ID INTEGER PRIMARY KEY,
            OBJECT_NAME TEXT,
            TLE_LINE0 TEXT,
            TLE_LINE1 TEXT,
            TLE_LINE2 TEXT
        )
    '''

    private static final String EL_COUNT = '''
        SELECT COUNT(*) AS count FROM elements
    '''

    private static final String EL_COUNT_IF = '''
        SELECT COUNT(*) AS count FROM elements WHERE
    '''

    private static final String EL_INSERT = '''
        INSERT INTO elements VALUES (
            :NORAD_CAT_ID,
            :OBJECT_NAME,
            :TLE_LINE0,
            :TLE_LINE1,
            :TLE_LINE2
        )
    '''

    private static final String EL_UPDATE = '''
        UPDATE elements SET
            OBJECT_NAME = :OBJECT_NAME,
            TLE_LINE0 = :TLE_LINE0,
            TLE_LINE1 = :TLE_LINE1,
            TLE_LINE2 = :TLE_LINE2
        WHERE
    '''

    private final Sql sql

    Catalog(String dbName) {
        this(Sql.newInstance("jdbc:sqlite:$dbName"))
    }

    @PackageScope
    Catalog(Sql sql) {
        this.sql = sql

        // Create element set table if it doesn't already exist
        sql.execute(EL_CREATE)
    }

    /**
     * Close the catalog.
     */
    void close() {
        sql.close()
    }

    /**
     * Return the count of element sets in the catalog.
     *
     * @return element set count
     */
    int elementCount() {
        return sql.firstRow(EL_COUNT).count
    }

    /**
     * Update an existing catalog element. Insert the element if it is not
     * already in the catalog.
     *
     * @param el catalog element
     * @return true if element was inserted, false if already present and updated
     */
    boolean updateElement(Map el) {
        def present = sql.firstRow(EL_COUNT_IF +
            "norad_cat_id = ${el.NORAD_CAT_ID}").count > 0

        if (present) {
            sql.executeUpdate(el, EL_UPDATE + "norad_cat_id = ${el.NORAD_CAT_ID}")
        } else {
            sql.executeInsert(el, EL_INSERT)
        }
        return !present
    }

}
