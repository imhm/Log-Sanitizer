import static org.junit.jupiter.api.Assertions.*;

class StringSanitizerTest {
    void main_valid_IPoutput() {
        StringSanitizer.stringSanitizer("115.76.36.47 - - [06/Oct/2021:00:44:53 -0700] \"POST "
                + "/empower/drive/ubiquitous/e-commerce HTTP/2.0\" 401 26695 \"https://www.humancollaborative"
                + ".name/robust/networks\" \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/5360 (KHTML, like Gecko) "
                + "Chrome/36.0.884.0 Mobile Safari/5360\"");
    }
}