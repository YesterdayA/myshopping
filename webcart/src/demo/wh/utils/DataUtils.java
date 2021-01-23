package demo.wh.utils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    public static <T> List<T> getAll(Class<T> cls, ResultSet rs) throws Exception {
        List<T> list = new ArrayList<>();

        while (rs.next()) {
            //获取一个实例化对象
            T t = cls.newInstance();
            //获取rs的元数据
            ResultSetMetaData metaData = rs.getMetaData();
            //获取有多少列
            int columnCount = metaData.getColumnCount();
            //获取每列的每个元素的名称
            for (int i = 1; i <= columnCount; i++) {
                //获取列名
                String columnLabel = metaData.getColumnLabel(i);
                //通过拼接来得到实力的set方法
                String setName = "set" + columnLabel.substring(0, 1).toUpperCase() + columnLabel.substring(1);
                //通过反射来获取属性的类型，因为在将数据实例中的时候需要；
                Class<?> fieldType = cls.getDeclaredField(columnLabel).getType();
                //获取对应行的数据
                Object object = rs.getObject(columnLabel);
                //获取set方法；
                Method method = cls.getDeclaredMethod(setName, fieldType);
                //对实例的属性进行值得设置
                method.invoke(t, object);
            }
            list.add(t);
        }
        return list;
    }
}
