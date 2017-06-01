package me.superkoh.kframework.lib.sns.wechat.common.exception;

import me.superkoh.kframework.core.exception.KException;

/**
 * Created by KOH on 2017/4/25.
 * <p>
 * webFramework
 */
public class WxSnsException extends KException {
    public WxSnsException() {
    }

    public WxSnsException(String message) {
        super(message);
    }

    public WxSnsException(int code, String message) {
        super(code, message);
    }
}
