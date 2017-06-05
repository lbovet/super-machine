package li.chee.supermachine.traverser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author John DeRegnaucourt (john@cedarsoftware.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
public final class ReflectionUtils
{
    private static final Map<Class, Collection<Field>> _reflectedFields = new ConcurrentHashMap<>();

    private ReflectionUtils()
    {
        super();
    }

    @SuppressWarnings("unchecked")
    public static Method getMethod(Class c, String method, Class...types)  {
        try {
            return c.getMethod(method, types);
        } catch (Exception nse) {
            return null;
        }
    }

    /**
     * Get all non static, non transient, fields of the passed in class, including
     * private fields. Note, the special this$ field is also not returned.  The result
     * is cached in a static ConcurrentHashMap to benefit execution performance.
     * @param c Class instance
     * @return Collection of only the fields in the passed in class
     * that would need further processing (reference fields).  This
     * makes field traverser on a class faster as it does not need to
     * continually process known fields like primitives.
     */
    public static Collection<Field> getDeepDeclaredFields(Class c)
    {
        if (_reflectedFields.containsKey(c))
        {
            return _reflectedFields.get(c);
        }
        Collection<Field> fields = new ArrayList<>();
        Class curr = c;

        while (curr != null)
        {
            getDeclaredFields(curr, fields);
            curr = curr.getSuperclass();
        }
        _reflectedFields.put(c, fields);
        return fields;
    }

    /**
     * Get all non static, non transient, fields of the passed in class, including
     * private fields. Note, the special this$ field is also not returned.  The
     * resulting fields are stored in a Collection.
     * @param c Class instance
     * that would need further processing (reference fields).  This
     * makes field traverser on a class faster as it does not need to
     * continually process known fields like primitives.
     */
    public static void getDeclaredFields(Class c, Collection<Field> fields) {
        try
        {
            Field[] local = c.getDeclaredFields();

            for (Field field : local)
            {
                if (!field.isAccessible())
                {
                    try
                    {
                        field.setAccessible(true);
                    }
                    catch (Exception ignored) { }
                }

                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) &&
                        !field.getName().startsWith("this$") &&
                        !Modifier.isTransient(modifiers))
                {   // speed up: do not count static fields, do not go back up to enclosing object in nested case, do not consider transients
                    fields.add(field);
                }
            }
        }
        catch (Throwable ignored)
        {
            // ignored
        }
    }
}
