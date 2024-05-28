package ar.org.centro35.colegio.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ar.org.centro35.colegio.connectors.Connector;
import ar.org.centro35.colegio.entities.Alumno;

public class AlumnoRepository {

    private Connection conn = Connector.getConnection();

    public void save(Alumno alumno) {
        if (alumno == null)
            return;

        // No hacer esto - es obsoleto e inseguro
        // String query="insert into alumnos (nombre,apellido,edad,idCurso) values
        // ('"+alumno.getNombre()+
        // "','"+alumno.getApellido()+"',"+alumno.getEdad()+","+alumno.getIdCurso()+")";
        // try (Statement st=conn.createStatement()){
        // st.execute(query);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // carlos rios 51 3
        // Ataque SQL injection
        // x x',18,3); delete from alumnos;-- 18 3
        // insert into alumnos (nombre,apellido,edad,idCurso) values ('x','x',18,3);
        // delete from alumnos;-- ',16,3);

        try (PreparedStatement ps = conn.prepareStatement(
                "insert into alumnos (nombre, apellido, edad, idCurso) values (?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido());
            ps.setInt(3, alumno.getEdad());
            ps.setInt(4, alumno.getIdCurso());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
                alumno.setId(rs.getInt(1));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void remove(Alumno alumno) {
        if (alumno == null)
            return;
        try (PreparedStatement ps = conn.prepareStatement("delete from alumnos where id=?")) {
            ps.setInt(1, alumno.getId());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Alumno> getAll() {
        List<Alumno> list = new ArrayList();
        try (ResultSet rs = conn.createStatement().executeQuery("select * from alumnos")) {
            while (rs.next()) {
                list.add(new Alumno(
                        rs.getInt("id"), // id
                        rs.getString("nombre"), // nombre
                        rs.getString("apellido"), // apellido
                        rs.getInt("edad"), // edad
                        rs.getInt("idCurso") // curso
                ));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public Alumno getById(int id) {
        return getAll()
                .stream()
                .filter(alumno -> alumno.getId() == id)
                .findAny()
                .orElse(new Alumno());
    }

    public List<Alumno> getLikeNombre(String nombre) {
        if (nombre == null)
            return new ArrayList();
        return getAll()
                .stream()
                .filter(alumno -> alumno.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

}