package me.superkoh.kframework.lib.db.mybatis.interceptor;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public class Author {
    private Long authorId;

    public Author(Long authorId) {
        this.authorId = authorId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
