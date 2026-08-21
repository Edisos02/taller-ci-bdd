package cl.empresa.demo;

public class LoginService {

    public boolean autenticar(String usuario, String clave) {
        return "admin".equals(usuario) && "1234".equals(clave);
    }
}