package funcional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import models.Meta;
import models.dao.GenericDAO;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import play.GlobalSettings;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.test.FakeApplication;
import play.test.Helpers;
import scala.Option;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {
	GenericDAO dao = new GenericDAO();
    List<Meta> metas;
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
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
    
    @Test
    public void renderTemplate() {
    	metas = dao.findAllByClass(Meta.class);
        Content html = views.html.index.render(metas,metasCump,metasNaoCump);
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("0 meta(s)");
    }
    
    @After
    public void tearDown() {
        em.getTransaction().commit();
        JPA.bindForCurrentThread(null);
        em.close();
        controllers.Application.setMetasCump(0);
        controllers.Application.setMetasNaoCump(0);
    }
}
