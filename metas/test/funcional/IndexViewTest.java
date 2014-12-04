package funcional;

import static org.fest.assertions.Assertions.assertThat;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.GlobalSettings;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.Helpers;
import play.twirl.api.Html;
import scala.Option;
import views.html.index;

import javax.persistence.EntityManager;




public class IndexViewTest {
	GenericDAO dao = new GenericDAO();
	List<Meta> metas;
	Meta meta1 = new Meta("Aprender Play",1,1);
	Meta meta2 = new Meta("Aprender AJAX", 1,2);
	Meta meta3 = new Meta("Aprender filosofia schopenhaueriana",2,1);
	int metasCump = controllers.Application.getMetasCump();
	int metasNaoCump = controllers.Application.getMetasNaoCump();
    public EntityManager em;

	@Before
    public void setUp() {
        FakeApplication app = Helpers.fakeApplication(new GlobalSettings());
        Helpers.start(app);
        Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(JPAPlugin.class);
        em = jpaPlugin.get().em("default");
        JPA.bindForCurrentThread(em);
        em.getTransaction().begin();
    }
	
	@Test
	public void deveAparecerMetaAdicionada() {
		dao.persist(meta1);
		metas = dao.findAllByClass(Meta.class);
		
		Html html = index.render(metas,metasCump,metasNaoCump);
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains("Aprender Play");		
	}
    
    @Test
    public void deveAparecerSemanaCorretamente1() {
    	dao.persist(meta1);
    	dao.persist(meta2);
    	
		metas = dao.findAllByClass(Meta.class);
		
		Html html = index.render(metas,metasCump,metasNaoCump);
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
		
		Html html = index.render(metas,metasCump,metasNaoCump);
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
    public void deveFazerComQueMetaDeletadaSuma() {
    	Map<String, String> formData = new HashMap<String, String>();
		formData.put("descricao", "Aprender Play");
		formData.put("semana", "3");
		formData.put("prioridade", "2");
		Result result1 = callAction(controllers.routes.ref.Application.newMeta(), fakeRequest()
						.withFormUrlEncodedBody(formData));
		metas = dao.findAllByClass(Meta.class);
		assertThat(metas.size()==1).isTrue();
		Html html = index.render(metas,metasCump,metasNaoCump);
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains("Aprender Play");
		
		formData = new HashMap<String, String>();
		formData.put("id", "1");
		Result result2 = callAction(controllers.routes.ref.Application.deleteMeta(), fakeRequest()
				.withFormUrlEncodedBody(formData));
		
		metas = dao.findAllByClass(Meta.class);
		html = index.render(metas,metasCump,metasNaoCump);
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).doesNotContain("Aprender Play");		
    }
    
    @Test
    public void deveCumprirMeta(){
    	Map<String, String> formData = new HashMap<String, String>();
		formData.put("descricao", "Aprender Play");
		formData.put("semana", "3");
		formData.put("prioridade", "2");
		List<Meta> metas = dao.findAllByClass(Meta.class);
		assertThat(metas.size()).isEqualTo(0);
		Result result1 = callAction(controllers.routes.ref.Application.newMeta(), fakeRequest()
				.withFormUrlEncodedBody(formData));
		dao.flush();
		metas = dao.findAllByClass(Meta.class);
		assertThat(metas.size()).isEqualTo(1);
		assertThat(metas.get(0).isCumprida()).isFalse();
		formData = new HashMap<String, String>();
		formData.put("id", "1");
		long id = Long.parseLong("1");
		Meta meta = dao.findByEntityId(Meta.class, id);			
		meta.setCumprida(true);
		dao.merge(meta);
		dao.flush();
		assertThat(meta.isCumprida()).isTrue();
    }
    @Test
    public void deveDeletarMeta(){
    	Map<String, String> formData = new HashMap<String, String>();
		formData.put("descricao", "Aprender Play");
		formData.put("semana", "3");
		formData.put("prioridade", "2");
		List<Meta> metas = dao.findAllByClass(Meta.class);
		assertThat(metas.size()).isEqualTo(0);
		Result result1 = callAction(controllers.routes.ref.Application.newMeta(), fakeRequest()
				.withFormUrlEncodedBody(formData));
		dao.flush();
		metas = dao.findAllByClass(Meta.class);
		assertThat(metas.size()).isEqualTo(1);
		formData = new HashMap<String, String>();
		formData.put("id", "1");
		Result result = callAction(controllers.routes.ref.Application.deleteMeta(), fakeRequest()
				.withFormUrlEncodedBody(formData));
		dao.flush();
		metas = dao.findAllByClass(Meta.class);
		assertThat(metas.size()).isEqualTo(0);
    }
    
    @After
    public void tearDown() {
        em.getTransaction().commit();
        JPA.bindForCurrentThread(null);
        em.close();
    }
}
