package ar.org.centro35.colegio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import ar.org.centro35.colegio.entities.Alumno;
import ar.org.centro35.colegio.repositories.AlumnoRepository;
import ar.org.centro35.colegio.repositories.CursoRepository;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AlumnoController {

    private String mensaje="Ingrese un nuevo alumno!";
    private AlumnoRepository alumnoRepository=new AlumnoRepository();
    private CursoRepository cursoRepository=new CursoRepository();

    @GetMapping("/alumnos")
    public String getAlumnos(Model model,@RequestParam(name="buscar", defaultValue="")String buscar) {
        Alumno alumno=new Alumno();
        alumno.setEdad(18);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("alumno", alumno);
        model.addAttribute(("cursos"), cursoRepository.getAll());
        model.addAttribute("likeNombre", alumnoRepository.getLikeNombre(buscar));
        return "alumnos";
    }

    @PostMapping("/alumnosSave")
    public String alumnosSave(@ModelAttribute Alumno alumno) {
        //System.out.println("**************************************************");
        //System.out.println(alumno);
        //System.out.println("**************************************************");
        alumnoRepository.save(alumno);
        if(alumno.getId()>0){
            mensaje="Se guardo el alumno id "+alumno.getId();
        }else{
            mensaje="No se guardo el alumno!";
        }
        return "redirect:alumnos";
    }
}
