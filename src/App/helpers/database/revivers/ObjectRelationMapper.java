package App.helpers.database.revivers;

import App.helpers.database.annotations.DBCol;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class ObjectRelationMapper<T> {

    private Class someClass;
    private Map<String, Field> fields = new HashMap<>();

    public ObjectRelationMapper(Class someClass) {
        this.someClass = someClass;

        List<Field> fieldList = Arrays.asList(someClass.getDeclaredFields());
        for (Field field : fieldList) {
            DBCol col = field.getAnnotation(DBCol.class);
            if (col != null) {
                field.setAccessible(true);
                fields.put(col.value().isEmpty() ? field.getName() : col.value(), field);
            }
        }
    }

    public T map(Map<String, Object> row) {
        T dto = null;
        try {
            dto = (T) someClass.getConstructor().newInstance();

            for (Map.Entry<String, Object> entity : row.entrySet()) {
                if (entity.getValue() == null) {
                    continue;
                }
                String DBCol = entity.getKey();
                Field field = fields.get(DBCol);
                if (field != null) {
                    Object val = entity.getValue();
                    field.set(dto, convertInstanceOfObject(val));
                }
            }
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return dto;
    }

    public List<?> map(List<Map<String, Object>> rows) {
        List<T> list = new LinkedList<>();

        for (Map<String, Object> row : rows) {
            list.add(map(row));
        }

        return list;
    }

    public List<?> map(ResultSet rs) {
        return this.map(this.resultSetToArrayList(rs));
    }

    public List resultSetToArrayList(ResultSet rs){
        ArrayList list = new ArrayList(50);
        try {
            ResultSetMetaData md = rs.getMetaData();
            int DBCols = md.getColumnCount();
            while (rs.next()) {
                HashMap row = new HashMap(DBCols);
                for (int i = 1; i <= DBCols; ++i) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(row);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        return list;
    }

    private T convertInstanceOfObject(Object o) {
        try {
            return (T) o;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
