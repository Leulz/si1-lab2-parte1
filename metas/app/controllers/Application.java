package controllers;

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
        return ok(views.html.index.render(metas));
    }

}
