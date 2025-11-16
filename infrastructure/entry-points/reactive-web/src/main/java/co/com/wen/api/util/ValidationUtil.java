package co.com.wen.api.util;

public class ValidationUtil {

	public static boolean isNumeric(String valor) {
		try {
			return Integer.parseInt(valor) >= 0;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	public static boolean isNullOrEmpty(String valor){
		if (valor == null || valor.isEmpty()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public static int convertStringToInt(String valor){
		return Integer.parseInt(valor);
	}
}
