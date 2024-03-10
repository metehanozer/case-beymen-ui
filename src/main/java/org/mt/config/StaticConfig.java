package org.mt.config;

/**
 * Environment bağımısz konfigürasyon için properties dosyası yerine java sabitleri kullanılıyor.
 * Proje her test koşumunda derlendiğinden properties dosyası kullanmanın bir anlamı yok.
 */
public final class StaticConfig {

    public static final class Path {
        public static final String TEST_DATA_PATH = System.getProperty("user.dir") + "/src/test/resources/data/";
    }

    public static final class Url {
        public static final String HOME_PAGE = "https://www.beymen.com/";
    }

    public static final class Selenium {
        public static final int TIMEOUT = 15;
        public static final int POLLING = 150;
    }
}

