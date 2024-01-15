package io.github.chensheng.dddboot.tools.base;

import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class BeanUtil {
    private static final Logger log = LoggerFactory.getLogger(BeanUtil.class);

    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, null);
    }

    public static void copyNotNullProperties(Object source, Object target) {
        copyNotNullProperties(source, target, null);
    }

    public static void copyNotBlankProperties(Object source, Object target) {
        copyNotBlankProperties(source, target, null);
    }

    public static void copyProperties(Object source, Object target, String[] ignores) {
        doCopyProperties(source, target, false, false, ignores);
    }

    public static void copyNotNullProperties(Object source, Object target, String[] ignores) {
        doCopyProperties(source, target, true, false, ignores);
    }

    public static void copyNotBlankProperties(Object source, Object target, String[] ignores) {
        doCopyProperties(source, target,true, true, ignores);
    }

    public static void copyProperty(Object target, String name, Object value) {
        if(target == null || name == null) {
            return;
        }

        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(target.getClass(), name);
            if(propertyDescriptor == null) {
                return;
            }

            Method writeMethod = propertyDescriptor.getWriteMethod();
            if(writeMethod == null) {
                return;
            }

            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                writeMethod.setAccessible(true);
            }
            writeMethod.invoke(target, value);
        } catch (Throwable e) {
            log.debug("Fail to copy property " + name, e);
        }
    }

    private static boolean isValidValue(Object value, boolean notNull, boolean notBlank) {
        if(notNull && value == null) {
            return false;
        }

        if(notBlank) {
            if(value == null) {
                return false;
            }

            if(value.getClass() == String.class) {
                String valueStr = (String) value;
                if(TextUtil.isBlank(valueStr)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void doCopyProperties(Object source, Object target, boolean notNull, boolean notBlank, String... ignoreProperties) {
        if(source == null || target == null) {
            return;
        }


        Class<?> actualEditable = target.getClass();
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
        PropertyDescriptor[] targetPds;
        try {
            targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        } catch (BeansException e){
            log.debug("Fail to copy properties", e);
            return;
        }

        for (PropertyDescriptor targetPd : targetPds) {
            try {
                Method writeMethod = targetPd.getWriteMethod();
                if(writeMethod == null || (ignoreList != null && ignoreList.contains(targetPd.getName()))) {
                    continue;
                }

                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if(sourcePd == null) {
                    continue;
                }

                Method readMethod = sourcePd.getReadMethod();
                if(readMethod == null) {
                    continue;
                }

                ResolvableType sourceResolvableType = ResolvableType.forMethodReturnType(readMethod);
                ResolvableType targetResolvableType = ResolvableType.forMethodParameter(writeMethod, 0);

                // Ignore generic types in assignable check if either ResolvableType has unresolvable generics.
                boolean isAssignable = (sourceResolvableType.hasUnresolvableGenerics() || targetResolvableType.hasUnresolvableGenerics() ?
                        ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType()) :
                        targetResolvableType.isAssignableFrom(sourceResolvableType));
                if(!isAssignable) {
                    continue;
                }

                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                Object value = readMethod.invoke(source);
                if(!isValidValue(value, notNull, notBlank)) {
                    continue;
                }

                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                writeMethod.invoke(target, value);
            } catch (Throwable ex) {
                log.debug("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
            }
        }

    }
}
