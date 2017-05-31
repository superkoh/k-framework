/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       MPI基本参数工具类
 * =============================================================================
 */
package me.superkoh.kframework.lib.payment.union.sdk;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

/**
 * 软件开发工具包 配制
 * 
 * @author xuyaowen
 * 
 */
public class SDKConfig {
	/** 支付宝签约商户号. */
	private String merchId;
	/** 签名证书路径. */
	private String signCertPath;
	/** 签名证书密码. */
	private String signCertPwd;
	/** 签名证书类型. */
	private String signCertType;
	/** 加密公钥证书路径. */
	private String encryptCertPath;
	/** 验证签名公钥证书目录. */
	private String validateCertDir;
	/** 证书使用模式(单证书/多证书) */
	private String singleMode;

	/**
	 * 是否在测试环境，影响urlConfig
	 */
	private Boolean isDebugMode;
	/**
	 * URL配置，区分测试环境，正式环境
	 */
	private AcpSDKUrls urlConfig;
    /**
     * 后台通知URL
     */
	private String backUrl;
    /**
     * 前端通知URL
     */
	private String frontUrl;
	
//	/** 配置文件中的前台URL常量. */
//	public static final String SDK_FRONT_URL = "acpsdk.frontTransUrl";
//	/** 配置文件中的后台URL常量. */
//	public static final String SDK_BACK_URL = "acpsdk.backTransUrl";
//	/** 配置文件中的单笔交易查询URL常量. */
//	public static final String SDK_SIGNQ_URL = "acpsdk.singleQueryUrl";
//	/** 配置文件中的批量交易查询URL常量. */
//	public static final String SDK_BATQ_URL = "acpsdk.batchQueryUrl";
//	/** 配置文件中的批量交易URL常量. */
//	public static final String SDK_BATTRANS_URL = "acpsdk.batchTransUrl";
//	/** 配置文件中的文件类交易URL常量. */
//	public static final String SDK_FILETRANS_URL = "acpsdk.fileTransUrl";
//	/** 配置文件中的有卡交易URL常量. */
//	public static final String SDK_CARD_URL = "acpsdk.cardTransUrl";
//	/** 配置文件中的app交易URL常量. */
//	public static final String SDK_APP_URL = "acpsdk.appTransUrl";
//
//
//	/** 以下缴费产品使用，其余产品用不到，无视即可 */
//	// 前台请求地址
//	public static final String JF_SDK_FRONT_TRANS_URL= "acpsdk.jfFrontTransUrl";
//	// 后台请求地址
//	public static final String JF_SDK_BACK_TRANS_URL="acpsdk.jfBackTransUrl";
//	// 单笔查询请求地址
//	public static final String JF_SDK_SINGLE_QUERY_URL="acpsdk.jfSingleQueryUrl";
//	// 有卡交易地址
//	public static final String JF_SDK_CARD_TRANS_URL="acpsdk.jfCardTransUrl";
//	// App交易地址
//	public static final String JF_SDK_APP_TRANS_URL="acpsdk.jfAppTransUrl";
//
//
//	/** 配置文件中签名证书路径常量. */
//	public static final String SDK_SIGNCERT_PATH = "acpsdk.signCert.path";
//	/** 配置文件中签名证书密码常量. */
//	public static final String SDK_SIGNCERT_PWD = "acpsdk.signCert.pwd";
//	/** 配置文件中签名证书类型常量. */
//	public static final String SDK_SIGNCERT_TYPE = "acpsdk.signCert.type";
//	/** 配置文件中密码加密证书路径常量. */
//	public static final String SDK_ENCRYPTCERT_PATH = "acpsdk.encryptCert.path";
//	/** 配置文件中磁道加密证书路径常量. */
//	public static final String SDK_ENCRYPTTRACKCERT_PATH = "acpsdk.encryptTrackCert.path";
//	/** 配置文件中磁道加密公钥模数常量. */
//	public static final String SDK_ENCRYPTTRACKKEY_MODULUS = "acpsdk.encryptTrackKey.modulus";
//	/** 配置文件中磁道加密公钥指数常量. */
//	public static final String SDK_ENCRYPTTRACKKEY_EXPONENT = "acpsdk.encryptTrackKey.exponent";
//	/** 配置文件中验证签名证书目录常量. */
//	public static final String SDK_VALIDATECERT_DIR = "acpsdk.validateCert.dir";
//
//	/** 配置文件中是否加密cvn2常量. */
//	public static final String SDK_CVN_ENC = "acpsdk.cvn2.enc";
//	/** 配置文件中是否加密cvn2有效期常量. */
//	public static final String SDK_DATE_ENC = "acpsdk.date.enc";
//	/** 配置文件中是否加密卡号常量. */
//	public static final String SDK_PAN_ENC = "acpsdk.pan.enc";
//	/** 配置文件中证书使用模式 */
//	public static final String SDK_SINGLEMODE = "acpsdk.singleMode";
//	/** 操作对象. */
//	private static SDKConfig config;
//	/** 属性文件对象. */
//	private Properties properties;
//
//
//	/**
//	 * 获取config对象.
//	 *
//	 * @return
//	 */
//	public static SDKConfig getConfig() {
//		if (null == config) {
//			config = new SDKConfig();
//		}
//		return config;
//	}
//
//	/**
//	 * 从properties文件加载
//	 *
//	 * @param rootPath
//	 *            不包含文件名的目录.
//	 */
//	public void loadPropertiesFromPath(String rootPath, String fileName) {
//		if (StringUtils.isNotBlank(rootPath)) {
//			File file = new File(rootPath + File.separator + fileName);
//			InputStream in = null;
//			if (file.exists()) {
//				try {
//					in = new FileInputStream(file);
//					BufferedReader bf = new BufferedReader(
//							new InputStreamReader(in, "utf-8"));
//					properties = new Properties();
//					properties.load(bf);
//					loadProperties(properties);
//				} catch (FileNotFoundException e) {
//					LogUtil.writeErrorLog(e.getMessage(), e);
//				} catch (IOException e) {
//					LogUtil.writeErrorLog(e.getMessage(), e);
//				} finally {
//					if (null != in) {
//						try {
//							in.close();
//						} catch (IOException e) {
//							LogUtil.writeErrorLog(e.getMessage(), e);
//						}
//					}
//				}
//			} else {
//				// 由于此时可能还没有完成LOG的加载，因此采用标准输出来打印日志信息
//				System.out.println(rootPath + fileName + "不存在,加载参数失败");
//			}
//		} else {
//			System.out.println("rootPath不存在,加载参数失败");
//		}
//
//	}

	/**
	 * 从classpath路径下加载配置参数
	 */
//	public void loadPropertiesFromSrc(String fileName) {
//		InputStream in = null;
//		try {
//			// Properties pro = null;
//			LogUtil.writeLog("从classpath获取属性文件" + fileName);
//			in = SDKConfig.class.getClassLoader()
//					.getResourceAsStream(fileName);
//			if (null != in) {
//				BufferedReader bf = new BufferedReader(new InputStreamReader(
//						in, "utf-8"));
//				properties = new Properties();
//				try {
//					properties.load(bf);
//				} catch (IOException e) {
//					throw e;
//				}
//			} else {
//				LogUtil.writeErrorLog(fileName + "属性文件未能在classpath目录下找到!");
//				return;
//			}
//			loadProperties(properties);
//		} catch (IOException e) {
//			LogUtil.writeErrorLog(e.getMessage(), e);
//		} finally {
//			if (null != in) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					LogUtil.writeErrorLog(e.getMessage(), e);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 根据传入的 {@link #loadProperties(Properties)}对象设置配置参数
//	 *
//	 * @param pro
//	 */
//	public void loadProperties(Properties pro) {
//		LogUtil.writeLog("开始从属性文件中加载配置项");
//		String value = null;
//		value = pro.getProperty(SDK_SINGLEMODE);
//		if (SDKUtil.isEmpty(value) || SDKConstants.TRUE_STRING.equals(value)) {
//			this.singleMode = SDKConstants.TRUE_STRING;
//			LogUtil.writeLog("单证书模式，使用配置文件配置的私钥签名证书，SingleCertMode:[" + this.singleMode + "]");
//			// 单证书模式
//			value = pro.getProperty(SDK_SIGNCERT_PATH);
//
//			if (!SDKUtil.isEmpty(value)) {
//				this.signCertPath = value.trim();
//				LogUtil.writeLog("配置项：私钥签名证书路径==>"+SDK_SIGNCERT_PATH +"==>"+ value+" 已加载");
//			}
//			value = pro.getProperty(SDK_SIGNCERT_PWD);
//			if (!SDKUtil.isEmpty(value)) {
//				this.signCertPwd = value.trim();
//				LogUtil.writeLog("配置项：私钥签名证书密码==>"+SDK_SIGNCERT_PWD +" 已加载");
//			}
//			value = pro.getProperty(SDK_SIGNCERT_TYPE);
//			if (!SDKUtil.isEmpty(value)) {
//				this.signCertType = value.trim();
//				LogUtil.writeLog("配置项：私钥签名证书类型==>"+SDK_SIGNCERT_TYPE +"==>"+ value+" 已加载");
//			}
//		} else {
//			// 多证书模式
//			this.singleMode = SDKConstants.FALSE_STRING;
//			LogUtil.writeLog("多证书模式，不需要加载配置文件中配置的私钥签名证书，SingleMode:[" + this.singleMode + "]");
//		}
//		value = pro.getProperty(SDK_ENCRYPTCERT_PATH);
//		if (!SDKUtil.isEmpty(value)) {
//			this.encryptCertPath = value.trim();
//			LogUtil.writeLog("配置项：敏感信息加密证书==>"+SDK_ENCRYPTCERT_PATH +"==>"+ value+" 已加载");
//		}
//		value = pro.getProperty(SDK_VALIDATECERT_DIR);
//		if (!SDKUtil.isEmpty(value)) {
//			this.validateCertDir = value.trim();
//			LogUtil.writeLog("配置项：验证签名证书路径(这里配置的是目录，不要指定到公钥文件)==>"+SDK_VALIDATECERT_DIR +"==>"+ value+" 已加载");
//		}
//		value = pro.getProperty(SDK_FRONT_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.frontRequestUrl = value.trim();
//		}
//		value = pro.getProperty(SDK_BACK_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.backRequestUrl = value.trim();
//		}
//		value = pro.getProperty(SDK_BATQ_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.batchQueryUrl = value.trim();
//		}
//		value = pro.getProperty(SDK_BATTRANS_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.batchTransUrl = value.trim();
//		}
//		value = pro.getProperty(SDK_FILETRANS_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.fileTransUrl = value.trim();
//		}
//		value = pro.getProperty(SDK_SIGNQ_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.singleQueryUrl = value.trim();
//		}
//		value = pro.getProperty(SDK_CARD_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.cardRequestUrl = value.trim();
//		}
//		value = pro.getProperty(SDK_APP_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.appRequestUrl = value.trim();
//		}
//		value = pro.getProperty(SDK_ENCRYPTTRACKCERT_PATH);
//		if (!SDKUtil.isEmpty(value)) {
//			this.encryptTrackCertPath = value.trim();
//		}
//
//		/**缴费部分**/
//		value = pro.getProperty(JF_SDK_FRONT_TRANS_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.jfFrontRequestUrl = value.trim();
//		}
//
//		value = pro.getProperty(JF_SDK_BACK_TRANS_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.jfBackRequestUrl = value.trim();
//		}
//
//		value = pro.getProperty(JF_SDK_SINGLE_QUERY_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.jfSingleQueryUrl = value.trim();
//		}
//
//		value = pro.getProperty(JF_SDK_CARD_TRANS_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.jfCardRequestUrl = value.trim();
//		}
//
//		value = pro.getProperty(JF_SDK_APP_TRANS_URL);
//		if (!SDKUtil.isEmpty(value)) {
//			this.jfAppRequestUrl = value.trim();
//		}
//
//		value = pro.getProperty(SDK_ENCRYPTTRACKKEY_EXPONENT);
//		if (!SDKUtil.isEmpty(value)) {
//			this.encryptTrackKeyExponent = value.trim();
//		}
//
//		value = pro.getProperty(SDK_ENCRYPTTRACKKEY_MODULUS);
//		if (!SDKUtil.isEmpty(value)) {
//			this.encryptTrackKeyModulus = value.trim();
//		}
//	}


	public String getSignCertPath() {
		return signCertPath;
	}

	public void setSignCertPath(String signCertPath) {
		this.signCertPath = signCertPath;
	}

	public String getSignCertPwd() {
		return signCertPwd;
	}

	public void setSignCertPwd(String signCertPwd) {
		this.signCertPwd = signCertPwd;
	}

	public String getSignCertType() {
		return signCertType;
	}

	public void setSignCertType(String signCertType) {
		this.signCertType = signCertType;
	}

	public String getEncryptCertPath() {
		return encryptCertPath;
	}

	public void setEncryptCertPath(String encryptCertPath) {
		this.encryptCertPath = encryptCertPath;
	}
	
	public String getValidateCertDir() {
		return validateCertDir;
	}

	public void setValidateCertDir(String validateCertDir) {
		this.validateCertDir = validateCertDir;
	}

//	public Properties getProperties() {
//		return properties;
//	}
//
//	public void setProperties(Properties properties) {
//		this.properties = properties;
//	}

	public String getSingleMode() {
		return singleMode;
	}

	public void setSingleMode(String singleMode) {
		this.singleMode = singleMode;
	}

	public SDKConfig() {
		super();
	}

	public Boolean getIsDebugMode() {
		return isDebugMode;
	}

	public void setIsDebugMode(Boolean isDebugMode) {
		this.isDebugMode = isDebugMode;
	}

	public String getMerchId() {
		return merchId;
	}

	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public AcpSDKUrls getUrlConfig() {
		if (isDebugMode) {
			return AcpSDKUrls.getTestUrls();
		}
		return AcpSDKUrls.getProdUrls();
	}
}
