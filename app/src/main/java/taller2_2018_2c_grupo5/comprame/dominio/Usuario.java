package taller2_2018_2c_grupo5.comprame.dominio;

public class Usuario {

    private final String nombreUsuario;
    private final String password;
    private final String nombre;
    private final String apellido;
    private final String email;

    public Usuario(String nombreUsuario, String password, String nombre, String apellido, String email) {
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
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
