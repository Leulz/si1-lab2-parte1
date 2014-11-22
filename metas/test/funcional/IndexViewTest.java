package funcional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;

import java.util.List;

import models.Meta;
import models.dao.GenericDAO;

import org.junit.Test;

import play.twirl.api.Html;
import views.html.index;
import base.AbstractTest;

public class IndexViewTest extends AbstractTest{
	GenericDAO dao = new GenericDAO();
	List<Meta> metas;
	Meta meta = new Meta("Aprender Play");
	
	@Test
	public void deveAparecerMetaAdicionada() {
		dao.persist(meta);
		metas = dao.findAllByClass(Meta.class);
		
		Html html = index.render(metas);
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains("Aprender Play");		
	}

}
