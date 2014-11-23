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
	private static Form<Meta> bookForm = Form.form(Meta.class);
	
	@Transactional
    public static Result index() {
		List<Meta> metas = dao.findAllByClass(Meta.class);
		Collections.sort(metas);
        return ok(views.html.index.render(metas));
    }
	
	@Transactional
	public static Result newMeta() {
		Form<Meta> filledForm = bookForm.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Meta> result = dao.findAllByClass(Meta.class);
			return badRequest(views.html.index.render(result));
		} else {
            Meta novoMeta = filledForm.get();
            Logger.debug("Criando Meta: " + filledForm.data().toString() + " como " + novoMeta.getDescricao());

			dao.persist(novoMeta);

			dao.flush();
            
			return redirect(routes.Application.index());
		}
	}
}
