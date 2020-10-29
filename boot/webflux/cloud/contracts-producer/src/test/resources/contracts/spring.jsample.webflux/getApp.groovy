package spring.jsample.webflux

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url($(c(regex('/getApp/(.+)')), p('/getApp/2')))
    }

    response {
        status OK()
        body([
                "id"                  : $(c(fromRequest().path(1)), p('2')),
                "name"                : $(anyNonEmptyString()),
                "running"             : $(anyBoolean()),
                "createdDateTime"     : $(regex('([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.\\d{2,9})')),
                "lastModifiedDateTime": $(regex('([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.\\d{2,9})')),
                "version"             : $(0)
        ])
        headers {
            contentType('application/json')
        }
    }
}