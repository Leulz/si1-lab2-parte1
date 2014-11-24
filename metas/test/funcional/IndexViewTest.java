package funcional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Meta;
import models.dao.GenericDAO;

import org.junit.Before;
import org.junit.Test;

import base.AbstractTest;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.index;

public class IndexViewTest extends AbstractTest{
	GenericDAO dao = new GenericDAO();
	List<Meta> metas;
	Meta meta1 = new Meta("Aprender Play",1,1);
	Meta meta2 = new Meta("Aprender AJAX", 1,2);
	Meta meta3 = new Meta("Aprender filosofia schopenhaueriana",2,1);
		
	@Test
	public void deveAparecerMetaAdicionada() {
		dao.persist(meta1);
		metas = dao.findAllByClass(Meta.class);
		
		Html html = index.render(metas);
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains("Aprender Play");		
	}
    
    @Test
    public void deveAparecerSemanaCorretamente1() {
    	dao.persist(meta1);
    	dao.persist(meta2);
    	
		metas = dao.findAllByClass(Meta.class);
		
		Html html = index.render(metas);
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains("Semana 1");
		assertThat(contentAsString(html)).contains("Aprender AJAX");
		assertThat(contentAsString(html)).contains("Aprender Play");
		assertThat(contentAsString(html)).doesNotContain("Aprender filosofia schopenhaueriana");
    }
    
    @Test
    public void deveAparecerSemanaCorretamente2() {
    	dao.persist(meta3);
    	dao.persist(meta1);
    	dao.persist(meta2);
    	
		metas = dao.findAllByClass(Meta.class);
		Collections.sort(metas);
		
		Html html = index.render(metas);
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains("Semana 1");
		assertThat(contentAsString(html)).contains("Semana 2");
		assertThat(metas.get(2).getSemana()).isEqualTo(2);
		assertThat(contentAsString(html)).contains("Aprender AJAX");
		assertThat(contentAsString(html)).contains("Aprender Play");
		assertThat(contentAsString(html)).contains("Aprender filosofia schopenhaueriana");
    }
    
    @Test
    public void deveCadastrarMeta(){
    	Map<String, String> formData = new HashMap<String, String>();
		formData.put("descricao", "Aprender Play");
		formData.put("semana", "3");
		formData.put("prioridade", "2");
		Result result = callAction(controllers.routes.ref.Application.newMeta(), fakeRequest()
						.withFormUrlEncodedBody(formData));
		List<Meta> metas = dao.findAllByClass(Meta.class);
		assertThat(metas.size()).isEqualTo(1);
        assertThat(metas.get(0).getDescricao()).isEqualTo("Aprender Play");
		List<Meta> result2 = dao.findByAttributeName("Meta", 
				"descricao", "Aprender Play");	
		assertThat(result2.size()).isEqualTo(1);
		assertThat(result2.get(0).getSemana()).isEqualTo(3);
		assertThat(result2.get(0).getPrioridade()).isEqualTo(2);
    }
    
    @Test
    public void deveCumprirMeta(){
    	//TODO
    }
    
    @Test
    public void deveDeletarMeta(){
    	//TODO
    	Map<String, String> formData = new HashMap<String, String>();
		formData.put("id", "1");
		dao.persist(meta1);
		List<Meta> metas = dao.findAllByClass(Meta.class);	
		Result result = callAction(controllers.routes.ref.Application.deleteMeta(), fakeRequest()
						.withFormUrlEncodedBody(formData));
		assertThat(metas.size()).isEqualTo(0);
    }
}
