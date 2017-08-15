package me.superkoh.kframework.lib.db.mybatis.builder;

/**
 * Created by KOH on 2017/5/23.
 * <p>
 * k-framework
 */
public class SqlBuilderConstant {
    public static final String INSERT = "insert";
    public static final String UPDATE_BY_PRIMARY_KEY_SELECTIVE = "updateByPrimaryKeySelective";
    public static final String SELECT_BY_QUERY = "selectByQuery";
    public static final String SELECT_PAGE_BY_QUERY = "selectPageByQuery";
    public static final String COUNT_BY_QUERY = "countByQuery";

    public static final String WHERE_SEGMENT =
            "<where>" +
                    "<foreach collection=\"example.oredCriteria\" item=\"criteria\" separator=\"or\">\n" +
                    "        <if test=\"criteria.valid\">\n" +
                    "          <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n" +
                    "            <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" +
                    "              <choose>\n" +
                    "                <when test=\"criterion.noValue\">\n" +
                    "                  and ${criterion.condition}\n" +
                    "                </when>\n" +
                    "                <when test=\"criterion.singleValue\">\n" +
                    "                  and ${criterion.condition} #{criterion.value}\n" +
                    "                </when>\n" +
                    "                <when test=\"criterion.betweenValue\">\n" +
                    "                  and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
                    "                </when>\n" +
                    "                <when test=\"criterion.listValue\">\n" +
                    "                  and ${criterion.condition}\n" +
                    "                  <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
                    "                    #{listItem}\n" +
                    "                  </foreach>\n" +
                    "                </when>\n" +
                    "              </choose>\n" +
                    "            </foreach>\n" +
                    "          </trim>\n" +
                    "        </if>\n" +
                    "      </foreach>" +
                    "</where>";
}
