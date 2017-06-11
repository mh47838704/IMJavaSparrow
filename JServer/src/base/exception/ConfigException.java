package base.exception;

/**
 * 配置适配异常 
 */
public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = 6118361499832602075L;
	
	public ConfigException(String msg){
		super(msg);
	}
	
	public ConfigException(Throwable t){
		super(t);
	}
	
}
