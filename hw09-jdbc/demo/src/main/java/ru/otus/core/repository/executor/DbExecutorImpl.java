package ru.otus.core.repository.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import ru.otus.core.sessionmanager.DataBaseOperationException;

public class DbExecutorImpl implements DbExecutor {

    @Override
    public long executeStatement(Connection connection, String sql, List<Object> params) {
        try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (var idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.executeUpdate();
            try (var rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DataBaseOperationException("executeInsert error", ex);
        }
    }

    @Override
    public <T> Optional<T> executeSelect(
            Connection connection, String sql, List<Object> params, Function<ResultSet, T> rsHandler) {
        try (var pst = connection.prepareStatement(sql)) {
            for (var idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            try (var rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        } catch (SQLException ex) {
            throw new DataBaseOperationException("executeSelect error", ex);
        }
    }

    @Override
    public <T> List<T> executeSelectAll(Connection connection, String sql, Function<ResultSet, T> rsHandler) {
        List<T> resultList = new LinkedList<>();
        try (var pst = connection.prepareStatement(sql);
                var rs = pst.executeQuery()) {
            T t;
            while ((t = rsHandler.apply(rs)) != null) {
                resultList.add(t);
            }
            return resultList;
        } catch (SQLException ex) {
            throw new DataBaseOperationException("executeSelectAll error", ex);
        }
    }
}
