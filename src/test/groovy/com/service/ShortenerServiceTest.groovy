package com.service

import spock.lang.Specification

class ShortenerServiceTest extends Specification {
    def "shorten the url"() {
        given:
        ShortenerService service = new ShortenerService()

        when:
        String result = service.shorten("http://looooong.com/somepath", "MY-NEW-PATH")

        then:
        result == "http://short.com/MY-NEW-PATH"
    }

    def "shorten the url without keyword"() {
        given:
        ShortenerService service = new ShortenerService()

        when:
        String result = service.shorten("http://looooong.com/somepath")

        then:
        result.length() == 21
    }

    def "keyword is already reserved"() {
        given:
        ShortenerService service = new ShortenerService()
        service.shorten("http://long.com/somepath", "MY-NEW-PATH")

        when:
        service.shorten("http://loooooooong.com/somepath", "MY-NEW-PATH")

        then:
        thrown(IllegalArgumentException)
    }

    def "url is not valid"() {
        given:
        ShortenerService service = new ShortenerService()

        when:
        service.shorten("http://loooooooong.com/somepath", "MY-NEW-PATHMY-NEW-PATHMY-NEW-PATHMY-NEW-PATH")

        then:
        thrown(IllegalArgumentException)
    }

    def "keyword is too long"() {
        given:
        ShortenerService service = new ShortenerService()

        when:
        service.shorten("!@#%^&*()", "MY-NEW-PATH")

        then:
        thrown(IllegalArgumentException)
    }


}
