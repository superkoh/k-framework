package me.superkoh.kframework.lib.db.mybatis.builder;

import me.superkoh.kframework.lib.db.mybatis.test.User;
import me.superkoh.kframework.lib.db.mybatis.test.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by KOH on 2017/5/19.
 * <p>
 * k-framework
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:ut.sql"})
public class AbstractCommonSqlBuilderTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert() throws Exception {
        User user = new User();
        user.setName("KOH");
        userMapper.insert(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals("KOH", user.getName());
    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {
        User user = new User();
        user.setName("KOH");
        userMapper.insert(user);
        User toUpdate = new User();
        toUpdate.setId(user.getId());
        toUpdate.setName("superkoh");
        int updated = userMapper.updateByPrimaryKeySelective(toUpdate);
        Assert.assertEquals(1, updated);
        Assert.assertEquals("superkoh", userMapper.selectById(user.getId()).getName());
    }

    @Test
    public void selectByQuery() throws Exception {
    }

    @Test
    public void selectPageByQuery() throws Exception {
    }

    @Test
    public void countByQuery() throws Exception {
    }
}