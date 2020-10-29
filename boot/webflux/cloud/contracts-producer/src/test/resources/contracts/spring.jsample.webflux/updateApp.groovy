package spring.jsample.webflux

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'PUT'
        url($(c(regex('/updateApp/(.+)')), p('/updateApp/3')))
        body([
                "name"   : $(c(optional(nonEmpty())), p('new name')),
                "running": $(c(optional(anyBoolean())), p(false))

        ])
        headers {
            contentType('application/json')
        }
    }
    response {
        status OK()
        body([
                "name"                : $(c(fromRequest().body('$.name')), p('new name')),
                "running"             : $(c(fromRequest().body('$.running')), p(false)),
                "id"                  : $(c(fromRequest().path(1)), p('3')),
                "createdDateTime"     : $(regex('([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.\\d{2,9})')),
                "lastModifiedDateTime": $(regex('([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.\\d{2,9})')),
                "version"             : $(c(anyPositiveInt()), p('1')),
        ])
        headers {
            contentType('application/json')
        }
    }
}