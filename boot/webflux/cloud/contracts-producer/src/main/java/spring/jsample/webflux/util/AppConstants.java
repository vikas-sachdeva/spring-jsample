package spring.jsample.webflux.util;

public class AppConstants {

    public static final class URI {

        public static final String GET_APPS = "/getApps";

        public static final String GET_APP = "/getApp/{id}";

        public static final String GET_APPS_PAGE_WISE = "/getAppsPageWise";

        public static final String GET_APPS_HAVING_NAMES_PAGE_WISE = "/getAppsHavingNamesPageWise";

        public static final String GET_RUNNING_APPS_PAGE_WISE = "/getRunningAppsPageWise";

        public static final String GET_RUNNING_APPS_HAVING_NAMES_PAGE_WISE = "/getRunningAppsHavingNamesPageWise";

        public static final String ADD_APP = "/addApp";

        public static final String UPDATE_APP = "/updateApp/{id}";

        public static final String DELETE_APP = "/deleteApp/{id}";

    }

    public static final class REQ_PARAM {

        public static final String PAGE_NUMBER = "pageNumber";

        public static final String PAGE_SIZE = "pageSize";

        public static final String NAMES_LIST = "names";

    }
}
