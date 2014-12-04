package unidade;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import models.Meta;
import models.dao.GenericDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.GlobalSettings;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.libs.F.Callback;
import play.test.FakeApplication;
import play.test.Helpers;
import play.test.TestBrowser;
import scala.Option;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class MetasTest {
	Meta meta1 = new Meta("Aprender Play",2,1);
	Meta meta2 = new Meta("Aprender AJAX",1,2);
    GenericDAO dao = new GenericDAO();
    List<Meta> metas;
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
	public void deveIniciarSemMeta() {
		metas = dao.findAllByClass(Meta.class);
		assertThat(metas.size()).isEqualTo(0);
	}
    
    @Test
    public void deveAdicionarMeta(){
    	dao.persist(meta1);
    	metas = dao.findAllByClass(Meta.class);
    	assertThat(metas.size()).isEqualTo(1);
    	assertThat(metas.get(0).getDescricao()).isEqualTo("Aprender Play");
    }
    
    @Test
    public void deveOrdenarMetasPorSemana(){
    	dao.persist(meta1);
    	dao.persist(meta2);
    	metas = dao.findAllByClass(Meta.class);
    	assertThat(metas.get(0).getDescricao()).isEqualTo("Aprender Play");
    	Collections.sort(metas);
    	assertThat(metas.get(0).getDescricao()).isEqualTo("Aprender AJAX");	
    }
    
    @Test
    public void deveConseguirCumprirMeta(){
    	dao.persist(meta1);
    	metas = dao.findAllByClass(Meta.class);
    	assertThat(metas.get(0).isCumprida()).isFalse();
    	metas.get(0).setCumprida(true);
    	assertThat(metas.get(0).isCumprida()).isTrue();
    }
    
    @After
    public void tearDown() {
        em.getTransaction().commit();
        JPA.bindForCurrentThread(null);
        em.close();
    }
}
