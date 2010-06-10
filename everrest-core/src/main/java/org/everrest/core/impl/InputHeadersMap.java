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

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Read only case insensitive {@link MultivaluedMap}.
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: InputHeadersMap.java -1 $
 */
// TODO this implementation is not efficient, probably can be better extend
// java.util.AbstractMap
public final class InputHeadersMap extends HashMap<String, List<String>> implements MultivaluedMap<String, String>
{

   /**
    * Generated by Eclipse.
    */
   private static final long serialVersionUID = -4181622019478475004L;

   /**
    * See {@link Map#entrySet()}.
    */
   private transient Set<Map.Entry<String, List<String>>> entrySet;

   /**
    * See {@link Map#keySet()}.
    */
   private transient Set<String> keySet;

   /**
    * See {@link Map#values()}.
    */
   private transient Set<List<String>> valueSet;

   /**
    * Source {@link Map}.
    */
   private final Map<String, List<String>> m;

   /**
    * @param m source map.
    */
   public InputHeadersMap(Map<String, List<String>> m)
   {
      if (m == null)
         throw new NullPointerException();
      this.m = new HashMap<String, List<String>>(m.size());
      for (Map.Entry<String, List<String>> e : m.entrySet())
         this.m.put(e.getKey().toLowerCase(), Collections.unmodifiableList(e.getValue()));
   }

   // Helper classes

   /**
    * Abstraction for read-only {@link Map.Entry}. All extended classes of this
    * abstraction is also read-only.
    * 
    * @param <K> key parameter
    * @param <V> value parameter
    */
   private abstract class AbstractReadOnlyEntry<K, V> implements Map.Entry<K, V>
   {
      /**
       * Entry is read-only. {@inheritDoc}
       */
      public final V setValue(V value)
      {
         throw new UnsupportedOperationException();
      }
   }

   /**
    * Abstraction of read-only iterator, method {@link Iterator#remove()} throws
    * {@link UnsupportedOperationException}. All extended classes of this
    * abstraction is also read-only.
    * 
    * @param <T> iterable parameter
    */
   private abstract class AbstractReadOnlyIterator<T> implements Iterator<T>
   {
      /**
       * Iterator is read-only. {@inheritDoc}
       */
      public final void remove()
      {
         throw new UnsupportedOperationException();
      }
   }

   /**
    * Abstraction of read-only {@link Set}, all methods which can change it
    * throws {@link UnsupportedOperationException}. All extended classes of this
    * abstraction is also read-only.
    * 
    * @param <T> Set parameter
    */
   private abstract class AbstractReadOnlySet<T> extends AbstractSet<T>
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public final boolean removeAll(Collection<?> c)
      {
         throw new UnsupportedOperationException();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public final boolean add(T o)
      {
         throw new UnsupportedOperationException();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public final boolean addAll(Collection<? extends T> c)
      {
         throw new UnsupportedOperationException();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public final void clear()
      {
         throw new UnsupportedOperationException();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public final boolean remove(Object o)
      {
         throw new UnsupportedOperationException();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public final boolean retainAll(Collection<?> c)
      {
         throw new UnsupportedOperationException();
      }
   }

   // HasMap

   /**
    * {@inheritDoc}
    */
   public List<String> get(Object o)
   {
      if (o != null)
         return m.get(((String)o).toLowerCase());

      return m.get(o);
   }

   /**
    * {@inheritDoc}
    */
   public Set<Map.Entry<String, List<String>>> entrySet()
   {
      if (entrySet == null)
         createEntrySet();
      return entrySet;
   }

   /**
    * {@inheritDoc}
    */
   public Set<String> keySet()
   {
      if (keySet == null)
         keySet = Collections.unmodifiableSet(m.keySet());
      return keySet;
   }

   /**
    * {@inheritDoc}
    */
   public Set<List<String>> values()
   {
      if (valueSet == null)
         createValues();
      return valueSet;
   }

   /**
    * {@inheritDoc}
    */
   public List<String> remove(Object key)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * {@inheritDoc}
    */
   public void clear()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * {@inheritDoc}
    */
   public List<String> put(String k, List<String> v)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * {@inheritDoc}
    */
   public void putAll(Map<? extends String, ? extends List<String>> m0)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * {@inheritDoc}
    */
   public int size()
   {
      return m.size();
   }

   // MultivaluedMap

   /**
    * {@inheritDoc}
    */
   public void add(String key, String value)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * {@inheritDoc}
    */
   public String getFirst(String key)
   {
      List<String> list = get(key);
      return list != null && list.size() > 0 ? list.get(0) : null;
   }

   /**
    * {@inheritDoc}
    */
   public void putSingle(String key, String value)
   {
      throw new UnsupportedOperationException();
   }

   // helpers methods

   /**
    * Create read only EntrySet.
    */
   private void createEntrySet()
   {
      entrySet = new AbstractReadOnlySet<Map.Entry<String, List<String>>>()
      {

         @Override
         public Iterator<Map.Entry<String, List<String>>> iterator()
         {

            return new AbstractReadOnlyIterator<Map.Entry<String, List<String>>>()
            {

               private final Iterator<Map.Entry<String, List<String>>> i = m.entrySet().iterator();

               public boolean hasNext()
               {
                  return i.hasNext();
               }

               public java.util.Map.Entry<String, List<String>> next()
               {
                  return new AbstractReadOnlyEntry<String, List<String>>()
                  {

                     private final Map.Entry<String, List<String>> e = i.next();

                     public String getKey()
                     {
                        return e.getKey();
                     }

                     public List<String> getValue()
                     {
                        return e.getValue();
                     }
                  };
               }

            };
         }

         @Override
         public int size()
         {
            return m.size();
         }

      };
   }

   /**
    * Create read only values.
    */
   private void createValues()
   {
      valueSet = new AbstractReadOnlySet<List<String>>()
      {

         @Override
         public Iterator<List<String>> iterator()
         {
            return new AbstractReadOnlyIterator<List<String>>()
            {

               private final Iterator<String> i = m.keySet().iterator();

               public boolean hasNext()
               {
                  return i.hasNext();
               }

               public List<String> next()
               {
                  if (!i.hasNext())
                     throw new NoSuchElementException();

                  return m.get(i.next());
               }

            };
         }

         @Override
         public int size()
         {
            return m.size();
         }

      };
   }

}
