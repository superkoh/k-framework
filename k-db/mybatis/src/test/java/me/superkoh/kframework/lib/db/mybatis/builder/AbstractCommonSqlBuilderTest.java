package me.superkoh.kframework.lib.db.mybatis.builder;

import com.google.common.collect.Lists;
import me.superkoh.kframework.core.type.Page;
import me.superkoh.kframework.lib.db.mybatis.test.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
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
    @Autowired
    private UserMapper2 userMapper2;

    @Test
    public void insert() throws Exception {
        User user = new User();
        user.setName("KOH");
        userMapper.insert(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals("KOH", user.getName());
        Assert.assertEquals(1L, user.getCreateUser().longValue());
    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {
        User2 user = new User2();
        user.setName("KOH");
        userMapper2.insert(user);
        User2 toUpdate = new User2();
        toUpdate.setId(user.getId());
        toUpdate.setName("superkoh");
        int updated = userMapper2.updateByPrimaryKeySelective(toUpdate);
        Assert.assertEquals(1, updated);
        Assert.assertEquals("superkoh", userMapper.selectById(user.getId()).getName());
    }

    @Test
    public void selectByExample() throws Exception {
        User user1 = new User();
        user1.setName("KOH1");
        user1.setGender(UserSex.FEMALE);
        userMapper.insert(user1);
        User user2 = new User();
        user2.setName("KOH2");
        user2.setGender(UserSex.MALE);
        userMapper.insert(user2);

        Example example = new Example();
        example.createCriteria().andIn("id", Lists.newArrayList(user1.getId(), user2.getId()));
        List<User> userList = userMapper.selectByExample(example);
        Assert.assertEquals(2, userList.size());

//        query = new UserQuery();
//        query.setMaxId(user2.getId() + 1);
//        userList = userMapper.selectByQuery(User.class, query, "id desc");
//        Assert.assertEquals(2, userList.size());
//        Assert.assertEquals(user2.getId(), userList.get(0).getId());
//
//        query = new UserQuery();
//        query.setMinId(user1.getId());
//        query.setName("%H2%");
//        userList = userMapper.selectByQuery(User.class, query, null);
//        Assert.assertEquals(1, userList.size());
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