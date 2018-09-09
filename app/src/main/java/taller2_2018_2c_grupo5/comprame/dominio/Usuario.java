package taller2_2018_2c_grupo5.comprame.dominio;

public class Usuario {

    private final String nombreUsuario;
    private final String nombre;
    private final String apellido;
    private final String password;
    private final String email;

    public Usuario(String nombreUsuario, String nombre, String apellido, String password, String email) {
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getPassword() {
        return password;
    }
}
