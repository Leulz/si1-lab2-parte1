package controllers;

import java.util.Collections;
import java.util.List;

import models.Meta;
import models.dao.GenericDAO;
import play.*;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	private static final GenericDAO dao = new GenericDAO();
	private static Form<Meta> metaForm = Form.form(Meta.class);
	
	@Transactional
    public static Result index() {
		List<Meta> metas = dao.findAllByClass(Meta.class);
		Collections.sort(metas);
        return ok(views.html.index.render(metas));
    }
	
	@Transactional
	public static Result newMeta() {
		Form<Meta> filledForm = metaForm.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Meta> result = dao.findAllByClass(Meta.class);
			return badRequest(views.html.index.render(result));
		} else {
            Meta novoMeta = filledForm.get();
            
            dao.persist(novoMeta);

			dao.flush();
			
			Logger.debug("Criando Meta: " + filledForm.data().toString() + " como " + novoMeta.getDescricao() + " ID: "+novoMeta.getId());
            
			return redirect(routes.Application.index());
		}
	}
	
	@Transactional
	public static Result cumprirMeta(){
		Form<Meta> filledForm = metaForm.bindFromRequest();
		
		if (filledForm.hasErrors()) {
			List<Meta> result = dao.findAllByClass(Meta.class);
			return badRequest(views.html.index.render(result));
		} else{
			long id = Long.parseLong(filledForm.data().get("id"));
			Meta meta = dao.findByEntityId(Meta.class, id);
			Logger.debug("Meta achada: " + meta.getDescricao());
			meta.setCumprida(true);
			return redirect(routes.Application.index());
		}
	}
	
	@Transactional
	public static Result deleteMeta(){
		Form<Meta> filledForm = metaForm.bindFromRequest();
		
		if (filledForm.hasErrors()) {
			List<Meta> result = dao.findAllByClass(Meta.class);
			return badRequest(views.html.index.render(result));
		} else{
			List<Meta> metas = dao.findAllByClass(Meta.class);
			if (metas.size()==0)
				return redirect(routes.Application.index());
			Long id = Long.parseLong(filledForm.data().get("id"));
			dao.removeById(Meta.class, id);;
			return redirect(routes.Application.index());
		}
	}
}
