package me.superkoh.kframework.mvc.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import me.superkoh.kframework.core.lang.BaseObject;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BizRes extends BaseObject {
}
