/*
 * Copyright 2010 Martin Grotzke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cn.wulin.brace.core.utils.test.serialize;

import java.awt.Container;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigDecimalSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigIntegerSerializer;

import cn.wulin.ioc.util.Holder;
import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.BitSetSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptyListSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptyMapSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptySetSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonListSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonMapSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonSetSerializer;
import de.javakaffee.kryoserializers.CopyForIterateCollectionSerializer;
import de.javakaffee.kryoserializers.CopyForIterateMapSerializer;
import de.javakaffee.kryoserializers.DateSerializer;
import de.javakaffee.kryoserializers.EnumMapSerializer;
import de.javakaffee.kryoserializers.EnumSetSerializer;
import de.javakaffee.kryoserializers.GregorianCalendarSerializer;
import de.javakaffee.kryoserializers.JdkProxySerializer;
import de.javakaffee.kryoserializers.KryoReflectionFactorySupport;
import de.javakaffee.kryoserializers.RegexSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.URISerializer;
import de.javakaffee.kryoserializers.UUIDSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

/**
 * Test for {@link Kryo} serialization.
 *
 * @author <a href="mailto:martin.grotzke@javakaffee.de">Martin Grotzke</a>
 */
public class KryoTest {
    
    private Kryo _kryo;

    protected void beforeTest() {
//    	_kryo = new KryoReflectionFactorySupport(); 或者 自定义 ,案例如下
        _kryo = new KryoReflectionFactorySupport() {

            @SuppressWarnings( { "rawtypes", "unchecked" } )
            public Serializer<?> getDefaultSerializer( final Class type ) {
                if ( EnumSet.class.isAssignableFrom( type ) ) {
                    return new EnumSetSerializer();
                }
                if ( EnumMap.class.isAssignableFrom( type ) ) {
                    return new EnumMapSerializer();
                }
                if ( Collection.class.isAssignableFrom( type ) ) {
                    return new CopyForIterateCollectionSerializer();
                }
                if ( Map.class.isAssignableFrom( type ) ) {
                    return new CopyForIterateMapSerializer();
                }
                if ( Date.class.isAssignableFrom( type ) ) {
                    return new DateSerializer( type );
                }
                return super.getDefaultSerializer( type );
            }
        };
        _kryo.setRegistrationRequired(false);
        _kryo.register( Arrays.asList( "" ).getClass(), new ArraysAsListSerializer() );
        _kryo.register( Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer() );
        _kryo.register( Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer() );
        _kryo.register( Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer() );
        _kryo.register( Collections.singletonList( "" ).getClass(), new CollectionsSingletonListSerializer() );
        _kryo.register( Collections.singleton( "" ).getClass(), new CollectionsSingletonSetSerializer() );
        _kryo.register( Collections.singletonMap( "", "" ).getClass(), new CollectionsSingletonMapSerializer() );
        _kryo.register( BigDecimal.class, new BigDecimalSerializer() );
        _kryo.register( BigInteger.class, new BigIntegerSerializer() );
        _kryo.register( Pattern.class, new RegexSerializer() );
        _kryo.register( BitSet.class, new BitSetSerializer() );
        _kryo.register( URI.class, new URISerializer() );
        _kryo.register( UUID.class, new UUIDSerializer() );
        _kryo.register( GregorianCalendar.class, new GregorianCalendarSerializer() );
        _kryo.register( InvocationHandler.class, new JdkProxySerializer() );
        UnmodifiableCollectionsSerializer.registerSerializers( _kryo );
        SynchronizedCollectionsSerializer.registerSerializers( _kryo );
    }

   @Test
   public void test(){
	   beforeTest();
	   Throwable param = null;
		try {
			int i =1/0;
		} catch (Exception e) {
			param = e;
		}
		
		byte[] serialize = serialize(param);
		Throwable deserialize = deserialize(serialize, Throwable.class);
		System.out.println(deserialize);
		
		
   }

    protected byte[] serialize( final Object o ) {
        return serialize(_kryo, o);
    }

    public static byte[] serialize(final Kryo kryo, final Object o) {
        if ( o == null ) {
            throw new NullPointerException( "Can't serialize null" );
        }

        final Output output = new Output(4096);
        kryo.writeObject(output, o);
        output.flush();
        return output.getBuffer();
    }

    protected <T> T deserialize( final byte[] in, final Class<T> clazz ) {
        return deserialize(_kryo, in, clazz);
    }

    public static <T> T deserialize(final Kryo kryo, final byte[] in, final Class<T> clazz) {
        final Input input = new Input(in);
        return kryo.readObject(input, clazz);
    }

}