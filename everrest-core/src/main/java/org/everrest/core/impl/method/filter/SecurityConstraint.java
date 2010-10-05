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
package org.everrest.core.impl.method.filter;

import org.everrest.core.Filter;
import org.everrest.core.impl.ApplicationContextImpl;
import org.everrest.core.method.MethodInvokerFilter;
import org.everrest.core.resource.GenericMethodResource;

import java.lang.annotation.Annotation;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Contract of this class thats constrains access to the resource method that
 * use JSR-250 security common annotations. See also https://jsr250.dev.java.net
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id$
 */
@Filter
public class SecurityConstraint implements MethodInvokerFilter
{

   /**
    * Check does <tt>method</tt> contains one on of security annotations
    * PermitAll, DenyAll, RolesAllowed.
    *
    * @see PermitAll
    * @see DenyAll
    * @see RolesAllowed {@inheritDoc}
    */
   public void accept(GenericMethodResource method) throws WebApplicationException
   {
      SecurityContext security = ApplicationContextImpl.getCurrent().getSecurityContext();
      checkSecurityConstraint(getSecurityAnnotation(method), security);
   }

   private void checkSecurityConstraint(Annotation sa, SecurityContext security)
   {
      if (sa != null)
      {
         Class<?> saclass = sa.annotationType();
         boolean allowed = false;
         if (saclass == PermitAll.class)
         {
            // all users allowed to call method
            allowed = true;
         }
         else if (saclass == RolesAllowed.class)
         {
            for (String role : ((RolesAllowed)sa).value())
            {
               if (security.isUserInRole(role))
               {
                  allowed = true;
                  break;
               }
            }
         }
         else if (saclass == DenyAll.class)
         {
            // nobody allowed to call method
         }
         if (!allowed)
         {
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
         }
      }

   }

   private Annotation getSecurityAnnotation(GenericMethodResource method)
   {
      for (Annotation a : method.getAnnotations())
      {
         Class<?> aclass = a.annotationType();
         if (aclass == PermitAll.class || aclass == DenyAll.class || aclass == RolesAllowed.class)
         {
            return a;
         }
      }
      return null;
   }

}