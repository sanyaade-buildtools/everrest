/*
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.everrest.core.impl.provider;

import org.everrest.core.impl.BaseTest;
import org.everrest.core.impl.ContainerResponse;
import org.everrest.core.impl.MultivaluedMapImpl;
import org.everrest.core.tools.ByteArrayContainerResponseWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id$
 */
public class FormEntityTest extends BaseTest {

    public void setUp() throws Exception {
        super.setUp();
    }

    @Path("/")
    public static class Resource1 {
        @POST
        @Path("a")
        @Consumes("application/x-www-form-urlencoded")
        public void m1(@FormParam("foo") String foo, @FormParam("bar") String bar, MultivaluedMap<String, String> form) {
            assertEquals(foo, form.getFirst("foo"));
            assertEquals(bar, form.getFirst("bar"));
        }

        @POST
        @Path("b")
        @Consumes("application/x-www-form-urlencoded")
        public void m2(MultivaluedMap<String, String> form) {
            assertEquals("to be or not to be", form.getFirst("foo"));
            assertEquals("hello world", form.getFirst("bar"));
        }
    }

    @Path("/")
    public static class Resource11 {
        @GET
        @Path("a")
        @Produces("application/x-www-form-urlencoded")
        public MultivaluedMap<String, String> m1() {
            MultivaluedMap<String, String> m = new MultivaluedMapImpl();
            m.putSingle("foo", "bar");
            return m;
        }
    }

    public void testFormEntityRead() throws Exception {
        Resource1 r1 = new Resource1();
        registry(r1);
        byte[] data = "foo=to%20be%20or%20not%20to%20be&bar=hello%20world".getBytes("UTF-8");
        MultivaluedMap<String, String> h = new MultivaluedMapImpl();
        h.putSingle("content-type", "application/x-www-form-urlencoded");
        h.putSingle("content-length", "" + data.length);
        assertEquals(204, launcher.service("POST", "/a", "", h, data, null).getStatus());
        assertEquals(204, launcher.service("POST", "/b", "", h, data, null).getStatus());
        unregistry(r1);
    }

    public void testFormEntityWrite() throws Exception {
        Resource11 r1 = new Resource11();
        registry(r1);
        MultivaluedMap<String, String> h = new MultivaluedMapImpl();
        h.putSingle("accept", "application/x-www-form-urlencoded");
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        ContainerResponse response = launcher.service("GET", "/a", "", h, null, writer, null);
        assertEquals(200, response.getStatus());
        //System.out.println(new String(writer.getBody()));
        assertEquals("foo=bar", new String(writer.getBody()));
        unregistry(r1);
    }
}
