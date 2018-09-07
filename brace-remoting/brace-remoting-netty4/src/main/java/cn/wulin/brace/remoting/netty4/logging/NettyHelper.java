package cn.wulin.brace.remoting.netty4.logging;

import cn.wulin.ioc.logging.Logger;
import cn.wulin.ioc.logging.LoggerFactory;
import io.netty.util.internal.logging.AbstractInternalLogger;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author <a href="mailto:gang.lvg@taobao.com">kimi</a>
 * @author <a href="mailto:liujie.qlj@alibaba-inc.com">qinliujie</a>
 */
public class NettyHelper {

    public static void setNettyLoggerFactory() {
        InternalLoggerFactory factory = InternalLoggerFactory.getDefaultFactory();
        if (factory == null || !(factory instanceof DubboLoggerFactory)) {
            InternalLoggerFactory.setDefaultFactory(new DubboLoggerFactory());
        }
    }

    static class DubboLoggerFactory extends InternalLoggerFactory {

        @Override
        public InternalLogger newInstance(String name) {
            return new DubboLogger(LoggerFactory.getLogger(name));
        }
    }

    static class DubboLogger extends AbstractInternalLogger {

        private Logger logger;

        DubboLogger(Logger logger) {
            super(logger.getClass().getName());
            this.logger = logger;
        }

        public boolean isTraceEnabled() {
            return logger.isTraceEnabled();
        }

        public void trace(String msg) {
            if (isTraceEnabled()) {
                logger.trace(msg);
            }
        }

        public void trace(String format, Object arg) {
            if (isTraceEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, arg);
                logger.trace(ft.getMessage(), ft.getThrowable());
            }

        }

        public void trace(String format, Object argA, Object argB) {
            if (isTraceEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, argA, argB);
                logger.trace(ft.getMessage(), ft.getThrowable());
            }
        }

        public void trace(String format, Object... arguments) {
            if (isTraceEnabled()) {
                FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
                logger.trace(ft.getMessage(), ft.getThrowable());
            }
        }

        public void trace(String msg, Throwable t) {
            if (isTraceEnabled()) {
                logger.trace(msg, t);
            }
        }

        public boolean isDebugEnabled() {
            return logger.isDebugEnabled();
        }

        public void debug(String msg) {
            if (isDebugEnabled()) {
                logger.debug(msg);
            }
        }

        public void debug(String format, Object arg) {
            if (isDebugEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, arg);
                logger.debug(ft.getMessage(), ft.getThrowable());
            }
        }

        public void debug(String format, Object argA, Object argB) {
            if (isDebugEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, argA, argB);
                logger.debug(ft.getMessage(), ft.getThrowable());
            }
        }

        public void debug(String format, Object... arguments) {
            if (isDebugEnabled()) {
                FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
                logger.debug(ft.getMessage(), ft.getThrowable());
            }
        }

        public void debug(String msg, Throwable t) {
            if (isDebugEnabled()) {
                logger.debug(msg, t);
            }
        }

        public boolean isInfoEnabled() {
            return logger.isInfoEnabled();
        }

        public void info(String msg) {
            if (isInfoEnabled()) {
                logger.info(msg);
            }
        }

        public void info(String format, Object arg) {
            if (isInfoEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, arg);
                logger.info(ft.getMessage(), ft.getThrowable());
            }
        }

        public void info(String format, Object argA, Object argB) {
            if (isInfoEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, argA, argB);
                logger.info(ft.getMessage(), ft.getThrowable());
            }
        }

        public void info(String format, Object... arguments) {
            if (isInfoEnabled()) {
                FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
                logger.info(ft.getMessage(), ft.getThrowable());
            }
        }

        public void info(String msg, Throwable t) {
            if (isInfoEnabled()) {
                logger.info(msg, t);
            }
        }

        public boolean isWarnEnabled() {
            return false;
        }

        public void warn(String msg) {
            if (isWarnEnabled()) {
                logger.warn(msg);
            }
        }

        public void warn(String format, Object arg) {
            if (isWarnEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, arg);
                logger.warn(ft.getMessage(), ft.getThrowable());
            }
        }

        public void warn(String format, Object... arguments) {
            if (isWarnEnabled()) {
                FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
                logger.warn(ft.getMessage(), ft.getThrowable());
            }
        }

        public void warn(String format, Object argA, Object argB) {
            if (isWarnEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, argA, argB);
                logger.warn(ft.getMessage(), ft.getThrowable());
            }
        }

        public void warn(String msg, Throwable t) {
            if (isWarnEnabled()) {
                logger.warn(msg, t);
            }
        }

        public boolean isErrorEnabled() {
            return logger.isErrorEnabled();
        }

        public void error(String msg) {
            if (isErrorEnabled()) {
                logger.error(msg);
            }
        }

        public void error(String format, Object arg) {
            if (isErrorEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, arg);
                logger.error(ft.getMessage(), ft.getThrowable());
            }
        }

        public void error(String format, Object argA, Object argB) {
            if (isErrorEnabled()) {
                FormattingTuple ft = MessageFormatter.format(format, argA, argB);
                logger.error(ft.getMessage(), ft.getThrowable());
            }
        }

        public void error(String format, Object... arguments) {
            if (isErrorEnabled()) {
                FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
                logger.error(ft.getMessage(), ft.getThrowable());
            }
        }

        public void error(String msg, Throwable t) {
            if (isErrorEnabled()) {
                logger.error(msg, t);
            }
        }
    }

}
