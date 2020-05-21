package com.hz.fine.master.core.common.utils.constant;

/**
 * <p>此类属于各个服务的项目缩写名称的枚举类；用于各个项目在定义redis的key的时候不会重叠</p>
 * <p>在使用此类是，如果新增类枚举，请一定在类CachedKeyUtils中添加自己生成key的方法</p>
 * <p>各个项目如果使用redis，请定义项目名称的缩写</p>
 * Created by df on 2018/6/20 12:02
 */
public enum SystemKeysEnum {

    FINE("FN", "美好"),


    ;

    private String name;

    /**
     * 描述
     */
    private String desc;

    private SystemKeysEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }


    public String getName() {
        return name;
    }


    /**
     * 获取描述
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }
}
