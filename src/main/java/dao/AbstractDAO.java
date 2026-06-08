package dao;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAO<T> implements CrudDAO<T> {
    protected Connection votingConnection() throws SQLException {
        return DatabaseConfig.getVotingConnection();
    }

    protected Connection citizensConnection() throws SQLException {
        return DatabaseConfig.getCitizensConnection();
    }
}
