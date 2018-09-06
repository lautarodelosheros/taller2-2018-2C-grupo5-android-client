package taller2_2018_2c_grupo5.comprame.dominio;

import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioFactory {

    public static Usuario fromJSONObject(JSONObject jsonObject) throws JSONException {
        Usuario usuario = new Usuario();
        usuario.setNombre(jsonObject.getString("nombre"));
        usuario.setApellido(jsonObject.getString("apellido"));
        usuario.setEmail(jsonObject.getString("email"));
        return usuario;
    }

    public static JSONObject toJSONObject (Usuario usuario) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", usuario.getNombre());
            jsonObject.put("apellido", usuario.getApellido());
            jsonObject.put("email", usuario.getEmail());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
