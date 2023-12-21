package derby;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTDG<T extends Persistable> implements GenericTDG<T> {

    private Map<Long, T> identityMap = new HashMap<>();

    @Override
    public final T findById(long id) throws SQLException {
        T cached = getCached(id);
        if (cached != null) {
            assert cached.getId() == id;
            return cached;
        }
        T fromDB = retrieveFromDB(id);
        if (fromDB != null) {
            cache(id, fromDB);
        }
        return fromDB;
    }

    protected abstract T retrieveFromDB(long id) throws SQLException;

    @Override
    public final T insert(T t) throws SQLException {
        if (identityMap.containsValue(t)) {
            throw new IllegalArgumentException("Object already persisted in database!");
        }
        T fromDB = insertIntoDB(t);
        if (t.getId()==0) {
            throw new IllegalStateException("No generated ID");
        }
        cache(t.getId(), fromDB);
        return fromDB;
    }

    protected abstract T insertIntoDB(T t) throws SQLException;
    
    @Override
    public T update(T t) throws SQLException {
        if (!identityMap.containsValue(t)) {
            throw new IllegalArgumentException("Object not persisted in database!");
        }
        return updateIntoDB(t);
    }

    protected abstract T updateIntoDB(T t) throws SQLException;
    
    @Override
    public T refresh(T t) throws SQLException {
        if (!identityMap.containsValue(t)) {
            throw new IllegalArgumentException("Object not persisted in database!");
        }
        return refreshIntoDB(t);
    }

    protected abstract T refreshIntoDB(T t) throws SQLException;
    
    @Override
    public T delete(T t) throws SQLException {
        if (!identityMap.containsValue(t)) {
            throw new IllegalArgumentException("Object not persisted in database!");
        }
        T fromDB = deleteFromDB(t);
        identityMap.remove(fromDB.getId());
        return fromDB;
    }

    protected abstract T deleteFromDB(T t) throws SQLException;
    
    /**
     * Makes a query to retrieve a set of objects.
     * 
     * @param clauseWhereWithJoker a prepared where clause
     * @param args the parameters to use with the prepared where clause
     * @return a list of objects
     * @throws SQLException
     */
    @Override
    public List<T> selectWhere(String clauseWhereWithJoker, Object... args) throws SQLException {
        List<T> result = new ArrayList<>();
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(getWherePrefix()+clauseWhereWithJoker)) {
            int index = 1;
            for (Object arg : args) {
                pst.setObject(index++, arg);
            }
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    result.add(findById(rs.getLong(1)));
                }
            }
        }
        return result;
    }
    
    protected abstract String getWherePrefix();
    
    protected T getCached(long id) {
        return identityMap.get(id);
    }
    
    protected void cache(long id, T value) {
        identityMap.put(id, value);
    }
    
    /**
     * To clean up the identity map.
     * Use at your own risks (e.g. for unit tests).
     * Once cleaned, the one to one relationship 
     * between an object on the domain side and
     * a row in the relational side is lost.
     */
    public void clearCache() {
    		identityMap.clear();
    }
}
