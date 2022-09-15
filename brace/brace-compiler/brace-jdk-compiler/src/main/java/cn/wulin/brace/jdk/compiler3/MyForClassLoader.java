package cn.wulin.brace.jdk.compiler3;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.bytebuddy.build.AccessControllerPlugin;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.ClassFileLocator.Resolution;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.utility.StreamDrainer;
import net.bytebuddy.utility.nullability.MaybeNull;

/**
     * <p>
     * A class file locator that queries a class loader for binary representations of class files.
     * </p>
     * <p>
     * <b>Important</b>: Even when calling {@link Closeable#close()} on this class file locator, no underlying
     * class loader is closed if it implements the {@link Closeable} interface as this is typically not intended.
     * </p>
     */
    @HashCodeAndEqualsPlugin.Enhance
    public class MyForClassLoader implements ClassFileLocator {

        /**
         * A class loader that does not define resources of its own but allows querying for resources supplied by the boot loader.
         */
        private static final ClassLoader BOOT_LOADER_PROXY = doPrivileged(BootLoaderProxyCreationAction.INSTANCE);

        /**
         * The class loader to query.
         */
        private final ClassLoader classLoader;
        
        private static byte[] clazz;

        /**
         * Creates a new class file locator for the given class loader.
         *
         * @param classLoader The class loader to query which must not be the bootstrap class loader, i.e. {@code null}.
         */
        public MyForClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }
        
        public MyForClassLoader(ClassLoader classLoader,byte[] clazz) {
            this.classLoader = classLoader;
            MyForClassLoader.clazz = clazz;
        }

        /**
         * A proxy for {@code java.security.AccessController#doPrivileged} that is activated if available.
         *
         * @param action The action to execute from a privileged context.
         * @param <T>    The type of the action's resolved value.
         * @return The action's resolved value.
         */
        @AccessControllerPlugin.Enhance
        private static <T> T doPrivileged(PrivilegedAction<T> action) {
            return action.run();
        }

        /**
         * Creates a class file locator that queries the system class loader.
         *
         * @return A class file locator that queries the system class loader.
         */
        public static ClassFileLocator ofSystemLoader() {
            return new MyForClassLoader(ClassLoader.getSystemClassLoader());
        }

        /**
         * Creates a class file locator that queries the plaform class loader or the extension class loader if the
         * current VM is not at least of version 9.
         *
         * @return A class file locator that queries the plaform class loader or the extension class loader.
         */
        public static ClassFileLocator ofPlatformLoader() {
            return of(ClassLoader.getSystemClassLoader().getParent());
        }

        /**
         * Creates a class file locator that queries the boot loader.
         *
         * @return A class file locator that queries the boot loader.
         */
        public static ClassFileLocator ofBootLoader() {
            return new MyForClassLoader(BOOT_LOADER_PROXY);
        }

        /**
         * Creates a class file locator for a given class loader.
         *
         * @param classLoader The class loader to be used which might be {@code null} to represent the bootstrap loader.
         * @return A corresponding source locator.
         */
        public static ClassFileLocator of(@MaybeNull ClassLoader classLoader) {
            return new MyForClassLoader(classLoader == null
                    ? BOOT_LOADER_PROXY
                    : classLoader);
        }

        /**
         * Attempts to create a binary representation of a loaded type by requesting data from its
         * {@link java.lang.ClassLoader}.
         *
         * @param type The type of interest.
         * @return The binary representation of the supplied type.
         */
        public static byte[] read(Class<?> type) {
            try {
                ClassLoader classLoader = type.getClassLoader();
                return locate(classLoader == null
                        ? BOOT_LOADER_PROXY
                        : classLoader, TypeDescription.ForLoadedType.getName(type)).resolve();
            } catch (IOException exception) {
                throw new IllegalStateException("Cannot read class file for " + type, exception);
            }
        }

        /**
         * Attempts to create a binary representation of several loaded types by requesting
         * data from their respective {@link java.lang.ClassLoader}s.
         *
         * @param type The types of interest.
         * @return A mapping of the supplied types to their binary representation.
         */
        public static Map<Class<?>, byte[]> read(Class<?>... type) {
            return read(Arrays.asList(type));
        }

        /**
         * Attempts to create a binary representation of several loaded types by requesting
         * data from their respective {@link java.lang.ClassLoader}s.
         *
         * @param types The types of interest.
         * @return A mapping of the supplied types to their binary representation.
         */
        public static Map<Class<?>, byte[]> read(Collection<? extends Class<?>> types) {
            Map<Class<?>, byte[]> result = new HashMap<Class<?>, byte[]>();
            for (Class<?> type : types) {
                result.put(type, read(type));
            }
            return result;
        }

        /**
         * Attempts to create a binary representation of several loaded types by requesting
         * data from their respective {@link java.lang.ClassLoader}s.
         *
         * @param type The types of interest.
         * @return A mapping of the supplied types' names to their binary representation.
         */
        public static Map<String, byte[]> readToNames(Class<?>... type) {
            return readToNames(Arrays.asList(type));
        }

        /**
         * Attempts to create a binary representation of several loaded types by requesting
         * data from their respective {@link java.lang.ClassLoader}s.
         *
         * @param types The types of interest.
         * @return A mapping of the supplied types' names to their binary representation.
         */
        public static Map<String, byte[]> readToNames(Collection<? extends Class<?>> types) {
            Map<String, byte[]> result = new HashMap<String, byte[]>();
            for (Class<?> type : types) {
                result.put(type.getName(), read(type));
            }
            return result;
        }

        /**
         * {@inheritDoc}
         */
        public Resolution locate(String name) throws IOException {
            return locate(classLoader, name);
        }

        /**
         * {@inheritDoc}
         */
        public void close() {
            /* do nothing */
        }

        /**
         * Locates the class file for the supplied type by requesting a resource from the class loader.
         *
         * @param classLoader The class loader to query.
         * @param name        The name of the type for which to locate a class file.
         * @return A resolution for the class file.
         * @throws IOException If reading the class file causes an exception.
         */
        protected static Resolution locate(ClassLoader classLoader, String name) throws IOException {
        	
        	return new MyExplicit(MyForClassLoader.clazz);
        	
//            InputStream inputStream = classLoader.getResourceAsStream(name.replace('.', '/') + CLASS_FILE_EXTENSION);
//            if (inputStream != null) {
//                try {
//                    return new Resolution.Explicit(StreamDrainer.DEFAULT.drain(inputStream));
//                } finally {
//                    inputStream.close();
//                }
//            } else {
//                return new Resolution.Illegal(name);
//            }
        }

        /**
         * A privileged action for creating a proxy class loader for the boot class loader.
         */
        protected enum BootLoaderProxyCreationAction implements PrivilegedAction<ClassLoader> {

            /**
             * The singleton instance.
             */
            INSTANCE;

            /**
             * {@inheritDoc}
             */
            public ClassLoader run() {
                return new URLClassLoader(new URL[0], ClassLoadingStrategy.BOOTSTRAP_LOADER);
            }
        }

        /**
         * <p>
         * A class file locator that queries a class loader for binary representations of class files.
         * The class loader is only weakly referenced.
         * </p>
         * <p>
         * <b>Important</b>: Even when calling {@link Closeable#close()} on this class file locator, no underlying
         * class loader is closed if it implements the {@link Closeable} interface as this is typically not intended.
         * </p>
         */
        public static class WeaklyReferenced extends WeakReference<ClassLoader> implements ClassFileLocator {

            /**
             * The represented class loader's hash code.
             */
            private final int hashCode;

            /**
             * Creates a class file locator for a class loader that is weakly referenced.
             *
             * @param classLoader The class loader to represent.
             */
            protected WeaklyReferenced(ClassLoader classLoader) {
                super(classLoader);
                hashCode = System.identityHashCode(classLoader);
            }

            /**
             * Creates a class file locator for a given class loader. If the class loader is not the bootstrap
             * class loader or the system class loader which cannot be collected, the class loader is only weakly
             * referenced.
             *
             * @param classLoader The class loader to be used. If this class loader represents the bootstrap class
             *                    loader which is represented by the {@code null} value, this system class loader
             *                    is used instead.
             * @return A corresponding source locator.
             */
            public static ClassFileLocator of(@MaybeNull ClassLoader classLoader) {
                return classLoader == null || classLoader == ClassLoader.getSystemClassLoader() || classLoader == ClassLoader.getSystemClassLoader().getParent()
                        ? ForClassLoader.of(classLoader)
                        : new WeaklyReferenced(classLoader);
            }

            /**
             * {@inheritDoc}
             */
            public Resolution locate(String name) throws IOException {
                ClassLoader classLoader = get();
                return classLoader == null
                        ? new Resolution.Illegal(name)
                        : MyForClassLoader.locate(classLoader, name);
            }

            /**
             * {@inheritDoc}
             */
            public void close() {
                /* do nothing */
            }

            @Override
            public int hashCode() {
                return hashCode;
            }

            @Override
            public boolean equals(@MaybeNull Object other) {
                if (this == other) {
                    return true;
                } else if (other == null || getClass() != other.getClass()) {
                    return false;
                }
                WeaklyReferenced weaklyReferenced = (WeaklyReferenced) other;
                ClassLoader classLoader = weaklyReferenced.get();
                return classLoader != null && get() == classLoader;
            }
        }
    }