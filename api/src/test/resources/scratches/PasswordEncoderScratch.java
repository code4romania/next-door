import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Used to generate the encoded passwords for the SQL scripts.
 */
class PasswordEncoderScratch {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("pass"));
    }
}