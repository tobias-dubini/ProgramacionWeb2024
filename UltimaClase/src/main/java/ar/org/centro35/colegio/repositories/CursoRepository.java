package ar.org.centro35.colegio.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ar.org.centro35.colegio.connectors.Connector;
import ar.org.centro35.colegio.entities.Curso;
import ar.org.centro35.colegio.enums.Dia;
import ar.org.centro35.colegio.enums.Turno;

public class CursoRepository {

    private Connection conn = Connector.getConnection();

    public void save(Curso curso) {
        if (curso == null)
            return;
        try (PreparedStatement ps = conn.prepareStatement(
                "insert into cursos (titulo,profesor,dia,turno) values (?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, curso.getTitulo());
            ps.setString(2, curso.getProfesor());
            ps.setString(3, curso.getDia().toString());
            ps.setString(4, curso.getTurno().toString());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
                curso.setId(rs.getInt(1));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void remove(Curso curso) {
        if (curso == null)
            return;
        try (PreparedStatement ps = conn.prepareStatement("delete from cursos where id=?")) {
            ps.setInt(1, curso.getId());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Curso> getAll() {
        List<Curso> list = new ArrayList<>();
        try (ResultSet rs = conn.createStatement().executeQuery("select * from cursos")) {
            while (rs.next()) {
                list.add(new Curso(
                        rs.getInt("id"), // id
                        rs.getString("titulo"), // titulo
                        rs.getString("profesor"), // profesor
                        Dia.valueOf(rs.getString("dia")), // dia
                        Turno.valueOf(rs.getString("turno")) // turno
                ));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public Curso getById(int id) {
        return getAll()
                .stream()
                .filter(curso -> curso.getId() == id)
                .findAny()
                .orElse(new Curso());
    }

    public List<Curso> getLikeTitulo(String titulo) {
        if (titulo == null)
            return new ArrayList<>();
        return getAll()
                .stream()
                .filter(curso -> curso.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .toList();
    }

}
