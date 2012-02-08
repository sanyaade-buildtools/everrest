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
package org.everrest.core.impl;

import java.util.HashMap;

/**
 * Keeps objects from environment (e. g. servlet container) which can be passed
 * in resource. Parameter must be annotated by {@link javax.ws.rs.core.Context}.
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id$
 */
public class EnvironmentContext extends HashMap<Class<?>, Object>
{
   private static final long serialVersionUID = 5409617947238152318L;

   /** {@link ThreadLocal} EnvironmentContext. */
   private static ThreadLocal<EnvironmentContext> current = new ThreadLocal<EnvironmentContext>();

   /**
    * @return preset {@link ThreadLocal} EnvironmentContext
    * @see ThreadLocal
    */
   public static EnvironmentContext getCurrent()
   {
      return current.get();
   }

   /**
    * @param env set {@link ThreadLocal} EnvironmentContext
    * @see ThreadLocal
    */
   public static void setCurrent(EnvironmentContext env)
   {
      current.set(env);
   }
}
