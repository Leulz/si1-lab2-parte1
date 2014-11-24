package unidade;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import models.Meta;
import models.Semana;
import models.dao.GenericDAO;

import org.junit.Test;

import base.AbstractTest;
import play.libs.F.Callback;
import play.test.TestBrowser;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class MetasTest extends AbstractTest{
	Meta meta1 = new Meta("Aprender Play",2,1);
	Meta meta2 = new Meta("Aprender AJAX",1,2);
    GenericDAO dao = new GenericDAO();
    List<Meta> metas;
	
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
}
