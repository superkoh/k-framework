package me.superkoh.kframework.lib.db.mybatis.test;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhangyh on 16/7/28.
 */
public class UserSexTypeHandler extends BaseTypeHandler<UserSex> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserSex parameter, JdbcType
            jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public UserSex getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return UserSex.getByValue(rs.getInt(columnName));
    }

    @Override
    public UserSex getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return UserSex.getByValue(rs.getInt(columnIndex));
    }

    @Override
    public UserSex getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return UserSex.getByValue(cs.getInt(columnIndex));
    }
}
