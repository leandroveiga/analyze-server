package studio.rockpile.server.analyze.protocol;

import java.util.Calendar;
import java.util.Date;

public class CommonResult<T> {

	public final static Integer HTTP_OK = 200;
	public final static Integer HTTP_ERROR = 500;

	private Date timestamp;
	private Integer status;
	private String message;
	private T data;

	private CommonResult() {
	}

	public static <T> CommonResult<T> success(T data) {
		CommonResult<T> result = new CommonResult<>();
		result.setTimestamp(Calendar.getInstance().getTime());
		result.setStatus(HTTP_OK);
		result.setMessage("请求成功处理!");
		result.setData(data);
		return result;
	}

	public static <T> CommonResult<T> failed(String message) {
		CommonResult<T> result = new CommonResult<>();
		result.setTimestamp(Calendar.getInstance().getTime());
		result.setStatus(HTTP_ERROR);
		result.setMessage(message);
		return result;
	}

	public static <T> CommonResult<T> failed(Integer code, String message, T data) {
		CommonResult<T> result = new CommonResult<>();
		result.setTimestamp(Calendar.getInstance().getTime());
		result.setStatus(code);
		result.setMessage(message);
		result.setData(data);
		return result;
	}

	@Override
	public String toString() {
		return "CommonResult [status=" + status + ", message=" + message + ", data=" + data + "]";
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
