package sklep.config;

public interface Constants {
    enum Role{
        ADMIN("ADMIN"), USER("USER");

        public final String name;

        Role(String name) {
            this.name = name;
        }
    }

    int COMMENT_CONTENT_MAX_LENGTH = 400;
    String ALLOWED_ORIGIN = "http://localhost:3000";


    int ONE_DAY_IN_MS = 86000000;
    int MAX_RATE_VALUE = 5;
    int MIN_RATE_VALUE = 1;
}
