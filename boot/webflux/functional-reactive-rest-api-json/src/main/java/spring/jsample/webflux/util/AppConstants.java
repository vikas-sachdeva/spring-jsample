package spring.jsample.webflux.util;

public class AppConstants {

    public static final class URI {

        public static final String GET_APPS = "/getApps";

        public static final String GET_APP = "/getApp";

        public static final String GET_APP_BY_ID = GET_APP + "/{" + REQ_PARAM.ID + "}";

        public static final String ADD_APP = "/addApp";

        public static final String UPDATE_APP = "/updateApp/{id}";

        public static final String DELETE_APP = "/deleteApp/{id}";

    }

    public static final class REQ_PARAM {

        public static final String ID = "id";

    }
}
