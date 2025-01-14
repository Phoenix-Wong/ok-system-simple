package cn.iosd.starter.dict.service;

import cn.iosd.starter.dict.vo.DictItem;

import java.util.List;

/**
 * @author ok1996
 */
public interface DictService {
    /**
     * 获取指定类型的字典项列表
     *
     * @param dictionaryParams 接口实现类所需的参数类型
     * @return 参数类型对应字典列表
     */
    List<DictItem> getDictItemList(String dictionaryParams);
}
