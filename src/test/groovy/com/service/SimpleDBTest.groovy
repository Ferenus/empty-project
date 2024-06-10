package com.service

import spock.lang.Specification

class SimpleDBTest extends Specification {
    def "integration test #1"() {
        given:
        SimpleDB db = new SimpleDB()

        when:
        db.set("a", 10)

        then:
        db.get("a") == 10

        when:
        db.delete("a")

        then:
        db.get("a") == null
    }

    def "integration test #2"() {
        given:
        SimpleDB db = new SimpleDB()

        when:
        db.set("a", 10)
        db.set("b", 10)

        then:
        db.count(10) == 2
        db.count(20) == 0

        when:
        db.delete("a")

        then:
        db.count(10) == 1

        when:
        db.set("b", 30)

        then:
        db.count(10) == 0
    }

    def "integration test #3"() {
        given:
        SimpleDB db = new SimpleDB()

        when:
        db.begin()
        db.set("a", 10)

        then:
        db.get("a") == 10

        when:
        db.begin()
        db.set("a", 20)

        then:
        db.get("a") == 20

        when:
        db.rollback()

        then:
        db.get("a") == 10

        when:
        db.rollback()

        then:
        db.get("a") == null
    }

    def "integration test #4"() {
        given:
        SimpleDB db = new SimpleDB()

        when:
        db.begin()
        db.set("a", 30)
        db.begin()
        db.set("a", 40)
        db.commit()

        then:
        db.get("a") == 40
        !db.rollback()
    }

    def "integration test #5"() {
        given:
        SimpleDB db = new SimpleDB()

        when:
        db.set("a", 50)
        db.begin()

        then:
        db.get("a") == 50

        when:
        db.set("a", 60)
        db.begin()
        db.delete("a")

        then:
        db.get("a") == null

        when:
        db.rollback()

        then:
        db.get("a") == 60

        when:
        db.commit()

        then:
        db.get("a") == 60
    }

    def "integration test #6"() {
        given:
        SimpleDB db = new SimpleDB()

        when:
        db.set("a", 10)
        db.begin()

        then:
        db.count(10) == 1

        when:
        db.begin()
        db.delete("a")

        then:
        db.count(10) == 0

        when:
        db.rollback()

        then:
        db.count(10) == 1
    }
}
