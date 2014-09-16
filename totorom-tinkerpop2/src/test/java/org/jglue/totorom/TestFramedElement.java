package org.jglue.totorom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
/**
 * @author Bryn Cooke (http://jglue.org)
 */

public class TestFramedElement {

	
	private FramedGraph fg;
	private Person p1;
	private Person p2;
	private Knows e1;
    
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Graph g = new TinkerGraph();
        fg = new FramedGraph(g);
        p1 = fg.addVertex(Person.class);
        p2 = fg.addVertex(Person.class);
        p1.setName("Bryn");
        p2.setName("Julia");
        e1 = p1.addKnows(p2);
        e1.setYears(15);

	}
	
    @Test
    public void testGetId() {
    	Assert.assertEquals("0", p1.getId());
        
    }
    
    @Test
    public void testGetPropertyKeys() {
    	Assert.assertEquals(Sets.newHashSet("name"), p1.getPropertyKeys());
    }
    
    @Test
    public void testGetProperty() {
    	Assert.assertEquals("Bryn", p1.getProperty("name"));
    }
    
    @Test
    public void testSetProperty() {
    	p1.setProperty("name", "Bryn Cooke");
    	Assert.assertEquals("Bryn Cooke", p1.getProperty("name"));
    }
    
    @Test
    public void testSetPropertyNull() {
    	p1.setProperty("name", null);
    	Assert.assertNull(p1.getProperty("name"));
    }
    
    
    @Test
    public void testV() {
    	Assert.assertEquals(2, p1.V().count());
    }
    
    @Test
    public void testE() {
    	Assert.assertEquals(1, p1.E().count());
    }
    
    @Test
    public void testv() {
    	Assert.assertEquals(p1, p1.v(p1.getId()).next(Person.class));
    }
    
    @Test
    public void teste() {
    	Assert.assertEquals(e1, p1.e(e1.getId()).next(Knows.class));
    }
    
    @Test
    public void testRemove() {
    	p1.remove();
    	Assert.assertEquals(1, p1.V().count());
    }
    
}
