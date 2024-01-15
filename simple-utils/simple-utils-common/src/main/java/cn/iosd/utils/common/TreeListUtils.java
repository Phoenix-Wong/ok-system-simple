package cn.iosd.utils.common;

import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 将列表转换为树形结构工具类
 *
 * @author ok1996
 */
public class TreeListUtils {

    /**
     * 将原始列表转换为树形结构，没有父级关联的数据视为一级节点
     *
     * @param origList          原始列表,包含（id字段、parentId字段、children字段）
     *                          其中，children字段 为空 用于建立树形结构和补充数据
     * @param idFieldName       id字段名
     * @param parentIdFieldName parentId字段名
     * @param childrenFieldName children字段名
     * @param isRootPredicate   判断是否为根节点的条件
     * @param <T>               实体类型
     * @return 树形结构的结果列表
     */
    public static <T> List<T> convert(List<T> origList, String idFieldName, String parentIdFieldName
            , String childrenFieldName, Predicate<String> isRootPredicate) {
        return convertAndAddData(
                origList, null, idFieldName, parentIdFieldName
                , childrenFieldName, null, null, isRootPredicate
        );
    }

    /**
     * 将原始列表转换为树形结构，没有父级关联的数据视为一级节点
     * <p>
     * 并将关联对象添加进去树形结构
     * <p/>
     *
     * @param origList          原始列表,包含（id字段、parentId字段、children字段、data字段、data主键字段）
     *                          其中，children字段、data字段 为空 用于建立树形结构和补充数据
     * @param idData            包含关联数据对象的映射，key为data主键字段名
     * @param idFieldName       id字段名
     * @param parentIdFieldName parentId字段名
     * @param childrenFieldName children字段名
     * @param dataFieldName     data字段名（表示要关联的数据字段名）
     * @param dataIdFieldName   data主键字段名（表示要关联的数据字段的主键字段名）
     * @param isRootPredicate   判断是否为根节点的条件
     * @param <T>               泛型类型，表示原始数据的类型
     * @param <V>               泛型类型，表示关联数据的类型
     * @return 树形结构的结果列表
     */
    public static <T, V> List<T> convertAndAddData(List<T> origList, Map<String, V> idData, String idFieldName
            , String parentIdFieldName, String childrenFieldName, String dataFieldName
            , String dataIdFieldName, Predicate<String> isRootPredicate) {
        // 是否需要添加关联数据
        boolean addDataBool = !(ObjectUtils.isEmpty(dataFieldName) || ObjectUtils.isEmpty(dataIdFieldName) || ObjectUtils.isEmpty(idData));

        Map<String, T> idMaps = new HashMap<>(origList.size());
        // 获取字段值的方法
        Function<T, String> getId = entity -> Objects.toString(getFieldValue(entity, idFieldName), "");
        Function<T, String> getDataId = entity -> Objects.toString(getFieldValue(entity, dataIdFieldName), "");

        // 预先遍历一次原始列表，将id赋值给idMaps
        for (T entity : origList) {
            String id = getId.apply(entity);
            if (id.isEmpty()) {
                throw new IllegalArgumentException("存在id为空的数据");
            }
            idMaps.put(id, entity);
        }

        List<T> result = new ArrayList<>();
        Function<T, String> getParentId = entity -> Objects.toString(getFieldValue(entity, parentIdFieldName), "");
        for (T entity : origList) {
            if (addDataBool) {
                String dataId = getDataId.apply(entity);
                setFieldValue(entity, dataFieldName, idData.get(dataId));
            }
            String parentId = getParentId.apply(entity);
            if (isRootPredicate.test(parentId)) {
                result.add(entity);
            } else {
                T parentEntity = idMaps.get(parentId);
                if (parentEntity != null) {
                    setChildrenValue(childrenFieldName, entity, parentEntity);
                } else {
                    result.add(entity);
                }
            }
        }
        return result;
    }


    /**
     * 设置实体的子节点
     *
     * @param childrenFieldName 子节点字段名
     * @param entity            当前实体
     * @param parentEntity      父实体
     * @param <T>               实体类型
     */
    private static <T> void setChildrenValue(String childrenFieldName, T entity, T parentEntity) {
        // 获取父实体的子节点列表，如果为空则创建一个新列表
        List<T> children = Optional.ofNullable(getFieldValue(parentEntity, childrenFieldName))
                .map(value -> (List<T>) value)
                .orElse(new ArrayList<>());
        // 将当前实体添加到父实体的子节点列表中
        children.add(entity);
        setFieldValue(parentEntity, childrenFieldName, children);
    }

    /**
     * 获取实体的字段值
     *
     * @param entity    实体
     * @param fieldName 字段名称
     * @param <T>       实体类型
     * @return 字段的值
     */
    private static <T> Object getFieldValue(T entity, String fieldName) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            ReflectionUtils.makeAccessible(field);
            return field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("字段名称[" + fieldName + "]不存在", e);
        }
    }

    /**
     * 设置实体的字段值
     *
     * @param entity    实体
     * @param fieldName 字段名称
     * @param value     字段的值
     * @param <T>       实体类型
     */
    private static <T> void setFieldValue(T entity, String fieldName, Object value) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            ReflectionUtils.makeAccessible(field);
            field.set(entity, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("字段名称[" + fieldName + "]不存在", e);
        }
    }
}
