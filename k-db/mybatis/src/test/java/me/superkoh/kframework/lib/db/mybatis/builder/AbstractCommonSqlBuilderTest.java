package me.superkoh.kframework.lib.db.mybatis.builder;

import me.superkoh.kframework.lib.db.common.domain.TimeTraceable;
import me.superkoh.kframework.lib.db.mybatis.test.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * Created by KOH on 2017/5/19.
 * <p>
 * k-framework
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:ut.sql"})
public class AbstractCommonSqlBuilderTest {
    @Autowired
    UserMapper userMapper;

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

    public static class UserSqlBuilder extends AbstractCommonSqlBuilder {

        @Override
        protected String getTableName() {
            return "user";
        }
    }

    public static class User implements TimeTraceable {
        private Long id;
        private String name;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public LocalDateTime getCreateTime() {
            return createTime;
        }

        @Override
        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }

        @Override
        public LocalDateTime getUpdateTime() {
            return updateTime;
        }

        @Override
        public void setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
        }
    }
}