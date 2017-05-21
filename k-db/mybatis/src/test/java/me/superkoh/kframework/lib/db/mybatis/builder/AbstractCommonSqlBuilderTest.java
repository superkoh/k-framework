package me.superkoh.kframework.lib.db.mybatis.builder;

import me.superkoh.kframework.core.type.Page;
import me.superkoh.kframework.lib.db.mybatis.test.User;
import me.superkoh.kframework.lib.db.mybatis.test.UserMapper;
import me.superkoh.kframework.lib.db.mybatis.test.UserQuery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
        Assert.assertEquals(0L, user.getCreateUser().longValue());
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
        User user1 = new User();
        user1.setName("KOH1");
        userMapper.insert(user1);
        User user2 = new User();
        user2.setName("KOH2");
        userMapper.insert(user2);

        UserQuery query = new UserQuery();
        query.setId(user1.getId());
        List<User> userList = userMapper.selectByQuery(User.class, query, null);
        Assert.assertEquals(1, userList.size());

        query = new UserQuery();
        query.setMaxId(user2.getId() + 1);
        userList = userMapper.selectByQuery(User.class, query, "id desc");
        Assert.assertEquals(2, userList.size());
        Assert.assertEquals(user2.getId(), userList.get(0).getId());

        query = new UserQuery();
        query.setMinId(user1.getId());
        query.setName("%H2%");
        userList = userMapper.selectByQuery(User.class, query, null);
        Assert.assertEquals(1, userList.size());
    }

    @Test
    public void selectPageByQuery() throws Exception {
        User user1 = new User();
        user1.setName("KOH1");
        userMapper.insert(user1);
        User user2 = new User();
        user2.setName("KOH2");
        userMapper.insert(user2);

        UserQuery query = new UserQuery();
        query.setMaxId(user2.getId() + 1);
        Page page = new Page(1, 1, "id desc");
        List<User> userList = userMapper.selectPageByQuery(User.class, query, page);
        Assert.assertEquals(1, userList.size());
        Assert.assertEquals(user2.getId(), userList.get(0).getId());
    }

    @Test
    public void countByQuery() throws Exception {
        User user1 = new User();
        user1.setName("KOH1");
        userMapper.insert(user1);
        User user2 = new User();
        user2.setName("KOH2");
        userMapper.insert(user2);
        UserQuery query = new UserQuery();
        query.setMaxId(user2.getId() + 1);
        long count = userMapper.countByQuery(query);
        Assert.assertEquals(2, count);
    }
}