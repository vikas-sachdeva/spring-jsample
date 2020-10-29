package spring.jsample.webflux

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'DELETE'
        url($(c(regex('/deleteApp/(.+)')), p('/deleteApp/2')))
    }
    response {
        status NO_CONTENT()
    }
}