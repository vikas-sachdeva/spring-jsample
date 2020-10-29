package spring.jsample.webflux

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/addApp'
        body([
                "name"   : $(c(optional(nonEmpty())), p('application-4')),
                "running": $(c(optional(anyBoolean())), p(true))
        ])
        headers {
            contentType('application/json')
        }
    }
    response {
        status OK()
        body([
                "name"                : $(c(fromRequest().body('$.name')), p('application-4')),
                "running"             : $(c(fromRequest().body('$.running')), p(true)),
                "id"                  : $(anyHex()),
                "createdDateTime"     : $(regex('([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.\\d{2,9})')),
                "lastModifiedDateTime": $(regex('([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.\\d{2,9})')),
                "version"             : $(0)
        ])
    }
}